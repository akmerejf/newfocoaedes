package com.example.supanonymous.focoaedes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.supanonymous.focoaedes.models.Ocorrencia;
import com.example.supanonymous.focoaedes.services.ApiService;
import com.example.supanonymous.focoaedes.utils.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalheDenunciaFocoActivity extends AppCompatActivity {

    private TextView voltarBtn;
    private TextView tituloTv;
    private TextView descricaoTv;
    private TextView enderecoTv;
    private ImageView denunciaIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_denuncia_foco);

        if (getIntent().getExtras()==null)
            finish();
        voltarBtn = findViewById(R.id.detalhe_denuncia_voltar);
        tituloTv = findViewById(R.id.detalhe_titulo);
        descricaoTv = findViewById(R.id.detalhe_descricao);
        enderecoTv = findViewById(R.id.detalhe_endereco);
        denunciaIv = findViewById(R.id.detalhe_foto);

        voltarBtn.setOnClickListener(v -> finish());

        Bundle bundle = getIntent().getExtras();

        loadDetails(bundle.getString("ocorrencia_id"));





    }

    private void loadDetails(String ocorrenciaId) {
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        apiService.getOcorrencia(ocorrenciaId).enqueue(new Callback<Ocorrencia>() {
            @Override
            public void onResponse(Call<Ocorrencia> call, Response<Ocorrencia> response) {
                if (!response.isSuccessful()){

                }else {
                    tituloTv.setText(response.body().getTitulo());
                    descricaoTv.setText(response.body().getDescricao());
                    enderecoTv.setText(response.body().getEndereco());
                    descricaoTv.setMovementMethod(new ScrollingMovementMethod());
                    Glide.with(DetalheDenunciaFocoActivity.this)
                            .load(response.body().getImagems().get(0).getUrl())
                            .into(denunciaIv);
                }
            }

            @Override
            public void onFailure(Call<Ocorrencia> call, Throwable t) {

            }
        });
    }
}
