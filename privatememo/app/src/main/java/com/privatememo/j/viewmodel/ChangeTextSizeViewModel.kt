package com.privatememo.j.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChangeTextSizeViewModel : ViewModel() {

    var email = String()
    var titleToggle = MutableLiveData<Boolean>()
    var contentToggle = MutableLiveData<Boolean>()

    init {
        email = ""
        titleToggle.value = false
        contentToggle.value = false
    }

    fun titleButton(){
        if(titleToggle.value == false){
            titleToggle.value = true
        }
        else if(titleToggle.value == true){
            titleToggle.value = false
        }
    }

    fun contentButton(){
        if(contentToggle.value == false){
            contentToggle.value = true
        }
        else if(contentToggle.value == true){
            contentToggle.value = false
        }
    }

}