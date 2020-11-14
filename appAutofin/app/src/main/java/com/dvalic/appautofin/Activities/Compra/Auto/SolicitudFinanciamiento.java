package com.dvalic.appautofin.Activities.Compra.Auto;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dvalic.appautofin.API.IActions;
import com.dvalic.appautofin.API.Model.MCatalogoDocumentos;
import com.dvalic.appautofin.API.Model.MRespuesta;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTBaseFragment;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.FragmentSolicitudFinanciamientoBinding;
import com.dvalic.appautofin.databinding.ItemDocumentoBinding;
import com.google.android.material.appbar.AppBarLayout;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class SolicitudFinanciamiento extends UTBaseFragment {
    private String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private FragmentSolicitudFinanciamientoBinding binding;
    private ItemDocumentoBinding bindingDocs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSolicitudFinanciamientoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readRecycler();

        binding.btnContinuar.setOnClickListener(v -> {
            interfaz.mostrarFragmentoPagar();
        });

        solicitudCatalogoDocumentos();
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
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    @SuppressLint("CheckResult")
    public  void solicitudCatalogoDocumentos(){
        mostrarProgresBar(true);
        /*mCompositeDisposable.add(apiInterface.obtenerCatalogoDocumentos()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));*/

        Observable<MRespuesta> request =   apiInterface.obtenerCatalogoDocumentos();
        request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::handleResponse, this::handleError);

    }


    private void handleResponse(MRespuesta respuesta) throws IOException, JSONException {
        try {

            if(respuesta != null){
                if(respuesta.getOk().equals("SI")){
                    MCatalogoDocumentos[] lstDocumentos = new Gson().fromJson(respuesta.getObjeto(), MCatalogoDocumentos[].class);
                    binding.rvDocs.setAdapter(new DocumentacionAdapter(lstDocumentos,interfaz, getFragmentManager(), getContext()));
                    mostrarProgresBar(false);

                }
            }

        }catch (Exception _exc){
            mostrarProgresBar(false);
            Log.i(TAG, _exc.getStackTrace().toString());
        }
    }

    public void readRecycler(){
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        binding.rvDocs.setLayoutManager(manager);
    }

    private void handleError(Throwable error)
    {
        mostrarProgresBar(false);
        Toast.makeText(getContext(), "Error "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public void mostrarProgresBar(boolean valor) {
        if (valor) {
            binding.pbProgreso.setVisibility(View.VISIBLE);
        } else {
            binding.pbProgreso.setVisibility(View.GONE);

        }
    }
}
