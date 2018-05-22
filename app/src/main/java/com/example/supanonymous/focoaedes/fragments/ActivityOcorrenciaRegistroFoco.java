package com.example.supanonymous.focoaedes.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentOcorrenciaRegistroFoco extends Fragment {

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_ocorrencia_registro_foco, container, false);


        titulo = (EditText) v.findViewById(R.id.nova_titulo);
        endereco = (EditText) v.findViewById(R.id.nova_endereco);
        bairro = (EditText) v.findViewById(R.id.nova_bairro);
        cancelar = v.findViewById(R.id.cancelar);
        pegar_localizacao = (ImageView) v.findViewById(R.id.atual_localizacao);
        foto = v.findViewById(R.id.nota_foto);
        descricao = (EditText) v.findViewById(R.id.nova_descricao);
        denunciar = (Button) v.findViewById(R.id.btn_nova_salvar_doenca);
        formato = new SimpleDateFormat("dd/MM/yyyy");
        data = formato.format(new Date());


        pegar_localizacao.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
//
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                                Geocoder geocoder;
                                List<Address> addresses;
                                geocoder = new Geocoder(getActivity(), Locale.getDefault());

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

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                }
            }
        });


        cancelar.setOnClickListener(v -> {

            //escoder teclado
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            getFragmentManager().popBackStack();
        });

        denunciar.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(endereco.getText()) && !TextUtils.isEmpty(bairro.getText()) && currentLatLng != null && !TextUtils.isEmpty(titulo.getText()) && !TextUtils.isEmpty(descricao.getText()) && pic_uri != null) {

                salvarStorage();

            }else {
                Toast.makeText(getContext(), "Preencha todos os campo.", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    //-------------------metodos pra camera----------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //se a camera for ativado
        Bitmap photo = null;
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Bundle extras = data.getExtras();
            photo = extras.getParcelable("data");

            foto.setImageBitmap(photo);


        }
        //se o cortador de foto for ativado
        else if (requestCode == CROP_PIC) {

            Bundle extras = data.getExtras();
            photo = extras.getParcelable("data");

            foto.setImageBitmap(photo);

        }
        Log.e("URI", data.getData().toString());
        pic_uri = data.getData();
    }


    void converterImagem(Uri photo) {

        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        pic_uri = Uri.fromFile(file);

//        salvarStorage();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
//        imagem = baos.toByteArray();
//        encodedImage = Base64.encodeToString(imagem, Base64.DEFAULT);
    }

    private void salvarStorage() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference itemImageRef = storageRef.child("ocorrencia/images/" + pic_uri.getLastPathSegment());
        UploadTask uploadTask = itemImageRef.putFile(pic_uri);
        // UserCredentials observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getContext(), "Falha ao enviar um dos arquivos, operação cancelada", Toast.LENGTH_SHORT).show();

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
                if (!response.isSuccessful()){
                    Toast.makeText(getContext(), "Não foi possivel salvar", Toast.LENGTH_SHORT).show();
                    return;
                }

//                Intent intent = new Intent(getContext(), DetalheDenunciaDoencaActivity.class);
//                intent.
                getActivity().finish();
            }

            @Override
            public void onFailure(Call<Ocorrencia> call, Throwable t) {
                Toast.makeText(getContext(), "Não foi possivel salvar", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
