package com.example.supanonymous.focoaedes;

import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.supanonymous.focoaedes.helpers.AuthHelper;
import com.example.supanonymous.focoaedes.models.User;
import com.example.supanonymous.focoaedes.services.ApiService;
import com.example.supanonymous.focoaedes.utils.ServiceGenerator;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {


    public static final String ADD_ACCOUNT = "ADD_ACCOUNT";
    ImageView cancelar;
    EditText email, senha;
    Button entrar, btn_fb, criar_conta;
    ProgressDialog progressDialog;

    LoginButton btnfacebook;

    CallbackManager callbackManager;

    private URL foto_perfil;
    private User user;
    private String accountType;
    private AuthHelper loginHelper;

    //------------------clico de vida-------------------------------------------------------------------//
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginHelper = new AuthHelper();
        //Pega o account type do AccountManager setado pelo DesapegoAccountAuthenticator
        accountType = getIntent().getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);

        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        progressDialog = new ProgressDialog(this);

        entrar = (Button) findViewById(R.id.login_btn_entrar);
        criar_conta = (Button) findViewById(R.id.login_btn_cadastrar);
        btn_fb = (Button) findViewById(R.id.login_btn_fb);

        btnfacebook = (LoginButton) findViewById(R.id.login_fb_button);
        btnfacebook.setReadPermissions(Arrays.asList("public_profile", "email"));

        cancelar = (ImageView) findViewById(R.id.login_cancelar);

        callbackManager = CallbackManager.Factory.create(); //utilizar os retornos do facebook
        //                  Verifica se há uma sessão do Facebook ou da API



//------------------Listeners-----------------------------------------------------------------------//


        //clique no botao X para entrar no app sem logar
//        cancelar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                authHelper.abrirActivityPrincipal("");
//            }
//        });

        //clique no botao entrar e validação dos campos de login
        entrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                validarLogin();
            }

        });

        criar_conta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });


        //executa um clique falso para o botão do facebook
        btn_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //botão nativo do facebook que está invisivel
                btnfacebook.performClick();
            }
        });

        //realiza os retornos do facebook logado
        btnfacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //faz a requisição na api GRAPH do Facebook

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        pegarInformacoesUsuario(object);
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,name");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

                Toast.makeText(LoginActivity.this, "Cancelado.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {

                Toast.makeText(LoginActivity.this, "Erro :(", Toast.LENGTH_LONG).show();
            }
        });

    }//fim do onCreate


//------------------para facilitar minha vida-------------------------------------------------------//

    //validação de login api nativa
    private void validarLogin() {
        final String senha = ((EditText) findViewById(R.id.login_senha)).getText().toString();
        final String email = ((EditText) findViewById(R.id.login_email)).getText().toString();
//      Chama a classe que contém a lógica de login

        Intent intent = new Intent(this, MainActivity.class);
        loginHelper.signIn(this, intent, email, senha);
    }

    private void pegarInformacoesUsuario(JSONObject object) {
        try {
            Intent intent;
            Bundle bundle = new Bundle();
            foto_perfil = new URL("https://graph.facebook.com/" + object.getString("id") + "/picture?width=250&height=250");
            bundle.putString("PICTURE", foto_perfil.toString());
            bundle.putString("EMAIL",object.getString("email"));
            bundle.putString("NAME",object.getString("name"));
            intent = new Intent(this, ConfirmarCadastroActivity.class);
            intent.putExtras(bundle);
            loginHelper.verifyUser(LoginActivity.this, intent);


        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }


//------------------Overrides extras----------------------------------------------------------------//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

//------------------fim de codigo-------------------------------------------------------------------//

}
