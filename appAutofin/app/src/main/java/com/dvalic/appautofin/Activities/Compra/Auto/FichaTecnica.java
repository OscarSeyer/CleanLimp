package com.dvalic.appautofin.Activities.Compra.Auto;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dvalic.appautofin.API.Model.MModelos;
import com.dvalic.appautofin.API.Model.ModelTest;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTBaseFragment;
import com.dvalic.appautofin.databinding.FragmentFichaTecnicaBinding;

import eightbitlab.com.blurview.RenderScriptBlur;


public class FichaTecnica extends UTBaseFragment {
    private String TAG = this.getClass().getSimpleName();
    private IComunicador interfaz;
    private FragmentFichaTecnicaBinding binding;
    private MModelos modelo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFichaTecnicaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        blurry();
        if(getArguments() != null){
            modelo = (MModelos)getArguments().getSerializable("modelo");
        }

        binding.tvDown.setOnClickListener((v)->{
            backFragment();
        });
        binding.lyFichaTecnica.setOnClickListener((v)->{});
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
        return false;

    }



    public  void backFragment(){
        getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left,
                R.animator.slide_out_right, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right).remove(this).commit();
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
}
