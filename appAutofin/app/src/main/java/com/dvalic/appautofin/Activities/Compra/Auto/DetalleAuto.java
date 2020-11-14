package com.dvalic.appautofin.Activities.Compra.Auto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dvalic.appautofin.API.Model.MMarcas;
import com.dvalic.appautofin.API.Model.MModelos;
import com.dvalic.appautofin.API.Model.MVersiones;
import com.dvalic.appautofin.API.Model.ModelTest;
import com.dvalic.appautofin.Activities.Cuenta.DialogRegistroCorreo;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTBaseFragment;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.DialogVersionesBinding;
import com.dvalic.appautofin.databinding.FragmentDetalleAutoBinding;
import com.dvalic.appautofin.databinding.FragmentMarcasBindingImpl;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class DetalleAuto extends UTBaseFragment {
    private String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private FragmentDetalleAutoBinding binding;
    private MModelos modelo;
    private Bundle bundle;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleAutoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null){
            this.modelo = (MModelos)getArguments().getSerializable("modelo");
            binding.txtMarca.setText(modelo.getNombreMarca());
            binding.txtModelo.setText(modelo.getNombreModelo());
            binding.txtPrecio.setText(UTHelpers.formatMoney(Double.valueOf(modelo.getPrecio())));
            Glide.with(getContext())
                    .asBitmap()
                    .load(modelo.getRutaFoto())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivAuto);
            binding.btnVersionesDialog.setText(modelo.getNombreVersion());


            binding.btnMeInteresa.setOnClickListener(v -> {
                interfaz.mostrarFragmentoModoContacto();
            });
            binding.clFicha.setOnClickListener((v)->{
                Bundle bundle = new Bundle();
                bundle.putSerializable("modelo",modelo);
                interfaz.mostrarFragmentoFichaTecnica(bundle);
            });
            binding.clCotizar.setOnClickListener((v)->{
                bundle = new Bundle();
                bundle.putSerializable("modelo", modelo);
                interfaz.mostrarFragmentoCotizar(bundle);
            });
            binding.btnVersionesDialog.setOnClickListener((v)->{
                DialogFragment dialogFragment = new DialogVersiones(interfaz, mCompositeDisposable, apiInterface, modelo, this);
                assert getFragmentManager() != null;
                dialogFragment.show(getFragmentManager(), dialogFragment.getClass().getSimpleName());
            });

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
       FragmentManager ok =  getActivity().getSupportFragmentManager();
       List<Fragment> lstFrag =  ok.getFragments();
        for (Fragment item: lstFrag) {
            if(item.getTag().equals("Cotizar") || item.getTag().equals("FichaTecnica")){
                item.getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left,
                        R.animator.slide_out_right, android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right).remove(item).commit();
                return false;
            }
        }

            MMarcas marca = new MMarcas();
            marca.setIdMarca(modelo.getIdMarca());
            marca.setDescripcionMarca(modelo.getNombreMarca());
            Bundle bundles = new Bundle();
            bundles.putSerializable("marca", marca);
            interfaz.mostrarFragmentoModelos(bundles);
            return true;
    }

    public void refrescarInformacion(MVersiones versionSelected){

       MModelos modelo = new MModelos();
        modelo.setIdMarca(versionSelected.getIdMarca());
        modelo.setIdModelo(versionSelected.getIdModelo());
        modelo.setIdVersion(versionSelected.getIdVersion());
        modelo.setNombreMarca(versionSelected.getNombreMarca());
        modelo.setNombreModelo(versionSelected.getNombreModelo());
        modelo.setNombreVersion(versionSelected.getNombreVersion());
        modelo.setAnio(versionSelected.getAnio());
        modelo.setPrecio(versionSelected.getPrecio());
        //modelo.setPrecioMax(); --> pending ToDo necesitamos este campo en versiones
        //modelo.setPrecioMin(); --> pending ToDo necesitamos este campo en versiones
        modelo.setClaveAuto(versionSelected.getClaveAuto());
        //modelo.setRutaFoto(); --> pending ToDo este propiedad cambiará ya que será un array

        binding.txtMarca.setText(modelo.getNombreMarca());
        binding.txtModelo.setText(modelo.getNombreModelo());
        binding.txtPrecio.setText(UTHelpers.formatMoney(Double.valueOf(modelo.getPrecio())));
        binding.btnVersionesDialog.setText(modelo.getNombreVersion());

    }
}
