package com.dvalic.appautofin.Utilerias;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.dvalic.appautofin.R;


import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;

public abstract class UTBaseActivity extends AppCompatActivity {
    protected CompositeDisposable mCompositeDisposable;
    protected String urlBase = "http://";
    public static final int NO_FLAGS = -1;
    protected String message = "";
    private String TAG = UTBaseActivity.class.getSimpleName();

    public ActivityCompat.OnRequestPermissionsResultCallback permissionsResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCompositeDisposable.clear();
    }

    public void lanzarActivity(Intent intent, int flags) {
        if (flags == NO_FLAGS) {
            startActivity(intent);
        } else {
            startActivity(intent.addFlags(flags));
        }
    }

    public void lanzarActivityForResult(Intent intent, int requestCode, int flags) {
        if (flags == NO_FLAGS) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivityForResult(intent.addFlags(flags), requestCode);
        }
    }

    public <T extends Fragment> void mostrarFragment(Class<T> fragmentClass, int containerViewId, Bundle bundle, boolean addToBackStack) {
        mostrarFragment(fragmentClass, containerViewId, bundle, addToBackStack, false);
    }

    public <T extends Fragment> void mostrarFragment(Class<T> fragmentClass, int containerViewId, Bundle bundle, boolean addToBackStack, boolean clearStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (clearStack) {
            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                fragmentManager.popBackStack();
            }
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentClass.getSimpleName());
        if (fragment == null) {
            try {
                fragment = fragmentClass.newInstance();
                fragment.setArguments(bundle);
            } catch (Exception e) {
                throw new RuntimeException(getString(R.string.string_nuevo_fragmento_creado), e);
            }
        }

        fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,
                R.animator.slide_out_right, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);

        fragmentTransaction.replace(containerViewId, fragment, fragmentClass.getSimpleName());

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragmentClass.getName());
        }
        fragmentTransaction.commit();
    }


    //ADD MARINS
    public <T extends Fragment> void mostrarFragmentRefreshBundles(Class<T> fragmentClass, int containerViewId, Bundle bundle, boolean addToBackStack, boolean clearStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (clearStack) {
            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                fragmentManager.popBackStack();
            }
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentClass.getSimpleName());

            try {
                if(fragment != null){
                    if(fragment.getArguments() != null){
                        if(!fragment.getArguments().isEmpty()){
                            Objects.requireNonNull(fragment.getArguments()).clear();
                        }

                    }
                }
                fragment = fragmentClass.newInstance();
                fragment.setArguments(bundle);
            } catch (Exception e) {
                throw new RuntimeException(getString(R.string.string_nuevo_fragmento_creado), e);
            }


        fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,
                R.animator.slide_out_right, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);

        fragmentTransaction.replace(containerViewId, fragment, fragmentClass.getSimpleName());

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragmentClass.getName());
        }
        fragmentTransaction.commit();
    }








    public <T extends Fragment> void mostrarFragmentApha(Class<T> fragmentClass, int containerViewId, Bundle bundle, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentClass.getSimpleName());
        if (fragment == null) {
            try {
                fragment = fragmentClass.newInstance();
                fragment.setArguments(bundle);
            } catch (Exception e) {
                throw new RuntimeException(getString(R.string.string_nuevo_fragmento_creado), e);
            }
        }

        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        fragmentTransaction.replace(containerViewId, fragment, fragmentClass.getSimpleName());

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean accion = false;
        Log.d(TAG, "empezando un key down");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
                if (getSupportFragmentManager().getFragments().get(i) instanceof UTBaseFragment) {
                    UTBaseFragment fragment = (UTBaseFragment) getSupportFragmentManager().getFragments().get(i);
                    assert fragment.getTag() != null;
                    if(!fragment.getTag().equals("Cotizar") &&  !fragment.getTag().equals("FichaTecnica")){
                        accion = accion || fragment.onBackPressed();
                    }

                }
            }
        }
        Log.d(TAG, " accion = " + accion);
        return accion || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionsResult != null) {
            permissionsResult.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
