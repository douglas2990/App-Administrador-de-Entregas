package com.douglas2990.app_motorista.di

import com.douglas2990.app_motorista.data.repository.AutenticacaoMotoristaRepositoryImpl
import com.douglas2990.app_motorista.data.repository.IAutenticacaoMotoristaRepository
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.RotaRepositoryImpl
import com.example.core.repository.IRotaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRotaRepository(
        rotaRepositoryImpl: RotaRepositoryImpl
    ): IRotaRepository

    @Binds
    @Singleton
    abstract fun bindAutenticacaoRepository(
        autenticacaoImpl: AutenticacaoMotoristaRepositoryImpl
    ): IAutenticacaoMotoristaRepository
}