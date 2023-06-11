package com.nishanth.callrecorder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel() : ViewModel() {
    fun updateContact(value: Contact?) {
        contact?.value =value
    }

    var contact: MutableLiveData<Contact>? = MutableLiveData()
}