package com.smedina.dive_log_app.ui.user

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.smedina.dive_log_app.data.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val storageService: StorageService
): ViewModel() {

    fun uploadBasicImage(uri: Uri){
        storageService.uploadUserImage(uri)
    }


}