package com.douglas2990.app_motorista.usecase;

import android.net.Uri;
import com.example.core.UIstatus;
import com.example.core.model.AgendaDia;
import com.example.core.model.Rota;
import com.example.core.repository.IRotaRepository;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J$\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bJ\"\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\u00062\u0006\u0010\u000f\u001a\u00020\u0007H\u0086@\u00a2\u0006\u0002\u0010\u0010J*\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\r0\u00062\u0006\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u0015J.\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u00072\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0086@\u00a2\u0006\u0002\u0010\u0018R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/douglas2990/app_motorista/usecase/RotaUseCase;", "", "repository", "Lcom/example/core/repository/IRotaRepository;", "(Lcom/example/core/repository/IRotaRepository;)V", "finalizarRotaComSucesso", "Lcom/example/core/UIstatus;", "", "idRota", "imageUri", "Landroid/net/Uri;", "(Ljava/lang/String;Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "listarAgendaMotorista", "", "Lcom/example/core/model/AgendaDia;", "idMotorista", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "listarRotasDoDia", "Lcom/example/core/model/Rota;", "dataTimestamp", "", "(Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reportarProblemaRota", "motivo", "(Ljava/lang/String;Ljava/lang/String;Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app-motorista_debug"})
public final class RotaUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.example.core.repository.IRotaRepository repository = null;
    
    @javax.inject.Inject()
    public RotaUseCase(@org.jetbrains.annotations.NotNull()
    com.example.core.repository.IRotaRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object listarAgendaMotorista(@org.jetbrains.annotations.NotNull()
    java.lang.String idMotorista, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.core.UIstatus<? extends java.util.List<com.example.core.model.AgendaDia>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object listarRotasDoDia(@org.jetbrains.annotations.NotNull()
    java.lang.String idMotorista, long dataTimestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.core.UIstatus<? extends java.util.List<com.example.core.model.Rota>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object finalizarRotaComSucesso(@org.jetbrains.annotations.NotNull()
    java.lang.String idRota, @org.jetbrains.annotations.NotNull()
    android.net.Uri imageUri, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.core.UIstatus<java.lang.String>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object reportarProblemaRota(@org.jetbrains.annotations.NotNull()
    java.lang.String idRota, @org.jetbrains.annotations.NotNull()
    java.lang.String motivo, @org.jetbrains.annotations.Nullable()
    android.net.Uri imageUri, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.core.UIstatus<java.lang.String>> $completion) {
        return null;
    }
}