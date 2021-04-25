package com.privatememo.j.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.privatememo.j.model.datamodel.MemoInfo
import com.privatememo.j.model.Repository
import com.privatememo.j.model.retrofit.Retrofit2Module
import com.privatememo.j.utility.Utility
import kotlin.collections.ArrayList

class EachMemoViewModel(var repository: Repository) : ViewModel() {

    val retrofit2module = Retrofit2Module.getInstance()

    var cateName = String()
    var email = String()
    var cateNum = String()

    var items = MutableLiveData<ArrayList<MemoInfo.MemoInfo2>>()
        get() = repository.MemoList_items
    var controler = MutableLiveData<Boolean>()
    var sortToggle = MutableLiveData<Boolean>()

    init {
        controler.value = false
        sortToggle.value = false
    }

    fun sortButton(){
        if(sortToggle.value == false){
            sortToggle.value = true
        }
        else if(sortToggle.value == true){
            sortToggle.value = false
        }
    }

    fun itemsEmpty(){
        items.value?.clear()
    }


    fun switching(){
        if(items.value?.size == 0){
            controler.value = false
        }
        else{
            controler.value = true
        }
    }

    fun search(Min:Int, Max: Int, SortState: Int){
        items.value?.clear()
        getMemoList_call(Min, Max, SortState)
    }

    fun whenScrolled(Mid:Int, Max: Int, SortState: Int){
        getMemoList_call(Mid, Max, SortState)
    }

    fun getMemoList_call(start:Int, end: Int, SortState: Int){
        Utility.repositoryModule.repositorymodule.getMemoList_call(Integer.parseInt(cateNum),start, end, SortState)
    }

    fun deleteMemo_call(position: Int){
        Utility.repositoryModule.repositorymodule.deleteMemo_call(position)
    }

    fun MemoReverse(){
        Utility.repositoryModule.repositorymodule.MemoReverse()
    }

}