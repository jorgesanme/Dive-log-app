package com.smedina.dive_log_app.ui.features.main

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smedina.dive_log_app.data.StorageService
import com.smedina.dive_log_app.utils.ParamName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val storageService: StorageService
) : ViewModel() {

    private val _uriList = MutableStateFlow<List<String>>(emptyList())
    val uriList: StateFlow<List<String>> = _uriList

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun uploadAndGetImage(uri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                withContext(Dispatchers.IO) {
                    storageService.uploadAnDownloadImageList(uri)
                }
            } catch (e: Exception) {
                Log.i("Error de Image", e.message.orEmpty())
            }
            _isLoading.value = false
            getAllImage()
        }
    }

    fun getAllImage() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = async {
                    withContext(Dispatchers.IO) {
                        storageService.getAllImage().map { it.toString() }
                    }
                }.await()
                _uriList.value = result
            } catch (e: Exception) {
                Log.i(ParamName.LOG_TAG, e.message.orEmpty())
            }
            _isLoading.value = false
        }
    }


    fun deletePhoto(imageRef: String) {
        viewModelScope.launch {
            _isLoading.value = true

            val result = withContext(Dispatchers.IO){
                storageService.removeImage(imageRef = imageRef)
            }
            delay(200)
            when (!result) {
                true -> {getAllImage()}
                false -> {getAllImage()}
            }
            _isLoading.value = false
        }
    }
}