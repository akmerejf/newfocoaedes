package com.example.supanonymous.focoaedes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by supanonymous on 21/05/18.
 */

public class Imagem {

    @SerializedName("_id")
    @Expose
    private MongoId id;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getId() {
        return id.get$oid();
    }

    public void setId(MongoId id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
