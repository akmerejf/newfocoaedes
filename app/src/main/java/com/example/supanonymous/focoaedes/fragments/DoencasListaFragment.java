package com.example.supanonymous.focoaedes.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.supanonymous.focoaedes.R;
import com.example.supanonymous.focoaedes.adapters.DoencasAdapter;
import com.example.supanonymous.focoaedes.adapters.FragmentTabLista;
import com.example.supanonymous.focoaedes.models.Disease;
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

public class DoencasListaFragment extends Fragment {

    private List<Disease> doencas;
    private RecyclerView recyclerView;
    private DoencasAdapter adapter;
    private ApiService apiService;
    private LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doencas_lista, container, false);

        doencas = new ArrayList<>();

        //Monta listview e adapter

        recyclerView = view.findViewById(R.id.doenca_lista);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DoencasAdapter(doencas, getContext(), recyclerView);
        recyclerView.setAdapter(adapter);

        getDoencas();


        Resources resources = getResources();
        FragmentTabLista adapter = new FragmentTabLista(getFragmentManager());
//        adapter.add(FragmentTabLista.newInstance(1), resources.getString(R.string.frag_one));


        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void getDoencas() {
        apiService = ServiceGenerator.createService(ApiService.class);
        apiService.getDoencas().enqueue(new Callback<List<Disease>>() {
            @Override
            public void onResponse(Call<List<Disease>> call, Response<List<Disease>> response) {
                if (!response.isSuccessful()) {
                    Log.i("ERRO_RESPOSTA", String.valueOf(response.code()));
                } else {
                    doencas.clear();
                    doencas.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Disease>> call, Throwable t) {
                Log.i("ERRO_ENVIAR", t.getMessage().toString());
            }
        });
    }
}
