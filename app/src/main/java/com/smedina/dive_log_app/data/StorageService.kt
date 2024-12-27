package com.smedina.dive_log_app.data

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject

class StorageService @Inject constructor(private val storage: FirebaseStorage) {

    fun basicExample(){
        val reference = storage.reference.child("ejemplo/chica.jpg")

        reference.name // --> chica.jpg
        reference.path // --> ejemplo/chica.jpg
        reference.bucket // -> gs://divelogapp-2330ac.firebasestorage.app/ejemplo

    }

    fun uploadUserImage(uri: Uri){
        val imageTitle = uri.lastPathSegment
        val reference = storage.reference.child("ejemplo/$imageTitle")
        reference.putFile(uri)

    }
}