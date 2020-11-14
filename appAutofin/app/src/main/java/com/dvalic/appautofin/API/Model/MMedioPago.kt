package com.dvalic.appautofin.API.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MMedioPago (
        @SerializedName("IdCuenta") var IdCuenta: Int? = null
        ,@SerializedName("NumeroTarjeta") var NumeroTarjeta: Long? = null
        ,@SerializedName("MesVencimiento") var MesVencimiento: Int? = null
        ,@SerializedName("AnioVencimiento") var AnioVencimiento: Int? = null
        ,@SerializedName("NombreTarjeta") var NombreTarjeta: String? = null
        ,@SerializedName("IdMedioPago") var IdMedioPago: String? = null

) : Serializable