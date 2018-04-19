package com.example.supanonymous.focoaedes.authenticator;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by supanonymous on 21/03/18.
 */

public class TokenInterceptor implements Interceptor {

    private final String token;

    public TokenInterceptor(String token){
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        // Request customization: add request headers
        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization", token); // <-- this is the important line

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
