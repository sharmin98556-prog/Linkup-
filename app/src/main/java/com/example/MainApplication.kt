package com.example

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initFirestore()
    }

    private fun initFirestore() {
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
            }
            val firestore = FirebaseFirestore.getInstance()
            val settings = FirebaseFirestoreSettings.Builder()
                .setLocalCacheSettings(PersistentCacheSettings.newBuilder().build())
                .build()
            firestore.firestoreSettings = settings
            Log.d("MainApplication", "Firebase Firestore initialized successfully with persistent cache for real-time data syncing.")
        } catch (e: Exception) {
            Log.w("MainApplication", "Firebase Firestore initialization note: ${e.message}")
        }
    }
}
