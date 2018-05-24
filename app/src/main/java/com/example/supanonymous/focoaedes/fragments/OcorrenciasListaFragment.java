package com.example.supanonymous.focoaedes.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.supanonymous.focoaedes.R;
import com.example.supanonymous.focoaedes.adapters.OcorrenciasAdapter;
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

public class OcorrenciasListaFragment extends Fragment {

    private List<Ocorrencia> ocorrencias;
    private RecyclerView recyclerView;
    private OcorrenciasAdapter adapter;
    private ApiService apiService;
    private LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ocorrencias_lista, container, false);

        ocorrencias = new ArrayList<>();

        //Monta listview e adapter

        recyclerView = view.findViewById(R.id.principal_lista);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new OcorrenciasAdapter(ocorrencias, getContext(), recyclerView);
        recyclerView.setAdapter(adapter);

        getOcorrencias();

        return view;
    }

    private void getOcorrencias() {
        apiService = ServiceGenerator.createService(ApiService.class);
        apiService.getOcorrencias().enqueue(new Callback<List<Ocorrencia>>() {
            @Override
            public void onResponse(Call<List<Ocorrencia>> call, Response<List<Ocorrencia>> response) {
                if (!response.isSuccessful()) {
                    Log.i("ERRO_RESPOSTA", String.valueOf(response.code()));
                } else {
                    ocorrencias.clear();
                    ocorrencias.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Ocorrencia>> call, Throwable t) {
                Log.i("ERRO_ENVIAR", t.getMessage().toString());
            }
        });
    }
}
