package com.example.tp1_android

import android.net.Uri

data class Etudiant (
    val id: Long,

    val nom: String,
                var etat: String,
                var imageUri: Uri? ,
                val defaultImageResId: Int = R.drawable.etudiant)
