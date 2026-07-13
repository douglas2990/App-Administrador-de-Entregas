package com.douglas2990.d2990entregasv2.repository

import com.douglas2990.d2990entregasv2.model.Motorista
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ItemsRepositoryTESTE {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("items")

    suspend fun addItem(item: Motorista) {
        collection.add(item).await()
    }

    /*
    suspend fun getItems(): List {
        return collection.get().await().documents.map { document ->
            document.toObject(Motorista::class.java)!!.copy(id = document.id)
        }
    }

     */
}