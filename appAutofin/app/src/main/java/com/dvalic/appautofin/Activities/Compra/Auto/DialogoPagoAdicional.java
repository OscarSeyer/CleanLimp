package com.dvalic.appautofin.Activities.Compra.Auto;

import android.annotation.SuppressLint;
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

import com.dvalic.appautofin.API.IActions;
//import com.dvalic.appautofin.API.Model.MCotizacion;
import com.dvalic.appautofin.API.Model.MCotizacionAuto;
import com.dvalic.appautofin.API.Model.MMarcas;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.DialogPagoAdicionalBinding;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class DialogoPagoAdicional extends DialogFragment {

    private  String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private DialogPagoAdicionalBinding binding;
    private Cotizar cotizar;
    private CompositeDisposable mCompositeDisposable;
    private IActions apiInterface;
    private String claveAuto;
    private MCotizacionAuto[] lstCotAuto;

    public DialogoPagoAdicional(Cotizar cotizar, CompositeDisposable mCompositeDisposable, IActions apiInterface, String claveAuto){
        this.cotizar = cotizar;
        this.mCompositeDisposable = mCompositeDisposable;
        this.apiInterface = apiInterface;
        this.claveAuto = claveAuto;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogPagoAdicionalBinding.inflate(inflater, container, false);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnConPago.setOnClickListener((v)->{
            if(!Objects.requireNonNull(binding.tvPagoAdicional.getText()).toString().trim().equals("")){
                String pago = binding.tvPagoAdicional.getText().toString().trim();
                obtenerCotizacion(pago);
                this.dismiss();
            }else{
                UTHelpers.showSnackBar(getView(), "Por favor ingresa la cantidad  adicional.", false);
            }
        });
        binding.btnSinpago.setOnClickListener((v)->{
            MCotizacionAuto[] cotByDefault = this.cotizar.obtenerCotizacionDefault();
            this.cotizar.showTextView(cotByDefault[0], 1);
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

    public void mostrarProgresBar(boolean valor) {
        if (valor) {
            binding.pbProgreso.setVisibility(View.VISIBLE);
            binding.pbProgresoBack.setVisibility(View.VISIBLE);

        } else {
            binding.pbProgreso.setVisibility(View.GONE);
            binding.pbProgresoBack.setVisibility(View.GONE);

        }
    }

    @SuppressLint("CheckResult")
    public void obtenerCotizacion(String pago) {
        try {
            mostrarProgresBar(true);
            /*mCompositeDisposable.add(apiInterface.obtenerPlanes(this.claveAuto,pago)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));*/


            Observable<ResponseBody> request = apiInterface.obtenerPlanes(this.claveAuto,pago);
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(this::handleResponse, this::handleError);

        }catch (Exception _exc){
        }
    }


    private void handleResponse(ResponseBody responseBody) throws IOException, JSONException {
        try {
            mostrarProgresBar(false);
            JSONObject jsonObject;
            jsonObject = new JSONObject(responseBody.string());
            if(jsonObject.get("Ok").equals("SI")){
                lstCotAuto = new Gson().fromJson(jsonObject.get("Objeto").toString(),MCotizacionAuto[].class);
                MCotizacionAuto cotizacionAuto = lstCotAuto[0];
                this.cotizar.showTextView(cotizacionAuto, 2);

            }else{
                UTHelpers.showSnackBar(getView(), jsonObject.get("Mensaje").toString(), false);
            }


        }catch (Exception _exc){
            mostrarProgresBar(false);
            throw  _exc;
        }
    }

    private void handleError(Throwable error)
    {
        Toast.makeText(getContext(), "Servicio no disponible "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        mostrarProgresBar(false);
    }



}
