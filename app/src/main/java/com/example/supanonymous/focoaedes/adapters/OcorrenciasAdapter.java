package com.example.supanonymous.focoaedes.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
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
import com.example.supanonymous.focoaedes.OcorrenciaDetails;
import com.example.supanonymous.focoaedes.R;
import com.example.supanonymous.focoaedes.helpers.OcorrenciasHelper;
import com.example.supanonymous.focoaedes.models.Ocorrencia;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class OcorrenciasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    static List<Ocorrencia> items;
    static Context context;
    private static OcorrenciasHelper itemsHelper;

    // Items para adicionar dinamicamente ao fim da lista
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean listEnd = false;
    private SparseBooleanArray expandState = new SparseBooleanArray();

    public OcorrenciasAdapter(List<Ocorrencia> items, Context context, RecyclerView recyclerView) {
        OcorrenciasAdapter.items = items;
        OcorrenciasAdapter.context = context;

        for (int i = 0; i < OcorrenciasAdapter.items.size(); i++) {
            expandState.append(i, false);
        }

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    totalItemCount = linearLayoutManager.getItemCount();
//                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//                        // End has been reached
//                        // Do something
//                        if (onLoadMoreListener != null) {
//                            onLoadMoreListener.onLoadMore();
//                        }
//                        loading = true;
//                    }
//                }
//            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
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
    public void onBindViewHolder(RecyclerView.ViewHolder visao, int position) {
        if (visao instanceof Visao) {
            ((Visao)visao).titulo.setText(items.get(position).getName());
            visao.setIsRecyclable(false);
            final boolean isExpanded = expandState.get(position);
            ((Visao)visao).expandableLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);

            ((Visao)visao).buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);
            ((Visao)visao).buttonLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickButton(((Visao)visao).expandableLayout, ((Visao)visao).buttonLayout,  position);
                }
            });
//            if (items.get(position).getFotinhas().size() > 0)
//                Glide.with(context).load(items.get(position).getFotinhas().get(0).getUrl()).into(((Visao)visao).item_image);//altera o icone

        } else {
            ((ProgressViewHolder) visao).progressBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {

        return items.size();
    }

    public boolean isListEnd() {
        return listEnd;
    }

    public  class Visao extends RecyclerView.ViewHolder{

        TextView titulo;

        RoundedImageView item_image;

        RelativeLayout buttonLayout;
        LinearLayout expandableLayout;

        public Visao(final View itemView) {
            super(itemView);
            buttonLayout = itemView.findViewById(R.id.button);
//            expandableLayout = itemView.findViewById(R.id.expandableLayout);
//            titulo = itemView.findViewById(R.id.principal_lista_titulo);
//            item_image = itemView.findViewById(R.id.principal_lista_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    itemsHelper = new OcorrenciasHelper(context);
//                    Intent intent = new Intent(context, OcorrenciaDetails.class);
//                    itemsHelper.showOcorrencia(intent, items.get(getAdapterPosition()).getId());
                }
            });
        }

    }
    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    public void setListEnd(){
        listEnd = true;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }


    private void onClickButton(final LinearLayout expandableLayout, final RelativeLayout buttonLayout, final  int i) {

        //Simply set View to Gone if not expanded
        //Not necessary but I put simple rotation on button layout
        if (expandableLayout.getVisibility() == View.VISIBLE){
            createRotateAnimator(buttonLayout, 180f, 0f).start();
            expandableLayout.setVisibility(View.GONE);
            expandState.put(i, false);
        }else{
            createRotateAnimator(buttonLayout, 0f, 180f).start();
            expandableLayout.setVisibility(View.VISIBLE);
            expandState.put(i, true);
        }
    }

    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }
}
