package com.privatememo.j.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.privatememo.j.model.datamodel.ReturnCheck
import com.privatememo.j.model.retrofit.Retrofit2Module
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    val retrofit2module = Retrofit2Module.getInstance()

    var email = ObservableField<String>()
    var password = ObservableField<String>()
    var checkNum = MutableLiveData<String>()
    var nickname = ObservableField<String>()
    var motto = ObservableField<String>()
    var pictureUri = ObservableField<Uri>()
    var pictureAddress = ObservableField<String>()
    var duplicate_email_check = MutableLiveData<String>()


    var emailStatus = ObservableField<Boolean>()
    var email_certificate = ObservableField<Boolean>()
    var sendImageToServer = MutableLiveData<String>()


    init {
        emailStatus.set(true)
        email_certificate.set(false)
        pictureAddress.set("")
    }

    fun duplicateButton(){
        SignUpEmailCheck_call(email.get().toString())
    }


    var random = Random
    var randomNum = 0
    fun checkButton(){
        if(email.get() == null){
            email.set("이메일 입력")
        }else{
            randomNum = random.nextInt(100000, 999999)

            SignUpEmailSender_call(email.get().toString(), Integer.toString(randomNum))
        }

    }

    var profileComment = ObservableField<String>()
    fun completeButton(){
        var imageAddress = "http://edit0@edit0.dothome.co.kr/MyRecords/OnlyImage/Profile/"

        if((email.get() != null && password.get() != null && nickname.get() != null && motto.get() != null) &&
            (email.get() != "" && password.get() != "" && nickname.get() != "" && motto.get() != "") && (duplicate_email_check.getValue() != "true") &&
            (checkNum.value!!.contains("인증완료")) && (pictureUri.get().toString().length > 5)){
            Log.i("tag","${pictureUri.get().toString().length} / ${pictureUri.get().toString()}")

            if(pictureUri.get() != null){
                sendImageToServer.value = "image_yes"
            }
            else{
                sendImageToServer.value = "image_no"
            }

            SignUpInsert_call(email.get().toString(), password.get().toString(),
                nickname.get().toString(), motto.get().toString(), imageAddress+pictureAddress.get().toString())
            //http://edit0@edit0.dothome.co.kr/MyRecords/OnlyImage/akdmadl34%40naver.com.png

            Log.i("TAG","보냄")
        }
        else{
            profileComment.set("프로필을 완성해주세요.")
            Log.i("TAG","못보냄")
        }

    }





    fun SignUpEmailSender_call(email: String, num: String){
        val call: Call<String> = retrofit2module.BaseModule().EmailSender(email, num)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val result: String? = response.body()
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("??","error")
            }
        })
    }

    fun SignUpInsert_call(vararg str: String){
        val call: Call<String> = retrofit2module.BaseModule().ProfileInsert(str[0], str[1], str[2], str[3], str[4])

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val result: String? = response.body()
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("??","error")
            }
        })
    }

    fun SignUpEmailCheck_call(email: String){
        val call: Call<ReturnCheck> = retrofit2module.BaseModule().ProfileEmailCheck(email)

        call.enqueue(object : Callback<ReturnCheck> {
            override fun onResponse(call: Call<ReturnCheck>, response: Response<ReturnCheck>) {
                val result: ReturnCheck? = response.body()
                duplicate_email_check.setValue(result?.returnvalue!!)
            }
            override fun onFailure(call: Call<ReturnCheck>, t: Throwable) {
                Log.i("??","error")
            }
        })

    }



}