package com.dvalic.appautofin.Activities.Compra.Auto;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dvalic.appautofin.API.Model.MMarcas;
import com.dvalic.appautofin.API.Model.MVersiones;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.ItemVersionesBinding;

import java.util.ArrayList;

import okhttp3.internal.Version;

public class VersionesAdapter extends RecyclerView.Adapter<VersionesAdapter.VersionesAdapterViewHolder> {

    ArrayList<MVersiones> arrayVersion;
    IComunicador interfaz;
    private DialogVersiones dialogVersiones;
    private Context context;

    public VersionesAdapter(ArrayList<MVersiones> arrayVersion, IComunicador interfaz, DialogVersiones dialogVersiones, Context context){
        this.arrayVersion = arrayVersion;
        this.interfaz = interfaz;
        this.dialogVersiones = dialogVersiones;
        this.context = context;
    }


    @NonNull
    @Override
    public VersionesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VersionesAdapterViewHolder(ItemVersionesBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VersionesAdapterViewHolder holder, int position) {
        MVersiones version = arrayVersion.get(position);
        holder.binding.cvVersion.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition));
        holder.binding.tvVersionItem.setText(version.getNombreVersion() + " -> " + UTHelpers.formatMoney(Double.parseDouble(version.getPrecio())));
        holder.binding.cvVersion.setOnClickListener((v)->{
            MVersiones selected = arrayVersion.get(position);
            this.dialogVersiones.refrescarInformacion(selected);

        });
    }





    @Override
    public int getItemCount() {
        return arrayVersion.size();
    }

    class VersionesAdapterViewHolder extends RecyclerView.ViewHolder {
        ItemVersionesBinding binding;

        VersionesAdapterViewHolder(ItemVersionesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
