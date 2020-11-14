package com.dvalic.appautofin.Activities.Contratos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dvalic.appautofin.API.Model.ModelTest;
import com.dvalic.appautofin.Activities.Compra.Auto.MarcasAdapter;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTBaseFragment;
import com.dvalic.appautofin.databinding.FragmentContratosBinding;


public class Contratos extends UTBaseFragment {

    private String TAG =  this.getClass().getSimpleName();
    private IComunicador interfaz;
    private FragmentContratosBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentContratosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readRecycler();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof  IComunicador){
            interfaz = (IComunicador)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    public void readRecycler(){
        binding.rvContratos.setHasFixedSize(true);
        binding.rvContratos.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        ModelTest modelTest = new ModelTest();
        binding.rvContratos.setAdapter(new AdapterContratos(modelTest.obtenerModelos(),interfaz, getContext()));
    }
}
