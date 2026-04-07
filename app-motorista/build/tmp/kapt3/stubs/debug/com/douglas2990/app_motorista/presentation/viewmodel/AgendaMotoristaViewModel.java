package com.douglas2990.app_motorista.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.douglas2990.app_motorista.usecase.RotaUseCase;
import com.example.core.UIstatus;
import com.example.core.model.AgendaDia;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011R \u0010\u0005\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R#\u0010\n\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u00070\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/douglas2990/app_motorista/presentation/viewmodel/AgendaMotoristaViewModel;", "Landroidx/lifecycle/ViewModel;", "rotaUseCase", "Lcom/douglas2990/app_motorista/usecase/RotaUseCase;", "(Lcom/douglas2990/app_motorista/usecase/RotaUseCase;)V", "_agenda", "Landroidx/lifecycle/MutableLiveData;", "Lcom/example/core/UIstatus;", "", "Lcom/example/core/model/AgendaDia;", "agenda", "Landroidx/lifecycle/LiveData;", "getAgenda", "()Landroidx/lifecycle/LiveData;", "atualizarAgenda", "", "idMotorista", "", "carregarAgenda", "app-motorista_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AgendaMotoristaViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.douglas2990.app_motorista.usecase.RotaUseCase rotaUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.core.UIstatus<java.util.List<com.example.core.model.AgendaDia>>> _agenda = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.core.UIstatus<java.util.List<com.example.core.model.AgendaDia>>> agenda = null;
    
    @javax.inject.Inject()
    public AgendaMotoristaViewModel(@org.jetbrains.annotations.NotNull()
    com.douglas2990.app_motorista.usecase.RotaUseCase rotaUseCase) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.core.UIstatus<java.util.List<com.example.core.model.AgendaDia>>> getAgenda() {
        return null;
    }
    
    /**
     * Chama o UseCase para buscar as rotas do motorista logado
     * e agrupá-las por data.
     */
    public final void carregarAgenda(@org.jetbrains.annotations.NotNull()
    java.lang.String idMotorista) {
    }
    
    /**
     * Função opcional para atualizar a lista (Swipe to Refresh)
     */
    public final void atualizarAgenda(@org.jetbrains.annotations.NotNull()
    java.lang.String idMotorista) {
    }
}