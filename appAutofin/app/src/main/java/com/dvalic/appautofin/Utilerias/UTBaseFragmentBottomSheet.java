package com.dvalic.appautofin.Utilerias;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.dvalic.appautofin.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import io.reactivex.disposables.CompositeDisposable;

public abstract class UTBaseFragmentBottomSheet extends BottomSheetDialogFragment {

    protected CompositeDisposable mCompositeDisposable;

    protected void lanzarActivity(Intent intent, int flags) {
        UTBaseActivity activity = (UTBaseActivity) getActivity();
        activity.lanzarActivity(intent, flags);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCompositeDisposable.clear();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected void lanzarActivityForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    public <T extends Fragment> void mostrarFragment(Class<T> fragmentClass, int containerViewId, Bundle bundle, boolean addToBackStack, boolean animation) throws RuntimeException {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentClass.getSimpleName());

        if (fragment == null) {
            try {
                fragment = fragmentClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(getString(R.string.string_nuevo_fragmento_creado), e);
            }
        }
        fragment.setArguments(bundle);

        if (animation) {
            fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,
                    R.animator.slide_out_right, android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
        }
        fragmentTransaction.replace(containerViewId, fragment, fragmentClass.getSimpleName());

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public boolean onBackPressed() {
        return false;
    }


}
