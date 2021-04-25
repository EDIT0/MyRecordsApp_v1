package com.privatememo.j.viewmodel

import android.net.Uri
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.privatememo.j.model.retrofit.Retrofit2Module
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviseCategoryViewModel : ViewModel() {

    val retrofit2module = Retrofit2Module.getInstance()

    var cateNum = ObservableField<Int>()
    var cateName = String()
    var cateExplanation = String()
    var catepicPath = String()
    var email = String()
    var CateBookmark = String()

    var categoryComment = ObservableField<String>()
    var pictureUri = ObservableField<Uri>()
    var pictureAddress = ObservableField<String>()
    var sendImageToServer = MutableLiveData<String>()

    init {
        pictureAddress.set("")
    }



    fun completeButton(){
        var imageAddress = "http://edit0@edit0.dothome.co.kr/MyRecords/OnlyImage/Category/"
        if((cateName != null && cateExplanation != null) && (cateName != "" && cateExplanation != "")){

            if(pictureUri.get() != Uri.parse("")){
                sendImageToServer.value = "image_yes"
                Log.i("tag","이미지 예스")
            }
            else{
                sendImageToServer.value = "image_no"
                Log.i("tag","이미지 노")
            }

            UpdateCategoryImageDelete_call()

            Thread.sleep(300)

            UpdateCategory_call(email, cateName,
                cateExplanation, imageAddress+pictureAddress.get().toString(), cateNum.get().toString(), CateBookmark)
            //http://edit0@edit0.dothome.co.kr/MyRecords/OnlyImage/akdmadl34%40naver.com.png

            Log.i("TAG","보냄")
        }
        else{
            categoryComment.set("프로필을 완성해주세요.")
            Log.i("TAG","못보냄")
        }
    }

    fun UpdateCategory_call(vararg str: String){
        val call: Call<String> = retrofit2module.BaseModule().UpdateCategory(str[0], str[1], str[2], str[3], Integer.parseInt(str[4]), str[5])

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val result: String? = response.body()
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("??","error")
            }
        })
    }

    fun UpdateCategoryImageDelete_call(){
        val call: Call<String> = retrofit2module.BaseModule().UpdateCategoryImageDelete(Integer.parseInt(cateNum.get().toString()))

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val result: String? = response.body()
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("??","error")
            }
        })
    }

}