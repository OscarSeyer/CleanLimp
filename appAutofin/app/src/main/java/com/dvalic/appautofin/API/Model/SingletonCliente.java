package com.dvalic.appautofin.API.Model;

import android.util.Log;

public class SingletonCliente {
    private static final String TAG = SingletonCliente.class.getSimpleName();
    private static volatile SingletonCliente instance;
    private MPersona cliente;

    private SingletonCliente(MPersona _modelPersona){
        this.cliente = _modelPersona;
    }

    public synchronized static MPersona getInstance(MPersona _client) {
        if (instance != null) {
            destroySingleton();
        }else{
            instance = new SingletonCliente(_client);
        }
        return instance.cliente;
    }

    public synchronized static MPersona getInstance() {
        return instance.cliente;
    }
    public synchronized static SingletonCliente getVerifyInstance()
    {
        return instance;
    }

    public static void destroySingleton() {
        instance = null;
        Log.e(TAG, "destroy instance Singleton");
    }
}
