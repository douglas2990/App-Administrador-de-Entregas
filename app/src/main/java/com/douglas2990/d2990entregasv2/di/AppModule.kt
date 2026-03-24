package com.douglas2990.d2990entregasv2.di

import android.content.Context
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.AutenticacaoMotoristaRepositoryImpl
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.CadastroAcessoRepositoryImpl
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.EmpresaRepositoryImpl
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IAutenticacaoMotoristaRepository
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.ICadastroAcessoRepository
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IEmpresaRepository
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IMotoristaRepository
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IRotaRepository
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.MotoristaRepositoryImpl
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.RotaRepositoryImpl
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.user.AutenticacaoRepositoryImpl
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.user.IAutenticacaoRepository
import com.douglas2990.d2990entregasv2.domain.usecase.AutenticacaoMotoristaUseCase
import com.douglas2990.d2990entregasv2.domain.usecase.AutenticacaoUseCase
import com.douglas2990.d2990entregasv2.domain.usecase.CadastroEmpresaUseCase
import com.douglas2990.d2990entregasv2.domain.usecase.ListarMotoristasUseCase
import com.douglas2990.d2990entregasv2.domain.usecase.RotaUseCase
import com.douglas2990.d2990entregasv2.domain.usecase.SalvarMotoristaUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class) // Alterado para Singleton para suportar Context e instâncias únicas
object AppModule {

    // --- SEÇÃO DE FIREBASE (Mantenha como está) ---

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    // --- NOVO REPOSITÓRIO (ESTRUTURA PROFISSIONAL) ---

    @Provides
    @Singleton
    fun provideCadastroAcessoRepository(
        @ApplicationContext context: Context, // Injeta o contexto necessário para o SecondaryApp
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): ICadastroAcessoRepository {
        return CadastroAcessoRepositoryImpl(context, firebaseAuth, firestore)
    }

    // --- SEUS REPOSITÓRIOS ATUAIS (Mantenha as injeções) ---

    @Provides
    fun provideAutenticacaoRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ) : IAutenticacaoRepository = AutenticacaoRepositoryImpl(firebaseAuth, firestore)

    @Provides
    fun provideEmpresaRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ) : IEmpresaRepository = EmpresaRepositoryImpl(firebaseAuth, firebaseFirestore)

    @Provides
    fun provideMotoristaRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ): IMotoristaRepository = MotoristaRepositoryImpl(firebaseAuth, firebaseFirestore)

    @Provides
    fun provideRotaRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): IRotaRepository = RotaRepositoryImpl(firebaseAuth, firebaseFirestore, firebaseStorage)

    // --- USE CASES (Mantenha as injeções) ---

    @Provides
    fun provideAutenticacaoUseCase() = AutenticacaoUseCase()

    @Provides
    fun provideAutenticacaoMotoristaUseCase() = AutenticacaoMotoristaUseCase()

    @Provides
    fun provideCadastroEmpresaUseCase() = CadastroEmpresaUseCase()

    @Provides
    fun provideSalvarMotoristaUseCase(repository: IMotoristaRepository) = SalvarMotoristaUseCase(repository)

    @Provides
    fun provideListarMotoristasUseCase(repository: IMotoristaRepository) = ListarMotoristasUseCase(repository)

    @Provides
    fun provideRotaUseCase(repository: IRotaRepository) = RotaUseCase(repository)
}
