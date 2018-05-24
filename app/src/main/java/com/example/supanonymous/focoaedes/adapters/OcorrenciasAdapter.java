package com.example.supanonymous.focoaedes.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.supanonymous.focoaedes.DetalheDenunciaFocoActivity;
import com.example.supanonymous.focoaedes.MainActivity;
import com.example.supanonymous.focoaedes.OcorrenciaDetails;
import com.example.supanonymous.focoaedes.R;
import com.example.supanonymous.focoaedes.helpers.OcorrenciasHelper;
import com.example.supanonymous.focoaedes.models.Ocorrencia;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class OcorrenciasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

     List<Ocorrencia> ocorrencias;
     Context context;

    // Items para adicionar dinamicamente ao fim da lista


    public OcorrenciasAdapter(List<Ocorrencia> ocorrencias, Context context, RecyclerView recyclerView) {
        this.context = context;
        this.ocorrencias = ocorrencias;

    }

    @Override
    public int getItemViewType(int position) {
        return ocorrencias.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_ocorrencias, parent, false);

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

        return ocorrencias.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder visao, int position) {

            ((Visao)visao).titulo.setText(ocorrencias.get(position).getTitulo());
            ((Visao)visao).endereco.setText(ocorrencias.get(position).getEndereco()+" - "+ocorrencias.get(position).getBairro());
            Glide.with(context).load(ocorrencias.get(position).getImagems().get(0).getUrl())
                    .into(((Visao)visao).item_image);
//            if (items.get(position).getFotinhas().size() > 0)
//                Glide.with(context).load(items.get(position).getFotinhas().get(0).getUrl()).into(((Visao)visao).item_image);//altera o icone


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
            endereco = itemView.findViewById(R.id.endereco);
            item_image = itemView.findViewById(R.id.principal_lista_img);
            data = itemView.findViewById(R.id.data);

            itemView.setOnClickListener(v -> {
                String ocorrenciaId = ocorrencias.get(getAdapterPosition()).getId();
                Intent intent = new Intent(context, DetalheDenunciaFocoActivity.class);
                intent.putExtra("ocorrencia_id", ocorrenciaId);
                context.startActivity(intent);
            });

        }

    }


}
