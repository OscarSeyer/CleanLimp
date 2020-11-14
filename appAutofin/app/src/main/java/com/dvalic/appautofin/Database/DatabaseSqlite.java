package com.dvalic.appautofin.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dvalic.appautofin.API.Model.MPersona;
import com.dvalic.appautofin.API.Model.SingletonCliente;
import com.dvalic.appautofin.Utilerias.UTHelpers;

import java.text.ParseException;
import java.util.Locale;

public class DatabaseSqlite extends SQLiteOpenHelper {
    private String TAG = this.getClass().getSimpleName();
    private static final String COMMENTS_TABLE_DIRECCION = "CREATE TABLE direccion(IdUbicacion TEXT, IdTipo TEXT, Nombre TEXT, DetalleEntrega TEXT, Direccion TEXT, Latitud TEXT, Longitud TEXT, Calle TEXT, NumeroExterior TEXT, NumeroInterior TEXT, Colonia TEXT, DelegacionMunicipio TEXT, Estado TEXT, Pais TEXT, CodigoPostal TEXT)";
    private static final String COMMENTS_TABLE_PERSON = "CREATE TABLE persona(ApellidoMaterno TEXT NOT NULL, ApellidoPaterno TEXT NOT NULL, Clave TEXT, Correo TEXT NOT NULL, IdCuenta TEXT PRIMARY KEY, Lada TEXT NOT NULL, Nombre TEXT NOT NULL, TelefonoMovil TEXT NOT NULL, Token TEXT)";
    private static final String COMMENTS_TABLE_PERSON_FISCAL = "CREATE TABLE personaFiscal(ApellidoMaterno TEXT, ApellidoPaterno TEXT, IdPersona TEXT, Nombre TEXT, Rfc TEXT, NombreORazonSocial TEXT, Calle TEXT, NumInterior TEXT, numExterior TEXT, Colonia TEXT, CP TEXT, Pais TEXT, Delegacion TEXT, Estado TEXT, IdCuenta TEXT PRIMARY KEY)";
    private static final String DB_NAME = "Autofin.sqlite";
    private static final int DB_VERSION = 1;


    public DatabaseSqlite(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COMMENTS_TABLE_DIRECCION);
        db.execSQL(COMMENTS_TABLE_PERSON);
        db.execSQL(COMMENTS_TABLE_PERSON_FISCAL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "persona");
        db.execSQL("DROP TABLE IF EXISTS " + "direccion");
        db.execSQL("DROP TABLE IF EXISTS " + "personaFisica");

        // Create tables again
        onCreate(db);
    }


    public MPersona getCliente() {
        try {
            MPersona user = null;
            SQLiteDatabase db = this.getReadableDatabase();
            if(db != null){
                Log.i("dbs", db.getPath().intern());
            }
            Cursor cursor = db.query("persona",
                    new String[]{"ApellidoMaterno"
                            ,"ApellidoPaterno"
                            ,"Clave"
                            ,"Correo"
                            ,"IdCuenta"
                            ,"Lada"
                            ,"Nombre"
                            ,"TelefonoMovil"
                            ,"Token"},
                    null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                user = new MPersona();
                cursor.moveToFirst();
                user.setApellidoMaterno(cursor.getString(cursor.getColumnIndex("ApellidoMaterno")));
                user.setApellidoPaterno(cursor.getString(cursor.getColumnIndex("ApellidoPaterno")));
                user.setClave(cursor.getString(cursor.getColumnIndex("Clave")));
                user.setCorreo(cursor.getString(cursor.getColumnIndex("Correo")));
                user.setIdCuenta(cursor.getString(cursor.getColumnIndex("IdCuenta")));
                user.setLadaMovil(cursor.getString(cursor.getColumnIndex("Lada")));
                user.setNombre(cursor.getString(cursor.getColumnIndex("Nombre")));
                user.setTelefonoMovil(cursor.getString(cursor.getColumnIndex("TelefonoMovil")));
                user.setToken(cursor.getString(cursor.getColumnIndex("Token")));

            }
            cursor.close();
                db.close();
            return user;

        } catch (Exception _exc) {
            Log.i("EXCEPTION MARIN", _exc.getMessage());
            throw _exc;
        }
    }

    public void deleteUsers(String idCuenta) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("persona", "IdCuenta" + " = ?",
                new String[]{String.valueOf(idCuenta)});
        db.close();

        int countExist = getClientCount();
        Log.i(TAG, String.valueOf(countExist));
    }

    public void deleteAllUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("persona", null,
                null);
        db.close();

        int countExist = getClientCount();
        Log.i(TAG, String.valueOf(countExist));
    }


    public int getClientCount() {
        String countQuery = "SELECT  * FROM " + "persona";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    public void insertStorageCliente(MPersona client) {
        try {

           if(getClientCount() > 0){
               deleteAllUsers();
           }
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(String.format(Locale.US, "INSERT INTO persona(ApellidoMaterno, ApellidoPaterno, Clave, Correo, IdCuenta, Lada, Nombre, TelefonoMovil, Token) VALUES ('%s', '%s','%s', '%s','%s', '%s','%s', '%s','%s')"
                    , client.getApellidoMaterno()
                    , client.getApellidoPaterno()
                    , client.getClave()
                    , client.getCorreo()
                    , client.getIdCuenta()
                    , client.getLadaMovil()
                    , client.getNombre()
                    , client.getTelefonoMovil()
                    , client.getToken()));

            int countExist = getClientCount();
            Log.i(TAG, String.valueOf(countExist));
            // close db connection
            db.close();


        } catch (Exception _exc) {
            throw _exc;
        }
    }

    public void insertStorageClientes(MPersona persona) {
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            if(getClientCount() > 0){
                deleteAllUsers();
            }

            ContentValues values = new ContentValues();
            values.put("ApellidoMaterno", persona.getApellidoMaterno());
            values.put("ApellidoPaterno", persona.getApellidoPaterno());
            values.put("Clave", persona.getClave());
            values.put("Correo", persona.getCorreo());
            values.put("IdCuenta", persona.getIdCuenta());
            values.put("Lada", persona.getLadaMovil());
            values.put("Nombre", persona.getNombre());
            values.put("TelefonoMovil", persona.getTelefonoMovil());
            values.put("Token", persona.getToken());
            // insert row
            long id = db.insert(COMMENTS_TABLE_PERSON, null, values);

            int one = getClientCount();

            db.close();


        } catch (Exception _exc) {
            throw _exc;
        }
    }



    public void updateStorageCliente(String idCuenta, String token, Context context) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(String.format(Locale.US, "UPDATE persona SET Token =" + token + "WHERE IdCuenta = " + idCuenta));
            MPersona persona = getCliente();
            db.close();
        } catch (Exception _exc) {
            throw _exc;
        }
    }

}