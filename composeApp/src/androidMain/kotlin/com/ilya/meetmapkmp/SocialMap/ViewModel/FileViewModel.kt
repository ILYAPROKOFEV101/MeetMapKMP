package com.ilya.meetmapkmp.SocialMap.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class FileViewModel : ViewModel() {
    private val _isUploading = MutableLiveData(false)
    val isUploading: LiveData<Boolean> = _isUploading

    fun setUploadingState(isUploading: Boolean) {
        _isUploading.postValue(isUploading) // Используем postValue для фоновых потоков
    }

    internal val _globalFile = MutableLiveData<File?>()

    fun setGlobalFile(file: File?) {
        _globalFile.postValue(file) // Используем postValue
    }
}
