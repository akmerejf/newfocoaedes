package com.example.supanonymous.focoaedes.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by supanonymous on 12/04/18.
 */

public class OcorrenciaClusterItem implements ClusterItem {
    private final LatLng mPosition;
    private  String mTitle;
    private  String mSnippet;
    private String urlImagem;
    private String mId;

    public OcorrenciaClusterItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public OcorrenciaClusterItem(double lat, double lng, String title, String snippet, String urlImagem, String id) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        this.urlImagem = urlImagem;
        mId = id;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public String getmId() {
        return mId;
    }
}
