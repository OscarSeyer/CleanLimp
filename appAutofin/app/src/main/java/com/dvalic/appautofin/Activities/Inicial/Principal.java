package com.dvalic.appautofin.Activities.Inicial;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.dvalic.appautofin.API.Model.MPromociones;
import com.dvalic.appautofin.Activities.Compra.Auto.PromocionesAdapter;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.Utilerias.UTBaseFragment;
import com.dvalic.appautofin.databinding.FragmentPrincipalBinding;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class Principal extends UTBaseFragment {

    private String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private FragmentPrincipalBinding binding;
    private boolean isActiveCarousel = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPrincipalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.ivBtnAuto.setOnClickListener((v) -> {
            interfaz.mostrarFragmentoMarcas();
        });
        binding.ivBtnCasa.setOnClickListener((v) -> {
            interfaz.mostrarFragmentoPresentacionCasa();
        });
        binding.ivBtnSolicitud.setOnClickListener((v) -> {
        });
        SolicitudPromociones();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IComunicador) {
            interfaz = (IComunicador) context;
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


    @SuppressLint("CheckResult")
    public void SolicitudPromociones() {
        mostrarProgresBar(true);

  /*      mCompositeDisposable.add(apiInterface.obtenerPromociones()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                //.subscribe(result->{}, throwable -> {}));
                .subscribe(this::handleResponse, this::handleError));
*/

        Observable<ArrayList<MPromociones>> request =   apiInterface.obtenerPromociones();
        request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::handleResponse, this::handleError);


    }


    private void handleResponse(ArrayList<MPromociones> lstPromociones) {
        binding.imageSlider.setSliderAdapter(new PromocionesAdapter(getContext(), lstPromociones));
        binding.imageSlider.startAutoCycle();
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.DROP);
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.CUBEINDEPTHTRANSFORMATION);
        mostrarProgresBar(false);
        isActiveCarousel = true;
    }

    private void handleError(Throwable error) {
        mostrarProgresBar(false);
        Toast.makeText(getContext(), "Error " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public void mostrarProgresBar(boolean valor) {
        if (valor) {
            binding.pbProgreso.setVisibility(View.VISIBLE);
            //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        } else {
            binding.pbProgreso.setVisibility(View.GONE);
            //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


}
