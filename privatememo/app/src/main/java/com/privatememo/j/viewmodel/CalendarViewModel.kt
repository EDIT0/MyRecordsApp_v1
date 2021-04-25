package com.privatememo.j.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.privatememo.j.model.Repository
import com.privatememo.j.model.datamodel.CategoryInfo
import com.privatememo.j.model.datamodel.MemoInfo
import com.privatememo.j.model.retrofit.Retrofit2Module
import com.privatememo.j.utility.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarViewModel(var repository: Repository) : ViewModel() {

    val retrofit2module = Retrofit2Module.getInstance()

    var items = ArrayList<MemoInfo.MemoInfo2>()
    var total_items = MutableLiveData<ArrayList<MemoInfo.MemoInfo2>>()
        get() = repository.Calendar_TotalItems
    var splitDateArray = ArrayList<List<String>>()

    var list = ArrayList<String>()


    var email = ObservableField<String>()
    var controler = MutableLiveData<Boolean>()
    var categoryToggle = MutableLiveData<Boolean>()

    var CategoryList_catenum = ArrayList<String>()
    var CategoryList_catename = ArrayList<String>()

    var ClickedYear = ObservableField<Int>()
    var ClickedMonth = ObservableField<Int>()
    var ClickedDay =ObservableField<Int>()



    init {
        controler.value = false
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

    fun insertCategoryintoList(){
        with(Utility.repositoryModule.repositorymodule){
            if(!CategoryList_items.value.isNullOrEmpty()) {
                for (i in 0 until CategoryList_items.value?.size!!) {
                    CategoryList_catenum.add(CategoryList_items.value?.get(i)?.catenum!!)
                    CategoryList_catename.add(CategoryList_items.value?.get(i)?.catename!!)

                    list.add(CategoryList_catename.get(i))
                }
            }
        }
    }

    fun setSplitDate(){
        for(i in 0 until total_items.value?.size!!){
            splitDateArray.add(total_items.value?.get(i)?.date?.split("_")!!)
        }
    }

    fun getCalendarMemo_call(){
        Utility.repositoryModule.repositorymodule.getCalendarMemo_call(email.get().toString())
        /*val call: Call<MemoInfo> = retrofit2module.BaseModule().getCalendarMemo(email.get().toString())

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
            }

            override fun onFailure(call: Call<MemoInfo>, t: Throwable) {
                Log.i("??","error")
            }
        })*/
    }

}