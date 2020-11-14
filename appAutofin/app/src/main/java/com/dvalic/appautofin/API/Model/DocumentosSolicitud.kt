package com.dvalic.appautofin.API.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DocumentosSolicitud (
        @SerializedName("IdTipoDocumento") var IdTipoDocumento: String? = null
        ,@SerializedName("urlPath") var urlPath: String? = null

): Serializable