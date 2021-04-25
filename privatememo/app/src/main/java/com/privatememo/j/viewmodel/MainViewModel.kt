package com.privatememo.j.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.privatememo.j.model.datamodel.CategoryInfo
import com.privatememo.j.model.Repository
import com.privatememo.j.utility.Retrofit2Module
import com.privatememo.j.utility.Utility

class MainViewModel(var repository: Repository) : ViewModel(){

    val retrofit2module = Retrofit2Module.getInstance()

    var email = MutableLiveData<String>()
    var nickname = MutableLiveData<String>()
    var motto = MutableLiveData<String>()
    var picPath = MutableLiveData<String>()
    var totalCateNum = MutableLiveData<Int>()
    var totalConNum = MutableLiveData<Int>()
        get() = repository.TotalConNum

    var password = String()

    var items = ObservableArrayList<CategoryInfo.CategoryInfo2>()

    init {
        totalCateNum.value = 0
        totalConNum.value = 0
    }

    fun getMemoCount_call(){
        Utility.repositoryModule.repositorymodule.getMemoCount_call(email.value.toString())
    }
}