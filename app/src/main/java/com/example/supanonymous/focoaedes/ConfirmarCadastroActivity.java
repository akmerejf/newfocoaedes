package com.example.supanonymous.focoaedes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.supanonymous.focoaedes.fragments.TermoServicoFragment;
import com.example.supanonymous.focoaedes.helpers.AuthHelper;
import com.example.supanonymous.focoaedes.models.ProfileAttributes;
import com.example.supanonymous.focoaedes.models.Register;
import com.facebook.AccessToken;
/**
 * Created by supanonymous on 25/03/18.
 */

public class ConfirmarCadastroActivity extends AppCompatActivity {
    private Button criar_conta;
    private TextView nome;
    private ImageView foto_perfil;
    private FragmentManager fragmentManager;
    private TextView info;
    private AuthHelper loginHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_cadastro);
        final Bundle bundle = getIntent().getExtras();
        criar_conta = (Button) findViewById(R.id.confirmar_cadastro_btn_confirmar);
        nome = (TextView) findViewById(R.id.confirmar_cadastro_nome);
        foto_perfil = (ImageView) findViewById(R.id.confirmar_cadastro_picture);
        info = (TextView) findViewById(R.id.confirmar_cadastro_info);
        SpannableString ss = new SpannableString("*. ao clickar em criar conta você está " +
                "concordando com os termos de serviço do desapego");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack("termos").replace(R.id.inflar, new TermoServicoFragment(), "termos").commit();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.MAGENTA);
            }
        };
        ss.setSpan(clickableSpan, 58, 76, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        info.setText(ss);
        info.setMovementMethod(LinkMovementMethod.getInstance());
        nome.setText(bundle.getString("NAME"));
       Glide.with(this).load(bundle.getString("PICTURE")).into(foto_perfil);//altera o icone para foto do perfil do facebook
        criar_conta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register new_user = new Register();
                ProfileAttributes new_user_profile = new ProfileAttributes();
                new_user_profile.setName(nome.getText().toString());
                new_user_profile.setImgUrl(bundle.getString("PICTURE"));
                new_user.setEmail(bundle.getString("EMAIL"));
                new_user.setPassword(nome.getText().toString());
                new_user.setPasswordConfirmation(nome.getText().toString());
                new_user.setProfileAttributes(new_user_profile);
                loginHelper = new AuthHelper();
                Intent intent = new Intent(ConfirmarCadastroActivity.this, MainActivity.class);
                loginHelper.createAccount(ConfirmarCadastroActivity.this, intent, new_user);
            }
        });
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AccessToken.setCurrentAccessToken(null);
    }
}
