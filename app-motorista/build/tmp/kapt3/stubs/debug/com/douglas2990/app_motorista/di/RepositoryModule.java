package com.douglas2990.app_motorista.di;

import com.douglas2990.app_motorista.data.repository.AutenticacaoMotoristaRepositoryImpl;
import com.douglas2990.app_motorista.data.repository.IAutenticacaoMotoristaRepository;
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.RotaRepositoryImpl;
import com.example.core.repository.IRotaRepository;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b!\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\'J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\'\u00a8\u0006\u000b"}, d2 = {"Lcom/douglas2990/app_motorista/di/RepositoryModule;", "", "()V", "bindAutenticacaoRepository", "Lcom/douglas2990/app_motorista/data/repository/IAutenticacaoMotoristaRepository;", "autenticacaoImpl", "Lcom/douglas2990/app_motorista/data/repository/AutenticacaoMotoristaRepositoryImpl;", "bindRotaRepository", "Lcom/example/core/repository/IRotaRepository;", "rotaRepositoryImpl", "Lcom/douglas2990/d2990entregasv2/data/remote/firebase/repository/RotaRepositoryImpl;", "app-motorista_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public abstract class RepositoryModule {
    
    public RepositoryModule() {
        super();
    }
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.core.repository.IRotaRepository bindRotaRepository(@org.jetbrains.annotations.NotNull()
    com.douglas2990.d2990entregasv2.data.remote.firebase.repository.RotaRepositoryImpl rotaRepositoryImpl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.douglas2990.app_motorista.data.repository.IAutenticacaoMotoristaRepository bindAutenticacaoRepository(@org.jetbrains.annotations.NotNull()
    com.douglas2990.app_motorista.data.repository.AutenticacaoMotoristaRepositoryImpl autenticacaoImpl);
}