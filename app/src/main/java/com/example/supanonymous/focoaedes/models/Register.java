package com.example.supanonymous.focoaedes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Register {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("password_confirmation")
    @Expose
    private String passwordConfirmation;
    @SerializedName("profile_attributes")
    @Expose
    private ProfileAttributes profileAttributes;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String paswordConfirmation) {
        this.passwordConfirmation = paswordConfirmation;
    }

    public ProfileAttributes getProfileAttributes() {
        return profileAttributes;
    }

    public void setProfileAttributes(ProfileAttributes profileAttributes) {
        this.profileAttributes = profileAttributes;
    }

}
