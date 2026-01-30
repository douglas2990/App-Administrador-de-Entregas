package com.douglas2990.d2990entregasv2.di

import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.AutenticacaoMotoristaRepositoryImpl
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.EmpresaRepositoryImpl
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IAutenticacaoMotoristaRepository
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IEmpresaRepository
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.user.AutenticacaoRepositoryImpl
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.user.IAutenticacaoRepository
import com.douglas2990.d2990entregasv2.domain.usecase.AutenticacaoMotoristaUseCase
import com.douglas2990.d2990entregasv2.domain.usecase.AutenticacaoUseCase
import com.douglas2990.d2990entregasv2.domain.usecase.CadastroEmpresaUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
object AppModule {


    @Provides
    fun provideAutenticacaoUseCase() : AutenticacaoUseCase {
        return AutenticacaoUseCase()
    }

    @Provides
    fun provideAutenticacaoRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ) : IAutenticacaoRepository {
        return AutenticacaoRepositoryImpl(firebaseAuth, firestore)
    }


    @Provides
    fun provideAutenticacaoMotoristaUseCase(): AutenticacaoMotoristaUseCase {
        return AutenticacaoMotoristaUseCase()
    }

    @Provides
    fun provideCadastroEmpresaUseCase(): CadastroEmpresaUseCase {
        return CadastroEmpresaUseCase()
    }


    @Provides
    fun provideAutenticacaoMotoristaRepository(
        firebaseAuth: FirebaseAuth
    ): IAutenticacaoMotoristaRepository {
        return AutenticacaoMotoristaRepositoryImpl(firebaseAuth)
    }

    @Provides
    fun provideEmpresaRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ) : IEmpresaRepository {
        return EmpresaRepositoryImpl(firebaseAuth, firebaseFirestore)
    }

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }


    @Provides
    fun provideFirebaseStorage() : FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    fun provideFirebaseFirestores() : FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
    
}