package com.dvalic.appautofin.API.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MCotizacionAuto (
        @SerializedName("Plan") var Plan: String? = null,
        @SerializedName("Enganche") var Monto: Double? = null,
        @SerializedName("Inscripcion") var Inscripcion: Double? = null,
        @SerializedName("PrimerMensualidad") var PrimerMensualidad: Double? = null,
        @SerializedName("CantidadMensualidadesPi") var CantidadMensualidadesPi: Int? = null,
        @SerializedName("MensualidadesPi") var MensualidadesPi: Double? = null,
        @SerializedName("CantidadMesualidadesRestantes") var CantidadMesualidadesRestantes: Int? = null,
        @SerializedName("MensualidadFija") var MensualidadFija: Double? = null,
        @SerializedName("Periodo") var Periodo: Double? = null,
        @SerializedName("AnualidadOpcionUno") var AnualidadOpcionUno: Double? = null,
        @SerializedName("MensualidadOpcionUno") var MensualidadOpcionUno: Double? = null,
        @SerializedName("AnualidadOpcionDos") var AnualidadOpcionDos: Double? = null,
        @SerializedName("MensualidadOpcionDos") var MensualidadOpcionDos: Double? = null,
        @SerializedName("AnualidadOpcionTres") var AnualidadOpcionTres: Double? = null,
        @SerializedName("MensualidadOpcionTres") var MensualidadOpcionTres: Double? = null

): Serializable

