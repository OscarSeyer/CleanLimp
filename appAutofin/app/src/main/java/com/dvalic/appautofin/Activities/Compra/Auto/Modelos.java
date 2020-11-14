package com.dvalic.appautofin.Activities.Compra.Auto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.dvalic.appautofin.API.Model.MMarcas;
import com.dvalic.appautofin.API.Model.MModelos;
import com.dvalic.appautofin.API.Model.ModelTest;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTBaseFragment;
import com.dvalic.appautofin.databinding.FragmentModelosBinding;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Modelos extends UTBaseFragment {
    private String TAG =  this.getClass().getSimpleName();
    private IComunicador interfaz;
    private FragmentModelosBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentModelosBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(this.getArguments() != null){
            if(!this.getArguments().isEmpty()){
                MMarcas marca = null;
                marca = (MMarcas) this.getArguments().getSerializable("marca");
                binding.txtMarca.setText(marca.getDescripcionMarca());
                solicitudModelos(marca.getIdMarca());
                this.getArguments().clear();
            }

        }
        binding.tvBack.setOnClickListener((v)->{
            onBackPressed();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof  IComunicador){
            interfaz = (IComunicador)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onBackPressed() {
        interfaz.mostrarFragmentoMarcas();
        return true;
    }



    @SuppressLint("CheckResult")
    private void solicitudModelos(String idMarca){
        mostrarProgresBar(true);
        Observable<ArrayList<MModelos>> request = apiInterface.obtenerModelosByIdMarca(idMarca);
        request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::handleResponse, this::handleError);

    }

    private void handleResponse(ArrayList<MModelos> lstModelos)
    {
        for (MModelos modelo: lstModelos) {
            modelo.setRutaFoto(modelo.getRutaFoto().replace("100", "400"));
        }
        binding.rvModelos.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        binding.rvModelos.setLayoutManager(manager);
        binding.rvModelos.setAdapter(new ModelosAdapter(lstModelos,interfaz, getContext()));
        mostrarProgresBar(false);
    }

    private void handleError(Throwable error)
    {
        mostrarProgresBar(false);
        Toast.makeText(getContext(), "Error "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public void mostrarProgresBar(boolean valor) {
        if (valor) {
            binding.pbProgreso.setVisibility(View.VISIBLE);
            binding.pbProgresoBack.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        } else {
            binding.pbProgreso.setVisibility(View.GONE);
            binding.pbProgresoBack.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
