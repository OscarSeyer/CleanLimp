package com.dvalic.appautofin.Activities.Cuenta;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import com.dvalic.appautofin.API.Model.MPersona;
import com.dvalic.appautofin.API.Model.SingletonCliente;
import com.dvalic.appautofin.Database.DatabaseSqlite;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.MainActivity;
import com.dvalic.appautofin.Utilerias.UTBaseFragment;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.FragmentLoginWithOptionsBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Objects;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class LoginWithOptions extends UTBaseFragment {

    private String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private FragmentLoginWithOptionsBinding binding;
    private LoginButton loginButton;
    private CallbackManager mCallbackManager;
    private Handler handler;
    private DatabaseSqlite db;
    private boolean isMobileAccess;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginWithOptionsBinding.inflate(inflater, container, false);
        loginButton = binding.btnFacebook;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseSqlite(getActivity());
        binding.tvRegistrarmeCorreo.setOnClickListener((v)->{
            DialogFragment dialogFragment = new DialogRegistroCorreo(interfaz, mCompositeDisposable, apiInterface);
            assert getFragmentManager() != null;
            dialogFragment.show(getFragmentManager(), dialogFragment.getClass().getSimpleName());
        });
        binding.tvOlvideContrasenia.setOnClickListener((v)->{
            DialogFragment dialogFragment = new DialogRecuperarContrasenia(interfaz, mCompositeDisposable, apiInterface);
            assert getFragmentManager() != null;
            dialogFragment.show(getFragmentManager(), dialogFragment.getClass().getSimpleName());
        });
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("AVISO", "INICIO DE SESION CON FACEBOOK EXITO");
                final AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, (object, response) -> {
                    Log.e("json", object.toString());
                    Log.e("Nombre", object.optString("name"));
                    Log.e("Id", object.optString("id"));
                    ingresoFacebook(object.optString("name"), object.optString("email"));
                });
                Bundle parametros = new Bundle();
                parametros.putString("fields", "id,name,email,birthday");
                graphRequest.setParameters(parametros);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("AVISO", "INICIO DE SESION CON FACEBOOK CANCELADO");

            }

            @Override
            public void onError(FacebookException error) {
                Log.e("AVISO", "INICIO DE SESION CON FACEBOOK ERROR");

            }
        });

        binding.btnAcceso.setOnClickListener((v)->{
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
            binding.btnAcceso.setVisibility(View.GONE);
            validaCuenta(binding.tvMail.getText().toString(), binding.tvContrasena.getText().toString(), isMobileAccess);

        });

    }


    private void validaCuenta(String value, String clave, boolean isMobileAuth) {
        try {
            mostrarProgresBar(true);
            binding.btnAcceso.setVisibility(View.GONE);
            if(isMobileAuth){
                mCompositeDisposable.add(apiInterface.obtenerCuentaPorTelefonoClave(value, clave)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        //.subscribe(result->{}, throwable -> {}));
                        .subscribe(this::handleResponseValidate, this::handleError));
            }else{
                mCompositeDisposable.add(apiInterface.obtenerCuentaPorCorreoClave(value, clave)
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
            binding.btnAcceso.setVisibility(View.VISIBLE);
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


    private void handleError(Throwable error)
    {
        binding.btnAcceso.setVisibility(View.VISIBLE);
        mostrarProgresBar(false);
        Toast.makeText(getContext(), "Error "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }


    public void mostrarProgresBar(boolean isShow) {
        if (isShow) {
            binding.pbProgreso.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            binding.pbProgreso.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
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


    private void ingresoFacebook(String nombre, String email){
        try {
            mCompositeDisposable.add(apiInterface.obtenerCuentaPorFcebook(email)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    //.subscribe(result->{}, throwable -> {}));
                    .subscribe(this::handleResponseFacebook, this::handleError));

        }catch (Exception _exc){
            throw _exc;
        }
    }

    private void handleResponseFacebook(ResponseBody responseBody) throws IOException, JSONException {
        try {
            assert responseBody != null;
            if(responseBody != null){
                JSONObject jsonObject;
                jsonObject = new JSONObject(responseBody.string());
                if(jsonObject.get("Ok").equals("SI")){
                    MPersona cuenta = new Gson().fromJson(jsonObject.get("Objeto").toString(),MPersona.class);
                    verifyToken(cuenta);                    //
                    Snackbar snackbar = Snackbar.make(Objects.requireNonNull(this.getView()), "Hola " + cuenta.getNombre() + " Bienvenido.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            interfaz.mostrarFragmentoPrincipal();

                        }
                    },3000);

                }else{


                }
            }else{
                UTHelpers.showSnackBar(getView(), "Por el momento no puede iniciar sesión, inténtelo de nuevo más tarde.", false);
            }

        }catch (Exception _exc){
            throw  _exc;
        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void verifyToken(MPersona cuenta) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(getActivity(), new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String refreshToken = instanceIdResult.getToken();
                if (!cuenta.getToken().equals(refreshToken)) {
                    cuenta.setToken(refreshToken);
                    updateCuenta(cuenta);
                    db.deleteUsers(cuenta.getIdCuenta());
                    db.insertStorageCliente(cuenta);
                    SingletonCliente.destroySingleton();
                    SingletonCliente.getInstance(cuenta);
                }
            }
        });
    }

    private void updateCuenta(MPersona cuenta){
        try {
            mCompositeDisposable.add(apiInterface.actualizarGenralesCuenta(cuenta, IDAPP)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(result->{}, throwable -> {}));
        }catch (Exception _exc){

        }
    }

    private void registro(String nombre, String correo) {
        try {
            int indexName = nombre.indexOf(" ");

            MPersona persona = new MPersona();
            persona.setNombre(nombre.substring(0,indexName));
            persona.setCorreo(correo);
            persona.setStatus(true);
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(getActivity(), new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String refreshToken = instanceIdResult.getToken();
                    //jsonObject.addProperty("Token", refreshToken);
                    persona.setToken(refreshToken);
                    mCompositeDisposable.add(apiInterface.registrarCuenta(persona, IDAPP)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(result -> {
                                mostrarProgresBar(false);
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
                                mostrarProgresBar(false);
                                Log.e("Error", Objects.requireNonNull(throwable.getMessage()));
                                Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getView()), "Error al registrar datos.", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }));
                }

            });


        } catch (Exception _exc) {
            Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getView()), "Por el momento no puedes registrarte, intentalo nuevamente más tarde", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }


}
