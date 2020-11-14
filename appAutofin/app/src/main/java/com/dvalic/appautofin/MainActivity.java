package com.dvalic.appautofin;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dvalic.appautofin.API.Model.MPersona;
import com.dvalic.appautofin.API.Model.SingletonCliente;
import com.dvalic.appautofin.Activities.Compra.Auto.Contactarme;
import com.dvalic.appautofin.Activities.Compra.Auto.Cotizar;
import com.dvalic.appautofin.Activities.Compra.Auto.DetalleAuto;
import com.dvalic.appautofin.Activities.Compra.Auto.FichaTecnica;
import com.dvalic.appautofin.Activities.Compra.Auto.Marcas;
import com.dvalic.appautofin.Activities.Compra.Auto.Modelos;
import com.dvalic.appautofin.Activities.Compra.Auto.ModoContacto;
import com.dvalic.appautofin.Activities.Compra.Auto.PagarAnticipo;
import com.dvalic.appautofin.Activities.Compra.Auto.SolicitudFinanciamiento;
import com.dvalic.appautofin.Activities.Compra.Casa.CotizarMonto;
import com.dvalic.appautofin.Activities.Compra.Casa.Presentacion;
import com.dvalic.appautofin.Activities.Contratos.Contratos;
import com.dvalic.appautofin.Activities.Contratos.DetalleContrato;
import com.dvalic.appautofin.Activities.Cuenta.DatosPersonales;
import com.dvalic.appautofin.Activities.Cuenta.LoginWithOptions;
import com.dvalic.appautofin.Activities.Inicial.Principal;
import com.dvalic.appautofin.Activities.MenuOpciones.MediosPago;
import com.dvalic.appautofin.Activities.MenuOpciones.MetodosPago;
import com.dvalic.appautofin.Activities.MenuOpciones.TerminosCondiciones;
import com.dvalic.appautofin.Database.DatabaseSqlite;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.Utilerias.UTBaseActivity;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.List;


public class MainActivity extends UTBaseActivity implements IComunicador, NavigationView.OnNavigationItemSelectedListener {
    private String TAG = this.getClass().getSimpleName();
    private ActivityMainBinding binding;
    DatabaseSqlite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        db = new DatabaseSqlite(this);

        setSupportActionBar(binding.toolbarMenu);
        binding.navView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbarMenu, R.string.open, R.string.close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.setCheckedItem(R.id.nav_view);

