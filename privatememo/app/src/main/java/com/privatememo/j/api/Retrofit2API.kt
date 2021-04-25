package com.privatememo.j.api

import com.privatememo.j.datamodel.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface Retrofit2API {

    @FormUrlEncoded
    @POST("MyRecords/SignUp/SignUpEmailSender.php")
    fun EmailSender(
        @Field("email") email: String,
        @Field("num") num: String
    ): Call<String>

    @FormUrlEncoded
    @POST("MyRecords/SignUp/SignUpProfileInsert.php")
    fun ProfileInsert(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("nickname") nickname: String,
        @Field("motto") motto: String,
        @Field("picPath") picPath: String
    ): Call<String>

    @FormUrlEncoded
    @POST("MyRecords/SignUp/SignUpEmailCheck.php")
    fun ProfileEmailCheck(
        @Field("email") email: String
    ): Call<ReturnCheck>

    @Multipart
    @POST("MyRecords/SignUp/SignUpProfileImageSender.php")
    fun ProfileImageSender(
        @Part("userId") userId: String,
        @Part imageFile : MultipartBody.Part
    ): Call<String>

    @Multipart
    @POST("MyRecords/Memo/CategoryImageSender.php")
    fun CategoryImageSender(
        @Part("userId") userId: String,
        @Part imageFile : MultipartBody.Part
    ): Call<String>

    @FormUrlEncoded
    @POST("MyRecords/Login/Login.php")
    fun Login(
            @Field("email") email: String,
            @Field("password") password: String
    ): Call<MemberInfo>

    @FormUrlEncoded
    @POST("MyRecords/Memo/CategoryInsert.php")
    fun CategoryInsert(
        @Field("email") email: String,
        @Field("catename") catename: String,
        @Field("explanation") explanation: String,
        @Field("catepicPath") catepicPath: String
    ): Call<String>

    @FormUrlEncoded
    @POST("MyRecords/Memo/getCategoryList.php")
    fun getCategoryList(
        @Field("email") email: String
    ): Call<CategoryInfo>

    @FormUrlEncoded
    @POST("MyRecords/Memo/MemoInsert.php")
    fun MemoInsert(
        @Field("title") title: String,
        @Field("memo") memo: String,
        @Field("date") date: String,
        @Field("time") time: String,
        @Field("memberlist_email") email: String,
        @Field("category_catenum") catenum: Int
    ): Call<String>

    // 프로필 이미지 보내기
    @Multipart
    @POST("MyRecords/Memo/MemoImageSender.php")
    fun MemoImageSender(
        @Part("userId") userId: String,
        @Part imageFile : MultipartBody.Part): Call<String>

    @FormUrlEncoded
    @POST("MyRecords/Memo/InsertImageAddressToServer.php")
    fun InsertImageAddressToServer(
        @Field("imagePath") imagePath: String,
        @Field("memberlist_email") memberlist_email: String,
        @Field("content_contentnum") content_contentnum: Int
    ): Call<String>

    @FormUrlEncoded
    @POST("MyRecords/Memo/getMemoList.php")
    fun getMemoList(
        @Field("category_catenum") catenum: Int,
        @Field("start") start: Int,
        @Field("end") end: Int,
        @Field("SortState") sortstate: Int
    ): Call<MemoInfo>

    @FormUrlEncoded
    @POST("MyRecords/Memo/DeleteMemo.php")
    fun DeleteMemo(
            @Field("contentNum") ContentNum: Int
    ): Call<String>

    @FormUrlEncoded
    @POST("MyRecords/Memo/getImageStorageContentNumber.php")
    fun getImageStorageContentNumber(
            @Field("date") date: String,
            @Field("time") time: String,
            @Field("cateNum") cateNum: Int
    ): Call<ContentNumberInfo>

    @FormUrlEncoded
    @POST("MyRecords/Memo/getMemoImage.php")
    fun getMemoImage(
        @Field("contentNum") contentNum: Int
    ): Call<MemoImageInfo>

    @FormUrlEncoded
    @POST("MyRecords/Memo/UpdateMemo.php")
    fun UpdateMemo(
            @Field("contentnum") contentnum : Int,
            @Field("title") title: String,
            @Field("memo") memo: String,
            @Field("date") date: String,
            @Field("reviseDate") reviseDate: String,
            @Field("time") time: String,
            @Field("reviseTime") reviseTime : String,
            @Field("ConBookmark") ConBookmark: String,
            @Field("memberlist_email") email: String,
            @Field("category_catenum") catenum: Int
    ): Call<String>

    @FormUrlEncoded
    @POST("MyRecords/Memo/DeleteImage.php")
    fun DeleteImage(
        @Field("imagePath") imagePath: String
    ): Call<String>

    @FormUrlEncoded
    @POST("MyRecords/Memo/DeleteCategory.php")
    fun DeleteCategory(
        @Field("cateNum") cateNum: Int
    ): Call<String>

    @FormUrlEncoded
    @POST("MyRecords/Memo/UpdateCategory.php")
    fun UpdateCategory(
        @Field("email") email: String,
        @Field("catename") catename: String,
        @Field("explanation") explanation: String,
        @Field("catepicPath") catepicPath: String,
        @Field("catenum") catenum: Int,
        @Field("CateBookmark") catebookmark: String
    ): Call<String>

    @FormUrlEncoded
    @POST("MyRecords/Memo/UpdateCategoryImageDelete.php")
    fun UpdateCategoryImageDelete(
        @Field("catenum") catenum: Int
    ): Call<String>

    @FormUrlEncoded
    @POST("MyRecords/Memo/getMemoCount.php")
    fun getMemoCount(
        @Field("email") email: String
    ): Call<MemoCountInfo>

    @FormUrlEncoded
    @POST("MyRecords/Memo/getOnlyPic.php")
    fun getOnlyPic(
        @Field("email") email: String,
        @Field("start") start: Int,
        @Field("end") end: Int
    ): Call<OnlyPicInfo>

    @FormUrlEncoded
    @POST("MyRecords/Search/getSearchResult.php")
    fun getSearchResult(
            @Field("email") email: String,
            @Field("keyword") keyword: String,
            @Field("start") start: Int,
            @Field("end") end: Int
    ): Call<SearchInfo>

    @FormUrlEncoded
    @POST("MyRecords/Calendar/getCalendarMemo.php")
    fun getCalendarMemo(
        @Field("email") email: String
    ): Call<MemoInfo>

    @FormUrlEncoded
    @POST("MyRecords/Setting/getMemberProfile.php")
    fun getMemberProfile(
        @Field("email") email: String
    ): Call<MemberProfileInfo>

    @FormUrlEncoded
    @POST("MyRecords/Setting/UpdateProfile.php")
    fun UpdateProfile(
        @Field("email") email: String,
        @Field("nickname") nickname: String,
        @Field("motto") motto: String
    ): Call<String>

    @FormUrlEncoded
    @POST("MyRecords/Setting/ForMatchingpassword.php")
    fun ForMatchingpassword(
            @Field("email") email: String,
            @Field("pw") pw: String
    ): Call<CheckPWMatching>

    @FormUrlEncoded
    @POST("MyRecords/Setting/DeleteMember.php")
    fun DeleteMember(
            @Field("email") email: String
    ): Call<String>
}