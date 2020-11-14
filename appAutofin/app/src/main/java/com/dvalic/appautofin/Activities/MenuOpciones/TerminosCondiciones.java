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
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.dvalic.appautofin.Activities.Compra.Casa.Presentacion;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTBaseFragment;
import com.dvalic.appautofin.databinding.FragmentTerminosCondicionesBinding;


public class TerminosCondiciones extends UTBaseFragment {
    private String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private FragmentTerminosCondicionesBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTerminosCondicionesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebSettings webSettings = binding.wbTermonosCondiciones.getSettings();
        webSettings.setJavaScriptEnabled(true);
        binding.wbTermonosCondiciones.setWebViewClient(new WebViewClient());
        binding.wbTermonosCondiciones.loadUrl("http://ws-smartit.divisionautomotriz.com/wsApiAutofin/swagger/ui/index#!/Cuenta/Cuenta_GetCuentaPorTelefonoClave");

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }
}
