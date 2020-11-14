package com.dvalic.appautofin.API;

import com.dvalic.appautofin.API.Model.MCatalogoDocumentos;
import com.dvalic.appautofin.API.Model.MMarcas;
import com.dvalic.appautofin.API.Model.MModelos;
import com.dvalic.appautofin.API.Model.MPersona;
import com.dvalic.appautofin.API.Model.MPromociones;
import com.dvalic.appautofin.API.Model.MRespuesta;
import com.dvalic.appautofin.API.Model.MVersiones;
import com.dvalic.appautofin.API.Model.MMedioPago;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IActions {
     public final  String APPID = "ANDRO";

    @GET("api/auto/GetMarcas")
    Observable<ArrayList<MMarcas>> obtenerMarcas();

    @GET("api/auto/GetModelos")
    Observable<ArrayList<MModelos>> obtenerModelosByIdMarca(@Query("IdMarca") String idMarca);

    @GET("api/auto/GetVersiones")
    Observable<ArrayList<MVersiones>> obtenerVersionesByIdMarcaIdModelo(@Query("IdMarca") String idMarca, @Query("IdModelo") String idModelo);

    @GET("api/Autofin/GetPromociones")
    Observable<ArrayList<MPromociones>> obtenerPromociones();

    @Headers({"Content-type:application/json"})
    @POST("api/Cuenta/PostRegistraCuenta")
    Observable<ResponseBody> registrarCuenta(@Body MPersona object, @Query("CvePlataforma") String cvePlataforma);

    @Headers({"Content-type:application/json"})
    @GET("api/Cuenta/GetGenerarClavePorCorreo")
    Observable<ResponseBody> obtenerClavePorCorreo(@Query("Correo") String correo);

    @Headers({"Content-type:application/json"})
    @GET("api/Cuenta/GetGenerarClavePorTelefono")
    Observable<ResponseBody> obtenerClavePorTelefono(@Query("Lada") String lada,@Query("Movil") String movil, @Query("cvePlataforma") String cvePlataforma);

    @GET("api/Cuenta/GetCuentaPorCorreoClave")
    Observable<ResponseBody> obtenerCuentaPorCorreoClave(@Query("Correo") String correo, @Query("Clave") String clave);

    @GET("api/Cuenta/GetCuentaPorTelefonoClave")
    Observable<ResponseBody> obtenerCuentaPorTelefonoClave(@Query("Movil") String movil ,@Query("Clave") String clave);

    @POST("api/Cuenta/PostRegistraMedioPago")
    Observable<ResponseBody> registrarMedioPago(@Body MMedioPago jsonObject, @Query("CvePlataforma") String CvePlataforma);

    @GET("api/Cuenta/GetHistoricosMedioPago")
    Observable<ResponseBody> obtenerHistoricoMedioPago(@Query("IdCuenta") int idCuenta);

    @DELETE("api/Cuenta/DeleteMedioPagoByIdCuentaNumeroTarjeta")
    Observable<ResponseBody> eliminarMedioPago(@Query("idCuenta") int idCuenta, @Query("NumeroTarjet") Long numeroTarjet, @Query("CvePlataforma") String CvePlataforma);

    @PUT("api/Cuenta/PutUpdateCuenta")
    Observable<ResponseBody> actualizarGenralesCuenta(@Body MPersona cuent, @Query("CvePlataforma") String CvePlataforma);

    @GET("api/Auto/GetPlanes")
    Observable<ResponseBody> obtenerPlanes(@Query("cveVehiculo") String CveVehiculo, @Query("PagoAdicional") String PagoAdicional);

    @GET("api/Cuenta/GetCuentaPorFacebook")
    Observable<ResponseBody> obtenerCuentaPorFcebook(@Query("Correo") String Correo);

    @GET("api/Cuenta/GetCatalogoDocumentos")
    Observable<MRespuesta> obtenerCatalogoDocumentos();

    @GET("api/Casa/GetMontos")
    Observable<MRespuesta> obtenerCatalogoMontos();

    @GET("api/Casa/GetCotizacion")
    Observable<MRespuesta> obtenerCotizacionCasa(@Query("IdMonto") String IdMonto, @Query("Monto") String Monto, @Query("Mensualidad") String Mensualidad);











}
