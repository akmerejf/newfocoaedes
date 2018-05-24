package com.example.supanonymous.focoaedes.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.supanonymous.focoaedes.fragments.DoencasOcorrenciasFragment;
import com.example.supanonymous.focoaedes.models.Ocorrencia;

import java.util.ArrayList;
import java.util.List;


public class FragmentTabLista extends FragmentStatePagerAdapter {

    private final String[] tituloAbas = {"DOENÇAS", "OCORRÊNCIAS"};
    private final String[] fragmentType = {"doencas", "ocorrencias"};
    private int position;

    public FragmentTabLista(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        this.position = position;
        Bundle bundle = new Bundle();
        bundle.putString("fragment_type", fragmentType[position]);
        Fragment fragment = new DoencasOcorrenciasFragment();
        fragment.setArguments(bundle);

        return fragment;

    }


    @Override
    public int getCount() {
        return tituloAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tituloAbas[position];
    }
}
