package com.example.supanonymous.focoaedes.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.supanonymous.focoaedes.R;

/**
 * Created by supanonymous on 11/04/18.
 */

public class OcorrenciasNovoRegistroFragment extends Fragment implements View.OnClickListener {

    private View view;
    private int item;
    TextView foco;
    TextView doenca;
    private FragmentManager fragmentManager = getFragmentManager();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_ocorrencias_novo_registro, container, false);

        foco = v.findViewById(R.id.txt_foco_descricao);
        doenca = v.findViewById(R.id.txt_descricao_doenca);
        doenca.setOnClickListener(this);
        foco.setOnClickListener(this);

        return v;
 }

    @Override
    public void onClick(View view) {
        Fragment fragment;
        switch (view.getId()) {
            case R.id.txt_foco_descricao:
                fragment = new FragmentOcorrenciaRegistroFoco();
                replaceFragment(fragment);
                break;
            case R.id.txt_descricao_doenca:
                fragment = new FragmentOcorrenciaRegistroDoenca();
                replaceFragment(fragment);
        }

    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.ocorrencias_tabs, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}