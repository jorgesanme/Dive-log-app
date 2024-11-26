package com.smedina.dive_log_app.data

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseInstance(context: Context) {

    private val database = Firebase.database
    private val databaseRef = database.reference
    private var total = 0

    init {
        FirebaseApp.initializeApp(context)
    }

    fun writeOnFireBase(){
        total ++
        databaseRef.setValue("El valor actual es: $total")
    }
}