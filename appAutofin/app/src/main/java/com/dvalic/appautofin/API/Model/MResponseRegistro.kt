package com.dvalic.appautofin.API.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MResponseRegistro (
        @SerializedName("Ok") var Ok: String? = null
        ,@SerializedName("Mensaje") var Mensaje: String? = null
        ,@SerializedName("Objeto") var Objeto: MPersona? = null
): Serializable