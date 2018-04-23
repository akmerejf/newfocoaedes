package com.example.supanonymous.focoaedes.helpers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.supanonymous.focoaedes.LoginActivity;
import com.example.supanonymous.focoaedes.MainActivity;
import com.example.supanonymous.focoaedes.models.Login;
import com.example.supanonymous.focoaedes.models.LoginResponse;
import com.example.supanonymous.focoaedes.models.Register;
import com.example.supanonymous.focoaedes.services.ApiService;
import com.example.supanonymous.focoaedes.utils.ServiceGenerator;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthHelper {

    private AccountManager mAccountManager = null;
    private ApiService apiService = ServiceGenerator.createService(ApiService.class);
    private String token = "";

    private void setAccountManager(Context context) {
        mAccountManager = AccountManager.get(context);
    }

    private void setApiService(String token) {
        apiService = ServiceGenerator.createService(ApiService.class, token);
    }

    public void signIn(final Context context, final Intent intent, final String email, final String password) {
        Login login = new Login();
        login.setPassword(password);
        login.setEmail(email);
        Toast.makeText(context, "Logando", Toast.LENGTH_SHORT).show();
        apiService.signIn(login).enqueue(new Callback<LoginResponse>() {
            //metodos de respostas
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                //condição se os dados foram capturados
                if (!response.isSuccessful()) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    abrirNovaActivity(context, intent);
                    Log.i("LISTA", "Erro: " + "Erro: " + response.code());
                } else {
                    //                  Atualiza os dados do usuario no Account Manager
                    updateDeviceAccountInfo(response, context, email);
                    //                  Inicia a nova atividade
                    Toast.makeText(context, "Logado com sucesso", Toast.LENGTH_SHORT).show();
                    abrirNovaActivity(context, intent);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(context, "Não foi possível logar, tente novamente.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, LoginActivity.class);

                abrirNovaActivity(context, intent);
            }
        });
    }

    public void createAccount(final Context context, final Intent intent, final Register register) {
        apiService.createUser(register).enqueue(new Callback<LoginResponse>() {
            //metodos de respostas
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                //condição se os dados foram capturados
                if (!response.isSuccessful()) {
                    Log.i("REGISTRO", "" + response.code()+": " + response.body());

                    Log.i("LISTA", "Erro: " + "Erro: " + response.code());
                } else {
                    //                  Atualiza os dados do usuario no Account Manager
                    updateDeviceAccountInfo(response, context, register.getEmail());
                    //                  Inicia a nova atividade
                    abrirNovaActivity(context, intent);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.i("REGISTRO", "Falha: " + t.getMessage());
                Intent intent = new Intent(context, LoginActivity.class);

                abrirNovaActivity(context, intent);
            }
        });
    }

    public String refreshAuthToken(final Context context, String token) {

        apiService.refreshAccessToken(token).enqueue(new Callback<String>() {
            //metodos de respostas
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //condição se os dados foram capturados
                if (!response.isSuccessful()) {
                    Log.i("REGISTRO", "" + response.code()+": " + response.body());
                } else {
                    //                  Atualiza os dados do usuario no Account Manager
                    updateAuthToken(response, context, response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Não foi possível logar, tente novamente.", Toast.LENGTH_SHORT).show();
                Log.i("LISTA", "Erro: " + t.toString());
            }
        });
        return token;
    }

    private void updateAuthToken(Response<String> response, Context context, String authToken) {
        if (mAccountManager == null)
            setAccountManager(context);
        Account account = mAccountManager.getAccountsByType("com.focoaedes")[0];
        mAccountManager.setAuthToken(account, "full_access", response.body());
        token = authToken;
    }

    public boolean isAnyUser(final Context context) {
        Intent intent;
        Account[] accounts;
        String authToken="";
        if (mAccountManager == null)
            setAccountManager(context);
        accounts = mAccountManager.getAccountsByType("com.focoaedes");
        if (accounts.length > 0)
            authToken = mAccountManager.peekAuthToken(accounts[0], "full_access");
        //        Se não há um token do Facebook ou da API invalida o token do face;
        FacebookSdk.sdkInitialize(context);
        if (TextUtils.isEmpty(authToken) && AccessToken.getCurrentAccessToken() == null) {

            return false;
        }

        return true;

    }

    private void updateDeviceAccountInfo(Response<LoginResponse> response, Context context, String email) {
//                  Usa a instância do account manager ou cria uma nova neste contexto
        Account account;
        if (mAccountManager == null)
            setAccountManager(context);
        //verifica se há um usuario no aparelho
        if (mAccountManager.getAccountsByType("com.focoaedes").length > 0) {
            account = mAccountManager.getAccountsByType("com.focoaedes")[0];
            //adiciona o novo usuário no lugar do outro
            mAccountManager.setUserData(account, AccountManager.KEY_ACCOUNT_NAME, email);
            mAccountManager.setAuthToken(account, "full_access", response.body().getAuthToken());
            return;
        }
        account = new Account(email, "com.desapego");
        mAccountManager.addAccountExplicitly(account, null, null);
        //Adiciona o novo token pro usuario
        mAccountManager.setAuthToken(account, "full_access", response.body().getAuthToken());
        AccessToken.setCurrentAccessToken(null);
    }

    //finaliza a sessão (Há mais detalhes aqui falta implementar)
    public void logOut(Context context) {

        //                  Usa a instância do account manager em uso ou cria uma nova neste contexto
        if (mAccountManager == null)
            setAccountManager(context);
        if (mAccountManager.getAccountsByType("com.desapego")[0] != null) {
            String token = mAccountManager.peekAuthToken(mAccountManager.getAccountsByType("com.focoaedes")[0], "full_access");
            mAccountManager.invalidateAuthToken("com.focoaedes", token);
            mAccountManager.removeAccount(mAccountManager.getAccountsByType("com.focoaedes")[0], null, null);
        }
        if (AccessToken.getCurrentAccessToken() != null) {
            AccessToken.setCurrentAccessToken(null);
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Toast.makeText(context, "Saiu", Toast.LENGTH_SHORT).show();
        abrirNovaActivity(context, intent);

    }


    private void abrirNovaActivity(Context context, Intent intent) {
        context.startActivity(intent);
//        ((Activity)context).finish();
    }


    public void verifyUser(final Context context, final Intent intent) {
        Login login = new Login();
        login.setEmail(intent.getStringExtra("EMAIL").toString());
        apiService.verifyUser(login).enqueue(new Callback<LoginResponse>() {
            //metodos de respostas
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                //condição se os dados foram capturados
                if (!response.isSuccessful()) {
                    //                  Atualiza os dados do usuario no Account Manager
                    ((Activity) context).startActivity(intent);
                } else {
                    Log.i("REGISTRO", "" + response.code()+": " + response.body());
                    updateDeviceAccountInfo(response, context, intent.getStringExtra("EMAIL" ));
                    Intent intent = new Intent(context, MainActivity.class);
                    ((Activity) context).startActivity(intent);
                    ((Activity) context).finish();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(context, "Não foi possível logar, tente novamente.", Toast.LENGTH_SHORT).show();
                Log.i("LISTA", "Erro: " + t.toString());
            }
        });
    }
}
