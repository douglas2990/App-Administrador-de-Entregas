package com.douglas2990.app_motorista.presentation.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.douglas2990.app_motorista.databinding.ItemAgendaMotoristaBinding;
import com.example.core.model.AgendaDia;
import java.text.SimpleDateFormat;
import java.util.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00142\u0012\u0012\u0004\u0012\u00020\u0002\u0012\b\u0012\u00060\u0003R\u00020\u00000\u0001:\u0002\u0013\u0014B\u0019\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u0006H\u0002J\u001c\u0010\u000b\u001a\u00020\u00072\n\u0010\f\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u001c\u0010\u000f\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000eH\u0016R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/douglas2990/app_motorista/presentation/ui/adapter/AgendaAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/example/core/model/AgendaDia;", "Lcom/douglas2990/app_motorista/presentation/ui/adapter/AgendaAdapter$AgendaViewHolder;", "onItemClick", "Lkotlin/Function1;", "", "", "(Lkotlin/jvm/functions/Function1;)V", "normalizarParaComparacao", "timestamp", "onBindViewHolder", "holder", "position", "", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "AgendaViewHolder", "DiffCallback", "app-motorista_debug"})
public final class AgendaAdapter extends androidx.recyclerview.widget.ListAdapter<com.example.core.model.AgendaDia, com.douglas2990.app_motorista.presentation.ui.adapter.AgendaAdapter.AgendaViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<java.lang.Long, kotlin.Unit> onItemClick = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.douglas2990.app_motorista.presentation.ui.adapter.AgendaAdapter.DiffCallback DiffCallback = null;
    
    public AgendaAdapter(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onItemClick) {
        super(null);
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.douglas2990.app_motorista.presentation.ui.adapter.AgendaAdapter.AgendaViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.douglas2990.app_motorista.presentation.ui.adapter.AgendaAdapter.AgendaViewHolder holder, int position) {
    }
    
    private final long normalizarParaComparacao(long timestamp) {
        return 0L;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/douglas2990/app_motorista/presentation/ui/adapter/AgendaAdapter$AgendaViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/douglas2990/app_motorista/databinding/ItemAgendaMotoristaBinding;", "(Lcom/douglas2990/app_motorista/presentation/ui/adapter/AgendaAdapter;Lcom/douglas2990/app_motorista/databinding/ItemAgendaMotoristaBinding;)V", "bind", "", "item", "Lcom/example/core/model/AgendaDia;", "app-motorista_debug"})
    public final class AgendaViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.douglas2990.app_motorista.databinding.ItemAgendaMotoristaBinding binding = null;
        
        public AgendaViewHolder(@org.jetbrains.annotations.NotNull()
        com.douglas2990.app_motorista.databinding.ItemAgendaMotoristaBinding binding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.example.core.model.AgendaDia item) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lcom/douglas2990/app_motorista/presentation/ui/adapter/AgendaAdapter$DiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/example/core/model/AgendaDia;", "()V", "areContentsTheSame", "", "oldItem", "newItem", "areItemsTheSame", "app-motorista_debug"})
    public static final class DiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.example.core.model.AgendaDia> {
        
        private DiffCallback() {
            super();
        }
        
        @java.lang.Override()
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull()
        com.example.core.model.AgendaDia oldItem, @org.jetbrains.annotations.NotNull()
        com.example.core.model.AgendaDia newItem) {
            return false;
        }
        
        @java.lang.Override()
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull()
        com.example.core.model.AgendaDia oldItem, @org.jetbrains.annotations.NotNull()
        com.example.core.model.AgendaDia newItem) {
            return false;
        }
    }
}