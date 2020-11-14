package com.dvalic.appautofin.API.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MVersiones(@SerializedName("Anio") var Anio: String? = null,
                 @SerializedName("IdMarca") var IdMarca: String? = null,
                 @SerializedName("NombreMarca") var NombreMarca: String? = null,
                 @SerializedName("IdModelo") var IdModelo: String? = null,
                 @SerializedName("NombreModelo") var NombreModelo: String? = null,
                 @SerializedName("IdVersion") var IdVersion: String? = null,
                 @SerializedName("NombreVersion") var NombreVersion: String? = null,
                 @SerializedName("Precio") var Precio: String? = null,
                 @SerializedName("ClaveAuto") var ClaveAuto: String? = null




): Serializable