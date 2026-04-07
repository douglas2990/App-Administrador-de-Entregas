package com.douglas2990.app_motorista.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.core.UIstatus;
import com.example.core.model.Rota;
import com.example.core.repository.IRotaRepository;
import com.google.firebase.auth.FirebaseAuth;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014R \u0010\u0007\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R#\u0010\f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\t0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0015"}, d2 = {"Lcom/douglas2990/app_motorista/presentation/viewmodel/RotasMotoristaViewModel;", "Landroidx/lifecycle/ViewModel;", "rotaRepository", "Lcom/example/core/repository/IRotaRepository;", "auth", "Lcom/google/firebase/auth/FirebaseAuth;", "(Lcom/example/core/repository/IRotaRepository;Lcom/google/firebase/auth/FirebaseAuth;)V", "_rotas", "Landroidx/lifecycle/MutableLiveData;", "Lcom/example/core/UIstatus;", "", "Lcom/example/core/model/Rota;", "rotas", "Landroidx/lifecycle/LiveData;", "getRotas", "()Landroidx/lifecycle/LiveData;", "atualizarManualmente", "", "observarMinhasRotas", "dataSelecionada", "", "app-motorista_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class RotasMotoristaViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.core.repository.IRotaRepository rotaRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.auth.FirebaseAuth auth = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.core.UIstatus<java.util.List<com.example.core.model.Rota>>> _rotas = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.core.UIstatus<java.util.List<com.example.core.model.Rota>>> rotas = null;
    
    @javax.inject.Inject()
    public RotasMotoristaViewModel(@org.jetbrains.annotations.NotNull()
    com.example.core.repository.IRotaRepository rotaRepository, @org.jetbrains.annotations.NotNull()
    com.google.firebase.auth.FirebaseAuth auth) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.core.UIstatus<java.util.List<com.example.core.model.Rota>>> getRotas() {
        return null;
    }
    
    /**
     * Inicia a observação em tempo real.
     * Toda alteração no Firestore refletirá aqui automaticamente.
     */
    public final void observarMinhasRotas(long dataSelecionada) {
    }
    
    /**
     * Mantemos este método caso você ainda queira disparar
     * um refresh manual pelo SwipeRefreshLayout.
     */
    public final void atualizarManualmente() {
    }
}