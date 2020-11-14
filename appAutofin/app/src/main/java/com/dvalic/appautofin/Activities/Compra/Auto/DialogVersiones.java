package com.dvalic.appautofin.Activities.Compra.Auto;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.dvalic.appautofin.API.IActions;
import com.dvalic.appautofin.API.Model.MModelos;
import com.dvalic.appautofin.API.Model.MVersiones;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.databinding.DialogVersionesBinding;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DialogVersiones extends DialogFragment{
    private String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private CompositeDisposable compositeDisposable;
    private DialogVersionesBinding binding;
    IActions apiInterface;
    private MModelos modelo;
    private DetalleAuto detalleAuto;


    public DialogVersiones(IComunicador interfaz, CompositeDisposable compositeDisposable, IActions apiInterface, MModelos modelo, DetalleAuto detalleAuto){
        this.interfaz = interfaz;
        this.compositeDisposable = compositeDisposable;
        this.apiInterface = apiInterface;
        this.modelo = modelo;
        this.detalleAuto = detalleAuto;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogVersionesBinding.inflate(inflater, container, false);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        solicitudVersiones(modelo.getIdMarca() ,modelo.getIdModelo());


    }

    @Override
    public void onResume() {
        super.onResume();
        int widthPixels = (int) (getResources().getDisplayMetrics().widthPixels * 0.7);
        int heightPixels = (int) (getResources().getDisplayMetrics().heightPixels * 0.5);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(widthPixels, heightPixels);
    }

      private void solicitudVersiones(String idMarca, String idModelo){
        mostrarProgresBar(true);
        this.compositeDisposable.add(apiInterface.obtenerVersionesByIdMarcaIdModelo(idMarca, idModelo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                //.subscribe(result->{}, throwable -> {}));
                .subscribe(this::handleResponse, this::handleError));

    }

    private void handleResponse(ArrayList<MVersiones> lstVersiones)
    {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        binding.rvVersiones.setLayoutManager(manager);
        binding.rvVersiones.setAdapter(new VersionesAdapter(lstVersiones,interfaz, this, getContext()));
        mostrarProgresBar(false);
    }

    private void handleError(Throwable error)
    {
        Toast.makeText(getContext(), "Error "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        mostrarProgresBar(false);
    }

    public void refrescarInformacion(MVersiones versionSelected){
        detalleAuto.refrescarInformacion(versionSelected);
        this.dismiss();
    }


    public void mostrarProgresBar(boolean valor) {
        if (valor) {
            binding.pbProgreso.setVisibility(View.VISIBLE);

        } else {
            binding.pbProgreso.setVisibility(View.GONE);

            }
    }

}
