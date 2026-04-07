package com.douglas2990.app_motorista.presentation.viewmodel;

import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.douglas2990.app_motorista.usecase.RotaUseCase;
import com.example.core.UIstatus;
import com.example.core.model.Rota;
import com.example.core.repository.IRotaRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\f2\u0006\u0010\u0018\u001a\u00020\u0019J\u0016\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\fJ\"\u0010\u001e\u001a\u00020\u00162\u0006\u0010\u001f\u001a\u00020\f2\u0006\u0010\u001d\u001a\u00020\f2\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u001cJ\u000e\u0010 \u001a\u00020\u00162\u0006\u0010!\u001a\u00020\u0007R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\n0\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R#\u0010\u0011\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\n0\t0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u001d\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\t0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0010\u00a8\u0006\""}, d2 = {"Lcom/douglas2990/app_motorista/presentation/viewmodel/DetalhesEntregaViewModel;", "Landroidx/lifecycle/ViewModel;", "rotaUseCase", "Lcom/douglas2990/app_motorista/usecase/RotaUseCase;", "(Lcom/douglas2990/app_motorista/usecase/RotaUseCase;)V", "_rotaAtual", "Landroidx/lifecycle/MutableLiveData;", "Lcom/example/core/model/Rota;", "_rotasDoDia", "Lcom/example/core/UIstatus;", "", "_statusAcao", "", "rotaAtual", "Landroidx/lifecycle/LiveData;", "getRotaAtual", "()Landroidx/lifecycle/LiveData;", "rotasDoDia", "getRotasDoDia", "statusAcao", "getStatusAcao", "carregarEntregasDoDia", "", "idMotorista", "dataTimestamp", "", "finalizarEntrega", "imageUri", "Landroid/net/Uri;", "rotaId", "reportarProblema", "motivo", "selecionarRota", "rota", "app-motorista_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class DetalhesEntregaViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.douglas2990.app_motorista.usecase.RotaUseCase rotaUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.core.UIstatus<java.util.List<com.example.core.model.Rota>>> _rotasDoDia = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.core.UIstatus<java.util.List<com.example.core.model.Rota>>> rotasDoDia = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.core.model.Rota> _rotaAtual = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.core.model.Rota> rotaAtual = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.core.UIstatus<java.lang.String>> _statusAcao = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.core.UIstatus<java.lang.String>> statusAcao = null;
    
    @javax.inject.Inject()
    public DetalhesEntregaViewModel(@org.jetbrains.annotations.NotNull()
    com.douglas2990.app_motorista.usecase.RotaUseCase rotaUseCase) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.core.UIstatus<java.util.List<com.example.core.model.Rota>>> getRotasDoDia() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.core.model.Rota> getRotaAtual() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.core.UIstatus<java.lang.String>> getStatusAcao() {
        return null;
    }
    
    /**
     * Busca todas as rotas do motorista naquela data específica
     */
    public final void carregarEntregasDoDia(@org.jetbrains.annotations.NotNull()
    java.lang.String idMotorista, long dataTimestamp) {
    }
    
    /**
     * Define qual rota está sendo manipulada (caso haja mais de uma no dia)
     */
    public final void selecionarRota(@org.jetbrains.annotations.NotNull()
    com.example.core.model.Rota rota) {
    }
    
    public final void finalizarEntrega(@org.jetbrains.annotations.NotNull()
    android.net.Uri imageUri, @org.jetbrains.annotations.NotNull()
    java.lang.String rotaId) {
    }
    
    public final void reportarProblema(@org.jetbrains.annotations.NotNull()
    java.lang.String motivo, @org.jetbrains.annotations.NotNull()
    java.lang.String rotaId, @org.jetbrains.annotations.Nullable()
    android.net.Uri imageUri) {
    }
}