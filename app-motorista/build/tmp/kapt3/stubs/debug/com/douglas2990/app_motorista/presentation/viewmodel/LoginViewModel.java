package com.douglas2990.app_motorista.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.douglas2990.app_motorista.data.repository.IAutenticacaoMotoristaRepository;
import com.example.core.UIstatus;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\bJ\u0006\u0010\u0011\u001a\u00020\u0012R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/douglas2990/app_motorista/presentation/viewmodel/LoginViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/douglas2990/app_motorista/data/repository/IAutenticacaoMotoristaRepository;", "(Lcom/douglas2990/app_motorista/data/repository/IAutenticacaoMotoristaRepository;)V", "_loginStatus", "Landroidx/lifecycle/MutableLiveData;", "Lcom/example/core/UIstatus;", "", "loginStatus", "Landroidx/lifecycle/LiveData;", "getLoginStatus", "()Landroidx/lifecycle/LiveData;", "login", "", "email", "senha", "usuarioLogado", "", "app-motorista_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class LoginViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.douglas2990.app_motorista.data.repository.IAutenticacaoMotoristaRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.core.UIstatus<java.lang.String>> _loginStatus = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.core.UIstatus<java.lang.String>> loginStatus = null;
    
    @javax.inject.Inject()
    public LoginViewModel(@org.jetbrains.annotations.NotNull()
    com.douglas2990.app_motorista.data.repository.IAutenticacaoMotoristaRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.core.UIstatus<java.lang.String>> getLoginStatus() {
        return null;
    }
    
    public final void login(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String senha) {
    }
    
    public final boolean usuarioLogado() {
        return false;
    }
}