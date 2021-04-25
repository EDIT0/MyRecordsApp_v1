package com.privatememo.j.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.privatememo.j.datamodel.OnlyPicInfo
import com.privatememo.j.model.Repository
import com.privatememo.j.utility.Retrofit2Module
import com.privatememo.j.utility.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnlyPicViewModel(var repository: Repository) : ViewModel(){

    val retrofit2module = Retrofit2Module.getInstance()

    var items = MutableLiveData<ArrayList<OnlyPicInfo.OnlyPicInfo2>>()
        get() = repository.OnlyPicList_items
    var email = ObservableField<String>()
    var controler = MutableLiveData<Boolean>()

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

    /*fun search(Min: Int, Max: Int){
        items.value?.clear()
        //getOnlyPic_call(Min, Max)
    }

    fun whenScrolled(Mid: Int, Max: Int){
        //getOnlyPic_call(Mid, Max)
    }*/


    fun getOnlyPic_call(){
        Utility.repositoryModule.repositorymodule.getOnlyPic_call(email.get().toString())

    }
    fun getOnlyPicPart_call(){
        Utility.repositoryModule.repositorymodule.getOnlyPicPart_call(email.get().toString())
    }


    fun deleteMemo_call(position: Int){
        //Utility.repositoryModule.repositorymodule.deleteMemo_call(position)
    }



}