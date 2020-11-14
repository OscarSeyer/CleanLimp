package com.dvalic.appautofin.Activities.Compra.Auto;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dvalic.appautofin.API.Model.MMarcas;
import com.dvalic.appautofin.API.Model.MModelos;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.databinding.ItemMarcasAutosBinding;

import java.util.ArrayList;

public class MarcasAdapter extends RecyclerView.Adapter<MarcasAdapter.MarcasAdapterViewHolder> {
    ArrayList<MMarcas> arrayMarcas;
    IComunicador interfaz;
    private Context context;
    private Marcas marcasFragment;

    public MarcasAdapter(ArrayList<MMarcas> servicioMarcas, IComunicador interfaz, Context context, Marcas marcasFragment){
        this.arrayMarcas = servicioMarcas;
        this.interfaz = interfaz;
        this.context = context;
        this.marcasFragment = marcasFragment;
    }

    @NonNull
    @Override
    public MarcasAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MarcasAdapterViewHolder(ItemMarcasAutosBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MarcasAdapterViewHolder holder, int position) {
        MMarcas servicioMarcas = arrayMarcas.get(position);
        holder.binding.clMarcas.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition));
        holder.binding.txtMarca.setText(servicioMarcas.getDescripcionMarca());
        holder.binding.clMarcas.setOnClickListener(v -> {
            MMarcas marca = arrayMarcas.get(position);
            Bundle bundles = new Bundle();
            bundles.putSerializable("marca", marca);
            /*  Fragment fragment = new Modelos();
            this.marcasFragment.getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.cl_centro, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            fragment.setArguments(bundles);*/
            interfaz.mostrarFragmentoModelos(bundles);

        });
    }

    @Override
    public int getItemCount() {
        return arrayMarcas.size();
    }

    class MarcasAdapterViewHolder extends RecyclerView.ViewHolder {
        ItemMarcasAutosBinding binding;

        MarcasAdapterViewHolder(ItemMarcasAutosBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
