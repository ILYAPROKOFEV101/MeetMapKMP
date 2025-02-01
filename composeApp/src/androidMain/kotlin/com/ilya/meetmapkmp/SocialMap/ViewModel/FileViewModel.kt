package com.ilya.meetmapkmp.SocialMap.ViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

    // Список файлов и их имен
    private val _fileList = MutableLiveData<List<Pair<File, String>>>(emptyList())
    val fileList: LiveData<List<Pair<File, String>>> = _fileList


    // Установка состояния загрузки
    fun setUploadingState(isUploading: Boolean) {
        _isUploading.postValue(isUploading)
    }

    // Добавление файла с именем
    fun addFile(file: File, filename: String) {
        val currentFiles = _fileList.value?.toMutableList() ?: mutableListOf()
        currentFiles.add(file to filename)
        _fileList.value = currentFiles
        _fileList.postValue(currentFiles)
        Log.d("FileViewModel", "Файл добавлен: $filename, всего файлов: ${currentFiles.size}")
    }

    // Удаление файла из списка
    fun removeFile(file: File) {
        val currentFiles = _fileList.value.orEmpty().toMutableList()
        currentFiles.removeAll { it.first == file }
        _fileList.postValue(currentFiles)

    }

    // Получение всех файлов
    fun getAllFiles(): List<File> {
        return _fileList.value.orEmpty().map { it.first }
    }

    // Получение всех имен файлов
    fun getAllFileNames(): List<String> {
        return _fileList.value.orEmpty().map { it.second }
    }
    fun getFileandFileNmae() : List<Pair<File, String>> {
        return _fileList.value.orEmpty()
    }

    fun clearFileList() {
        _fileList.postValue(emptyList())
    }

}

