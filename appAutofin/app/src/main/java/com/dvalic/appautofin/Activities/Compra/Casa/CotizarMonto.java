package com.dvalic.appautofin.Activities.Compra.Casa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.dvalic.appautofin.API.Model.MCatalogoDocumentos;
import com.dvalic.appautofin.API.Model.MCotizacion;
import com.dvalic.appautofin.API.Model.MMontos;
import com.dvalic.appautofin.API.Model.MRespuesta;
import com.dvalic.appautofin.Activities.Compra.Auto.DocumentacionAdapter;
import com.dvalic.appautofin.Activities.Compra.Auto.MontoSpinner;
import com.dvalic.appautofin.Activities.Cuenta.DialogRegistroCorreo;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTBaseActivity;
import com.dvalic.appautofin.Utilerias.UTBaseFragment;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.FragmentCotizarMontoBinding;
import com.google.gson.Gson;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CotizarMonto extends UTBaseFragment {

    private String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private FragmentCotizarMontoBinding binding;
    private ArrayList<MCotizacion> lstCotizacion;
    private DialogFragment dialogFragment;
    private MontoSpinner montoSpinner;
    private MMontos montoSelected;
    private boolean showAnimate;
    private CotizacionAdapter adapterCoti;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCotizarMontoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.spMontos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //if(position > 0){
                    montoSelected = montoSpinner.getItem(position);
                    /*DialogFragment  dialogFragment = new DialogMesAdjudicar(interfaz, mCompositeDisposable, apiInterface, monto);
                    assert getFragmentManager() != null;
                    dialogFragment.show(getFragmentManager(), dialogFragment.getClass().getSimpleName());*/
                //}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        this.showAnimate = true;
        textWatcher();
        solicitudCatalogoMontos();
        initRecyclerViewCotizar();
        binding.btnAceptar.setOnClickListener((v)->{
            if(montoSelected.getVal().equals("0")){
                UTHelpers.showSnackBar(getView(), "Por favor selecciona un monto.", false);
                return;
            }
            if(TextUtils.isEmpty(binding.tvMesAdjudicar.getText().toString().trim())){
                UTHelpers.showSnackBar(getView(), "Por favor ingresa una mensualidad a adjudicar.", false);
                return;
            }

            solicitudCotizacion(binding.tvMesAdjudicar.getText().toString().trim());
            binding.tvMesAdjudicar.setEnabled(false);
        });

        binding.btnLimpiar.setOnClickListener((v)->{
            binding.spMontos.setSelection(0);
            binding.tvMesAdjudicar.setEnabled(true);
            binding.tvMesAdjudicar.setText("");
            adapterCoti.clearItems();
            binding.constraintLayout4.setVisibility(View.GONE);
            this.showAnimate = true;
        });

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IComunicador) {
            interfaz = (IComunicador) context;
        }
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }



    @SuppressLint("CheckResult")
    private void solicitudCatalogoMontos() {
        Observable<MRespuesta> request = apiInterface.obtenerCatalogoMontos();
        request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::handleResponse, this::handleError);
    }


    private void handleResponse(MRespuesta mRespuesta) {
        try {

            if (mRespuesta != null) {
                if (mRespuesta.getOk().equals("SI")) {
                    MMontos[] lstMontos = new Gson().fromJson(mRespuesta.getObjeto(), MMontos[].class);
                    ArrayList<MMontos> lstResult = new ArrayList<>();
                    lstResult.addAll(Arrays.asList(lstMontos));
                    for (MMontos monto : lstResult) {
                        double importe = Double.parseDouble(Objects.requireNonNull(monto.getMonto()));
                        monto.setMonto(UTHelpers.formatMoney(importe));
                    }
                    lstResult.add(0, new MMontos("","Selecciona", "0"));
                    montoSpinner = new MontoSpinner(getContext(), R.layout.item_monto_sipnner, lstResult);
                    binding.spMontos.setAdapter(montoSpinner);

                }
            }

        } catch (Exception _exc) {
            Log.i(TAG, _exc.getStackTrace().toString());
        }
    }

    private void handleError(Throwable throwable) {
        Toast.makeText(getContext(), "Error " + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public void moveToTopScroll(int position) {
        binding.rvExpanded.smoothScrollToPosition(position);
    }

    public void textWatcher(){
        binding.tvMesAdjudicar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() > 0){
                    int val = Integer.parseInt(charSequence.toString());
                    if(val > 60){
                        View view = binding.constraintLayout4; //getView().findViewById(R.id.constraintLayout4);
                        UTHelpers.showSnackBar(view , "Rango permitido: 1-60 meses", true);
                        binding.tvMesAdjudicar.setText(charSequence.toString().substring(0,0));

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @SuppressLint("CheckResult")
    private void solicitudCotizacion(String mensualidad) {
        String monto = montoSelected.getMonto().replace("$", "").replace(",", "");
        Observable<MRespuesta> request = apiInterface.obtenerCotizacionCasa(montoSelected.getIdMonto(), monto , mensualidad);
        request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::handleResponseCot, this::handleError);
    }


    private void handleResponseCot(MRespuesta mRespuesta) {
        try {

            if (mRespuesta != null) {
                if (mRespuesta.getOk().equals("SI")) {
                    MCotizacion cotizcion = new Gson().fromJson(mRespuesta.getObjeto(), MCotizacion.class);
                    ArrayList<MCotizacion> lstResult = new ArrayList<>();
                    lstResult.add(cotizcion);
                    if(this.showAnimate){
                        adapterCoti.addItems(lstResult);
                        binding.constraintLayout4.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_scale));
                        binding.constraintLayout4.setVisibility(View.VISIBLE);
                        showAnimate = false;
                    }else {
                        adapterCoti.addItem(cotizcion);
                    }
                    sumarMontos();
                }else if(mRespuesta.getOk().equals("NO")){
                    UTHelpers.showSnackBar(getView(), mRespuesta.getMensaje(), false);
                }
            }

        } catch (Exception _exc) {
            Log.i(TAG, _exc.getStackTrace().toString());
        }
    }

    public void initRecyclerViewCotizar(){
        adapterCoti = new CotizacionAdapter(this);
        binding.rvExpanded.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvExpanded.setAdapter(adapterCoti);
    }

    public void sumarMontos(){
        double sum = 0;
        for (MCotizacion item: adapterCoti.getItems()) {
            sum += Double.parseDouble(Objects.requireNonNull(item.getMonto()));
        }
        String summary =  "Monto total: " +UTHelpers.formatMoney(sum) +"\nMensualidades adjudicar: " + binding.tvMesAdjudicar.getText().toString().trim();
        binding.tvResumen.setText(summary);
    }



}
