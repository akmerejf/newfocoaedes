package com.example.supanonymous.focoaedes.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.List;

/**
 * Created by supanonymous on 11/04/18.
 */

public class Ocorrencia {
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

    @SerializedName("images")
    @Expose
    private List<Imagem> imagems = null;

//    @SerializedName("profile_id")
//    @Expose
//    private MongoId profile_id;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("name")
    @Expose
    private String titulo;


    public Ocorrencia(String descricao, String endereco, String bairro, List<Imagem> images, String lat, String lng, String titulo) {
        this.descricao = descricao;
        this.endereco = endereco;
        this.bairro = bairro;
        this.imagems = images;
        this.lat = lat;
        this.lng = lng;
        this.titulo = titulo;
    }

    public List<Imagem> getImagems() {
        return imagems;
    }

    public void setImagems(List<Imagem> imagems) {
        this.imagems = imagems;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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
//    public MongoId getProfile_id() {
//        return profile_id;
//    }
//
//    public void setProfile_id(MongoId profile_id) {
//        this.profile_id = profile_id;
//    }
}
