package com.privatememo.j.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.privatememo.j.model.datamodel.*
import com.privatememo.j.model.retrofit.Retrofit2Module
import com.privatememo.j.utility.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class Repository {

    companion object mo {
        private var instance: Repository? = null

        @JvmStatic
        fun getInstance(): Repository = instance ?: synchronized(this){
            instance ?: Repository().also {
                instance = it
            }
        }
    }

    val retrofit2module = Retrofit2Module.getInstance()

    var CategoryList_items = MutableLiveData<ArrayList<CategoryInfo.CategoryInfo2>>()
    var TotalConNum = MutableLiveData<Int>()
    var OnlyPicList_items = MutableLiveData<ArrayList<OnlyPicInfo.OnlyPicInfo2>>()
    var MemoList_items = MutableLiveData<ArrayList<MemoInfo.MemoInfo2>>()
    var MemoList_items_arrayList = ArrayList<MemoInfo.MemoInfo2>()
    var SearchList_items = MutableLiveData<ArrayList<SearchInfo.SearchInfo2>>()
    var SearchList_items_arrayList = ArrayList<SearchInfo.SearchInfo2>()
    var Calendar_TotalItems = MutableLiveData<ArrayList<MemoInfo.MemoInfo2>>()




    fun getCalendarMemo_call(email: String){

        val call: Call<MemoInfo> = retrofit2module.BaseModule().getCalendarMemo(email)

        call.enqueue(object : Callback<MemoInfo> {
            override fun onResponse(call: Call<MemoInfo>, response: Response<MemoInfo>) {
                val result: MemoInfo? = response.body()

                if(!result?.result.isNullOrEmpty()){
                    Calendar_TotalItems.setValue(result?.result)
                }
                else{
                    Calendar_TotalItems.setValue(null)
                }

                Log.i("live","캘린더 토탈아이템")
            }

            override fun onFailure(call: Call<MemoInfo>, t: Throwable) {
                Log.i("??","error")
            }
        })
    }




    fun getSearchResult_call(email: String, keyword: String, start:Int, end: Int){

        val call: Call<SearchInfo> = retrofit2module.BaseModule().getSearchResult(email, keyword, start, end)

        call.enqueue(object : Callback<SearchInfo> {
            override fun onResponse(call: Call<SearchInfo>, response: Response<SearchInfo>) {
                val result: SearchInfo? = response.body()

                if(result?.result != null){
                    SearchList_items_arrayList.addAll(result?.result)
                    SearchList_items.setValue(SearchList_items_arrayList)
                }
            }

            override fun onFailure(call: Call<SearchInfo>, t: Throwable) {
                Log.i("??","error")
            }
        })
    }


    fun deleteMemoInSearch_call(position: Int){
        val call: Call<String> = retrofit2module.BaseModule().DeleteMemo(SearchList_items.value?.get(position)!!.contentnum)

        SearchList_items_arrayList.removeAt(position)
        SearchList_items.setValue(SearchList_items_arrayList)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val result: String? = response.body()
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
            }
        })
    }



    fun getMemoList_call(cateNum: Int, start:Int, end: Int, SortState: Int){

        if(start == 0){
            MemoList_items.value?.clear()
            MemoList_items_arrayList.clear()
        }

        val call: Call<MemoInfo> = retrofit2module.BaseModule().getMemoList(cateNum, start, end, SortState)

        call.enqueue(object : Callback<MemoInfo> {
            override fun onResponse(call: Call<MemoInfo>, response: Response<MemoInfo>) {
                val result: MemoInfo? = response.body()

                if(Utility.EachMemoFloating.FloatingState == 1){
                    MemoReverse()
                }

                if(result?.result != null){
                    MemoList_items_arrayList.addAll(result.result)
                    MemoList_items.setValue(MemoList_items_arrayList)
                }

                if(Utility.EachMemoFloating.FloatingState == 1){
                    MemoReverse()
                }

            }

            override fun onFailure(call: Call<MemoInfo>, t: Throwable) {
                Log.i("??","error")
            }
        })
    }


    fun deleteMemo_call(position: Int){
        val call: Call<String> = retrofit2module.BaseModule().DeleteMemo(MemoList_items.value?.get(position)!!.contentnum)

        //MemoList_items.value?.removeAt(position)
        MemoList_items_arrayList.removeAt(position)
        MemoList_items.setValue(MemoList_items_arrayList)

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

    fun MemoReverse(){
        Collections.reverse(MemoList_items.value)
        //Collections.reverse(MemoList_items_arrayList)
        MemoList_items.setValue(MemoList_items_arrayList)
    }



    fun getOnlyPic_call(email: String){
        var end = 6
        var start = 0

        val call: Call<OnlyPicInfo> = retrofit2module.BaseModule().getOnlyPic(email, start, end)

        call.enqueue(object : Callback<OnlyPicInfo> {
            override fun onResponse(call: Call<OnlyPicInfo>, response: Response<OnlyPicInfo>) {
                val result: OnlyPicInfo? = response.body()

                OnlyPicList_items.setValue(result?.result)

            }

            override fun onFailure(call: Call<OnlyPicInfo>, t: Throwable) {
                Log.i("??","error")
            }
        })
    }


    fun getOnlyPicPart_call(email: String){
        var end = OnlyPicList_items.value?.size!!
        var start = end - 6
        if(end < 6){
            end = 6
        }
        else{
            if(end%6 == 0){
                end += 6
                start = end - 6
            }
            else{
                end += (6-OnlyPicList_items.value?.size!! %6)
                start = (end-6) + OnlyPicList_items.value?.size!!%6
            }

        }
        if(start < 0) {start = 0}


        val call: Call<OnlyPicInfo> = retrofit2module.BaseModule().getOnlyPic(email, start, end)

        call.enqueue(object : Callback<OnlyPicInfo> {
            override fun onResponse(call: Call<OnlyPicInfo>, response: Response<OnlyPicInfo>) {
                val result: OnlyPicInfo? = response.body()

                if(result?.result != null) {
                    if(end == 6){

                    }
                    else {
                        OnlyPicList_items.value?.addAll(result?.result)
                        OnlyPicList_items.setValue(OnlyPicList_items.value)
                        Log.i("live", "더보기 호출")
                    }
                }
            }

            override fun onFailure(call: Call<OnlyPicInfo>, t: Throwable) {
                Log.i("??","error")
            }
        })
    }

    /*fun deleteMemo_call(position: Int){
        val call: Call<String> = retrofit2module.BaseModule().DeleteMemo(OnlyPicList_items.value?.get(position)?.contentnum!!)

        OnlyPicList_items.value?.removeAt(position)
        OnlyPicList_items.setValue(OnlyPicList_items.value)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val result: String? = response.body()
                Log.i("tag","이거 출력됩니까?")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("??","이거 출력 안됩니까?")
            }
        })
    }*/


    fun getMemoCount_call(email : String){
        val call: Call<MemoCountInfo> = retrofit2module.BaseModule().getMemoCount(email)

        call.enqueue(object : Callback<MemoCountInfo> {
            override fun onResponse(call: Call<MemoCountInfo>, response: Response<MemoCountInfo>) {
                val result: MemoCountInfo? = response.body()
                TotalConNum.setValue(Integer.parseInt(result?.MemoCount))
                Log.i("live","메모갯수 찾기1 ${TotalConNum.value.toString()}")
            }

            override fun onFailure(call: Call<MemoCountInfo>, t: Throwable) {
                TotalConNum.setValue(0)
                Log.i("live","메모갯수 찾기2 ${TotalConNum.value.toString()}")
            }
        })
    }


    fun getCategoryList_call(email : String){
        CategoryList_items.value?.clear()
        val call: Call<CategoryInfo> = retrofit2module.BaseModule().getCategoryList(email)

        call.enqueue(object : Callback<CategoryInfo> {
            override fun onResponse(call: Call<CategoryInfo>, response: Response<CategoryInfo>) {
                val result: CategoryInfo? = response.body()

                CategoryList_items.setValue(result?.result)
            }

            override fun onFailure(call: Call<CategoryInfo>, t: Throwable) {
                Log.i("??","error")
            }
        })
    }

    fun DeleteCategory(cateNum: Int, position: Int, email: String){
        Log.i("live","삭제될 데이터: ${CategoryList_items.value?.get(position)}")
        CategoryList_items.value?.removeAt(position)
        CategoryList_items.setValue(CategoryList_items.value)
        val call: Call<String> = retrofit2module.BaseModule().DeleteCategory(cateNum)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val result: String? = response.body()
                Log.i("??","딜리트카테고리ok")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("??","딜리트카테고리error")
                getMemoCount_call(email)
            }
        })
    }
}