package com.privatememo.j.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.privatememo.j.model.datamodel.CategoryInfo
import com.privatememo.j.model.Repository
import com.privatememo.j.utility.Utility

class CategoryViewModel(var repository: Repository) : ViewModel() {

    var email = ObservableField<String>()
    var controler = MutableLiveData<Boolean>()

    val items: MutableLiveData<ArrayList<CategoryInfo.CategoryInfo2>>
        get() = repository.CategoryList_items


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

    fun getCategoryList_call(){
        Utility.repositoryModule.repositorymodule.getCategoryList_call(email.get().toString())
    }

    fun DeleteCategory(cateNum: Int, position: Int){
        Utility.repositoryModule.repositorymodule.DeleteCategory(cateNum, position, email.get().toString())
    }


}