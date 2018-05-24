package com.example.supanonymous.focoaedes.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.supanonymous.focoaedes.models.Ocorrencia;

import java.util.ArrayList;
import java.util.List;


public class FragmentTabLista extends FragmentPagerAdapter {


        private List<FragmentTabLista> listFragments = new ArrayList<FragmentTabLista>();
    private List<String> listFragmentsTitle =  new ArrayList<>();



    public FragmentTabLista(FragmentManager fm) {
        super(fm);
    }


    public void add(FragmentTabLista frag, String title){
        this.listFragments.add(frag);
        this.listFragmentsTitle.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
