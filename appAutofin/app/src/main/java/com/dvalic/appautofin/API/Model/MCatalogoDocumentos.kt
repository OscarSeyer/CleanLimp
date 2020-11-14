package com.dvalic.appautofin.API.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MCatalogoDocumentos (
        @SerializedName("IdDocumento") var IdDocumento: String? = null,
        @SerializedName("DescripcionDocumento") var DescripcionDocumento: String? = null,
        @SerializedName("Status") var Status: Boolean? = null
): Serializable