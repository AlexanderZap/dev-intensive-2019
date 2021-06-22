package ru.skillbranch.devintensive.extensions

import androidx.lifecycle.MutableLiveData

fun <T> mutableLiveData(defaultData: T? = null): MutableLiveData<T> {
    val data = MutableLiveData<T>()

    if (defaultData != null) {
        data.value = defaultData
    }

    return data
}