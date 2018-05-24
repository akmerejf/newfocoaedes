package com.example.supanonymous.focoaedes.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.supanonymous.focoaedes.R;
import com.example.supanonymous.focoaedes.adapters.FragmentTabLista;
import com.example.supanonymous.focoaedes.adapters.OcorrenciasAdapter;
import com.example.supanonymous.focoaedes.helpers.SlidingTabLayout;
import com.example.supanonymous.focoaedes.models.Ocorrencia;
import com.example.supanonymous.focoaedes.services.ApiService;
import com.example.supanonymous.focoaedes.utils.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by supanonymous on 11/04/18.
 */

public class ListaFragment extends Fragment {

    private List<Ocorrencia> ocorrencias;
    private RecyclerView recyclerView;
    private OcorrenciasAdapter adapter;
    private ApiService apiService;
    private LinearLayoutManager layoutManager;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_lista, container, false);

        //Menus Slides do Activity principal
        slidingTabLayout = view.findViewById(R.id.stl_tabs);
        viewPager = view.findViewById(R.id.vp_pagina);

        //Configurar sliding tabs
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(getContext(),R.color.colorAccent));
        //Configurar adapter
        FragmentTabLista tabAdapter = new FragmentTabLista( getFragmentManager() );
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);
        slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//         Inicia a atividade de ocorrências

        slidingTabLayout.setViewPager(viewPager);

        return view;
    }

}
