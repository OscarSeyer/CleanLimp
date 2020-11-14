package com.dvalic.appautofin.API.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MCotizacion (
        @SerializedName("IdMonto") var IdMonto: String? = null
        ,@SerializedName("Monto") var Monto: String? = null
        ,@SerializedName("Cuota") var Cuota: String? = null
        ,@SerializedName("Inscripcion") var Inscripcion: String? = null
        ,@SerializedName("CuotaInscripcion") var CuotaInscripcion: String? = null
        ,@SerializedName("MensualidadAdjudicatario") var MensualidadAdjudicatario: String? = null
        ,@SerializedName("MensualidadAdjudicado") var MensualidadAdjudicado: String? = null
        ,@SerializedName("Seguro") var Seguro: String? = null
        ,@Transient var expanded: Boolean? = false //Ignore properties to deserealize gson by retrofit call
        ) : Serializable