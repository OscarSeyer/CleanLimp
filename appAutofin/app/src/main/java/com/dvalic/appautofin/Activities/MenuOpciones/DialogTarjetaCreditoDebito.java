package com.dvalic.appautofin.Activities.MenuOpciones;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.dvalic.appautofin.API.Model.MMedioPago;
import com.dvalic.appautofin.API.Model.SingletonCliente;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.DialogTarjetaCreditoDebitoBinding;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class DialogTarjetaCreditoDebito extends DialogFragment {
    private String TAG = this.getClass().getSimpleName();
    private DialogTarjetaCreditoDebitoBinding binding;
    private CompositeDisposable mCompositeDisposable;
    IActions apiInterface;
    private String cadenaTemp;
    private String metodoPago;
    private MediosPago mediosPago;
    private Handler handler;
    private String IDAPP = "ANDRO";



    public DialogTarjetaCreditoDebito(CompositeDisposable mCompositeDisposable, IActions apiInterface, MediosPago mediosPago){
        this.mCompositeDisposable = mCompositeDisposable;
        this.apiInterface = apiInterface;
        this.mediosPago = mediosPago;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogTarjetaCreditoDebitoBinding.inflate(inflater, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cadenaTemp = "";
        binding.btnContinuar.setOnClickListener((v)->{
            if(validarCampos()){
                String date = binding.edtFechaExpiracion.getText().toString().trim().replace("/","");
                //String clear = test.replace("/","");
                int anio = Integer.parseInt(date.substring(0,2));
                int mes  = Integer.parseInt(date.substring(2,4));
                Long num_tarj = Long.parseLong(binding.edtNumeroTarjeta.getText().toString().trim());
                registraMedioPago(anio, mes, binding.edtNombreTarjeta.getText().toString().trim(), num_tarj);
            }

        });


        binding.edtNumeroTarjeta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String cadenaVieja = "";
                binding.txtNumeroTarjeta.setText(charSequence);

                int posicionCursor = binding.edtNumeroTarjeta.getSelectionStart();
                String cadenaLimpia = charSequence.toString().replaceAll("[^\\d.]", "");
                if (!charSequence.toString().isEmpty()) {
                    if (cadenaLimpia.length() > 4) {
                        cadenaLimpia = cadenaLimpia.substring(0, 4) + " " + cadenaLimpia.substring(4);
                    }
                    if (cadenaLimpia.length() > 9) {
                        cadenaLimpia = cadenaLimpia.substring(0, 9) + " " + cadenaLimpia.substring(9);
                    }
                    if (cadenaLimpia.length() > 14) {
                        cadenaLimpia = cadenaLimpia.substring(0, 14) + " " + cadenaLimpia.substring(14);
                    }
                    if (cadenaTemp.length() > cadenaLimpia.length()) {
                        //decrement
                        if (posicionCursor > 0 && !UTHelpers.TryParseLong(cadenaTemp.charAt(posicionCursor - 1) + "")) {
                            posicionCursor--;
                        }
                    } else {
                        //increase
                        if (posicionCursor < cadenaLimpia.length() && !UTHelpers.TryParseLong(cadenaLimpia.charAt(posicionCursor - 1) + "")) {
                            posicionCursor++;
                        }
                    }
                    if (cadenaLimpia.substring(0, 1).equals("4")){
                        binding.ivTipoTarjeta.setBackground(getResources().getDrawable(R.drawable.ic_visa));
                        metodoPago = "1";
                    }
                    if (cadenaLimpia.substring(0, 1).equals("5")){
                        binding.ivTipoTarjeta.setBackground(getResources().getDrawable(R.drawable.ic_mastercard));
                        metodoPago = "2";
                    }
                    if (cadenaLimpia.substring(0, 1).equals("4")){
                        binding.ivTipoTarjetaBack.setBackground(getResources().getDrawable(R.drawable.ic_visa));
                    }
                    if (cadenaLimpia.substring(0, 1).equals("5")){
                        binding.ivTipoTarjetaBack.setBackground(getResources().getDrawable(R.drawable.ic_mastercard));
                    }
                }
                cadenaTemp = cadenaLimpia;
                binding.txtNumeroTarjeta.setText(cadenaLimpia);
                // binding.edtNumeroTarjeta.setSelection(posicionCursor);
                cadenaVieja = cadenaTemp;


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtNombreTarjeta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtNombreTarjeta.setText(s.toString().toUpperCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtFechaExpiracion.addTextChangedListener(new TextWatcher() {
            String cadenaLimpia = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                cadenaLimpia = charSequence.toString();
                if (before != 1) {
                    if (!charSequence.toString().isEmpty()) {
                        if (cadenaLimpia.length() == 2) {
                            cadenaLimpia = cadenaLimpia.substring(0, 2) + "/" + cadenaLimpia.substring(2);
                            binding.txtFechaExpiracion.setText(cadenaLimpia);
                            binding.edtFechaExpiracion.setText(cadenaLimpia);
                            binding.edtFechaExpiracion.setSelection(3);
                        }
                    }
                }
                cadenaTemp = cadenaLimpia;
                binding.txtFechaExpiracion.setText(cadenaLimpia);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.edtCvvc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtCvvc.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }

    private void registraMedioPago(int anio, int mes, String nombre, Long numero) {
        try{
            mostrarProgresBar(true);
            MMedioPago mp = new MMedioPago();
            mp.setIdCuenta(Integer.parseInt(SingletonCliente.getInstance().getIdCuenta()));
            mp.setIdMedioPago("DEBIT");
            mp.setAnioVencimiento(anio);
            mp.setMesVencimiento(mes);
            mp.setNombreTarjeta(nombre);
            mp.setNumeroTarjeta(numero);
            mCompositeDisposable.add(apiInterface.registrarMedioPago(mp, IDAPP)
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
                UTHelpers.showSnackBar(getView(), "Medio de pago agregado.", false);
                this.mediosPago.solicitudHistoricoMedioPago();
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDialog().dismiss();
                    }
                }, 3000);


            }else{
                UTHelpers.showSnackBar(getView(), "Medio de pago no agregado: " + jsonObject.get("Mensaje").toString(), false);
            }


        }catch (Exception _exc){
            throw  _exc;
        }
    }

    private void handleError(Throwable error)
    {
        mostrarProgresBar(false);
        Toast.makeText(getContext(), "Error "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        super.onResume();
        int widthPixels = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int heightPixels = (int) (getResources().getDisplayMetrics().heightPixels * 0.9);
        getDialog().getWindow().setLayout(widthPixels, heightPixels);
    }


    public boolean validarCampos()
    {

        if (binding.edtNombreTarjeta.getText().toString().equals("")) {
            UTHelpers.showSnackBar(getView(), "Ingresa nombre completo", false);
            return false;
        }
        if (binding.edtNumeroTarjeta.getText().toString().equals("") || binding.edtNumeroTarjeta.getText().toString().length() < 16) {
            UTHelpers.showSnackBar(getView(), "Número de tarjeta invalida", false);
            return false;
        }
        if (binding.edtFechaExpiracion.getText().toString().equals("") || binding.edtFechaExpiracion.getText().toString().length() < 5) {
            UTHelpers.showSnackBar(getView(), "Ingresa vigencia", false);
         return false;
        }
        return  true;
    }

    public void mostrarProgresBar(boolean valor) {
        if (valor) {
            binding.pbProgreso.setVisibility(View.VISIBLE);
            binding.btnContinuar.setVisibility(View.GONE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        } else {
            binding.pbProgreso.setVisibility(View.GONE);
            binding.btnContinuar.setVisibility(View.VISIBLE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }





}
