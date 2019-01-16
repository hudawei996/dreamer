package com.zhangwenshuan.dreamer.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.tools.PictureFileUtils
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.AvatarUpload
import com.zhangwenshuan.dreamer.bean.UpdateIntroduce
import com.zhangwenshuan.dreamer.bean.UpdateNickname
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_person_info.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.toast
import java.io.File

var glideOptions = RequestOptions().transform(RoundedCorners(10)).placeholder(R.mipmap.ic_launcher)

class PersonInfoActivity : BaseActivity() {
    override fun setResourceId(): Int {
        return R.layout.activity_person_info
    }

    override fun preInitData() {
        EventBus.getDefault().register(this)
    }

    override fun initViews() {
        tvTitle.visibility = View.GONE

        tvSubtitle.visibility = View.VISIBLE

        tvSubtitle.text = "个人信息"

        val typeface = Typeface.createFromAsset(assets, "icon_action.ttf")
        tvRight1.typeface = typeface
        tvRight2.typeface = typeface
        tvRight3.typeface = typeface


        val user = BaseApplication.user

        if (user?.nickname != null) {
            tvNickname.text = user?.nickname
        }

        if (user?.introduce != null) {
            tvIntroduce.text = user?.introduce
        }

        if (user?.sex != null) {
            tvSex.text = user?.sex
        }

        if (BaseApplication.avatar.isEmpty()) {
            Glide.with(this).asBitmap().load(R.mipmap.img_logo).into(ivAvatar)
        } else {
            Glide.with(this).asBitmap().load(BaseApplication.avatar).apply(glideOptions).into(ivAvatar)
        }
    }

    override fun initListener() {

        tvRight1.setOnClickListener {
            tvNickname.performClick()
        }


        tvNickname.setOnClickListener {
            startActivity(Intent(this@PersonInfoActivity, NicknameActivity::class.java))
        }

        llIntroduce.setOnClickListener {
            startActivity(Intent(this@PersonInfoActivity, IntroduceActivity::class.java))
        }
        llSex.setOnClickListener {
            showSexDialog()
        }

        llAvatar.setOnClickListener {
            selectAvatar()
        }
    }

    private fun selectAvatar() {
        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())
            .selectionMode(PictureConfig.SINGLE)
            .isCamera(true)
            .previewImage(true)
            .withAspectRatio(1, 1)
            .enableCrop(true)
            .rotateEnabled(true)
            .scaleEnabled(true)
            .isDragFrame(true)
            .forResult(0)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val list = PictureSelector.obtainMultipleResult(data)

            val media = list[0]

            toUploadImage(media.cutPath)

        }

    }

    private fun toUploadImage(path: String) {
        val file = File(path)

        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)

        val body = MultipartBody.Part.createFormData("file", file.getName(), requestBody)

        var params = mutableMapOf<String, Int>()

        params["userId"] = BaseApplication.userId

        NetUtils.data(NetUtils.getApiInstance().uploadAvatar(params, body), Consumer {
            toast("上传成功")

            if (it.code == 200) {
                Glide.with(this).load(path).apply(glideOptions).into(ivAvatar)
                BaseApplication.setAvatarLocal(it.data)
                PictureFileUtils.deleteCacheDirFile(this)
                EventBus.getDefault().post(AvatarUpload())
            }
        })

    }

    var sex = mutableListOf("男", "女")

    private fun showSexDialog() {

        val view = OptionsPickerBuilder(this, object : OnOptionsSelectListener {
            override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
                var sex = sex[options1]

                val user = BaseApplication.user

                NetUtils.data(NetUtils.getApiInstance().updateSex(BaseApplication.userId, sex), Consumer {
                    toast(it.message)
                    if (it.code == 200) {
                        tvSex.text = sex
                        user!!.sex = sex
                        BaseApplication.setUserLocal(user)
                    }
                })

            }
        })
            .setContentTextSize(18)
            .build<String>()

        view.setPicker(sex)

        view.show()
    }

    override fun initData() {
    }

    @Subscribe
    fun subscribe(update: UpdateNickname) {
        tvNickname.text = update.name
    }

    @Subscribe
    fun subscribe(update: UpdateIntroduce) {
        tvIntroduce.text = update.introduce
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}