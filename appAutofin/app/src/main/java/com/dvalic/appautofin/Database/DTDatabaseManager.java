package com.dvalic.appautofin.Database;

import com.dvalic.appautofin.Utilerias.UTHelpers;

public class DTDatabaseManager {
    private static DTDatabase database;

    public DTDatabaseManager() {

    }

    public static DTDatabase database(DTDatabaseType tipoDatabase, String ruta) {

        DTDatabase db = null;
        switch (tipoDatabase) {
            case DTDatabaseTypeDatosPersonales:
                db = DTDatabaseManager.sharedInstanceDatosPersonales(ruta);
                break;
        }
        return db;
    }

    private static DTDatabase sharedInstanceDatosPersonales(String ruta) {

        if (database != null) {
            //Log.e("usrmdk", "Solo devuelve la instancia ");
            return database;
        } else {
            //Log.i("usrmdk", "No existe instancia , crearla ");
        }
        String name = ruta+"ClienteFestin.sqlite";
        if (UTHelpers.existeArchivo(name)) {
            //Log.i("usrmdk", "Abrir la base de datos " + name);
            database = new DTDatabase(name);
        } else {
            //Log.i("usrmdk", "No existe la base de datos ");
            throw new IllegalArgumentException("No existe la base de datos ");
        }
        return database;
    }
}
