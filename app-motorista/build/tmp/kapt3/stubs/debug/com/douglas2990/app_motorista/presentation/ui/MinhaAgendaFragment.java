package com.douglas2990.app_motorista.presentation.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.douglas2990.app_motorista.databinding.FragmentMinhaAgendaBinding;
import com.douglas2990.app_motorista.presentation.ui.MinhaAgendaFragmentDirections;
import com.douglas2990.app_motorista.presentation.ui.adapter.AgendaAdapter;
import com.douglas2990.app_motorista.presentation.viewmodel.AgendaMotoristaViewModel;
import com.example.core.UIstatus;
import com.google.firebase.auth.FirebaseAuth;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0019\u001a\u00020\u001aH\u0002J\u001a\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\u001dH\u0002J$\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010$2\b\u0010%\u001a\u0004\u0018\u00010&H\u0016J\b\u0010\'\u001a\u00020\u001aH\u0016J\u001a\u0010(\u001a\u00020\u001a2\u0006\u0010)\u001a\u00020 2\b\u0010%\u001a\u0004\u0018\u00010&H\u0016J\b\u0010*\u001a\u00020\u001aH\u0002J\b\u0010+\u001a\u00020\u001aH\u0002J\u0010\u0010,\u001a\u00020\u001a2\u0006\u0010-\u001a\u00020.H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u000b\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u001e\u0010\u000e\u001a\u00020\u000f8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001b\u0010\u0014\u001a\u00020\u00158BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0018\u0010\n\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006/"}, d2 = {"Lcom/douglas2990/app_motorista/presentation/ui/MinhaAgendaFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/douglas2990/app_motorista/databinding/FragmentMinhaAgendaBinding;", "agendaAdapter", "Lcom/douglas2990/app_motorista/presentation/ui/adapter/AgendaAdapter;", "getAgendaAdapter", "()Lcom/douglas2990/app_motorista/presentation/ui/adapter/AgendaAdapter;", "agendaAdapter$delegate", "Lkotlin/Lazy;", "binding", "getBinding", "()Lcom/douglas2990/app_motorista/databinding/FragmentMinhaAgendaBinding;", "firebaseAuth", "Lcom/google/firebase/auth/FirebaseAuth;", "getFirebaseAuth", "()Lcom/google/firebase/auth/FirebaseAuth;", "setFirebaseAuth", "(Lcom/google/firebase/auth/FirebaseAuth;)V", "viewModel", "Lcom/douglas2990/app_motorista/presentation/viewmodel/AgendaMotoristaViewModel;", "getViewModel", "()Lcom/douglas2990/app_motorista/presentation/viewmodel/AgendaMotoristaViewModel;", "viewModel$delegate", "carregarDados", "", "exibirEstados", "loading", "", "isEmpty", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "view", "setupObservers", "setupRecyclerView", "toast", "msg", "", "app-motorista_debug"})
public final class MinhaAgendaFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.douglas2990.app_motorista.databinding.FragmentMinhaAgendaBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @javax.inject.Inject()
    public com.google.firebase.auth.FirebaseAuth firebaseAuth;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy agendaAdapter$delegate = null;
    
    public MinhaAgendaFragment() {
        super();
    }
    
    private final com.douglas2990.app_motorista.databinding.FragmentMinhaAgendaBinding getBinding() {
        return null;
    }
    
    private final com.douglas2990.app_motorista.presentation.viewmodel.AgendaMotoristaViewModel getViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.google.firebase.auth.FirebaseAuth getFirebaseAuth() {
        return null;
    }
    
    public final void setFirebaseAuth(@org.jetbrains.annotations.NotNull()
    com.google.firebase.auth.FirebaseAuth p0) {
    }
    
    private final com.douglas2990.app_motorista.presentation.ui.adapter.AgendaAdapter getAgendaAdapter() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void carregarDados() {
    }
    
    private final void setupObservers() {
    }
    
    private final void exibirEstados(boolean loading, boolean isEmpty) {
    }
    
    private final void toast(java.lang.String msg) {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}