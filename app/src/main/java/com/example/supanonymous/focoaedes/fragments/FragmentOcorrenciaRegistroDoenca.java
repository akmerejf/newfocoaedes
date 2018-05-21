package com.example.supanonymous.focoaedes.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.supanonymous.focoaedes.R;


public class FragmentOcorrenciaRegistroDoenca extends Fragment {
    private Spinner tipo_doenca;
    EditText titulo, endereco, bairro, descricao;
    Button denunciar;
    ImageView pegar_localizacao, foto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ocorrencia_registro_doenca, container, false);

        tipo_doenca = v.findViewById(R.id.nova_tipo);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.tipo_valores, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        tipo_doenca.setAdapter(adapter);

        return v;

    }
}
