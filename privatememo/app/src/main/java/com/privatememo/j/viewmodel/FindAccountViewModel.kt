package com.privatememo.j.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.privatememo.j.model.datamodel.ReturnCheck
import com.privatememo.j.utility.Retrofit2Module
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class FindAccountViewModel : ViewModel() {
    val retrofit2module = Retrofit2Module.getInstance()

    var email = ObservableField<String>()
    var password = ObservableField<String>()
    var duplicate_email_check = MutableLiveData<String>()
    var stepOne = MutableLiveData<Boolean>()
    var AuthenticationNumber = String()




    init {
        email.set("akdmadl34@naver.com")
        stepOne.value = false
    }


    //1단계
    fun duplicateButton(){
        SignUpEmailCheck_call(email.get().toString())
    }

    var random = Random
    var randomNum = 0
    fun checkButton(){
        randomNum = random.nextInt(100000, 999999)
        SignUpEmailSender_call(email.get().toString(), Integer.toString(randomNum))
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

    fun SignUpEmailCheck_call(email: String){
        val call: Call<ReturnCheck> = retrofit2module.BaseModule().ProfileEmailCheck(email)

        call.enqueue(object : Callback<ReturnCheck> {
            override fun onResponse(call: Call<ReturnCheck>, response: Response<ReturnCheck>) {
                val result: ReturnCheck? = response.body()
                duplicate_email_check.setValue(result?.returnvalue!!)

                if(duplicate_email_check.value.toString() == "true"){
                    checkButton() //난수와 함께 입력된 이메일로 전송..
                    stepOne.value = true
                }
                else{
                    //유효하지 않은 이메일
                    stepOne.value = false
                    Log.i("tag","유효하지 않은 이메일")
                }

            }
            override fun onFailure(call: Call<ReturnCheck>, t: Throwable) {
                Log.i("??","error")
            }
        })
    }

    //2단계
    fun AuthenticationNumberCheck(){

    }
}