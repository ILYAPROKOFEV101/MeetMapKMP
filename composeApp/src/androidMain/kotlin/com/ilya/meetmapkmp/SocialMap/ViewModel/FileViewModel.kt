package com.ilya.meetmapkmp.SocialMap.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class FileViewModel : ViewModel() {
    // Переменная для хранения глобального файла
    internal val _globalFile = MutableLiveData<File?>()

    // Список для хранения имен файлов
    val fileNameList = mutableStateListOf<String>()

    fun setGlobalFile(file: File?) {
        _globalFile.value = file
    }
}
