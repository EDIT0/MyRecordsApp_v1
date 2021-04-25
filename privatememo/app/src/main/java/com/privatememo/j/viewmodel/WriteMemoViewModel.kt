package com.privatememo.j.viewmodel

import android.net.Uri
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.privatememo.j.datamodel.ContentNumberInfo
import com.privatememo.j.utility.Retrofit2Module
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class WriteMemoViewModel : ViewModel() {

    val retrofit2module = Retrofit2Module.getInstance()

    var title = String()
    var textMemo = ObservableField<String>()

    var email = String()
    var cateNum = String()

    var uriHash = HashMap<String, Uri>()
    var sendImageToServer = MutableLiveData<String>()

    var FromCalendar = String()

    init {
        FromCalendar = "000"
    }


    var MemoComment = ObservableField<String>()
    fun completeButton(){
        var imageAddress = "http://edit0@edit0.dothome.co.kr/MyRecords/OnlyImage/"

        if(title != "" && textMemo.get().toString() != "" && textMemo.get() != null){

            var dt = Date()
            var date = SimpleDateFormat("yyyy_MM_dd")
            var time = SimpleDateFormat("HH_mm_ss")
            var currentDate = date.format(dt).toString()
            var currentTime = time.format(dt).toString()

            if(FromCalendar != "000"){
                currentDate = FromCalendar
                currentTime = "00_00_00"
            }

            MemoInsert_call(title, textMemo.get().toString(), currentDate, currentTime, email, cateNum)
            //http://edit0@edit0.dothome.co.kr/MyRecords/OnlyImage/akdmadl34%40naver.com.png

            Log.i("TAG","보냄")
        }
        else{
            MemoComment.set("제목과 내용을 적어주세요.")
            Log.i("TAG","못보냄")
        }

    }


    var contentnum = String() //메모 넣고 나온 auto숫자로 이미지 저장을 위한
    fun MemoInsert_call(vararg str: String){
        val call: Call<String> = retrofit2module.BaseModule().MemoInsert(str[0], str[1], str[2], str[3], str[4], Integer.parseInt(str[5]))

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val result: String? = response.body()
                Log.i("??","ok")
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("??","error MemoInsert_Call()")
                getImageStorageContentNumber_call(str[2], str[3], str[5])
            }
        })
    }

    fun getImageStorageContentNumber_call(vararg str: String){
        val call: Call<ContentNumberInfo> = retrofit2module.BaseModule().getImageStorageContentNumber(str[0], str[1], Integer.parseInt(str[2]))

        Log.i("tag","들어가는 정보: ${str[0]} ${str[1]} ${str[2]}")
        call.enqueue(object : Callback<ContentNumberInfo> {
            override fun onResponse(call: Call<ContentNumberInfo>, response: Response<ContentNumberInfo>) {
                val result: ContentNumberInfo? = response.body()

                contentnum = result?.contentnum!!
                Log.i("tag","콘텐트 넘버 ${contentnum}")

                //만약 첫번째 사진이 null이 아니라면 .. 사진 하나라도 있다는 것이니,,
                if(uriHash.size > 0){
                    sendImageToServer.value = "image_yes"
                }
                else{
                    sendImageToServer.value = "image_no"
                }

                Log.i("??","ok")
            }
            override fun onFailure(call: Call<ContentNumberInfo>, t: Throwable) {
                Log.i("??","error...")
            }
        })
    }
}