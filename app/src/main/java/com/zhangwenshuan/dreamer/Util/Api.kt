package com.zhangwenshuan.dreamer.util

import android.animation.ArgbEvaluator
import com.zhangwenshuan.dreamer.bean.*
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {


    @POST("auth/login")
    @FormUrlEncoded
    fun login(@Field("username") username: String, @Field("password") password: String): Observable<Result<LoginBean>>


    @POST("auth/getPhoneCode")
    @FormUrlEncoded
    fun getCheckCode(@Field("phone") username: String): Observable<Result<Any>>


    @POST("auth/register")
    @FormUrlEncoded
    fun register(
        @Field("username") username: String, @Field("password") password: String,
        @Field("code") code: String
    ): Observable<Result<Any>>

    @POST("user/updatePassword")
    @FormUrlEncoded
    fun updatePassword(
        @Field("userId") userId: Int, @Field("oldPassword") oldPassword: String,
        @Field("newPassword") newPassword: String
    ): Observable<Result<Any>>

  @POST("user/updateNickname")
    @FormUrlEncoded
    fun updateNickname(
        @Field("userId") userId: Int, @Field("nickname") nickname: String
    ): Observable<Result<Any>>

    @POST("user/updateIntroduce")
    @FormUrlEncoded
    fun updateIntroduce(
        @Field("userId") userId: Int, @Field("introduce") introduce: String
    ): Observable<Result<Any>>

    @POST("user/saveFeedback")
    @FormUrlEncoded
    fun saveFeedback(
        @Field("userId") userId: Int, @Field("feedback") feedback: String
    ): Observable<Result<Any>>






    @POST("password/save")
    @FormUrlEncoded
    fun savePassword(
        @Field("username") username: String,
        @Field("name") name: String,
        @Field("password") password: String,
        @Field("userId") userId: Int
    ): Observable<Result<Password>>


    @POST("password/getAll")
    @FormUrlEncoded
    fun getAllPassword(
        @Field("userId") userId: Int
    ): Observable<Result<List<Password>>>

    @POST("password/search")
    @FormUrlEncoded
    fun searchPassword(
        @Field("name") password: String,
        @Field("userId") userId: Int
    ): Observable<Result<List<Password>>>


    @POST("password/delete")
    @FormUrlEncoded
    fun deletePassword(
        @Field("id") id: Int
    ): Observable<Result<Any>>


    @POST("finance/save")
    @FormUrlEncoded
    fun saveFinance(
        @Field("userId") userId: Int,
        @Field("type") type: String,
        @Field("time") time: String,
        @Field("account") account: String,
        @Field("remark") remark: String,
        @Field("bankName") bankName: String,
        @Field("bankId") bankId: Int,
        @Field("isExpense") isExpense: Int
    ): Observable<Result<Finance>>

    @POST("finance/update")
    @FormUrlEncoded
    fun updateFinance(
        @Field("id") id: Int,
        @Field("type") type: String,
        @Field("time") time: String,
        @Field("account") account: Double,
        @Field("remark") remark: String,
        @Field("bankName") bankName: String,
        @Field("bankId") bankId: Int,
        @Field("isExpense") isExpense: Int
    ): Observable<Result<Any>>


    @POST("finance/get")
    @FormUrlEncoded
    fun getFinance(@Field("userId") userId: Int): Observable<Result<Account>>

    @POST("finance/getByTime")
    @FormUrlEncoded
    fun getFinanceByTime(
        @Field("userId") userId: Int,
        @Field("time") time: String,
        @Field("isExpense") isExpense: Int
    ): Observable<Result<List<Finance>>>

    @POST("finance/search")
    @FormUrlEncoded
    fun getFinanceBySearch(
        @Field("userId") userId: Int,
        @Field("beginTime") beginDate: String,
        @Field("stopTime") stopDate: String
    ): Observable<Result<List<Finance>>>

    @POST("finance/getByBankId")
    @FormUrlEncoded
    fun getFinanceByBankId(
        @Field("id") id: Int
    ): Observable<Result<List<Finance>>>

    @POST("finance/getTotalAccount")
    @FormUrlEncoded
    fun getTotalFinance(
        @Field("userId") id: Int
    ): Observable<Result<TotalAccount>>


    @POST("finance/delete")
    @FormUrlEncoded
    fun deleteFinance(@Field("id") id: Int): Observable<Result<Any>>


    @POST("budget/save")
    fun saveBudget(@Body body: RequestBody): Observable<Result<BudgetBean>>

    @POST("budget/get")
    @FormUrlEncoded
    fun getBudget(@Field("userId") userId: Int, @Field("month") month: String): Observable<Result<BudgetBean>>

    @POST("finance/getDetail")
    @FormUrlEncoded
    fun getBudgetDetail(@Field("userId") userId: Int, @Field("month") month: String, @Field("expense") expense: Int): Observable<Result<List<BudgetDetail>>>


    @POST("bank/saveBank")
    @FormUrlEncoded
    fun saveBank(
        @Field("userId") userId: Int,
        @Field("name") bankName: String,
        @Field("account") account: String,
        @Field("number") number: String,
        @Field("remark") remark: String
    ): Observable<Result<Bank>>


    @POST("bank/saveMobile")
    @FormUrlEncoded
    fun saveMobile(
        @Field("userId") userId: Int,
        @Field("name") bankName: String,
        @Field("account") account: String,
        @Field("username") username: String,
        @Field("remark") remark: String
    ): Observable<Result<Bank>>


    @POST("bank/saveCash")
    @FormUrlEncoded
    fun saveCash(
        @Field("userId") userId: Int,
        @Field("name") bankName: String,
        @Field("account") account: String,
        @Field("remark") remark: String
    ): Observable<Result<Bank>>

    @POST("bank/saveCredit")
    @FormUrlEncoded
    fun saveCredit(
        @Field("userId") userId: Int,
        @Field("name") bankName: String,
        @Field("amount") amount: String,
        @Field("debt") debt: String,
        @Field("billDate") billDate: String,
        @Field("returnDate") returnDate: String,
        @Field("remark") remark: String,
        @Field("number") number: String
    ): Observable<Result<Bank>>

    @POST("bank/getCash")
    @FormUrlEncoded
    fun getCash(
        @Field("userId") userId: Int
    ): Observable<Result<Bank>>


    @POST("bank/get")
    @FormUrlEncoded
    fun getBank(@Field("userId") userId: Int): Observable<Result<List<Bank>>>

    @POST("bank/delete")
    @FormUrlEncoded
    fun deleteBank(@Field("id") id: Int): Observable<Result<Any>>

    @POST("bank/updateBank")
    @FormUrlEncoded
    fun updateBank(@Field("id") id: Int, @Field("account") account: String,
                   @Field("name")name: String,@Field("remark")remark: String,@Field("number")number: String): Observable<Result<Any>>

    @POST("bank/updateCredit")
    @FormUrlEncoded
    fun updateCredit(@Field("id") id: Int, @Field("amount") amount: String,@Field("debt") debt: String,
                     @Field("billDate")billDate: String,@Field("returnDate")returnDate: String,
                   @Field("name")name: String,@Field("number")number: String,@Field("remark")remark: String): Observable<Result<Any>>


     @POST("bank/updateMobile")
    @FormUrlEncoded
    fun updateMobile(@Field("id") id: Int, @Field("name") name: String,@Field("username") username: String,
                     @Field("account")account: String,@Field("remark")remark: String): Observable<Result<Any>>





    @POST("book/save")
    @FormUrlEncoded
    fun saveBook(
        @Field("userId") userId: Int,
        @Field("name") name: String,
        @Field("begin") date: String,
        @Field("end") end: String,
        @Field("content") content: String,
        @Field("evaluate") evaluator: String
    ): Observable<Result<Any>>

    @POST("book/get")
    @FormUrlEncoded
    fun getBook(@Field("userId") userId: Int): Observable<Result<List<Book>>>

    @POST("book/updateEndTime")
    @FormUrlEncoded
    fun updateBook(@Field("id") id: Int, @Field("end") endDate: String): Observable<Result<Any>>

    @POST("book/updateBook")
    @FormUrlEncoded
    fun updateBook(@Field("id") id: Int, @Field("content") content: String, @Field("evaluate") evaluator: String): Observable<Result<Any>>


    @POST("book/delete")
    @FormUrlEncoded
    fun deleteBook(@Field("id") id: Int): Observable<Result<Any>>
}