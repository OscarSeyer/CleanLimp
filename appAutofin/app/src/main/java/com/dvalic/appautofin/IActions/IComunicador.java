package com.dvalic.appautofin.IActions;

import android.os.Bundle;

import com.dvalic.appautofin.API.Model.MPersona;

public interface IComunicador {
    void mostrarFragmentoPrincipal();
    void mostrarFragmentoMarcas();
    void mostrarFragmentoModelos(Bundle bundle);
    void mostrarFragmentoDetalleAuto(Bundle bundle);
    void mostrarFragmentoModoContacto();
    void mostrarFragmentoFichaTecnica(Bundle bundle);
    void mostrarFragmentoCotizar(Bundle bundle);
    void mostrarFragmentoContacto();
    void mostrarFragmentoFinanciamiento();
    void mostrarFragmentoPagar();
    void mostrarFragmentoContratos();
    void mostrarFragmentoDetalleContrato();
    void mostrarFragmentoLoginWithOptions();
    void mostrarFragmentoDatosPersonales();
    void mostrarFragmentoMetodosPago();
    void mostrarFragmentoMediosPago();

    //CASA
    void mostrarFragmentoCotizaMonto();
    void mostrarFragmentoPresentacionCasa();

    //ACTIONS CONTROLS
    void hideToolBar();
    void showToolBar();
    void showSnack(String Message);

    //TÉRMINOS Y CONDICIONES AUTOFIN
    void mostrarFragmentoTerminosCondiciones();

    //STORAGE
    void deleteUserStorage(String idCuenta);
    void insertStorageClient(MPersona cliente);





}
