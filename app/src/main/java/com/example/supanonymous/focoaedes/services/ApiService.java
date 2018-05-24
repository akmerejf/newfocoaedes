package com.example.supanonymous.focoaedes.services;

import com.example.supanonymous.focoaedes.models.Disease;
import com.example.supanonymous.focoaedes.models.Login;
import com.example.supanonymous.focoaedes.models.LoginResponse;
import com.example.supanonymous.focoaedes.models.Ocorrencia;
import com.example.supanonymous.focoaedes.models.Perfil;
import com.example.supanonymous.focoaedes.models.Register;

import java.security.DigestInputStream;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ApiService {
    @GET("ocorrencias")
    Call<List<Ocorrencia>> getOcorrencias(
    );

    @GET("ocorrencias")
    Call<List<Ocorrencia>> getOcorrenciasMap(
    );

    @GET("ocorrencias/{id}")
    Call<Ocorrencia> getOcorrencia(@Path("id") String id);

//-----------------------------------------------------------

    @GET("doencas")
    Call<List<Disease>> getDoencas(
    );
    @GET("doencas")
    Call<List<Disease>> getDoencasMap(
    );

    @GET("doencas/{id}")
    Call<Disease> getDoenca(@Path("id") String id);

    @GET("perfil")
    Call<Perfil> getProfile();

    @GET("perfil/ocorrencias")
    Call<List<Ocorrencia>> getUserOcorrencias();

    @POST("users")
    Call<LoginResponse> createUser(@Body Register register);

    @POST("users/login")
    Call<LoginResponse> signIn(@Body Login user_login);

    @POST("users/login")
    Call<String> refreshAccessToken(@Body String user_token);

    @POST("users/verify_user")
    Call<LoginResponse> verifyUser(@Body Login login);

    @POST("ocorrencias")
    Call<Ocorrencia> createOcorrencia(@Body Ocorrencia ocorrencia);

    @POST("doencas")
    Call<Disease> createDoenca(@Body Disease doenca);

}
