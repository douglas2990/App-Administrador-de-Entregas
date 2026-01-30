package com.douglas2990.d2990entregasv2.data.remote.firebase.repository

import com.douglas2990.d2990entregasv2.model.Motorista
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AutenticacaoMotoristaRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): IAutenticacaoMotoristaRepository{
    override suspend fun cadastrarMotorista(motorista: Motorista): Boolean {
        return firebaseAuth.createUserWithEmailAndPassword(
            motorista.email , motorista.senha
        ).await() != null
    }
}