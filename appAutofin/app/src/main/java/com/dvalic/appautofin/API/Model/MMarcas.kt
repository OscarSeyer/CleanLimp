package com.dvalic.appautofin.API.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MMarcas(@SerializedName("IdMarca") var IdMarca: String? = null,
              @SerializedName("Nombre") var DescripcionMarca: String? = null
): Serializable