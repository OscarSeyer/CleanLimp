package com.dvalic.appautofin.API.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MPromociones (
        @SerializedName("IdPromocion") var IdPromocion: String? = null
        ,@SerializedName("IdDetalle") var IdDetalle: String? = null
        ,@SerializedName("Titulo") var Titulo: String? = null
        ,@SerializedName("RutaPromocion") var RutaPromocion: String? = null
): Serializable