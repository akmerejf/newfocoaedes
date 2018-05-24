package com.example.supanonymous.focoaedes.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.supanonymous.focoaedes.DetalheDenunciaFocoActivity;
import com.example.supanonymous.focoaedes.R;
import com.example.supanonymous.focoaedes.models.Disease;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class DoencasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    View v;

    List<Disease> doencas;
    Context context;

    public DoencasAdapter(List<Disease> doencas, Context context, RecyclerView recyclerView) {
        this.context = context;
        this.doencas = doencas;

    }
    @Override
    public int getItemViewType(int position) {
        return doencas.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_doencas, parent, false);

            vh = new Visao(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }



    @Override
    public int getItemCount() {

        return doencas.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder visao, int position) {

        ((DoencasAdapter.Visao)visao).titulo.setText(doencas.get(position).getDescricao());
        ((DoencasAdapter.Visao)visao).endereco.setText(doencas.get(position).getEndereco()+" - "+doencas.get(position).getBairro());
        }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {


        ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

    public  class Visao extends RecyclerView.ViewHolder{

        TextView data;
        TextView endereco;
        TextView titulo;

        RoundedImageView item_image;


        public Visao(final View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.principal_lista_titulo);
            endereco = itemView.findViewById(R.id.endereco_doenca_lista);
            data = itemView.findViewById(R.id.data_lista_doenca);

            itemView.setOnClickListener(v -> {
                String doencaId = doencas.get(getAdapterPosition()).getId();
                Intent intent = new Intent(context, DetalheDenunciaFocoActivity.class);
                intent.putExtra("doenca_id", doencaId);
                context.startActivity(intent);
            });

        }

    }
}
