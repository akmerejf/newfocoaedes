package com.example.supanonymous.focoaedes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.supanonymous.focoaedes.models.Disease;
import com.example.supanonymous.focoaedes.models.Ocorrencia;
import com.example.supanonymous.focoaedes.services.ApiService;
import com.example.supanonymous.focoaedes.utils.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalheDenunciaDoencaActivity extends AppCompatActivity {

    TextView doencaTv, descricaoTv,enderecoTv, voltarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_denuncia_doenca);

        doencaTv = findViewById(R.id.detalhe_doenca);
        descricaoTv = findViewById(R.id.detalhe_descricao);
        enderecoTv = findViewById(R.id.detalhe_endereco_doenca);
        voltarBtn = findViewById(R.id.btnVoltar);

        voltarBtn.setOnClickListener(v -> finish());

        Bundle bundle = getIntent().getExtras();

        loadDetails(bundle.getString("doenca_id"));
    }

    private void loadDetails(String doencaId) {
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        apiService.getDoenca(doencaId).enqueue(new Callback<Disease>() {
            @Override
            public void onResponse(Call<Disease> call, Response<Disease> response) {
                if (!response.isSuccessful()) {

                } else {
                    doencaTv.setText(response.body().getTipo_doenca());
                    enderecoTv.setText(response.body().getEndereco());
                    descricaoTv.setText(response.body().getDescricao());
                }
            }

            @Override
            public void onFailure(Call<Disease> call, Throwable t) {

            }
        });
    }
}