        IsLogged();
        mostrarFragmentoPrincipal();
        if(SingletonCliente.getVerifyInstance() == null){
            binding.navView.getMenu().getItem(1).getSubMenu().getItem(0).setVisible(false);
            binding.navView.getMenu().getItem(1).getSubMenu().getItem(1).setVisible(true);

        }else{
            binding.navView.getMenu().getItem(1).getSubMenu().getItem(0).setVisible(true);
            binding.navView.getMenu().getItem(1).getSubMenu().getItem(1).setVisible(false);
        }
    }


    @Override
    public void mostrarFragmentoPrincipal() {
        mostrarFragment(Principal.class, binding.clCentro.getId(), null, true);
    }

    @Override
    public void mostrarFragmentoMarcas() {
        mostrarFragment(Marcas.class, binding.clCentro.getId(), null, true, false);
    }

    @Override
    public void mostrarFragmentoModelos(Bundle bundle) {
        mostrarFragmentRefreshBundles(Modelos.class, binding.clCentro.getId(), bundle, true, false);
    }

    @Override
    public void mostrarFragmentoDetalleAuto(Bundle bundle) {
        //mostrarFragment(DetalleAuto.class, binding.clCentro.getId(), bundle, true);
        mostrarFragmentRefreshBundles(DetalleAuto.class, binding.clCentro.getId(), bundle, true, false);
    }


    @Override
    public void mostrarFragmentoModoContacto() {
        mostrarFragment(ModoContacto.class, binding.clCentro.getId(), null, true);
    }

    @Override
    public void mostrarFragmentoFichaTecnica(Bundle bundle) {
        mostrarFragment(FichaTecnica.class, binding.clActivity.getId(), bundle, true);
    }

    @Override
    public void mostrarFragmentoCotizar(Bundle bundle) {
        mostrarFragment(Cotizar.class, binding.clActivity.getId(), bundle, false);
    }

    @Override
    public void mostrarFragmentoContacto() {
        mostrarFragment(Contactarme.class, binding.clCentro.getId(), null, true);
    }

    @Override
    public void mostrarFragmentoFinanciamiento() {
        mostrarFragment(SolicitudFinanciamiento.class, binding.clCentro.getId(), null, true);
    }

    @Override
    public void mostrarFragmentoPagar() {
        mostrarFragment(PagarAnticipo.class, binding.clCentro.getId(), null, true);
    }

    @Override
    public void mostrarFragmentoContratos() {
        mostrarFragment(Contratos.class, binding.clCentro.getId(), null, true);
    }


    @Override
    public void mostrarFragmentoDetalleContrato() {
        mostrarFragment(DetalleContrato.class, binding.clCentro.getId(), null, true);
    }

    @Override
    public void mostrarFragmentoLoginWithOptions() {
        mostrarFragment(LoginWithOptions.class, binding.clCentro.getId(), null, true);
    }

    @Override
    public void mostrarFragmentoDatosPersonales() {
        mostrarFragment(DatosPersonales.class, binding.clCentro.getId(), null, true);
    }

    @Override
    public void mostrarFragmentoMetodosPago() {
        mostrarFragment(MetodosPago.class, binding.clCentro.getId(), null, true);
    }

    @Override
    public void mostrarFragmentoMediosPago(){
        mostrarFragment(MediosPago.class, binding.clActivity.getId(), null, true);
    }

    @Override
    public void mostrarFragmentoCotizaMonto() {
        mostrarFragment(CotizarMonto.class, binding.clCentro.getId(), null, true);
    }

    @Override
    public void mostrarFragmentoPresentacionCasa() {
        mostrarFragment(Presentacion.class, binding.clCentro.getId(), null, true);
    }

  @Override
  public void mostrarFragmentoTerminosCondiciones(){
      mostrarFragment(TerminosCondiciones.class, binding.clCentro.getId(), null, true);
  }

    @Override
    public void hideToolBar() {

    }


    @Override
    public void showToolBar() {

    }

    @Override
    public void showSnack(String message) {
        UTHelpers.showSnackBar(findViewById(android.R.id.content), message, false);
    }


    public boolean IsLogged() {
        try {
            boolean isExist = false;
            db = new DatabaseSqlite(this);
            MPersona cliente =db.getCliente();
            if (cliente != null) {
                if(SingletonCliente.getVerifyInstance()!= null){
                    SingletonCliente.destroySingleton();
                    SingletonCliente.getInstance(cliente);
                }else{
                    SingletonCliente.getInstance(cliente);
                }
                verifyToken();

                isExist = true;
            }
            return isExist;

        } catch (Exception _exc) {
            throw _exc;
        }
    }

    private void verifyToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String refreshToken = instanceIdResult.getToken();
                if(!SingletonCliente.getInstance().getToken().equals(refreshToken)){
                    //ToDo TENGO QUE HACER UN UPDATE EN EL SERVICIO
                    //ToDo Eliminamos el registro que existe y creamos uno nuevo
                    MPersona client = SingletonCliente.getInstance();
                    client.setToken(refreshToken);
                    deleteUserStorage(client.getIdCuenta());
                    insertStorageClient(client);
                    SingletonCliente.destroySingleton();
                    SingletonCliente.getInstance(client);
                }
            }
        });
    }


    @Override
    public void deleteUserStorage(String idCuenta){
        if(db == null){
            db = new DatabaseSqlite(this);
        }
        db.deleteUsers(idCuenta);
    }
    @Override
    public void insertStorageClient(MPersona cliente){
        if(db == null){
            db = new DatabaseSqlite(this);
        }
        db.insertStorageCliente(cliente);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_datos_perfil:
                if(SingletonCliente.getVerifyInstance() != null){
                    mostrarFragmentoDatosPersonales();
                }else{
                    mostrarFragmentoLoginWithOptions();
                }
                break;
            case R.id.option_metodos_pago:
                if(SingletonCliente.getVerifyInstance() != null){
                    mostrarFragmentoMetodosPago();
                }else{
                    mostrarFragmentoLoginWithOptions();
                    showSnack("Inicia sesión o regístrate");
                }
                break;
            case R.id.option_cerrar_sesion:
                deleteUserStorage(SingletonCliente.getInstance().getIdCuenta());
                SingletonCliente.destroySingleton();
                showSnack("Cerraste sesión");
                break;
            case R.id.option_iniciar_sesion:
                mostrarFragmentoLoginWithOptions();
                break;
            case R.id.option_terminos_condiciones:
                mostrarFragmentoTerminosCondiciones();
                break;

            case R.id.option_contratos:
                mostrarFragmentoContratos();
                break;
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return  true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.home:
                mostrarFragmentoPrincipal();
                /*Fragment fragment = new Principal();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.cl_centro, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();*/
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        boolean isFind = false;
        List<Fragment> lstFrag =  this.getSupportFragmentManager().getFragments();
        for (Fragment item: lstFrag ) {
            if(item.getTag().equals("DetalleAuto")){
                isFind = true;
                break;
            }
            if(item.getTag().equals("Principal")){
                isFind = true;
                break;
            }
        }

        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }else if(!isFind){
            super.onBackPressed();
        }
    }


}

