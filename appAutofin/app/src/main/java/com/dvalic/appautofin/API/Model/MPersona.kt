package com.dvalic.appautofin.API.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MPersona (
        @SerializedName("IdCuenta") var IdCuenta: String? = null
        ,@SerializedName("Nombre") var Nombre: String? = null
        ,@SerializedName("ApellidoPaterno") var ApellidoPaterno: String? = null
        ,@SerializedName("ApellidoMaterno") var ApellidoMaterno: String? = null
        ,@SerializedName("Correo") var Correo: String? = null
        ,@SerializedName("Lada") var LadaMovil: String? = null
        ,@SerializedName("TelefonoMovil") var TelefonoMovil: String? = null
        ,@SerializedName("ClaveAcceso") var Clave: String? = null
        ,@SerializedName("Token") var Token: String? = null
        ,@SerializedName("DescripcionEstado") var DescripcionEstado: String? = null
        ,@SerializedName("IdEstado") var IdEstado: String? = null
        ,@SerializedName("Status") var Status: Boolean? = null



        ): Serializable