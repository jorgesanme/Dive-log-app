package com.smedina.dive_log_app.ui.features.user

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smedina.dive_log_app.data.StorageService
import com.smedina.dive_log_app.utils.ParamName.Companion.USER_IMAGE_STORAGE_REFERENCE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val storageService: StorageService
): ViewModel() {

    private val _userImage = MutableStateFlow<Uri?>(null)
    val userImage: StateFlow<Uri?> = _userImage

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        initUserImage()
    }

    fun uploadAndGetImage(uri: Uri){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = async{withContext(Dispatchers.IO){
                    storageService.uploadAndDownLoadUserImage(uri)}}.await()
                _userImage.value = result
            }catch (e: Exception){
                Log.i("Error de Image", e.message.orEmpty())
            }
            _isLoading.value = false
        }
    }

    private fun initUserImage(){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = async {  withContext(Dispatchers.IO){
                    storageService.downLoadUserImage(USER_IMAGE_STORAGE_REFERENCE)}}.await()
                _userImage.value = result
            }catch (e: Exception){
                Log.i("Error de Image", e.message.orEmpty())
            }
            _isLoading.value = false
        }
    }

    fun deletePhoto(imageRef: String){
        storageService.removeImage(imageRef = imageRef )
    }

}