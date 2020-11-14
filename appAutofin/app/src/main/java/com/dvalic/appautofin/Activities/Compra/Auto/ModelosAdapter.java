package com.dvalic.appautofin.Activities.Compra.Auto;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dvalic.appautofin.API.Model.MModelos;
import com.dvalic.appautofin.API.Model.ModelTest;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.ItemModelosAutosBinding;

import java.util.ArrayList;
import java.util.Objects;

public class ModelosAdapter extends RecyclerView.Adapter<ModelosAdapter.MarcasAdapterViewHolder> {
    ArrayList<MModelos> arrayModelos;
    IComunicador interfaz;
    private Context context;

    public ModelosAdapter(ArrayList<MModelos> servicioMarcas, IComunicador interfaz, Context context) {
        this.arrayModelos = servicioMarcas;
        this.interfaz = interfaz;
        this.context = context;
    }

    @NonNull
    @Override
    public ModelosAdapter.MarcasAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ModelosAdapter.MarcasAdapterViewHolder(ItemModelosAutosBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ModelosAdapter.MarcasAdapterViewHolder holder, int position) {
        MModelos modelo = arrayModelos.get(position);
        holder.binding.cvModelo.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale));
        holder.binding.tVModelo.setText(modelo.getNombreModelo());
        holder.binding.tVVersion.setText(modelo.getNombreVersion());
        holder.binding.tvPrecioModelo.setText(UTHelpers.formatMoney(Double.valueOf(modelo.getPrecio())));
        Glide.with(context)
                .asBitmap()
                .load(modelo.getRutaFoto())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.ivModelo);
        //holder.binding.ivModelo.setImageDrawable(UTHelpers.getDrawable(context, R.drawable.muestra_coche));
        holder.binding.cvModelo.setOnClickListener((v) -> {
            MModelos model = arrayModelos.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("modelo", model);
            interfaz.mostrarFragmentoDetalleAuto(bundle);
        });
    }

    @Override
    public int getItemCount() {
        return arrayModelos.size();
    }

    class MarcasAdapterViewHolder extends RecyclerView.ViewHolder {
        ItemModelosAutosBinding binding;

        MarcasAdapterViewHolder(ItemModelosAutosBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

