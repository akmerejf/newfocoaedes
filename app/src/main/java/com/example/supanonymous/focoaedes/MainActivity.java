package com.example.supanonymous.focoaedes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.supanonymous.focoaedes.fragments.OcorrenciasListaFragment;
import com.example.supanonymous.focoaedes.fragments.OcorrenciasMapaFragment;
import com.example.supanonymous.focoaedes.fragments.OcorrenciasNovoRegistroFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    //Listener do navigation menu
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        switch (item.getItemId()) {
            case R.id.navigation_mapas:
                switchFragment("ocorrencias_mapa", new OcorrenciasMapaFragment());
                return true;
            case R.id.navigation_novo_registro:
                switchFragment("ocorrencias_novo_registro", new OcorrenciasNovoRegistroFragment());
                return true;
            case R.id.navigation_lista_ocorrencias:
                switchFragment("ocorrencias_lista", new OcorrenciasListaFragment());
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
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {

                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.ocorrencias_tabs, new OcorrenciasMapaFragment(), "ocorrencias_mapa").commit();

                    }
                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        /* ... */
                    }
                }).check();
    }

}
