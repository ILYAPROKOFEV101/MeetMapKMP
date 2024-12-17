package com.ilya.meetmapkmp.SocialMap.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class FileViewModel : ViewModel() {

    // Состояние загрузки
    private val _isUploading = MutableLiveData(false)
    val isUploading: LiveData<Boolean> = _isUploading

    private var filename = mutableStateListOf<String>()
    // Список файлов
    internal val _globalFiles = MutableLiveData<List<File?>>()
    val globalFiles: LiveData<List<File?>> = _globalFiles

    // Установка состояния загрузки
    fun setUploadingState(isUploading: Boolean) {
        _isUploading.postValue(isUploading)
    }


    // Установка списка файлов
    fun setGlobalFiles(files: List<File?>) {
        _globalFiles.postValue(files)
    }

    // Добавление одного файла
    fun addFile(file: File?) {
        val currentFiles = _globalFiles.value.orEmpty().toMutableList()
        file?.let { currentFiles.add(it) }
        setGlobalFiles(currentFiles)
    }

    // Очистка всех файлов
    fun clearGlobalFiles() {
        _globalFiles.postValue(emptyList())
    }
    // Сохронеие и получение названий файлов
    fun saveFileNames(fileNames: List<String>) {
        filename = fileNames.toMutableStateList()
    }
    fun getFileNames(): List<String> {
        return filename
    }
    fun clearFileNames(){
        filename.clear()
    }


}
