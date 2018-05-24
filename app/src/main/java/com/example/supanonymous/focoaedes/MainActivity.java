package com.example.supanonymous.focoaedes;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.animation.Animation;

import com.example.supanonymous.focoaedes.fragments.DoencasOcorrenciasFragment;
import com.example.supanonymous.focoaedes.fragments.InformacoesFragment;
import com.example.supanonymous.focoaedes.fragments.ListaFragment;
import com.example.supanonymous.focoaedes.fragments.OcorrenciasMapaFragment;
import com.example.supanonymous.focoaedes.fragments.OcorrenciasNovoRegistroFragment;
import com.example.supanonymous.focoaedes.models.Localizacao;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity  {

    private FloatingActionButton fab_faq, fab_perfil, fab_menu;
    private Animation fab_open, fab_close, fab_rotate, fab_back_rotate;
    private boolean aberto = false;
    private Typeface fontFace = null;
    public Localizacao localizacao;

    private FragmentManager fragmentManager;
    //Listener do navigation menu
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = (MenuItem item) -> {

        switch (item.getItemId()) {
            case R.id.navigation_mapas:
                switchFragment("ocorrencias_mapa", new OcorrenciasMapaFragment());
                return true;
            case R.id.navigation_novo_registro:
                switchFragment("ocorrencias_novo_registro", new OcorrenciasNovoRegistroFragment());
                return true;
            case R.id.navigation_lista_ocorrencias:
                switchFragment("tabed_fragment", new ListaFragment());
                return true;
            case R.id.navigation_configuracoes:
                switchFragment("informacoesFragment", new InformacoesFragment());
                return true;
        }
        return false;
    };


    //Troca pro fragment selecionado no menu de navegação
    private void switchFragment(String tag, Fragment fragment) {

        FragmentTransaction transaction = MainActivity.this.fragmentManager.beginTransaction();
        transaction.addToBackStack(tag);
        //confirmar transição
        transaction.replace(R.id.ocorrencias_tabs, fragment, tag).commit();
    }

    // Mesma coisa que BottomNavigationView navigation = findViewById(...)
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Configura todas as views com seus respectivos binds ex: navigation = findViewById(...)
        ButterKnife.bind(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Pede as permissões do usuário para acessar a localização
        Dexter.withActivity(this)
                .withPermissions(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.ocorrencias_tabs, new OcorrenciasMapaFragment(), "ocorrencias_mapa").commit();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        /* ... */
                    }
                }).check();

//        //--------Botões de menu
//        fab_faq = (FloatingActionButton) findViewById(R.id.fab_faq);
//        fab_perfil = (FloatingActionButton) findViewById(R.id.fab_perfil);
//        fab_menu = (FloatingActionButton) findViewById(R.id.fab_menu);
//
//        //--------Animação do menu
//
//        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_menu_open);
//        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_menu_close);
//        fab_rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_menu_rotate);
//        fab_back_rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_menu_back_rotate);
//
//        //-------Fab rotações
//
//        fab_menu.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                if (aberto) {
//                    fab_menu.startAnimation(fab_back_rotate);
//                    fab_perfil.startAnimation(fab_close);
//                    fab_perfil.setClickable(false);
//                    fab_faq.startAnimation(fab_close);
//                    fab_faq.setClickable(false);
//                    aberto = false;
//
//                } else {
//
//                    fab_menu.startAnimation(fab_rotate);
//                    fab_perfil.startAnimation(fab_open);
//                    fab_perfil.setClickable(true);
//                    fab_faq.startAnimation(fab_open);
//                    fab_faq.setClickable(true);
//                    aberto = true;
//                }
//            }
//        });
//
    }
}
