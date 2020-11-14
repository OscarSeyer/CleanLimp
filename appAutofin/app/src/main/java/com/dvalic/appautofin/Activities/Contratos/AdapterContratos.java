package com.dvalic.appautofin.Activities.Contratos;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dvalic.appautofin.API.Model.MMarcas;
import com.dvalic.appautofin.API.Model.ModelTest;
import com.dvalic.appautofin.Activities.Compra.Auto.MarcasAdapter;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.ItemContratoBinding;
import com.dvalic.appautofin.databinding.ItemMarcasAutosBinding;

import java.util.ArrayList;

public class AdapterContratos extends RecyclerView.Adapter<AdapterContratos.AdapterContratosViewHolder> {
    ArrayList<ModelTest> arrayMarcas;
    IComunicador interfaz;
    Context context;

    public AdapterContratos(ArrayList<ModelTest> servicioMarcas, IComunicador interfaz, Context context){
        this.arrayMarcas = servicioMarcas;
        this.interfaz = interfaz;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterContratos.AdapterContratosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterContratos.AdapterContratosViewHolder(ItemContratoBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterContratos.AdapterContratosViewHolder holder, int position) {
        ModelTest model = arrayMarcas.get(position);

        holder.binding.tvModeloConstrato.setText(model.getModelo());
        holder.binding.tvVersionContrato.setText(model.getVersion());
        holder.binding.tvCantidadContrato.setText(model.getPrecio());
        holder.binding.tvEstatusContrato.setText(model.getEstado());
        int color = 0;
        color  = (model.getEstado().equals("ACTIVO") ? R.color.activo : R.color.adeudo);
        holder.binding.tvEstatusContrato.setTextColor(ContextCompat.getColor(context,color));
        holder.binding.cvContrato.setOnClickListener((v)->{
            interfaz.mostrarFragmentoDetalleContrato();
        });


    }

    @Override
    public int getItemCount() {
        return arrayMarcas.size();
    }

    class AdapterContratosViewHolder extends RecyclerView.ViewHolder {
        ItemContratoBinding binding;

        AdapterContratosViewHolder(ItemContratoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
