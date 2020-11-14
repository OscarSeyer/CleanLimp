package com.dvalic.appautofin.Activities.MenuOpciones;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTBaseFragment;
import com.dvalic.appautofin.databinding.FragmentMetodosPagoBinding;

public class MetodosPago extends UTBaseFragment {
    private String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private FragmentMetodosPagoBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMetodosPagoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.cvTarjetas.setOnClickListener((v)->{
            interfaz.mostrarFragmentoMediosPago();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IComunicador){
            interfaz = (IComunicador)context;
        }
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }
}
