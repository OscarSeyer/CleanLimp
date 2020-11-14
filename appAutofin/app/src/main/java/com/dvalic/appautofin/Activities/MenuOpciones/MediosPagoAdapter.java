package com.dvalic.appautofin.Activities.MenuOpciones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dvalic.appautofin.API.Model.MMedioPago;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.databinding.ItemCardCreditBinding;


import java.util.ArrayList;

public class MediosPagoAdapter extends RecyclerView.Adapter<MediosPagoAdapter.MetodosPagoAdapterViewHolder> {
    ArrayList<MMedioPago> arrayMediop;
    Context context;
    MediosPago mediosPago;

    public MediosPagoAdapter(ArrayList<MMedioPago> servicioMediop, Context context, MediosPago mediosPago){
        this.arrayMediop = servicioMediop;
        this.context = context;
        this.mediosPago = mediosPago;
    }

    @NonNull
    @Override
    public MediosPagoAdapter.MetodosPagoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MediosPagoAdapter.MetodosPagoAdapterViewHolder(ItemCardCreditBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MediosPagoAdapter.MetodosPagoAdapterViewHolder holder, int position) {
            MMedioPago medioPago = arrayMediop.get(position);

        if (medioPago.getNumeroTarjeta().toString().trim().substring(0, 1).equals("4")){
            holder.binding.ivTipoTarjeta.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_visa));
            holder.binding.ivCardBack.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_visa));
        }
        if (medioPago.getNumeroTarjeta().toString().trim().substring(0, 1).equals("5")){
            holder.binding.ivTipoTarjeta.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mastercard));
            holder.binding.ivCardBack.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mastercard));
        }
        holder.binding.txtNombreTarjeta.setText(medioPago.getNombreTarjeta());
        holder.binding.txtNumeroTarjeta.setText(medioPago.getNumeroTarjeta().toString().trim().substring(0, 4).concat(" ").concat(medioPago.getNumeroTarjeta().toString().trim().substring(4, 8)).concat(" ").concat(medioPago.getNumeroTarjeta().toString().trim().substring(8, 12)).concat(" ").concat(medioPago.getNumeroTarjeta().toString().trim().substring(12, 16)));
        holder.binding.txtFechaExpiracion.setText(medioPago.getMesVencimiento().toString().concat("/").concat(medioPago.getAnioVencimiento().toString()));
        holder.binding.ivEliminarTarjeta.setOnClickListener((v)->{
            try{
                MMedioPago mp = arrayMediop.get(position);
                assert mp != null;
                mediosPago.eliminarMedioPago(mp.getIdCuenta(), mp.getNumeroTarjeta());
            }catch (Exception _exc){
                throw _exc;
            }
        });
        holder.binding.cdCard.setOnClickListener(v -> {
            MMedioPago mp = arrayMediop.get(position);


        });
    }

    @Override
    public int getItemCount() {
        return arrayMediop.size();
    }

    class MetodosPagoAdapterViewHolder extends RecyclerView.ViewHolder {
        ItemCardCreditBinding binding;

        MetodosPagoAdapterViewHolder(ItemCardCreditBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



}
