package com.example.supanonymous.focoaedes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User{

    private String realm_id;
    private Perfil perfil;
    private String auth_token;
    @SerializedName("_id")
    @Expose
    private MongoId id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password_digest")
    @Expose
    private String passwordDigest;
    @SerializedName("email_confirmed")
    @Expose
    private Boolean emailConfirmed;
    @SerializedName("confirmation_code")
    @Expose
    private String confirmationCode;
    @SerializedName("confirmation_sent_at")
    @Expose
    private String confirmationSentAt;

    public User(){
        perfil = new Perfil();
    }


    public String getId() {
        return id.get$oid();
    }

    public void setId(MongoId id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordDigest() {
        return passwordDigest;
    }

    public void setPasswordDigest(String passwordDigest) {
        this.passwordDigest = passwordDigest;
    }

    public Boolean getEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(Boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getConfirmationSentAt() {
        return confirmationSentAt;
    }

    public void setConfirmationSentAt(String confirmationSentAt) {
        this.confirmationSentAt = confirmationSentAt;
    }

    public String getRealm_id() {
        return realm_id;
    }

    public void setRealm_id(String realm_id) {
        this.realm_id = realm_id;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }
}
