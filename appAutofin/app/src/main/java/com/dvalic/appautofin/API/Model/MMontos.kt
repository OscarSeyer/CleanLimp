package com.dvalic.appautofin.API.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MMontos(
        @SerializedName("IdMonto") var IdMonto: String? = null
        ,@SerializedName("Monto") var Monto: String? = null
        ,@SerializedName("Val") var Val: String? = null
): Serializable