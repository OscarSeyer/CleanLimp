package com.dvalic.appautofin.API.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class ModelTest implements Serializable {

    private String Modelo;
    private String Version;
    private String Precio;
    private String Estado;


    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String modelo) {
        Modelo = modelo;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public ArrayList<ModelTest> obtenerModelos(){
        ModelTest m1 = new ModelTest();
        m1.setModelo("SPARK");
        m1.setVersion("VERSION 1");
        m1.setEstado("ACTIVO");;
        m1.setPrecio("$250,000");

        ModelTest m2 = new ModelTest();
        m2.setModelo("AVEO");
        m2.setVersion("VERSION 2");
        m2.setPrecio("$250,000");
        m2.setEstado("ACTIVO");

        ModelTest m3 = new ModelTest();
        m3.setModelo("EQUINOX");
        m3.setVersion("VERSION 2");
        m3.setPrecio("$250,000");
        m3.setEstado("ADEUDO");;

        ModelTest m4 = new ModelTest();
        m4.setModelo("SUBURBAN");
        m4.setVersion("VERSION 3");
        m4.setPrecio("$250,000");
        m4.setEstado("ACTIVO");;

        ModelTest m5= new ModelTest();
        m5.setModelo("COLORADO");
        m5.setVersion("VERSION 4");
        m5.setPrecio("$250,000");
        m5.setEstado("ACTIVO");;
        ArrayList<ModelTest> lstModelos = new ArrayList<>();
        lstModelos.add(m1);
        lstModelos.add(m2);
        lstModelos.add(m3);
        lstModelos.add(m4);

        return lstModelos;



    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }
}
