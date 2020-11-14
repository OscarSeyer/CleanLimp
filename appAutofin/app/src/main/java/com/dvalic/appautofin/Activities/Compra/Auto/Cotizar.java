package com.dvalic.appautofin.Activities.Compra.Auto;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.dvalic.appautofin.API.Model.MCotizacionAuto;
import com.dvalic.appautofin.API.Model.MModelos;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTBaseFragment;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.FragmentCotizarBinding;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import eightbitlab.com.blurview.RenderScriptBlur;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class Cotizar extends UTBaseFragment {
    private String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private FragmentCotizarBinding binding;
    private MModelos modelo;
    private MCotizacionAuto[] lstCotAuto;
    DialogoPagoAdicional dialogoPagoAdicional;
    private MCotizacionAuto cotizacionAuto;
    private DetalleAuto detalleAuto;

    public void Cotizar(DetalleAuto detalleAuto){
        this.detalleAuto = detalleAuto;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCotizarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        blurry();
        fillSpinerMeses();
        if( this.getArguments() != null){
                this.modelo = new MModelos();
                this.modelo = (MModelos)getArguments().getSerializable("modelo");
                obtenerCotizacion();
            }

        binding.tvDown.setOnClickListener((v)->{
            backFragment();
        });
        binding.lyCotizar.setOnClickListener((v)->{});

        binding.btnMeInteresa.setOnClickListener((v)->{
            interfaz.mostrarFragmentoModoContacto();
            backFragment();
        });

        binding.spPlanes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int posicion, long l) {
              if(lstCotAuto != null){
                  if(posicion == 0){
                      MCotizacionAuto cot =  lstCotAuto[1];
                      showTextView(cot, 1);
                      binding.tvOpciones.setVisibility(View.GONE);
                  }else{
                   showDialog();
                  }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.tvOpciones.setOnClickListener((v)->{
            showDialogOpciones(this.cotizacionAuto);
        });



    }

    public void obtenerCotizacion() {
        try {
            this.mostrarProgresBar(true);
            mCompositeDisposable.add(apiInterface.obtenerPlanes(this.modelo.getClaveAuto(),"0")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));

        }catch (Exception _exc){

        }
    }

    public MCotizacionAuto[] obtenerCotizacionDefault(){
        return lstCotAuto;

    }

    public void showDialog(){
        DialogFragment dialogFragment = new DialogoPagoAdicional(this, mCompositeDisposable, apiInterface, this.modelo.getClaveAuto());
        assert getFragmentManager() != null;
        dialogFragment.setCancelable(false);
        dialogFragment.show(getFragmentManager(), dialogFragment.getClass().getSimpleName());
    }

    public void showDialogOpciones(MCotizacionAuto cotizacionAuto){
        DialogFragment dialogFragment = new DialogMasOpcionesPago(cotizacionAuto, interfaz, this);
        assert getFragmentManager() != null;
        dialogFragment.setCancelable(false);
        dialogFragment.show(getFragmentManager(), dialogFragment.getClass().getSimpleName());
    }


    private void handleResponse(ResponseBody responseBody) throws IOException, JSONException {
        try {
            this.mostrarProgresBar(false);
            JSONObject jsonObject;
            jsonObject = new JSONObject(responseBody.string());
            if(jsonObject.get("Ok").equals("SI")){
                lstCotAuto = new Gson().fromJson(jsonObject.get("Objeto").toString(),MCotizacionAuto[].class);
                MCotizacionAuto cotizacionAuto = lstCotAuto[lstCotAuto.length -1];
                showTextView(cotizacionAuto,1 );

            }else{
                UTHelpers.showSnackBar(getView(), jsonObject.get("Mensaje").toString(), false);
            }
            if(dialogoPagoAdicional != null){
                dialogoPagoAdicional.mostrarProgresBar(false);
            }

        }catch (Exception _exc){
            this.mostrarProgresBar(false);
            throw  _exc;
        }
    }

    private void handleError(Throwable error)
    {
        this.mostrarProgresBar(false);
        Toast.makeText(getContext(), "Servicio no disponible "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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


    public  void backFragment(){
        getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left,
                R.animator.slide_out_right, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right).remove(this).commit();
    }


    @Override
    public boolean onBackPressed() {
        return false;
    }




    public void blurry(){
        float radius = 2f;

        View decorView = getActivity().getWindow().getDecorView();
        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        //ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        //ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        ConstraintLayout rootView = decorView.findViewById(R.id.cl_activity);
        //Set drawable to draw in the beginning of each blurred frame (Optional).
        //Can be used in case your layout has a lot of transparent space and your content
        //gets kinda lost after after blur is applied.
        Drawable windowBackground = decorView.getBackground();


        binding.blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                //.setHasFixedTransformationMatrix(true)
                .setBlurEnabled(true)
                .setBlurAlgorithm(new RenderScriptBlur(getContext()))
                .setBlurRadius(radius)
                .setBlurAutoUpdate(true);

    }

    public void fillSpinerMeses(){
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext(),android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.string_array_meses));

        binding.spPlanes.setAdapter(arrayAdapter);
    }

    public void showTextView(MCotizacionAuto cotizacionAuto, int origin)
    {
        this.cotizacionAuto = cotizacionAuto;
        if(origin ==2){
            binding.tvOpciones.setVisibility(View.VISIBLE);
        }else{
            binding.tvOpciones.setVisibility(View.GONE);
        }

        binding.tvMarcaModelo.setText(modelo.getNombreMarca() + " /" + modelo.getNombreModelo());
        binding.tvVersion.setText(modelo.getNombreVersion());
        binding.txtPago.setText(UTHelpers.formatMoney(cotizacionAuto.getMonto()));
        binding.tvInscripcion.setText(UTHelpers.formatMoney(cotizacionAuto.getInscripcion()));
        binding.tvPrimerMensualidad.setText(UTHelpers.formatMoney(cotizacionAuto.getPrimerMensualidad()));
        binding.tvTitleMpi.setText(cotizacionAuto.getCantidadMensualidadesPi().toString() + "  Mens. pago inmediato:");
        binding.tvMensPagoInmediato.setText(UTHelpers.formatMoney(cotizacionAuto.getMensualidadesPi()));
        binding.tvLeyend.setText("Como propietario te quedan \n" + cotizacionAuto.getCantidadMesualidadesRestantes() + "  mensualidades puntuales de:");
        binding.tvMensualidadFija.setText(UTHelpers.formatMoney(cotizacionAuto.getMensualidadFija()));

    }


    public void mostrarProgresBar(boolean valor) {
        if (valor) {
            binding.pbProgreso.setVisibility(View.VISIBLE);
            //binding.pbProgresoBack.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            binding.pbProgreso.setVisibility(View.GONE);
            //binding.pbProgresoBack.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


}
