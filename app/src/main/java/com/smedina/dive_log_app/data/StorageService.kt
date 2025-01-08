package com.smedina.dive_log_app.data

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.UploadTask.TaskSnapshot
import com.google.firebase.storage.storageMetadata
import com.smedina.dive_log_app.utils.ParamName
import com.smedina.dive_log_app.utils.ParamName.Companion.LIST_OF_IMAGE_STORAGE_REFERENCE
import com.smedina.dive_log_app.utils.ParamName.Companion.LOG_TAG
import com.smedina.dive_log_app.utils.ParamName.Companion.USER_IMAGE_STORAGE_REFERENCE
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class StorageService @Inject constructor(private val storage: FirebaseStorage) {

    suspend fun downLoadUserImage(storeRef: String): Uri {
        val reference = storage.reference.child(storeRef)
//        readMetaDataBasic()
        readMetaDataAdvance()
        return reference.downloadUrl.await()
    }

    suspend fun uploadAnDownloadImageList(uri: Uri): List<Uri> {
        return suspendCancellableCoroutine<List<Uri>> { cancelableContinuation ->
            val reference = storage.reference.child(
                "${LIST_OF_IMAGE_STORAGE_REFERENCE}${uri.lastPathSegment}"
            )
            reference.putFile(uri).addOnSuccessListener { it ->
                downLoadImageList(it, cancelableContinuation)
            }.addOnFailureListener {
                cancelableContinuation.resumeWithException(it)
            }
        }
    }

    private fun downLoadImageList(
        uploadTask: TaskSnapshot,
        cancelableContinuation: CancellableContinuation<List<Uri>>
    ) {
        val list: MutableList<Uri> = mutableListOf()
        uploadTask.storage.listAll().addOnSuccessListener { result ->
            result.items.map { it ->
                it.downloadUrl.addOnSuccessListener { uri ->
                    list.add(uri)
                }.addOnFailureListener {
                    cancelableContinuation.resumeWithException(it)
                }
            }
            cancelableContinuation.resume(list)
        }.addOnFailureListener { cancelableContinuation.resumeWithException(it) }
    }


    suspend fun uploadAndDownLoadUserImage(uri: Uri): Uri {
        return suspendCancellableCoroutine<Uri> { cancellableContinuation ->
            val reference = storage.reference.child(USER_IMAGE_STORAGE_REFERENCE)
            reference.putFile(uri, createMetaData()).addOnSuccessListener {
                downLoadImage(it, cancellableContinuation)
            }.addOnFailureListener {
                cancellableContinuation.resumeWithException(it)
            }
        }
    }


    private fun downLoadImage(
        uploadTask: UploadTask.TaskSnapshot,
        cancellableContinuation: CancellableContinuation<Uri>
    ) {
        uploadTask.storage.downloadUrl
            .addOnSuccessListener { uri ->
                cancellableContinuation.resume(uri) { cause: Throwable ->
                    cancellableContinuation.resumeWithException(cause)
                }
            }
            .addOnFailureListener { cancellableContinuation.resumeWithException(it) }
    }

    fun removeImage(imageRef: String): Boolean {
        var isSuccess = false
        val reference = storage.getReferenceFromUrl(imageRef)
        reference.delete()
            .addOnSuccessListener { isSuccess = true }
            .addOnFailureListener { isSuccess = false }
        return isSuccess
    }


    private fun createMetaData(): StorageMetadata = storageMetadata {
        contentType = "image/jpg"
        setCustomMetadata("date", "23-04-1975")
        setCustomMetadata("userType", "pro-elite")
    }


    private suspend fun readMetaDataAdvance() {
        val response = storage.reference.child(USER_IMAGE_STORAGE_REFERENCE).metadata.await()
        response.customMetadataKeys.forEach { key ->
            response.getCustomMetadata(key)?.let { value ->
                Log.i(
                    LOG_TAG,
                    "$key:\t $value"
                )
            }
        }
    }


    suspend fun getAllImage(): List<Uri> {
        val reference = storage.reference.child(LIST_OF_IMAGE_STORAGE_REFERENCE)
        val list = reference.listAll().await().items.map { it.downloadUrl.await() }
        return list

        /* PARA RECOPILAR EL LISTADO DE UN HIJO
        val reference = storage.reference.child("ejemplo/")
        reference.listAll().addOnSuccessListener { result ->
            result.items.forEach {
                Log.i("diveLog", it.name)
            }
        }*/
    }

    /** Ejemplo */
    private fun uploadImageWithProgress(uri: Uri) {
        val reference = storage.reference.child(USER_IMAGE_STORAGE_REFERENCE)
        reference.putFile(uri).addOnProgressListener { uploadTask ->
            val progress = (100.0 * uploadTask.bytesTransferred) / uploadTask.totalByteCount
            Log.i(ParamName.LOG_TAG, progress.toString())
        }
    }

    /** Ejemplo de getMetadata*/
    private suspend fun readMetaDataBasic() {
        val response = storage.reference.child(USER_IMAGE_STORAGE_REFERENCE).metadata.await()
        Log.i(
            LOG_TAG,
            "Date:${response.getCustomMetadata("date")} \t  UserType:${response.getCustomMetadata("userType")}"
        )
    }

    fun basicExample() {
        /** Ejemplo de las funciones de reference*/
        val reference = storage.reference.child("ejemplo/chica.jpg")
        reference.name // --> chica.jpg
        reference.path // --> ejemplo/chica.jpg
        reference.bucket // -> gs://divelogapp-2330ac.firebasestorage.app/ejemplo
    }

}