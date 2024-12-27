package com.smedina.dive_log_app.utils

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.util.Date


class GenerateFileUri( private val context: Context) {

    fun generateUri(): Uri {
        return  FileProvider.getUriForFile(
            context,
            "com.smedina.dive_log_app.provider",
            createFile()
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun createFile(): File {
        val name: String = SimpleDateFormat("yyyyMMdd_hhmmss").format(Date())+"_"
        return  File.createTempFile(name, "_UserImage.jpg", context.externalCacheDir)
    }
}