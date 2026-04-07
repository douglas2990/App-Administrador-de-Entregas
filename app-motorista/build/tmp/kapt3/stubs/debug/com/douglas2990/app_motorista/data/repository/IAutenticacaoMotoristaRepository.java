package com.douglas2990.app_motorista.data.repository;

import com.example.core.UIstatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J$\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0004H\u00a6@\u00a2\u0006\u0002\u0010\u0007J\b\u0010\b\u001a\u00020\tH&\u00a8\u0006\n"}, d2 = {"Lcom/douglas2990/app_motorista/data/repository/IAutenticacaoMotoristaRepository;", "", "login", "Lcom/example/core/UIstatus;", "", "email", "senha", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "usuarioLogado", "", "app-motorista_debug"})
public abstract interface IAutenticacaoMotoristaRepository {
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object login(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String senha, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.core.UIstatus<java.lang.String>> $completion);
    
    public abstract boolean usuarioLogado();
}