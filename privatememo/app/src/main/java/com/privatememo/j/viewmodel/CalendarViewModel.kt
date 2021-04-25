package com.privatememo.j.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.privatememo.j.model.datamodel.MemoInfo
import com.privatememo.j.utility.Retrofit2Module
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarViewModel : ViewModel() {

    val retrofit2module = Retrofit2Module.getInstance()

    var items = ObservableArrayList<MemoInfo.MemoInfo2>()
    var total_items = ObservableArrayList<MemoInfo.MemoInfo2>()
    var splitDateArray = ArrayList<List<String>>()
    var email = ObservableField<String>()
    var controler = MutableLiveData<Boolean>()

    var CompleteGettingData = MutableLiveData<Boolean>()
    var CategoryList_catenum = ArrayList<String>()
    var CategoryList_catename = ArrayList<String>()

    var ClickedYear = ObservableField<Int>()
    var ClickedMonth = ObservableField<Int>()
    var ClickedDay =ObservableField<Int>()

    var categoryToggle = MutableLiveData<Boolean>()

    init {
        controler.value = false
        CompleteGettingData.value = false
        categoryToggle.value = false
    }

    fun fontButton(){
        if(categoryToggle.value == false){
            categoryToggle.value = true
        }
        else if(categoryToggle.value == true){
            categoryToggle.value = false
        }
    }

    fun switching(){
        if(items.size == 0){
            controler.value = false
        }
        else{
            controler.value = true
        }
    }

    fun search(){
        items.clear()
        //getCategoryList_call()
    }

    fun getCalendarMemo_call(){

        val call: Call<MemoInfo> = retrofit2module.BaseModule().getCalendarMemo(email.get().toString())

        call.enqueue(object : Callback<MemoInfo> {
            override fun onResponse(call: Call<MemoInfo>, response: Response<MemoInfo>) {
                val result: MemoInfo? = response.body()

                for (i in 0 until result?.result?.size!!) {
                    total_items.add(result.result.get(i))

                    //Log.i("tag","${total_items.get(i).title}}")

                    splitDateArray.add(total_items.get(i).date.split("_"))
                    //Log.i("tag","${splitDateArray.get(i)[0]}, ${splitDateArray.get(i)[1]}, ${splitDateArray.get(i)[2]}")

                }

                CompleteGettingData.value = true

                //Log.i("tag","설명 입니다. ${result?.result?.get(0)?.explanation}")
                //Log.i("tag","싱글톤 객체: ${retrofit2module}")
            }

            override fun onFailure(call: Call<MemoInfo>, t: Throwable) {
                Log.i("??","error")
            }
        })
    }

}