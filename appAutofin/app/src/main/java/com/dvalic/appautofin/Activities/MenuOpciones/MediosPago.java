package com.dvalic.appautofin.Activities.MenuOpciones;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import com.dvalic.appautofin.API.Model.MMedioPago;
import com.dvalic.appautofin.API.Model.SingletonCliente;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.Utilerias.UTBaseFragment;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.FragmentMediosPagoBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MediosPago extends UTBaseFragment {
    private String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private FragmentMediosPagoBinding binding;
    private MediosPagoAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMediosPagoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readRecycler();
        solicitudHistoricoMedioPago();
        binding.btnTarjeta.setOnClickListener((v)->{
            if(adapter == null){
                adapter = new MediosPagoAdapter(new ArrayList<MMedioPago>(), getContext(), this);
            };
            DialogFragment dialogFragment = new DialogTarjetaCreditoDebito(mCompositeDisposable, apiInterface, this);
            assert getFragmentManager() != null;
            dialogFragment.show(getFragmentManager(), dialogFragment.getClass().getSimpleName());
        });
    }


    public void solicitudHistoricoMedioPago(){
        try {
            mostrarProgresBar(true);
            mCompositeDisposable.add(apiInterface.obtenerHistoricoMedioPago(Integer.parseInt(SingletonCliente.getInstance().getIdCuenta()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        }catch (Exception _exc){
            throw  _exc;
        }
    }


    private void handleResponse(ResponseBody responseBody) throws IOException, JSONException {
        try {
            mostrarProgresBar(false);
            JSONObject jsonObject;
            jsonObject = new JSONObject(responseBody.string());
            if(jsonObject.get("Ok").equals("SI")){
                MMedioPago[] lstMedioPago = new Gson().fromJson(jsonObject.get("Objeto").toString(), MMedioPago[].class);
                ArrayList<MMedioPago> lst = new ArrayList<>();
                Collections.addAll(lst, lstMedioPago);
                adapter = new MediosPagoAdapter(lst, getContext(), this);
                binding.rvCard.setAdapter(adapter);
            }else{
                UTHelpers.showSnackBar(getView(), jsonObject.get("Mensaje").toString(), false);
            }

        }catch (Exception _exc){
            throw  _exc;
        }
    }

    private void handleError(Throwable error)
    {
        mostrarProgresBar(false);
        Toast.makeText(getContext(), "Servicio no disponible "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public void readRecycler(){
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        binding.rvCard.setLayoutManager(manager);
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
        interfaz.showToolBar();
        return super.onBackPressed();
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

    public void eliminarMedioPago(int idCuenta, Long numTarjet){
        try{
            mostrarProgresBar(true);
            mCompositeDisposable.add(apiInterface.eliminarMedioPago(idCuenta, numTarjet, IDAPP)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponseDelete, this::handleError));
        }catch (Exception _exc){

        }
    }

    private boolean handleResponseDelete(ResponseBody responseBody) throws IOException, JSONException {
        boolean isDelete = false;
        try {
            mostrarProgresBar(false);
            JSONObject jsonObject;
            jsonObject = new JSONObject(responseBody.string());
            if(!jsonObject.isNull("Ok")){
                if(jsonObject.get("Ok").equals("SI")){
                    solicitudHistoricoMedioPago();
                    isDelete = true;
                }else{
                    UTHelpers.showSnackBar(getView(), jsonObject.get("Mensaje").toString(), false);

                    Snackbar snackbar = Snackbar.make(getView(), jsonObject.get("Mensaje").toString(), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    isDelete = false;
                }
            }

        }catch (Exception _exc){
            throw  _exc;
        }
        return isDelete;
    }
}
