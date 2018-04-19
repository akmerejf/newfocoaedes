package com.example.supanonymous.focoaedes.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.supanonymous.focoaedes.R;

/**
 * Created by supanonymous on 25/03/18.
 */

public class TermoServicoFragment extends Fragment {

    private TextView termo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_termo_servico, container, false);
        termo = view.findViewById(R.id.termo_servico);

        return view;
    }


}
