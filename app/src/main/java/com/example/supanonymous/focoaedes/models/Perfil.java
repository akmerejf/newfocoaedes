package com.example.supanonymous.focoaedes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Perfil {
    @SerializedName("_id")
    @Expose
    private MongoId id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("img_url")
    @Expose
    private String img_url;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("user_id")
    @Expose
    private MongoId user_id;

    public String getId() {
        return id.get$oid();
    }

    public void setId(MongoId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public MongoId getUserId() {
        return user_id;
    }

    public void setUserId(MongoId mongoId) {
        this.user_id = mongoId;
    }



}
