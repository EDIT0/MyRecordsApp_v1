package com.privatememo.j.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChangeFontViewModel : ViewModel() {
    var email = String()
    var fontToggle = MutableLiveData<Boolean>()

    init {
        email = ""
        fontToggle.value = false
    }

    fun fontButton(){
        if(fontToggle.value == false){
            fontToggle.value = true
        }
        else if(fontToggle.value == true){
            fontToggle.value = false
        }
    }
}