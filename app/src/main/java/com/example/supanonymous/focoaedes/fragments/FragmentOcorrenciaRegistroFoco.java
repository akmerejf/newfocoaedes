package com.example.supanonymous.focoaedes.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.supanonymous.focoaedes.MainActivity;
import com.example.supanonymous.focoaedes.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FragmentOcorrenciaRegistroFoco extends Fragment {

    EditText titulo, endereco, bairro, descricao;
    Button denunciar;
    ImageView pegar_localizacao, foto;
    private final int CAMERA_REQUEST = 1888;
    final int   CROP_PIC = 2;
    private Uri pic_uri;
    private byte[] imagem;
    private String encodedImage;
    private View v;
    String data = "";
    SimpleDateFormat formato;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_ocorrencia_registro_foco, container, false);


        titulo = (EditText) v.findViewById(R.id.nova_titulo);
        endereco = (EditText) v.findViewById(R.id.nova_endereco);
        bairro = (EditText) v.findViewById(R.id.nova_bairro);
        pegar_localizacao = (ImageView) v.findViewById(R.id.atual_localizacao);
        foto =  v.findViewById(R.id.nota_foto);
        descricao = (EditText) v.findViewById(R.id.nova_descricao);
        denunciar = (Button) v.findViewById(R.id.btn_nova_salvar_doenca);
        formato = new SimpleDateFormat("dd/MM/yyyy");
        data = formato.format(new Date());


        pegar_localizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Geocoder geocoder = new Geocoder(getActivity());
                List<Address> addresses;

//                addresses = geocoder.getFromLocation(
//                        ((MainActivity) getActivity()).localizacao.getLatitude(),
//                        ((MainActivity) getActivity()).localizacao.getLongetude(),
//                        1
//                );
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


        return v;
    }

    //-------------------metodos pra camera----------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //se a camera for ativado
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap photo = extras.getParcelable("data");

            foto.setImageBitmap(photo);

            converterImagem(photo);
        }
        //se o cortador de foto for ativado
        else if (requestCode == CROP_PIC) {

            Bundle extras = data.getExtras();
            Bitmap photo = extras.getParcelable("data");

            foto.setImageBitmap(photo);
        }
    }


    void converterImagem(Bitmap photo){

        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        pic_uri = Uri.fromFile(file);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        imagem = baos.toByteArray();
        encodedImage = Base64.encodeToString(imagem, Base64.DEFAULT);
    }
}
