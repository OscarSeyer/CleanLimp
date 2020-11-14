package com.dvalic.appautofin.Activities.Compra.Auto;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.dvalic.appautofin.API.Model.MCotizacionAuto;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.DialogMasOpcionesPagoBinding;
import com.dvalic.appautofin.databinding.DialogVersionesBinding;

import java.util.Objects;

public class DialogMasOpcionesPago extends DialogFragment {
    private String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private DialogMasOpcionesPagoBinding binding;
    private MCotizacionAuto cotizacionAuto;
    private Cotizar cotizarFragment;


    public DialogMasOpcionesPago(MCotizacionAuto cotizacionAuto, IComunicador interfaz, Cotizar cotizarFragment){
        this.cotizacionAuto = cotizacionAuto;
        this.interfaz = interfaz;
        this.cotizarFragment = cotizarFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogMasOpcionesPagoBinding.inflate(inflater, container, false);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvAnualidadUno.setText(UTHelpers.formatMoney(this.cotizacionAuto.getAnualidadOpcionUno()));
        binding.tvMensualidadUno.setText(UTHelpers.formatMoney(this.cotizacionAuto.getMensualidadOpcionUno()));
        binding.tvAnualidadDos.setText(UTHelpers.formatMoney(this.cotizacionAuto.getAnualidadOpcionDos()));
        binding.tvMensualidadDos.setText(UTHelpers.formatMoney(this.cotizacionAuto.getMensualidadOpcionDos()));
        binding.tvAnualidadTres.setText(UTHelpers.formatMoney(this.cotizacionAuto.getAnualidadOpcionTres()));
        binding.tvMensualidadTres.setText(UTHelpers.formatMoney(this.cotizacionAuto.getMensualidadOpcionTres()));
        binding.btnContinuar.setOnClickListener((v)->{
            interfaz.mostrarFragmentoModoContacto();
            cotizarFragment.backFragment();
            this.dismiss();
        });
        binding.btnCancelar.setOnClickListener((v)->{
            this.dismiss();
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        int widthPixels = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int heightPixels = (int) (getResources().getDisplayMetrics().heightPixels * 0.5);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(widthPixels, heightPixels);
    }
}
