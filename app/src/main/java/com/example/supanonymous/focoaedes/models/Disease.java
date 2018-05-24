package com.example.supanonymous.focoaedes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Disease {

    @SerializedName("_id")
    @Expose
    private MongoId id;

    @SerializedName("description")
    @Expose
    private String descricao;
    @SerializedName("address")
    @Expose
    private String endereco;
    @SerializedName("district")
    @Expose
    private String bairro;

    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("disease_type")
    @Expose
    private String tipo_doenca;


    public Disease(String descricao, String endereco, String bairro, String lat, String lng, String tipo) {
        this.descricao = descricao;
        this.endereco = endereco;
        this.bairro = bairro;
        this.lat = lat;
        this.lng = lng;
        this.tipo_doenca = tipo;
    }


    public String getLat() {
        return lat;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getTipo_doenca() {
        return tipo_doenca;
    }

    public void setTitulo(String tipo) {
        this.tipo_doenca = tipo;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getId() {
        return id.get$oid();
    }

    public void setId(MongoId id) {
        this.id = id;
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

//
}
