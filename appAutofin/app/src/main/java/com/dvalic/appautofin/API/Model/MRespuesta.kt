package com.dvalic.appautofin.API.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MRespuesta (
        @SerializedName("Ok") var Ok: String? = null,
        @SerializedName("Mensaje") var Mensaje: String? = null,
        @SerializedName("Objeto") var Objeto: String? = null

): Serializable