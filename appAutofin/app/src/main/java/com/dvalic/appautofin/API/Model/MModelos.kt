package com.dvalic.appautofin.API.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MModelos(
                @SerializedName("Anio") var Anio: String? = null
               ,@SerializedName("IdMarca") var IdMarca: String? = null
               ,@SerializedName("IdModelo") var IdModelo: String? = null
               ,@SerializedName("IdVersion") var IdVersion: String? = null
               ,@SerializedName("NombreMarca") var NombreMarca: String? = null
               ,@SerializedName("NombreModelo") var NombreModelo: String? = null
               ,@SerializedName("NombreVersion") var NombreVersion: String? = null
               ,@SerializedName("Precio") var Precio: String? = null
               ,@SerializedName("PrecioMax") var PrecioMax: String? = null
               ,@SerializedName("PrecioMin") var PrecioMin: String? = null
               ,@SerializedName("RutaFoto") var RutaFoto: String? = null
               ,@SerializedName("ClaveAuto") var ClaveAuto: String? = null

): Serializable