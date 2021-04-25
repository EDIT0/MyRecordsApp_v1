package com.privatememo.j.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.privatememo.j.model.Repository
import com.privatememo.j.model.datamodel.*
import com.privatememo.j.model.retrofit.Retrofit2Module
import com.privatememo.j.utility.Utility

class SearchViewModel(var repository: Repository) : ViewModel() {

    val retrofit2module = Retrofit2Module.getInstance()

    val items: MutableLiveData<ArrayList<SearchInfo.SearchInfo2>>
        get() = repository.SearchList_items
    var email = ObservableField<String>()
    var controler = MutableLiveData<Boolean>()
    var keyword = String()


    init {
        controler.value = false
    }

    fun switching(){
        if(items.value?.size == 0){
            controler.value = false
        }
        else{
            controler.value = true
        }
    }

    fun search(Min:Int, Max: Int){
        items.value?.clear()
        getSearchResult_call(Min, Max)
    }

    fun whenScrolled(Mid:Int, Max: Int){
        getSearchResult_call(Mid, Max)
    }


    fun getSearchResult_call(start:Int, end: Int){
        Utility.repositoryModule.repositorymodule.getSearchResult_call(email.get().toString(), keyword, start, end)
    }

    fun deleteMemoInSearch_call(position: Int){
        Utility.repositoryModule.repositorymodule.deleteMemoInSearch_call(position)
    }

}