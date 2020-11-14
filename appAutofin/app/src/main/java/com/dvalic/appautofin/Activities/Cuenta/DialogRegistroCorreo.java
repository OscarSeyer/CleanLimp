package com.dvalic.appautofin.Activities.Cuenta;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.dvalic.appautofin.API.IActions;
import com.dvalic.appautofin.API.Model.MPersona;
import com.dvalic.appautofin.API.Model.SingletonCliente;
import com.dvalic.appautofin.Database.DatabaseSqlite;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.MainActivity;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.DialogRegistroCorreoBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.Objects;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class DialogRegistroCorreo extends DialogFragment {
    private String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private CompositeDisposable compositeDisposable;
    private DialogRegistroCorreoBinding binding;
    private DatabaseSqlite db;
    private IActions apiInterface;
    private boolean IsCorrect = false;
    private Handler handler;
    private boolean isMobileAccess;
    private String IDAPP = "ANDRO";

    public DialogRegistroCorreo(IComunicador interfaz, CompositeDisposable compositeDisposable, IActions apiInterface) {
        this.interfaz = interfaz;
        this.compositeDisposable = compositeDisposable;
        this.apiInterface = apiInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogRegistroCorreoBinding.inflate(inflater, container, false);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseSqlite(getActivity());
        watchedMailOrMobile();
        binding.btnRegistroCorreo.setOnClickListener((v) -> {
            if(!Objects.requireNonNull(binding.tvMail.getText()).toString().trim().equals("")){
                if(UTHelpers.TryParseLong(binding.tvMail.getText().toString().trim())){
                    if (Objects.requireNonNull(binding.tvMail.getText()).toString().length() < 10) {
                        Snackbar snackbar = Snackbar.make(Objects.requireNonNull(this.getView()), "Ingresa un número de teléfono válido!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        return;
                    }else{
                     isMobileAccess = true;
                    }
                }else{
                    String emailAcceso = Objects.requireNonNull(binding.tvMail.getText()).toString().trim();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if (!emailAcceso.matches(emailPattern)) {
                        Snackbar snackbar = Snackbar.make(Objects.requireNonNull(this.getView()), "Ingresa un correo válido!", Snackbar.LENGTH_SHORT);
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
            if (!binding.chkAceptoTerminos.isChecked()) {
                Snackbar snackbar = Snackbar.make(Objects.requireNonNull(this.getView()), "Acepta los términos y condiciones", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return;
            }
            /*if (Objects.requireNonNull(binding.tvNombre.getText()).toString().equals("")) {
                Snackbar snackbar = Snackbar.make(Objects.requireNonNull(this.getView()), "Ingresa tu nombre", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return;
            }
            if (Objects.requireNonNull(binding.tvApellidoPaterno.getText()).toString().equals("")) {
                Snackbar snackbar = Snackbar.make(Objects.requireNonNull(this.getView()), "Ingresa tu apellido paterno", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return;
            }*/
            mostrarProgress(true);
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    registro(binding.tvNombre.getText().toString().trim(), binding.tvApellidoPaterno.getText().toString().trim(),binding.tvApellidoMaterno.getText().toString().trim() , binding.tvMail.getText().toString().trim(), isMobileAccess, binding.tvLada.getText().toString().trim());
                }
            },3000);


        });

    }

    private void registro(String nombre, String apellido_paterno, String apellido_materno, String value, boolean isMobileAuth, String lada) {
        try {

            MPersona persona = new MPersona();
            persona.setNombre(nombre);
            persona.setApellidoPaterno(apellido_paterno);
            persona.setApellidoMaterno(apellido_materno);
            persona.setCorreo(!isMobileAuth ? value : null);
            persona.setTelefonoMovil(isMobileAuth ? value : null);
            persona.setLadaMovil(isMobileAuth ? lada: null);
            persona.setStatus(true);
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(getActivity(), new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String refreshToken = instanceIdResult.getToken();
                    //jsonObject.addProperty("Token", refreshToken);
                    persona.setToken(refreshToken);
                    compositeDisposable.add(apiInterface.registrarCuenta(persona, IDAPP)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(result -> {
                                mostrarProgress(false);
                                JSONObject jsonObj;
                                jsonObj = new JSONObject(result.string());
                                if(jsonObj != null){
                                    if(jsonObj.get("Ok").equals("SI")){
                                        MPersona client = new Gson().fromJson(jsonObj.get("Objeto").toString(), MPersona.class);
                                        if (client != null) {
                                            Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getView()), jsonObj.get("Mensaje").toString(), Snackbar.LENGTH_SHORT);
                                            snackbar.show();
                                            db.insertStorageCliente(client);
                                            SingletonCliente.getInstance(client);
                                            handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                                    startActivity(intent);
                                                    Objects.requireNonNull(getActivity()).finish();
                                                    dismiss();
                                                }
                                            },3000);

                                        } else {
                                            Log.e("Error", "Error al consultar el servicio Error al parsear Json");
                                            Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getView()), jsonObj.get("Mensaje").toString(), Snackbar.LENGTH_SHORT);
                                            snackbar.show();
                                        }

                                    }
                                }

                            }, throwable -> {
                                mostrarProgress(false);
                                Log.e("Error", Objects.requireNonNull(throwable.getMessage()));
                                Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getView()), "Error al registrar datos.", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismiss();
                                    }
                                },3000);
                            }));
                    //.subscribe(this::handleResponse, this::handleError));
                }

            });


        } catch (Exception _exc) {
            Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getView()), "Por el momento no puedes registrarte, intentalo nuevamente más tarde", Snackbar.LENGTH_SHORT);
            snackbar.show();
            mostrarProgress(false);
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            },3000);
            Log.e("error", _exc.toString());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        int widthPixels = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int heightPixels = (int) (getResources().getDisplayMetrics().heightPixels * 0.8);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(widthPixels, heightPixels);
    }

    public void mostrarProgress(boolean isShow) {
        if (isShow) {
            binding.btnRegistroCorreo.setVisibility(View.GONE);
            binding.pbProgreso.setVisibility(View.VISIBLE);
            //binding.pbProgresoBack.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            binding.btnRegistroCorreo.setVisibility(View.VISIBLE);
            binding.pbProgreso.setVisibility(View.GONE);
            //binding.pbProgresoBack.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public void watchedMailOrMobile(){
        binding.tvMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(UTHelpers.TryParseLong(charSequence.toString().trim())){
                    if (Objects.requireNonNull(binding.tvMail.getText()).toString().length() >= 10) {
                        binding.ilyLada.setVisibility(View.VISIBLE);


                    }else{
                          binding.ilyLada.setVisibility(View.GONE);
                    }
                }else{
                       binding.ilyLada.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


}
