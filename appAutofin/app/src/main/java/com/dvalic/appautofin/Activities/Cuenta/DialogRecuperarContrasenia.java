package com.dvalic.appautofin.Activities.Cuenta;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
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
import com.dvalic.appautofin.API.Model.MPersona;
import com.dvalic.appautofin.API.Model.SingletonCliente;
import com.dvalic.appautofin.Database.DatabaseSqlite;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.DialogRecuperarContraseniaBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class DialogRecuperarContrasenia extends DialogFragment {
    private String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private CompositeDisposable compositeDisposable;
    private DialogRecuperarContraseniaBinding binding;
    IActions apiInterface;
    private DatabaseSqlite db;
    Handler handler;
    private boolean isMobileAccess;

    public DialogRecuperarContrasenia(IComunicador interfaz, CompositeDisposable compositeDisposable, IActions apiInterface){
        this.interfaz = interfaz;
        this.compositeDisposable = compositeDisposable;
        this.apiInterface = apiInterface;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogRecuperarContraseniaBinding.inflate(inflater, container, false);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnOk.setText("Enviar código");
        db = new DatabaseSqlite(getActivity());

        binding.btnOk.setOnClickListener((v)->{
            if(binding.ilyClave.getVisibility() == View.GONE){
                if(!Objects.requireNonNull(binding.tvMail.getText()).toString().equals("")){
                    if(UTHelpers.TryParseLong(binding.tvMail.getText().toString())){
                        if (Objects.requireNonNull(binding.tvMail.getText()).toString().length() < 10) {
                            Snackbar snackbar = Snackbar.make(Objects.requireNonNull(this.getView()), "Ingresa un número de teléfono valido!", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            return;
                        }else{
                            isMobileAccess = true;
                        }
                    }else{
                        String emailAcceso = Objects.requireNonNull(binding.tvMail.getText()).toString().trim();
                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        if (!emailAcceso.matches(emailPattern)) {
                            Snackbar snackbar = Snackbar.make(Objects.requireNonNull(this.getView()), "Ingresa un correo valido!", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            return;
                        }{
                            isMobileAccess = false;
                        }
                    }
                }else {
                    Snackbar snackbar = Snackbar.make(Objects.requireNonNull(this.getView()), "Ingresa un número de teléfono o correo", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }
                binding.btnOk.setVisibility(View.GONE);
                obtenerClave(binding.tvMail.getText().toString(), isMobileAccess);
            }else if(binding.tvClave.getVisibility() == View.VISIBLE){
                if(!TextUtils.isEmpty(binding.tvMail.getText()) && !TextUtils.isEmpty(binding.tvClave.getText())){
                    validaClave(binding.tvMail.getText().toString(), binding.tvClave.getText().toString(), isMobileAccess);
                }
            }
        });
    }

    private void validaClave(String value, String clave, boolean isMobileAuth) {
        try {
            mostrarProgresBar(true);
            binding.btnOk.setVisibility(View.GONE);
            if(isMobileAuth){
                this.compositeDisposable.add(apiInterface.obtenerCuentaPorTelefonoClave(value, clave)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        //.subscribe(result->{}, throwable -> {}));
                        .subscribe(this::handleResponseValidate, this::handleError));
            }else{
                this.compositeDisposable.add(apiInterface.obtenerCuentaPorCorreoClave(value, clave)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        //.subscribe(result->{}, throwable -> {}));
                        .subscribe(this::handleResponseValidate, this::handleError));
            }


        }catch (Exception _exc){
            throw  _exc;
        }
    }

    private  void handleResponseValidate(ResponseBody responseBody){
       try {

           mostrarProgresBar(false);
           binding.btnOk.setVisibility(View.VISIBLE);

           JSONObject jsonObject;
           jsonObject = new JSONObject(responseBody.string());
           if(jsonObject != null){
               if(jsonObject.get("Ok").equals("SI")){
                   MPersona cuenta = new Gson().fromJson(jsonObject.get("Objeto").toString(), MPersona.class);
                   db.insertStorageCliente(cuenta);
                   if(SingletonCliente.getVerifyInstance() != null){
                       SingletonCliente.destroySingleton();
                       SingletonCliente.getInstance(cuenta);
                   }else{
                       SingletonCliente.getInstance(cuenta);
                   }

                   Snackbar snackbar = Snackbar.make(Objects.requireNonNull(this.getView()), "Hola " + cuenta.getNombre() + " Bienvenido.", Snackbar.LENGTH_LONG);
                   snackbar.show();
                   handler = new Handler();
                   handler.postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           interfaz.mostrarFragmentoPrincipal();
                           dismiss();
                       }
                   },3000);
               }else{
                   UTHelpers.showSnackBar(getView(), jsonObject.get("Mensaje").toString(), false);
               }

           }else{
               UTHelpers.showSnackBar(getView(), "Por el momento no podemos recuperar su cuenta, intente de nuevo más tarde", false);
           }


       }catch (Exception _exc){
           Log.i(TAG, _exc.getStackTrace().toString());
       }
    }

    @Override
    public void onResume() {
        super.onResume();
        int widthPixels = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int heightPixels = (int) (getResources().getDisplayMetrics().heightPixels * 0.8);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(widthPixels, heightPixels);
    }



    private void obtenerClave(String value, boolean isMobileAuth){
        mostrarProgresBar(true);
        if(isMobileAuth){
            this.compositeDisposable.add(apiInterface.obtenerClavePorTelefono("52",value, "ANDRO")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    //.subscribe(result->{}, throwable -> {}));
                    .subscribe(this::handleResponse, this::handleError));
        }else{
            this.compositeDisposable.add(apiInterface.obtenerClavePorCorreo(value)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    //.subscribe(result->{}, throwable -> {}));
                    .subscribe(this::handleResponse, this::handleError));
        }

    }

    private void handleResponse(ResponseBody response) throws IOException {
        mostrarProgresBar(false);
        binding.btnOk.setVisibility(View.VISIBLE);
        assert response != null;
        JsonObject json = new Gson().fromJson(response.string(), JsonObject.class);
        if (json == null || json.isJsonNull()) {
            Log.e("Error", "Error al consultar el servicio Error al parsear Json");
        } else {
            Log.e("response", json.toString());
            if (json.get("Ok").getAsString().equals("SI")) {
                binding.ilyClave.setVisibility(View.VISIBLE);
                binding.btnOk.setText("Ingresar");
                Snackbar snackbar = Snackbar.make(Objects.requireNonNull(this.getView()), json.get("Mensaje").getAsString(), Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                Snackbar snackbar = Snackbar.make(Objects.requireNonNull(this.getView()), json.get("Mensaje").getAsString(), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    private void handleError(Throwable error)
    {
        binding.btnOk.setVisibility(View.VISIBLE);
        mostrarProgresBar(false);
        Toast.makeText(getContext(), "Error "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public void mostrarProgresBar(boolean valor) {
        if (valor) {
            binding.pbProgreso.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        } else {
            binding.pbProgreso.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

}
