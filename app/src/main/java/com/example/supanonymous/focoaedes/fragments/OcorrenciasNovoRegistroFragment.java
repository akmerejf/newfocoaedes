package com.example.supanonymous.focoaedes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.supanonymous.focoaedes.R;
import com.example.supanonymous.focoaedes.fragments.FragmentOcorrenciaRegistroFoco;

/**
 * Created by supanonymous on 11/04/18.
 */

public class OcorrenciasNovoRegistroFragment extends Fragment implements View.OnClickListener {

    private View view;
    private int item;
    private FragmentManager fragmentManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_ocorrencias_novo_registro, container, false);
        return v;

    }

    @Override
    public void onClick(View view) {
        RelativeLayout foco = (RelativeLayout) view.findViewById(R.id.novaFoco);

        foco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = OcorrenciasNovoRegistroFragment.this.fragmentManager.beginTransaction();
                transaction.addToBackStack(null);
                //confirmar transição
                transaction.replace(R.id.NovaOcorrencia, new FragmentOcorrenciaRegistroFoco(), "Registro_foco").commit();
            }
        });
    }
}