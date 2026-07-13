package com.douglas2990.app_motorista.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    // REMOVA OU COMENTE ESTE BLOCO ABAIXO:
    /*
    @Provides
    @Singleton
    fun provideRotaRepository(...): IRotaRepository {
        return RotaRepositoryImpl(...)
    }
    */

    // O AutenticacaoRepository você também deve remover daqui
    // se já o colocou no RepositoryModule com @Binds
}