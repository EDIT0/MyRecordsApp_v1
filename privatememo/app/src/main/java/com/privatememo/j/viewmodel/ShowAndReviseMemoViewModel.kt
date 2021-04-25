package com.privatememo.j.viewmodel

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.privatememo.j.model.datamodel.*
import com.privatememo.j.model.retrofit.Retrofit2Module
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ShowAndReviseMemoViewModel : ViewModel() {

    val retrofit2module = Retrofit2Module.getInstance()

    var contentNum = ObservableField<Int>()
    var title = String()
    var memo = String()
    var date = String()
    var revisedate = String()
    var time = String()
    var revisetime = String()
    var ConBookmark = String()
    var email = String()
    var cateNum = ObservableField<Int>()

    var controler = MutableLiveData<Boolean>()
    var items = ObservableArrayList<MemoImageInfo.MemoImageInfo2>()
    var connectionFinish = MutableLiveData<Boolean>()

    var uriHash = HashMap<String, Uri>()
    var sendImageToServer = MutableLiveData<String>()

    lateinit var showImageView: LinearLayout
    var ViewArray = ArrayList<View>()

    var save_original_pic_array = ArrayList<Uri>()


    init {
        controler.value = false
        connectionFinish.value = false
    }


    fun change_layout(){
        if(controler.value == false){
            controler.value = true
        }
        else if(controler.value == true){
            controler.value = false
        }
    }



    fun getMemoImage_call(){

        Log.i("tag","무슨 숫자가 나옵니까? ${contentNum.get()}")
        val call: Call<MemoImageInfo> = retrofit2module.BaseModule().getMemoImage(contentNum.get()!!)

        call.enqueue(object : Callback<MemoImageInfo> {
            override fun onResponse(call: Call<MemoImageInfo>, response: Response<MemoImageInfo>) {
                val result: MemoImageInfo? = response.body()
                Log.i("tag","성공 ${result?.result?.size}")

                for (i in 0 until result?.result?.size!!) {
                    items.add(result.result.get(i))
                }

                for(i in 0 until 5){
                    try {
                        save_original_pic_array.add(Uri.parse(items.get(i).imagePath))
                    }catch (e:Exception){
                        save_original_pic_array.add(Uri.parse(""))
                    }
                }

                Log.i("tag","세이브 오리지날 픽 어레이 사이즈: ${save_original_pic_array.size}")

                for(i in 0 until items.size){
                    Log.i("tag","이미지 목록: ${items.get(i).imagePath}")
                }
                connectionFinish.value = true

            }

            override fun onFailure(call: Call<MemoImageInfo>, t: Throwable) {
                Log.i("??","error")
            }
        })
    }



    fun deleteMemo_call(contentNum: Int){
        val call: Call<String> = retrofit2module.BaseModule().DeleteMemo(contentNum)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val result: String? = response.body()
                Log.i("tag","이거 출력됩니까?")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("??","이거 출력 안됩니까?")
            }
        })
    }


    //수정페이지

    var MemoComment = ObservableField<String>()
    fun completeButton(){
        var imageAddress = "http://edit0@edit0.dothome.co.kr/MyRecords/OnlyImage/"
        Log.i("tag", "컨텐 넘 뭐나옵니까? ${contentNum.get().toString()}")

        if(title != "" && memo != ""){

            var dt = Date()
            var r_date = SimpleDateFormat("yyyy_MM_dd")
            var r_time = SimpleDateFormat("HH_mm_ss")
            var currentDate = r_date.format(dt).toString()
            var currentTime = r_time.format(dt).toString()

            //contentnum(int), title, memo, date, revicedate, time, revicetime, ConBookmark, memberlist_email, category_catenum(int)
            UpdateMemo_call(contentNum.get().toString(), title, memo, date, currentDate, time, currentTime, ConBookmark, email, cateNum.get().toString())

            Log.i("TAG","보냄")
        }
        else{
            MemoComment.set("제목과 내용을 적어주세요.")
            Log.i("TAG","못보냄")
        }

    }

    fun UpdateMemo_call(vararg str: String){
        val call: Call<String> = retrofit2module.BaseModule().UpdateMemo(Integer.parseInt(str[0]), str[1], str[2], str[3],
                str[4], str[5], str[6], str[7], str[8], Integer.parseInt(str[9]))

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val result: String? = response.body()
                Log.i("??","ok")
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("??","error")

                //만약 첫번째 사진이 null이 아니라면 .. 사진 하나라도 있다는 것이니,,
                if(uriHash.size > 0){
                    sendImageToServer.value = "image_yes"
                    Log.i("tag","image yes !!!!!")
                }
                else{
                    sendImageToServer.value = "image_no"
                    Log.i("??","image_no !!!!!")
                    for(i in save_original_pic_array){
                        DeleteImage(i.toString())
                        Log.i("tag","viewmodel 삭제될 이미지 uri ${i.toString()}")
                    }
                    Log.i("tag","아니 이거 실행이,, ${save_original_pic_array.size}")
                }
            }
        })
    }


    fun DeleteImage(imageuri: String){
        val call: Call<String> = retrofit2module.BaseModule().DeleteImage(imageuri)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val result: String? = response.body()
                Log.i("??","ok")
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("??","error")
            }
        })
    }



}