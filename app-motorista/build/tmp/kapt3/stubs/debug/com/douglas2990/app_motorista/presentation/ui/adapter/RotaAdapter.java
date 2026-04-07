package com.douglas2990.app_motorista.presentation.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.douglas2990.app_motorista.databinding.ItemRotaBinding;
import com.example.core.model.Rota;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u0012\u0012\u0004\u0012\u00020\u0002\u0012\b\u0012\u00060\u0003R\u00020\u00000\u0001:\u0002\u0011\u0012B-\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\bJ\u001c\u0010\t\u001a\u00020\u00062\n\u0010\n\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u001c\u0010\r\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\fH\u0016R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/douglas2990/app_motorista/presentation/ui/adapter/RotaAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/example/core/model/Rota;", "Lcom/douglas2990/app_motorista/presentation/ui/adapter/RotaAdapter$RotaViewHolder;", "onItemClick", "Lkotlin/Function1;", "", "onItemLongClick", "(Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "onBindViewHolder", "holder", "position", "", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "DiffCallback", "RotaViewHolder", "app-motorista_debug"})
public final class RotaAdapter extends androidx.recyclerview.widget.ListAdapter<com.example.core.model.Rota, com.douglas2990.app_motorista.presentation.ui.adapter.RotaAdapter.RotaViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.example.core.model.Rota, kotlin.Unit> onItemClick = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.example.core.model.Rota, kotlin.Unit> onItemLongClick = null;
    
    public RotaAdapter(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.example.core.model.Rota, kotlin.Unit> onItemClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.example.core.model.Rota, kotlin.Unit> onItemLongClick) {
        super(null);
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.douglas2990.app_motorista.presentation.ui.adapter.RotaAdapter.RotaViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.douglas2990.app_motorista.presentation.ui.adapter.RotaAdapter.RotaViewHolder holder, int position) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lcom/douglas2990/app_motorista/presentation/ui/adapter/RotaAdapter$DiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/example/core/model/Rota;", "()V", "areContentsTheSame", "", "oldItem", "newItem", "areItemsTheSame", "app-motorista_debug"})
    public static final class DiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.example.core.model.Rota> {
        
        public DiffCallback() {
            super();
        }
        
        @java.lang.Override()
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull()
        com.example.core.model.Rota oldItem, @org.jetbrains.annotations.NotNull()
        com.example.core.model.Rota newItem) {
            return false;
        }
        
        @java.lang.Override()
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull()
        com.example.core.model.Rota oldItem, @org.jetbrains.annotations.NotNull()
        com.example.core.model.Rota newItem) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/douglas2990/app_motorista/presentation/ui/adapter/RotaAdapter$RotaViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/douglas2990/app_motorista/databinding/ItemRotaBinding;", "(Lcom/douglas2990/app_motorista/presentation/ui/adapter/RotaAdapter;Lcom/douglas2990/app_motorista/databinding/ItemRotaBinding;)V", "bind", "", "rota", "Lcom/example/core/model/Rota;", "app-motorista_debug"})
    public final class RotaViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.douglas2990.app_motorista.databinding.ItemRotaBinding binding = null;
        
        public RotaViewHolder(@org.jetbrains.annotations.NotNull()
        com.douglas2990.app_motorista.databinding.ItemRotaBinding binding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.example.core.model.Rota rota) {
        }
    }
}