package com.privatememo.j.viewmodel

import android.net.Uri
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.privatememo.j.model.datamodel.MemberProfileInfo
import com.privatememo.j.utility.Retrofit2Module
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileManagementViewModel : ViewModel() {

    val retrofit2module = Retrofit2Module.getInstance()

    var items = ObservableArrayList<MemberProfileInfo.MemberProfileInfo2>()

    var email = String()
    var nickname = MutableLiveData<String>()
    var motto = MutableLiveData<String>()
    var picPath = MutableLiveData<String>()

    var pictureUri = ObservableField<Uri>()
    var pictureAddress = ObservableField<String>()
    var completeWorking = MutableLiveData<Boolean>()


    init {
        nickname.value = ""
        motto.value = ""
        picPath.value = ""
        completeWorking.value = false
    }


    fun getMemberProfile_call(){
        val call: Call<MemberProfileInfo> = retrofit2module.BaseModule().getMemberProfile(email)

        call.enqueue(object : Callback<MemberProfileInfo> {
            override fun onResponse(call: Call<MemberProfileInfo>, response: Response<MemberProfileInfo>) {
                val result: MemberProfileInfo? = response.body()

                for (i in 0 until result?.result?.size!!) {
                    items.add(result.result.get(i))
                }
                nickname.setValue(items.get(0).nickname)
                motto.setValue(items.get(0).motto)
                picPath.setValue(items.get(0).picPath)
            }

            override fun onFailure(call: Call<MemberProfileInfo>, t: Throwable) {
                Log.i("??","error")
            }
        })
    }

    var profileComment = ObservableField<String>()
    fun completeButton(){
        if((nickname.value != null && motto.value != null) && (nickname.value != "" && motto.value != "")){
            Log.i("tag","${pictureUri.get().toString().length} / ${pictureUri.get().toString()}")

            UpdateProfile_call(email, nickname.value!!, motto.value!!)

            completeWorking.value = true

            Log.i("TAG","보냄")
        }
        else{
            profileComment.set("프로필을 완성해주세요.")
            Log.i("TAG","못보냄")
        }

    }

    fun UpdateProfile_call(vararg str: String){
        val call: Call<String> = retrofit2module.BaseModule().UpdateProfile(str[0], str[1], str[2])

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