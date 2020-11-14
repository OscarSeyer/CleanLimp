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
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.dvalic.appautofin.API.APIClient;
import com.dvalic.appautofin.API.IActions;
import com.dvalic.appautofin.API.Model.MMarcas;
import com.dvalic.appautofin.API.Model.MPromociones;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTBaseActivity;
import com.dvalic.appautofin.Utilerias.UTBaseFragment;
import com.dvalic.appautofin.databinding.FragmentMarcasBinding;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

public class Marcas extends UTBaseFragment {
    private String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private FragmentMarcasBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMarcasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //apiInterface = APIClient.getClient().create(IActions.class);
        binding.tvBack.setOnClickListener((v)->{
            onBackPressed();
        });
        readRecycler();
        solicitudMarcas();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IComunicador){
            interfaz = (IComunicador)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onBackPressed()
    {
        interfaz.mostrarFragmentoPrincipal();
        return false;
    }

    @SuppressLint("CheckResult")
    private void solicitudMarcas(){
        mostrarProgresBar(true);
        Observable<ArrayList<MMarcas>> request = apiInterface.obtenerMarcas();
        request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::handleResponse, this::handleError)
        ;
    }


    private void handleResponse(ArrayList<MMarcas> lstMarcas)
    {
        binding.rvMarcas.setAdapter(new MarcasAdapter(lstMarcas,interfaz, getContext(), this));
        mostrarProgresBar(false);
    }

    private void handleError(Throwable error)
    {

        mostrarProgresBar(false);
        Toast.makeText(getContext(), "Error "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }




    public void readRecycler(){
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        binding.rvMarcas.setLayoutManager(manager);
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
