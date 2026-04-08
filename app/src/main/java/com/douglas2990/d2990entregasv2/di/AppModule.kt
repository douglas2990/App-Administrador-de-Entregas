package com.douglas2990.d2990entregasv2.di

import android.content.Context
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.*
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.user.*
import com.douglas2990.d2990entregasv2.domain.usecase.AutenticacaoUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // --- FIREBASE INSTANCES ---

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    // --- REPOSITORIES ---

    @Provides
    @Singleton
    fun provideCadastroAcessoRepository(
        @ApplicationContext context: Context,
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): ICadastroAcessoRepository {
        return CadastroAcessoRepositoryImpl(context, firebaseAuth, firestore)
    }

    @Provides
    @Singleton
    fun provideAutenticacaoRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): IAutenticacaoRepository = AutenticacaoRepositoryImpl(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun provideEmpresaRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ): IEmpresaRepository = EmpresaRepositoryImpl(firebaseAuth, firebaseFirestore)

    @Provides
    @Singleton
    fun provideMotoristaRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ): IMotoristaRepository = MotoristaRepositoryImpl(firebaseAuth, firebaseFirestore)

    @Provides
    @Singleton
    fun provideRotaRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): IRotaRepository = RotaRepositoryImpl(firebaseAuth, firebaseFirestore, firebaseStorage)

    // Nota: UseCases não precisam de @Provides se tiverem @Inject constructor.
    // O Hilt os injetará automaticamente.


    @Provides
    @Singleton
    fun provideAutenticacaoUseCase(
        repository: IAutenticacaoRepository
    ): AutenticacaoUseCase {
        return AutenticacaoUseCase(repository)
    }

}


