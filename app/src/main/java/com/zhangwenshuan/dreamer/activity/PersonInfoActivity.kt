package com.zhangwenshuan.dreamer.activity

import android.content.Intent
import android.graphics.Typeface
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.zhangwenshuan.dreamer.R
import com.zhangwenshuan.dreamer.bean.UpdateIntroduce
import com.zhangwenshuan.dreamer.bean.UpdateNickname
import com.zhangwenshuan.dreamer.util.BaseApplication
import com.zhangwenshuan.dreamer.util.NetUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_finance_add.*
import kotlinx.android.synthetic.main.activity_person_info.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.toast

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
    }

    var sex = mutableListOf("男", "女")

    private fun showSexDialog() {

        val view = OptionsPickerBuilder(this, object : OnOptionsSelectListener {
            override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
                var sex = sex[options1]

                val user=BaseApplication.user

                NetUtils.data(NetUtils.getApiInstance().updateSex(BaseApplication.userId, sex), Consumer {
                    toast(it.message)
                    if (it.code == 200) {
                        tvSex.text = sex
                        user!!.sex=sex
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