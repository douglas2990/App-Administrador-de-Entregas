package com.douglas2990.d2990entregasv2.model.user

import android.net.Uri


data class UploadStorage(
    val local: String,
    val nomeImagem: String,
    val uriImagem: Uri
)