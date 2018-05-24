package com.example.supanonymous.focoaedes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.supanonymous.focoaedes.R;
import com.example.supanonymous.focoaedes.adapters.DoencasAdapter;
import com.example.supanonymous.focoaedes.adapters.OcorrenciasAdapter;
import com.example.supanonymous.focoaedes.models.Disease;
import com.example.supanonymous.focoaedes.models.Ocorrencia;
import com.example.supanonymous.focoaedes.services.ApiService;
import com.example.supanonymous.focoaedes.utils.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by supanonymous on 24/05/18.
 */

public class DoencasOcorrenciasFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private FrameLayout progressView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private String fragmentType;
    private List<Disease> listaDoencas;
    private LinearLayoutManager layoutManager;
    private DoencasAdapter doencasAdapter;
    private ApiService apiService;
    private List<Ocorrencia> listaOcorrencias;
    private OcorrenciasAdapter ocorrenciasAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_doencas_ocorrencias_lista, container, false);
        progressView = view.findViewById(R.id.progress_view);
        //Monta listview e adapter
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        recyclerView = view.findViewById(R.id.fragment_doencas_ocorrencias_lista);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        // Recebe o tipo de fragmento do TabsAdapter
        fragmentType = getArguments().getString("fragment_type");


        /* Verifica se o fragmento atual é do tipo noticias ou avisos.
           Caso seja noticias, cria uma instancia do Retrofit para chamadas à API.
           Caso seja avisos, cria uma instância para o Firebase.
         */
        if (TextUtils.equals(fragmentType, "doencas")) {

            listaDoencas = new ArrayList<>();
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            doencasAdapter = new DoencasAdapter(listaDoencas, getContext(), recyclerView);
            recyclerView.setAdapter(doencasAdapter);

            //Instânciar objetos da API
            getDoencas();
        } else if (TextUtils.equals(fragmentType, "ocorrencias")) {

            listaOcorrencias = new ArrayList<>();
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            ocorrenciasAdapter = new OcorrenciasAdapter(listaOcorrencias, getContext(), recyclerView);
            recyclerView.setAdapter(ocorrenciasAdapter);

            //Instânciar objetos da API
            getOcorrencias();

        }

        return view;

    }

    private void getOcorrencias() {
        progressView.setVisibility(View.VISIBLE);
        if (apiService==null)
            apiService = ServiceGenerator.createService(ApiService.class);
        apiService.getOcorrencias().enqueue(new Callback<List<Ocorrencia>>() {
            @Override
            public void onResponse(Call<List<Ocorrencia>> call, Response<List<Ocorrencia>> response) {
                if (!response.isSuccessful()) {
                    Log.i("ERRO", "" + response.code());
                } else {
                    listaOcorrencias.clear();
                    List<Ocorrencia> noticias = response.body();
                    listaOcorrencias.addAll(noticias);
                    ocorrenciasAdapter.notifyDataSetChanged();
                }
                progressView.setVisibility(View.GONE);
                if (mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Ocorrencia>> call, Throwable t) {
                Log.i("ERRO", t.getMessage().toString());
                progressView.setVisibility(View.GONE);
                if (mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getDoencas() {
        progressView.setVisibility(View.VISIBLE);
        if (apiService==null)
            apiService = ServiceGenerator.createService(ApiService.class);
        apiService.getDoencas().enqueue(new Callback<List<Disease>>() {
            @Override
            public void onResponse(Call<List<Disease>> call, Response<List<Disease>> response) {
                if (!response.isSuccessful()) {
                    Log.i("ERRO", "" + response.code());
                } else {

                    listaDoencas.clear();
                    List<Disease> doencas = response.body();
                    listaDoencas.addAll(doencas);
                    doencasAdapter.notifyDataSetChanged();
                }
                progressView.setVisibility(View.GONE);
                if (mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Disease>> call, Throwable t) {
                Log.i("ERRO", "ERRO AO ENVIAR");
                progressView.setVisibility(View.GONE);
                if (mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        if (TextUtils.equals(fragmentType, "doencas")) {

            listaDoencas = new ArrayList<>();
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            doencasAdapter = new DoencasAdapter(listaDoencas, getContext(), recyclerView);
            recyclerView.setAdapter(doencasAdapter);

            //Instânciar objetos da API
            getDoencas();
        } else if (TextUtils.equals(fragmentType, "ocorrencias")) {

            listaOcorrencias = new ArrayList<>();
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            ocorrenciasAdapter = new OcorrenciasAdapter(listaOcorrencias, getContext(), recyclerView);
            recyclerView.setAdapter(ocorrenciasAdapter);

            //Instânciar objetos da API
            getOcorrencias();
        }
    }
}
