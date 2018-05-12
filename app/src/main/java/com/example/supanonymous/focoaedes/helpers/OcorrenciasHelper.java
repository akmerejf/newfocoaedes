package com.example.supanonymous.focoaedes.helpers;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supanonymous.focoaedes.OcorrenciaDetails;
import com.example.supanonymous.focoaedes.R;
import com.example.supanonymous.focoaedes.adapters.OcorrenciasAdapter;
import com.example.supanonymous.focoaedes.models.Ocorrencia;
import com.example.supanonymous.focoaedes.services.ApiService;
import com.example.supanonymous.focoaedes.utils.ServiceGenerator;
import com.example.supanonymous.focoaedes.views.HorizontalDottedProgress;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OcorrenciasHelper {

    private final Context context;
    private final AccountManager mAccountManager;
    private ApiService apiService;
    private String authToken;
    private Intent intent;
    private int totalPages;
    private int nextPage;

    public OcorrenciasHelper(Context context) {
        this.context = context;
        mAccountManager = AccountManager.get(context);
    }

    public void userOcorrenciasList(final List<Ocorrencia> itemsList, final RecyclerView.Adapter adapter) {
        Account[] accounts = mAccountManager.getAccountsByType("com.desapego");
        authToken = mAccountManager.peekAuthToken(accounts[0], "full_access");
        apiService = ServiceGenerator.createService(ApiService.class, authToken);
        apiService.getUserOcorrencias().enqueue(new Callback<List<Ocorrencia>>() {
            //metodos de respostas
            @Override
            public void onResponse(Call<List<Ocorrencia>> call, Response<List<Ocorrencia>> response) {

                if (!response.isSuccessful()) {
//                    final Intent res = new Intent(context, LoginActivity.class);
//                    context.startActivity(res);
                    Log.i("LISTA", "Erro: " + "Erro: " + response.code());
                } else {
                    //condição se os dados foram capturados
////                    HorizontalDottedProgress loadingBar = ((Activity) context).findViewById(R.id.loading_bar);
//                    itemsList.addAll(response.body());
//                    //Atualiza o adapter com os items da api
//                    adapter.notifyDataSetChanged();
//                    loadingBar.clearAnimation();
//                    loadingBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Ocorrencia>> call, Throwable t) {
                Toast.makeText(context, "Houve um erro.", Toast.LENGTH_SHORT).show();

                Log.i("LISTA", "Erro: " + t.toString());
            }
        });
    }
    public void ocorrenciasList(final List<Ocorrencia> itemsList, final RecyclerView.Adapter adapter, final HorizontalDottedProgress loadingBar, final TextView noItems) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", String.valueOf(1));
        apiService = ServiceGenerator.createService(ApiService.class);
        apiService.getOcorrencias(queryParams).enqueue(new Callback<List<Ocorrencia>>() {
            //metodos de respostas
            @Override
            public void onResponse(Call<List<Ocorrencia>> call, Response<List<Ocorrencia>> response) {
                loadingBar.clearAnimation();
                loadingBar.setVisibility(View.GONE);
                if (!response.isSuccessful()) {
                    Log.i("LISTA", "Erro: " + "Erro: " + response.code());
                    switch (response.code()){
                        case 404:
                            noItems.setText("Nenhum item encontrado.");
                            noItems.setVisibility(View.VISIBLE);
                            break;
                        default:
                            noItems.setText("Ocorreu um erro, tente novamente.");
                            noItems.setVisibility(View.VISIBLE);
                            break;
                    }
                } else {
                    //condição se os dados foram capturados
                    itemsList.addAll(response.body());
                    if (itemsList.size() > 0){
                        //Atualiza o adapter com os items da api
                        adapter.notifyDataSetChanged();
                    }else{
                        noItems.setText("Nenhum item encontrado.");
                        noItems.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Ocorrencia>> call, Throwable t) {
                loadingBar.clearAnimation();
                loadingBar.setVisibility(View.GONE);
                noItems.setText("Falha ao se conectar com o servidor. Tente novamente");
                noItems.setVisibility(View.VISIBLE);
                Log.i("LISTA", "Erro: " + t.toString());
            }
        });
    }


    public void nextOcorrencias(final List<Ocorrencia> itemsList, final OcorrenciasAdapter adapter) {
        // parametros de busca
        Map<String, String> queryParams = new HashMap<>();
//        queryParams.put("search", "Marcus");
        queryParams.put("page", String.valueOf(nextPage));
        apiService = ServiceGenerator.createService(ApiService.class);
        apiService.getOcorrencias(queryParams).enqueue(new Callback<List<Ocorrencia>>() {
            //metodos de respostas
            @Override
            public void onResponse(Call<List<Ocorrencia>> call, Response<List<Ocorrencia>> response) {
                if (!response.isSuccessful()) {
                    Log.i("LISTA", "Erro: " + "Erro: " + response.code());
                } else { //condição se os dados foram capturados
                    //Salva os headers de paginação
                    totalPages = Integer.parseInt(response.headers().get("X-Total")) / Integer.parseInt(response.headers().get("X-Per-Page"));
                    itemsList.remove(itemsList.size() - 1);
                    adapter.notifyItemRemoved(itemsList.size());
                    if (nextPage > totalPages){
                        adapter.setListEnd();
                    }
                    nextPage = Integer.parseInt(response.headers().get("X-Page")) + 1;
                    itemsList.addAll(response.body());
                    //Atualiza o adapter com os items da api
                    adapter.notifyDataSetChanged();
                    adapter.setLoaded();

                }
            }

            @Override
            public void onFailure(Call<List<Ocorrencia>> call, Throwable t) {
                Toast.makeText(context, "Houve um erro.", Toast.LENGTH_SHORT).show();

                Log.i("LISTA", "Erro: " + t.toString());
            }
        });
    }


    public void showOcorrencia(final Intent intent, String itemId) {
        apiService = ServiceGenerator.createService(ApiService.class);
        apiService.getUserOcorrencia(itemId).enqueue(new Callback<Ocorrencia>() {
            //metodos de respostas
            @Override
            public void onResponse(Call<Ocorrencia> call, Response<Ocorrencia> response) {

                if (!response.isSuccessful()) {
//                    final Intent res = new Intent(context, LoginActivity.class);
//                    context.startActivity(res);
                    Log.i("LISTA", "Erro: " + "Erro: " + response.code());
                } else {
                    intent.putExtra("OCORRENCIA_TIPO", response.body().getTipo());
                    intent.putExtra("OCORRENCIA_DESCRIPTION", response.body().getDescricao());
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Ocorrencia> call, Throwable t) {
                Toast.makeText(context, "Houve um erro.", Toast.LENGTH_SHORT).show();

                Log.i("LISTA", "Erro: " + t.toString());
            }
        });
    }

    public void newOcorrencia(final StorageReference storageRef, final ArrayList<Uri> arrayUri, final Ocorrencia item) {
        final ArrayList<Uri> downloadUrls = new ArrayList<>();
        StorageReference itemImageRef;
        UploadTask uploadTask;
        //Envia as imagens pro firebase
        for (int i = 0; i < arrayUri.size(); i++) {
            itemImageRef = storageRef.child("item/images/" + arrayUri.get(i).getLastPathSegment());
            uploadTask = itemImageRef.putFile(arrayUri.get(i));
            // Register observers to listen for when the download is done or if it fails
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle unsuccessful uploads
//                    Toast.makeText(context, "Falha ao enviar um dos arquivos, operação cancelada", Toast.LENGTH_SHORT).show();
//
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                    downloadUrls.add(taskSnapshot.getDownloadUrl());
//                    //Envia o novo item pra api com suas respectivas urls das imagens
//                    if (downloadUrls.size() > 0) {
//                        Fotinha fotinha;
//                        List<Fotinha> fotinhas = new ArrayList<>();
//                        for (Uri fotinhaUrl :
//                                downloadUrls) {
//                            fotinha = new Fotinha();
//                            fotinha.setUrl(fotinhaUrl.toString());
//                            fotinhas.add(fotinha);
//                        }
//                        item.setFotinhas(fotinhas);
//                        createItem(item);
//                    }
//
//                }
//            });

        }

    }

    private void createOcorrencia(final Ocorrencia item) {
        Account[] accounts;
        accounts = mAccountManager.getAccountsByType("com.desapego");
        authToken = mAccountManager.peekAuthToken(accounts[0], "full_access");
        apiService = ServiceGenerator.createService(ApiService.class, authToken);
        apiService.createOcorrencia(item).enqueue(new Callback<Ocorrencia>() {
            //metodos de respostas
            @Override
            public void onResponse(Call<Ocorrencia> call, Response<Ocorrencia> response) {
                if (!response.isSuccessful()) {
//                    final Intent res = new Intent(context, LoginActivity.class);
//                    context.startActivity(res);
                    Log.i("CREATE_ITEM", "Erro: " + response.code());
                } else {
                    intent = new Intent(context, OcorrenciaDetails.class);
                    intent.putExtra("OCORRENCIA_TIPO", response.body().getTipo());
                    intent.putExtra("OCORRENCIA_DESC", response.body().getDescricao());
//                    intent.putExtra("ITEM_IMAGE_DETAIL", response.body().getFotinhas().get(0).getUrl());
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }

            @Override
            public void onFailure(Call<Ocorrencia> call, Throwable t) {
                Toast.makeText(context, "Houve um erro.", Toast.LENGTH_SHORT).show();

                Log.i("LISTA", "Erro: " + t.toString());
            }
        });
    }
}