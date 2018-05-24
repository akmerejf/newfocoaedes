package com.example.supanonymous.focoaedes.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supanonymous.focoaedes.DetalheDenunciaDoencaActivity;
import com.example.supanonymous.focoaedes.DetalheDenunciaFocoActivity;
import com.example.supanonymous.focoaedes.R;
import com.example.supanonymous.focoaedes.models.Disease;
import com.example.supanonymous.focoaedes.models.Imagem;
import com.example.supanonymous.focoaedes.models.Ocorrencia;
import com.example.supanonymous.focoaedes.services.ApiService;
import com.example.supanonymous.focoaedes.utils.ServiceGenerator;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityOcorrenciaRegistroDoenca extends AppCompatActivity {
    private Spinner spinner;
    EditText endereco, bairro, descricao;
    Button denunciar;
    ImageView pegar_localizacao, foto;
    TextView btnCancelar;
    FusedLocationProviderClient mFusedLocationClient;
    LatLng currentLatLng;
    String tipo_doenca;
    private ApiService apiService;
    private ProgressDialog progress;

    @Override
    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        setContentView(R.layout.fragment_ocorrencia_registro_doenca);

        spinner = findViewById(R.id.nova_tipo);
        endereco = findViewById(R.id.nova_endereco);
        bairro = findViewById(R.id.nova_bairro);
        pegar_localizacao = findViewById(R.id.atual_localizacao);
        descricao = findViewById(R.id.nova_descricao);
        denunciar = findViewById(R.id.btn_nova_salvar_doenca);
        btnCancelar = findViewById(R.id.cancelarDoenca);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipo_valores, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //---------Listeners------------------

        //localizacao pelo btn
        pegar_localizacao.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
//
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ActivityOcorrenciaRegistroDoenca.this);
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                                Geocoder geocoder;
                                List<Address> addresses;
                                geocoder = new Geocoder(ActivityOcorrenciaRegistroDoenca.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                    String address = addresses.get(0).getThoroughfare() + "," + addresses.get(0).getFeatureName(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                    endereco.setText(address);
                                    bairro.setText(addresses.get(0).getSubLocality());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }
                        });
            }
        });

        // btn voltar
        btnCancelar.setOnClickListener(v -> {

            //escoder teclado
            InputMethodManager imm = (InputMethodManager) ActivityOcorrenciaRegistroDoenca.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            getFragmentManager().popBackStack();
        });

        //salvar denuncia
        denunciar.setOnClickListener(v -> {
            if (TextUtils.isEmpty(endereco.getText()) && TextUtils.isEmpty(bairro.getText()) && currentLatLng == null && TextUtils.isEmpty(descricao.getText())) {

                Toast.makeText(ActivityOcorrenciaRegistroDoenca.this, "Preencha todos os campo.", Toast.LENGTH_SHORT).show();

            }else {
                createIncidentReport();
            }
        });
    }


    private void createIncidentReport() {

        Disease doenca = new Disease(
                descricao.getText().toString(),
                endereco.getText().toString(),
                bairro.getText().toString(),
                String.valueOf(currentLatLng.latitude),
                String.valueOf(currentLatLng.longitude),
                spinner.getSelectedItem().toString());

        apiService = ServiceGenerator.createService(ApiService.class);
        apiService.createDoenca(doenca).enqueue(new Callback<Disease>() {
            @Override
            public void onResponse(Call<Disease> call, Response<Disease> response) {
                if (progress!=null)
                    progress.dismiss();

                if (!response.isSuccessful()){
                    Toast.makeText(ActivityOcorrenciaRegistroDoenca.this, "Não foi possivel salvar", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(ActivityOcorrenciaRegistroDoenca.this, DetalheDenunciaDoencaActivity.class);
                    intent.putExtra("doenca_id", response.body().getId());
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<Disease> call, Throwable t) {
                if (progress!=null)
                    progress.dismiss();

                Toast.makeText(ActivityOcorrenciaRegistroDoenca.this, "Não foi possivel salvar", Toast.LENGTH_SHORT).show();
                Log.e("Erro:", t.getMessage());
            }
        });
    }
}
