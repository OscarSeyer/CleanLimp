package com.dvalic.appautofin.Activities.Compra.Auto;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.dvalic.appautofin.API.IActions;

import com.dvalic.appautofin.API.Model.MMontos;
import com.dvalic.appautofin.API.Model.MVersiones;
import com.dvalic.appautofin.Activities.Compra.Casa.CotizarMonto;
import com.dvalic.appautofin.Activities.Compra.Casa.DialogMesAdjudicar;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTHelpers;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class MontoSpinner extends ArrayAdapter<MMontos>{

    private Context context;
    private ArrayList<MMontos> lstResult;
    private static ArrayList<MMontos> lstCotiza;
    private IComunicador interfaz;
    private CompositeDisposable compositeDisposable;
    private IActions apiInterface;
    private  FragmentManager fragmentManager;

    public MontoSpinner(@NonNull Context context, int resource, ArrayList<MMontos> lstResult) {
        //super(context, resource,lstResult);
        super(context, resource, lstResult);
        this.context = context;
        this.lstResult = lstResult;
        this.lstCotiza = lstResult;


    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public MMontos getItem(int position) {
        return super.getItem(position);
    }

    public static MMontos getVersion(int position){
        return lstCotiza.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setAllCaps(false);
        label.setText( lstResult.get(position).getMonto());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(lstResult.get(position).getMonto());
        return label;
    }


}


