package com.dvalic.appautofin.Activities.Cuenta;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.dvalic.appautofin.API.IActions;
import com.dvalic.appautofin.Database.DatabaseSqlite;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.databinding.DialogRegistroCorreoBinding;
import com.dvalic.appautofin.databinding.DialogRegistroTelefonoBinding;

import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;

public class DialogRegistroTelefono extends DialogFragment {
    private String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private CompositeDisposable compositeDisposable;
    private DialogRegistroTelefonoBinding binding;
    private DatabaseSqlite db;
    private IActions apiInterface;
    private boolean IsCorrect = false;
    private Handler handler;


    public DialogRegistroTelefono(IComunicador interfaz, CompositeDisposable compositeDisposable, IActions apiInterface) {
        this.interfaz = interfaz;
        this.compositeDisposable = compositeDisposable;
        this.apiInterface = apiInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogRegistroTelefonoBinding.inflate(inflater, container, false);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseSqlite(getActivity());
        binding.btnRegistroTelefono.setOnClickListener((v)->{});

    }


}
