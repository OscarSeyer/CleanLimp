package com.dvalic.appautofin.Activities.Cuenta;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.dvalic.appautofin.API.Model.MPersona;
import com.dvalic.appautofin.API.Model.SingletonCliente;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTBaseFragment;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.FragmentDatosPersonalesBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class DatosPersonales extends UTBaseFragment {
    private String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private FragmentDatosPersonalesBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDatosPersonalesBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MPersona person = UTHelpers.setEmptyCuenta(SingletonCliente.getInstance());

        binding.txtNombre.setText(!SingletonCliente.getInstance().getNombre().equals("null") ? SingletonCliente.getInstance().getNombre() : "");
        binding.txtApellidoPaterno.setText(!SingletonCliente.getInstance().getApellidoPaterno().equals("null") ? SingletonCliente.getInstance().getApellidoPaterno() : "");
        binding.txtApellidoMaterno.setText(!SingletonCliente.getInstance().getApellidoMaterno().equals("null") ? SingletonCliente.getInstance().getApellidoMaterno() : "");
        binding.txtCorreo.setText(!SingletonCliente.getInstance().getCorreo().equals("null") ? SingletonCliente.getInstance().getCorreo() : "");
        binding.txtTelefono.setText(!SingletonCliente.getInstance().getTelefonoMovil().equals("null") ? SingletonCliente.getInstance().getTelefonoMovil(): "");
        binding.btnOk.setOnClickListener((v)->{
            if(validaCampos()){
                person.setNombre(binding.txtNombre.getText().toString());
                person.setApellidoPaterno(binding.txtApellidoPaterno.getText().toString());
                person.setApellidoMaterno(binding.txtApellidoMaterno.getText().toString());
                person.setCorreo(binding.txtCorreo.getText().toString());
                person.setTelefonoMovil(binding.txtTelefono.getText().toString());
                person.setStatus(true);
                //MPersona cuenta = UTHelpers.ValidateNullCuenta(person);
                actualizarDatosGeneralesCuenta(person);
            }



        });

    }

    private void actualizarDatosGeneralesCuenta(MPersona cuenta) {
        try {
            mCompositeDisposable.add(apiInterface.actualizarGenralesCuenta(cuenta, IDAPP)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        }catch (Exception _exc){
            throw  _exc;
        }
    }


    private void handleResponse(ResponseBody responseBody){
        try {
            mostrarProgresBar(false);
            JSONObject jsonObject;
            jsonObject = new JSONObject(responseBody.string());
            if(jsonObject.get("Ok").equals("SI")){
                UTHelpers.showSnackBar(getView(), jsonObject.get("Mensaje").toString(), false);
            }else{
                UTHelpers.showSnackBar(getView(), "No se actualizaron los datos: " + jsonObject.get("Mensaje").toString(), false);
            }
        }catch (Exception _exc){
                UTHelpers.showSnackBar(getView(), "Error añ actualizar los datos: " + _exc.getMessage(), false);
        }
    }

    private void handleError(Throwable error)
    {
        mostrarProgresBar(false);
        Toast.makeText(getContext(), "Error "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IComunicador){
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


    public boolean validaCampos(){

        if (Objects.requireNonNull(binding.txtNombre.getText()).toString().equals("")) {
            Snackbar snackbar = Snackbar.make(Objects.requireNonNull(this.getView()), "Ingresa tu apellido paterno", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        if (Objects.requireNonNull(binding.txtApellidoPaterno.getText()).toString().equals("")) {
            Snackbar snackbar = Snackbar.make(Objects.requireNonNull(this.getView()), "Ingresa apellido paterno", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;

        }
        if (Objects.requireNonNull(binding.txtCorreo.getText()).toString().equals("") && Objects.requireNonNull(binding.txtTelefono.getText()).toString().equals("")){
            Snackbar snackbar = Snackbar.make(Objects.requireNonNull(this.getView()), "Ingresa correo o teléfoono de contacto", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        if(!Objects.requireNonNull(binding.txtCorreo.getText()).toString().trim().equals("")){
            String emailAcceso = Objects.requireNonNull(binding.txtCorreo.getText()).toString().trim();
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (!emailAcceso.matches(emailPattern)) {
                Snackbar snackbar = Snackbar.make(Objects.requireNonNull(this.getView()), "Ingresa un correo válido!", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }
        }

        if(!Objects.requireNonNull(binding.txtTelefono.getText()).toString().trim().equals("")){
            if (Objects.requireNonNull(binding.txtTelefono.getText()).toString().length() < 10) {
                Snackbar snackbar = Snackbar.make(Objects.requireNonNull(this.getView()), "Ingresa un número de teléfono válido!", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }
        }

        return true;
    }

    public void mostrarProgresBar(boolean valor) {
        if (valor) {
            binding.pbProgreso.setVisibility(View.VISIBLE);
            binding.btnOk.setVisibility(View.GONE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            binding.pbProgreso.setVisibility(View.GONE);
            binding.btnOk.setVisibility(View.VISIBLE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
