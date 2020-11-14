package com.dvalic.appautofin.Activities.Compra.Casa;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.dvalic.appautofin.API.IActions;
import com.dvalic.appautofin.API.Model.MMontos;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.DialogMesAdjudicarBinding;
import com.dvalic.appautofin.databinding.DialogRecuperarContraseniaBinding;

import org.w3c.dom.Text;

import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;

public class DialogMesAdjudicar extends DialogFragment {

    private IComunicador interfaz;
    private CompositeDisposable compositeDisposable;
    private DialogMesAdjudicarBinding binding;
    IActions apiInterface;
    private  MMontos monto;

    public  DialogMesAdjudicar(IComunicador interfaz, CompositeDisposable compositeDisposable, IActions apiInterface, MMontos monto){
        this.interfaz = interfaz;
        this.compositeDisposable = compositeDisposable;
        this.apiInterface = apiInterface;
        this.monto = monto;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogMesAdjudicarBinding.inflate(inflater, container, false);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnOk.setOnClickListener((v)->{this.dismiss();});
        textWatcher();
    }

    @Override
    public void onResume() {
        super.onResume();
        int widthPixels = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int heightPixels = (int) (getResources().getDisplayMetrics().heightPixels * 0.5);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(widthPixels, heightPixels);
    }

    public void textWatcher(){
        binding.tvMes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() > 0){
                    int val = Integer.parseInt(charSequence.toString());
                    if(val > 60){
                        UTHelpers.showSnackBar(getView(), "Rango permitido: 1-60 meses", false);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


}
