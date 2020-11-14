package com.dvalic.appautofin.Activities.MenuOpciones;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dvalic.appautofin.API.Model.SingletonCliente;
import com.dvalic.appautofin.IActions.IComunicador;
import com.dvalic.appautofin.R;
import com.dvalic.appautofin.Utilerias.UTBaseFragmentBottomSheet;
import com.dvalic.appautofin.Utilerias.UTHelpers;
import com.dvalic.appautofin.databinding.FragmentMenuConfiguracionBinding;



public class MenuConfiguracion extends UTBaseFragmentBottomSheet {
    private String TAG = getTag();
    private FragmentMenuConfiguracionBinding binding;
    private IComunicador interfaz;
    Handler handler;

    public MenuConfiguracion() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMenuConfiguracionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(SingletonCliente.getVerifyInstance() == null){
            binding.navConfiguracion.getMenu().getItem(1).getSubMenu().getItem(0).setVisible(false);
            binding.navConfiguracion.getMenu().getItem(1).getSubMenu().getItem(1).setVisible(true);

        }else{
            binding.navConfiguracion.getMenu().getItem(1).getSubMenu().getItem(0).setVisible(true);
            binding.navConfiguracion.getMenu().getItem(1).getSubMenu().getItem(1).setVisible(false);
        }
        binding.navConfiguracion.setNavigationItemSelectedListener((menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.option_datos_perfil:
                    if(SingletonCliente.getVerifyInstance() != null){
                        interfaz.mostrarFragmentoDatosPersonales();
                    }else{
                        interfaz.mostrarFragmentoLoginWithOptions();
                    }
                    this.dismiss();
                    break;
                case R.id.option_metodos_pago:
                    if(SingletonCliente.getVerifyInstance() != null){
                        interfaz.mostrarFragmentoMetodosPago();
                        interfaz.hideToolBar();
                    }else{
                        interfaz.mostrarFragmentoLoginWithOptions();
                        interfaz.showSnack("Inicia sesión o regístrate");
                    }


                    this.dismiss();
                    break;
                case R.id.option_cerrar_sesion:
                    interfaz.deleteUserStorage(SingletonCliente.getInstance().getIdCuenta());
                    SingletonCliente.destroySingleton();
                    binding.navConfiguracion.getMenu().getItem(1).getSubMenu().getItem(0).setVisible(false);
                    binding.navConfiguracion.getMenu().getItem(1).getSubMenu().getItem(1).setVisible(true);
                    this.dismiss();
                    interfaz.showSnack("Cerraste sesión");
                    break;
                case R.id.option_iniciar_sesion:
                    interfaz.mostrarFragmentoLoginWithOptions();
                    this.dismiss();
                    break;
                case R.id.option_terminos_condiciones:
                    this.dismiss();
                    break;
            }
            binding.navConfiguracion.bringToFront();

            return true;
        }));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context); 
        if (context instanceof IComunicador) {
            interfaz = (IComunicador) context;
        }
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

}
