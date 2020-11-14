package com.dvalic.appautofin.Utilerias;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.dvalic.appautofin.API.Model.MPersona;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UTHelpers {

    public static String formatMoney(double imports) {
        DecimalFormat formato = new DecimalFormat("$#,###.00");
        String valorFormateado = formato.format(imports);
        return valorFormateado;
    }

    public static String formatDate_dd_mm_yyyy(String _date) throws ParseException {

        //Formato inicial.
        SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
        //String fechaInicio  = "2019/05/31";
        Date d = formato.parse(_date.replace("-", "/"));

        //Aplica formato requerido.
        formato.applyPattern("dd/MM/yyyy");
        String nuevoFormato = formato.format(d);
        return nuevoFormato;
    }

    public static String getDate_yyyy_mm_dd_String()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        return fecha;
    }

    public static String getHourNow()
    {
        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String hour =  hourFormat.format(date);
        return hour;

    }

    public static boolean validateEmailAddress(String emailAddress) {
        String expression = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }


    public static String twoDigits(int n) {
        return (n <= 9) ? ("0" + n) : String.valueOf(n);
    }

    public static boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() == 10;
        }
        return false;
    }

    public static final Drawable getDrawable(Context context, int id) {
        return context.getDrawable(id);
    }


    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public static boolean existeArchivo(String path) {
        boolean existe = false;
        File file = new File(path);
        if (file.exists()) {
            existe = true;
        }
        return existe;
    }

    public static boolean TryParseLong(String someText) {
        try {
            Long.parseLong(someText);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static void showSnackBar(View view, String message, boolean showTop){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        if(showTop)
        {
            View viewPosition = snackbar.getView();
            FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)viewPosition.getLayoutParams();
            params.gravity = Gravity.TOP;
            viewPosition.setLayoutParams(params);
        }
        snackbar.show();
    }

    public static MPersona ValidateNullCuenta(MPersona persona){

        if(Objects.requireNonNull(persona.getNombre()).toString().equals("null")){
            persona.setNombre(null);
        }
        if(Objects.requireNonNull(persona.getApellidoPaterno()).toString().equals("null")){
            persona.setApellidoPaterno(null);
        }
        if(Objects.requireNonNull(persona.getApellidoMaterno()).toString().equals("null")){
            persona.setApellidoMaterno(null);
        }

        if(Objects.requireNonNull(persona.getCorreo()).toString().equals("null")){
            persona.setCorreo(null);
        }
        if(Objects.requireNonNull(persona.getLadaMovil()).toString().equals("null")){
            persona.setLadaMovil(null);
        }
        if(Objects.requireNonNull(persona.getTelefonoMovil()).toString().equals("null") || Objects.requireNonNull(persona.getTelefonoMovil()).toString().equals("")){
            persona.setTelefonoMovil(null);
        }
        if(Objects.requireNonNull(persona.getClave()).toString().equals("null")){
            persona.setClave(null);
        }
        if(Objects.requireNonNull(persona.getToken()).toString().equals("null")){
            persona.setToken(null);
        }
        persona.setDescripcionEstado(null);
        persona.setIdEstado(null);
        persona.setStatus(true);
        persona.setIdCuenta(null);
        return  persona;


    }

    public static MPersona setEmptyCuenta(MPersona persona){
        if(persona.getNombre() == null){
            persona.setNombre("");
        }
        if(persona.getApellidoPaterno() == null){
            persona.setApellidoPaterno("");
        }
        if(persona.getApellidoMaterno() == null){
            persona.setApellidoMaterno("");
        }
        if(persona.getCorreo() == null){
            persona.setCorreo("");
        }
        if(persona.getLadaMovil() == null){
            persona.setLadaMovil("");
        }
        if(persona.getTelefonoMovil() == null){
            persona.setTelefonoMovil("");
        }
        if(persona.getToken() == null){
            persona.setToken("");
        }
        if(persona.getClave() == null){
            persona.setClave("");
        }
        if(persona.getIdCuenta() == null){
            persona.setIdCuenta("");
        }
        if(persona.getDescripcionEstado() == null){
            persona.setDescripcionEstado("");
        }
        return  persona;
    }
}
