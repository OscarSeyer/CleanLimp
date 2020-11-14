package com.dvalic.appautofin.Activities.Compra.Auto;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dvalic.appautofin.API.Model.MCatalogoDocumentos;
import com.dvalic.appautofin.API.Model.MMarcas;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.ItemDocumentoBinding;
import com.dvalic.appautofin.databinding.ItemMarcasAutosBinding;

import java.util.ArrayList;

public class DocumentacionAdapter extends RecyclerView.Adapter<DocumentacionAdapter.MarcasAdapterViewHolder> {
    MCatalogoDocumentos[] arrayDocs;
    IComunicador interfaz;
    private Context context;
    private FragmentManager fragmentManager;

    public DocumentacionAdapter(MCatalogoDocumentos[] arrayDocs, IComunicador interfaz, FragmentManager fragmentManager, Context context){
        this.arrayDocs = arrayDocs;
        this.interfaz = interfaz;
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    @NonNull
    @Override
    public DocumentacionAdapter.MarcasAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DocumentacionAdapter.MarcasAdapterViewHolder(ItemDocumentoBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentacionAdapter.MarcasAdapterViewHolder holder, int position) {
        MCatalogoDocumentos doc = arrayDocs[position];
        holder.binding.clTipoDoc.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition));
        holder.binding.tvTitle.setText(doc.getDescripcionDocumento());
        Drawable image ;
        if(doc.getIdDocumento().trim().equals("DOMIC")){
            image = UTHelpers.getDrawable(context, R.drawable.icono_comprobante);
            holder.binding.ivDoc.setImageDrawable(image);
        }else if(doc.getIdDocumento().trim().equals("INGRE")){
            image = UTHelpers.getDrawable(context, R.drawable.icono_ingresos);
            holder.binding.ivDoc.setImageDrawable(image);

        }else if(doc.getIdDocumento().trim().equals("IDENT")){
            image = UTHelpers.getDrawable(context, R.drawable.icono_identificacion);
            holder.binding.ivDoc.setImageDrawable(image);
        }
        holder.binding.tvCountDoc.setText( String.valueOf(position + 1));
        holder.binding.clTipoDoc.setOnClickListener((v)->{
            Log.i("OK_TAG", v.toString());
            DialogFragment dialogFragment = new DialogDocumentacion(interfaz, doc.getDescripcionDocumento(), (CardView)v);
            dialogFragment.setCancelable(true);
            dialogFragment.show(fragmentManager, dialogFragment.getClass().getSimpleName());
        });

    }

    @Override
    public int getItemCount() {
        return arrayDocs.length;

    }

    class MarcasAdapterViewHolder extends RecyclerView.ViewHolder {
        ItemDocumentoBinding binding;

        MarcasAdapterViewHolder(ItemDocumentoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
