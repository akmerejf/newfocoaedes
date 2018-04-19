package com.example.supanonymous.focoaedes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by supanonymous on 11/04/18.
 */

public class Ocorrencia {
    @SerializedName("_id")
    @Expose
    private MongoId id;
    @SerializedName("tipo")
    @Expose
    private String tipo;
    @SerializedName("descricao")
    @Expose
    private String descricao;
    @SerializedName("local")
    @Expose
    private String local;
    @SerializedName("profile_id")
    @Expose
    private MongoId profile_id;

    public String getId() {
        return id.get$oid();
    }

    public void setId(MongoId id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public MongoId getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(MongoId profile_id) {
        this.profile_id = profile_id;
    }
}
