package com.douglas2990.app_motorista.presentation.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.douglas2990.app_motorista.R;
import com.douglas2990.app_motorista.databinding.DialogReportarProblemaBinding;
import com.douglas2990.app_motorista.databinding.FragmentDetalhesEntregaBinding;
import com.douglas2990.app_motorista.presentation.viewmodel.DetalhesEntregaViewModel;
import com.example.core.model.Rota;
import com.example.core.UIstatus;
import com.google.firebase.auth.FirebaseAuth;
import dagger.hilt.android.AndroidEntryPoint;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.inject.Inject;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\"\u001a\u00020#H\u0002J\u0010\u0010$\u001a\u00020#2\u0006\u0010%\u001a\u00020\u0017H\u0002J\u0010\u0010&\u001a\u00020#2\u0006\u0010%\u001a\u00020\u0017H\u0002J\b\u0010\'\u001a\u00020#H\u0002J$\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020+2\b\u0010,\u001a\u0004\u0018\u00010-2\b\u0010.\u001a\u0004\u0018\u00010/H\u0016J\b\u00100\u001a\u00020#H\u0016J\u001a\u00101\u001a\u00020#2\u0006\u00102\u001a\u00020)2\b\u0010.\u001a\u0004\u0018\u00010/H\u0016J\u0010\u00103\u001a\u00020#2\u0006\u00104\u001a\u000205H\u0002J\b\u00106\u001a\u00020#H\u0002J\u0010\u00107\u001a\u00020\u00152\u0006\u00108\u001a\u00020\u0015H\u0002J\b\u00109\u001a\u00020#H\u0002J\b\u0010:\u001a\u00020#H\u0002J\u0010\u0010;\u001a\u00020#2\u0006\u0010<\u001a\u00020\u0017H\u0002J\b\u0010=\u001a\u00020#H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u000b\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u001e\u0010\u000e\u001a\u00020\u000f8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0018\u001a\u0010\u0012\f\u0012\n \u001a*\u0004\u0018\u00010\u00170\u00170\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u001b\u001a\u0010\u0012\f\u0012\n \u001a*\u0004\u0018\u00010\u00150\u00150\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u001c\u001a\u00020\u001d8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b \u0010!\u001a\u0004\b\u001e\u0010\u001f\u00a8\u0006>"}, d2 = {"Lcom/douglas2990/app_motorista/presentation/ui/DetalhesEntregaFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/douglas2990/app_motorista/databinding/FragmentDetalhesEntregaBinding;", "args", "Lcom/douglas2990/app_motorista/presentation/ui/DetalhesEntregaFragmentArgs;", "getArgs", "()Lcom/douglas2990/app_motorista/presentation/ui/DetalhesEntregaFragmentArgs;", "args$delegate", "Landroidx/navigation/NavArgsLazy;", "binding", "getBinding", "()Lcom/douglas2990/app_motorista/databinding/FragmentDetalhesEntregaBinding;", "firebaseAuth", "Lcom/google/firebase/auth/FirebaseAuth;", "getFirebaseAuth", "()Lcom/google/firebase/auth/FirebaseAuth;", "setFirebaseAuth", "(Lcom/google/firebase/auth/FirebaseAuth;)V", "imageUri", "Landroid/net/Uri;", "motivoAtual", "", "requestPermissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "kotlin.jvm.PlatformType", "takePictureLauncher", "viewModel", "Lcom/douglas2990/app_motorista/presentation/viewmodel/DetalhesEntregaViewModel;", "getViewModel", "()Lcom/douglas2990/app_motorista/presentation/viewmodel/DetalhesEntregaViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "abrirMapa", "", "confirmarComFotoOpcional", "motivo", "enviarRelatoProblema", "exibirDialogProblema", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "view", "preencherDados", "rota", "Lcom/example/core/model/Rota;", "prepararCamera", "redimensionarImagem", "uri", "setupListeners", "setupObservers", "toast", "msg", "verificarPermissaoECamera", "app-motorista_debug"})
public final class DetalhesEntregaFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.douglas2990.app_motorista.databinding.FragmentDetalhesEntregaBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.navigation.NavArgsLazy args$delegate = null;
    @javax.inject.Inject()
    public com.google.firebase.auth.FirebaseAuth firebaseAuth;
    @org.jetbrains.annotations.Nullable()
    private android.net.Uri imageUri;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String motivoAtual;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> requestPermissionLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<android.net.Uri> takePictureLauncher = null;
    
    public DetalhesEntregaFragment() {
        super();
    }
    
    private final com.douglas2990.app_motorista.databinding.FragmentDetalhesEntregaBinding getBinding() {
        return null;
    }
    
    private final com.douglas2990.app_motorista.presentation.viewmodel.DetalhesEntregaViewModel getViewModel() {
        return null;
    }
    
    private final com.douglas2990.app_motorista.presentation.ui.DetalhesEntregaFragmentArgs getArgs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.google.firebase.auth.FirebaseAuth getFirebaseAuth() {
        return null;
    }
    
    public final void setFirebaseAuth(@org.jetbrains.annotations.NotNull()
    com.google.firebase.auth.FirebaseAuth p0) {
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
    
    private final void preencherDados(com.example.core.model.Rota rota) {
    }
    
    private final void setupListeners() {
    }
    
    private final android.net.Uri redimensionarImagem(android.net.Uri uri) {
        return null;
    }
    
    private final void verificarPermissaoECamera() {
    }
    
    private final void prepararCamera() {
    }
    
    private final void setupObservers() {
    }
    
    private final void exibirDialogProblema() {
    }
    
    private final void confirmarComFotoOpcional(java.lang.String motivo) {
    }
    
    private final void enviarRelatoProblema(java.lang.String motivo) {
    }
    
    private final void abrirMapa() {
    }
    
    private final void toast(java.lang.String msg) {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}