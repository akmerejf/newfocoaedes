package com.example.supanonymous.focoaedes.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supanonymous.focoaedes.DetalheDenunciaDoencaActivity;
import com.example.supanonymous.focoaedes.DetalheDenunciaFocoActivity;
import com.example.supanonymous.focoaedes.MainActivity;
import com.example.supanonymous.focoaedes.R;
import com.example.supanonymous.focoaedes.models.Imagem;
import com.example.supanonymous.focoaedes.models.Localizacao;
import com.example.supanonymous.focoaedes.models.Ocorrencia;
import com.example.supanonymous.focoaedes.services.ApiService;
import com.example.supanonymous.focoaedes.utils.ServiceGenerator;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityOcorrenciaRegistroFoco extends AppCompatActivity implements IPickResult, IPickCancel {

    EditText titulo, endereco, bairro, descricao;
    Button denunciar;
    ImageView pegar_localizacao, foto;
    TextView cancelar;
    private final int CAMERA_REQUEST = 1888;
    final int CROP_PIC = 2;
    private Uri pic_uri;
    private byte[] imagem;
    private String encodedImage;
    private View v;
    String data = "";
    LatLng userLocation;
    SimpleDateFormat formato;
    FusedLocationProviderClient mFusedLocationClient;
    LatLng currentLatLng;
    private ApiService apiService;
    private Uri downloadUrl;
    private Bitmap bitmapImage;
    private ProgressDialog progress;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    // Inflate the layout for this fragment
        setContentView(R.layout.fragment_ocorrencia_registro_foco);

        titulo = findViewById(R.id.nova_titulo);
        endereco = findViewById(R.id.nova_endereco);
        bairro = findViewById(R.id.nova_bairro);
        cancelar = findViewById(R.id.cancelar);
        pegar_localizacao = findViewById(R.id.atual_localizacao);
        foto = findViewById(R.id.nota_foto);
        descricao = findViewById(R.id.nova_descricao);
        denunciar = findViewById(R.id.btn_nova_salvar_doenca);
        formato = new SimpleDateFormat("dd/MM/yyyy");
        data = formato.format(new Date());


        pegar_localizacao.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
//
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ActivityOcorrenciaRegistroFoco.this);
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                                Geocoder geocoder;
                                List<Address> addresses;
                                geocoder = new Geocoder(ActivityOcorrenciaRegistroFoco.this, Locale.getDefault());

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

        foto.setOnClickListener(view -> {
            PickSetup setup = new PickSetup()
                    .setTitle("Escolha")
                    .setProgressText("Aguarde...")
                    .setFlip(true)
                    .setMaxSize(640)
                    .setPickTypes(EPickType.GALLERY, EPickType.CAMERA)
                    .setIconGravity(Gravity.LEFT)
                    .setButtonOrientation(LinearLayoutCompat.VERTICAL)
                    .setSystemDialog(false);
            PickImageDialog.build(setup).setOnPickCancel(this).show(this); // Seleciona a forma de buscar a imagem

        });

        cancelar.setOnClickListener(v -> {

            //escoder teclado
            InputMethodManager imm = (InputMethodManager) ActivityOcorrenciaRegistroFoco.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            getFragmentManager().popBackStack();
        });

        denunciar.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(endereco.getText()) && !TextUtils.isEmpty(bairro.getText()) && currentLatLng != null && !TextUtils.isEmpty(titulo.getText()) && !TextUtils.isEmpty(descricao.getText()) && pic_uri != null) {

                salvarStorage();

            }else {
                Toast.makeText(ActivityOcorrenciaRegistroFoco.this, "Preencha todos os campo.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //-------------------metodos pra camera----------------

    private void salvarStorage() {
        showEnviando();
        FirebaseAuth.getInstance().getCurrentUser();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference itemImageRef = storageRef.child("ocorrencia/images/" + pic_uri.getLastPathSegment());
        UploadTask uploadTask = itemImageRef.putFile(pic_uri);
        // UserCredentials observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(ActivityOcorrenciaRegistroFoco.this, "Falha ao enviar um dos arquivos, operação cancelada", Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(taskSnapshot -> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            downloadUrl = taskSnapshot.getDownloadUrl();
            //Envia o novo item pra api com suas respectivas urls das imagens
            if (downloadUrl != null) {
                Imagem imagem = new Imagem();
                List<Imagem> imagems = new ArrayList<>();
                imagem.setUrl(downloadUrl.toString());
                imagems.add(imagem);
                createIncidentReport(imagems);
            }

        });
    }

    private void showEnviando() {
        progress = new ProgressDialog(ActivityOcorrenciaRegistroFoco.this);
        //Configurações do Dialog
        progress.setMessage("Enviando.");
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }



    private void createIncidentReport(List<Imagem> imagems) {
        Ocorrencia ocorrencia = new Ocorrencia(descricao.getText().toString(),
                endereco.getText().toString(),
                bairro.getText().toString(),
                imagems,
                String.valueOf(currentLatLng.latitude),
                String.valueOf(currentLatLng.longitude),
                titulo.getText().toString());

        apiService = ServiceGenerator.createService(ApiService.class);
        apiService.createOcorrencia(ocorrencia).enqueue(new Callback<Ocorrencia>() {
            @Override
            public void onResponse(Call<Ocorrencia> call, Response<Ocorrencia> response) {
                if (progress!=null)
                    progress.dismiss();

                if (!response.isSuccessful()){
                    Toast.makeText(ActivityOcorrenciaRegistroFoco.this, "Não foi possivel salvar", Toast.LENGTH_SHORT).show();

                }else {
                    Intent intent = new Intent(ActivityOcorrenciaRegistroFoco.this, DetalheDenunciaFocoActivity.class);
                    intent.putExtra("ocorrencia_id", response.body().getId());
                    startActivity(intent);
                finish();
                }

            }

            @Override
            public void onFailure(Call<Ocorrencia> call, Throwable t) {
                if (progress!=null)
                    progress.dismiss();

                Toast.makeText(ActivityOcorrenciaRegistroFoco.this, "Não foi possivel salvar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCancelClick() {

    }

    @Override
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError() == null) {
            try {
                String realPath = getRealPathFromURI(this, pickResult.getUri());
                if (!TextUtils.isEmpty(realPath)){
                    File compressedImageFile = new Compressor(this)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(new File(realPath));
                    pic_uri = Uri.fromFile(compressedImageFile);
                    //manda a imagem para o adapter
                    bitmapImage = BitmapFactory.decodeFile(compressedImageFile.getPath());
//                //Adiciona a imagem para upload no FirebaseStorage
                    foto.setImageBitmap(bitmapImage);
                }else {
                    pic_uri = pickResult.getUri();
                    foto.setImageBitmap(pickResult.getBitmap());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, pickResult.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e("REALPATH", "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
