package com.example.supanonymous.focoaedes.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.supanonymous.focoaedes.OcorrenciaDetails;
import com.example.supanonymous.focoaedes.R;
import com.example.supanonymous.focoaedes.models.Ocorrencia;
import com.example.supanonymous.focoaedes.models.OcorrenciaClusterItem;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by supanonymous on 11/04/18.
 */

public class OcorrenciasMapaFragment extends Fragment implements
        OnMapReadyCallback,
        LocationListener,
        ClusterManager.OnClusterClickListener<OcorrenciaClusterItem>,
        ClusterManager.OnClusterInfoWindowClickListener<OcorrenciaClusterItem>,
        ClusterManager.OnClusterItemClickListener<OcorrenciaClusterItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<OcorrenciaClusterItem> {

    private MapView mapView;
    private GoogleMap map;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private OcorrenciaClusterItem infoWindowOcorrenciaClusterItem;
    private List<Ocorrencia> listaOcorrencia;
    private HashMap<String, OcorrenciaClusterItem> ocorrenciaMarkerIdMap;
    private LatLng userLocation;
    private List<OcorrenciaClusterItem> listaClusterItems;
    private ClusterManager<OcorrenciaClusterItem> mClusterManager;
    private OcorrenciaClusterItem clickedClusterItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ocorrencias_mapa, container, false);
        mapView = v.findViewById(R.id.ocorrencias_mapa_mapview);
        //Inicia a lista de ocorrencias
        listaOcorrencia = new ArrayList<>();
        // Gets the MapView from the XML layout and creates it
        mapView.onCreate(savedInstanceState);
        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this);
        // Updates the location and zoom of the MapView

        return v;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);

        map.setMyLocationEnabled(true);
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
        MapsInitializer.initialize(this.getActivity());
        //configurações do botões nativos da google
        map.getUiSettings().setMapToolbarEnabled(false);

        try {
            geoLocaliza();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void geoLocaliza() throws IOException {

        //Pega a Tabela do FireBase
//        ocorrenciasRef = FirebaseDatabase.getInstance ().getReference ().child ( "ocorrencias" );

        // Read from the database
//        ocorrenciasRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            private MarkerOptions options;
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                options = new MarkerOptions();
//                LatLng latLng;
//                mClusterManager = new ClusterManager<>(getContext(), map);
//                listaClusterItems = new ArrayList<>();
//                ocorrenciaMarkerIdMap = new HashMap<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Ocorrencia ocorrencia = snapshot.getValue(Ocorrencia.class);
//                    listaOcorrencia.add(ocorrencia);
//                    double lat = Double.valueOf(ocorrencia.getLatitude()) ;
//                    double lng = Double.valueOf(ocorrencia.getLongitude());
//                    OcorrenciaClusterItem clusterItem = new OcorrenciaClusterItem(lat, lng, ocorrencia.getEndereco(), ocorrencia.getComentario(), ocorrencia.getUrlImagem(), snapshot.getKey());
//                    ocorrenciaMarkerIdMap.put(snapshot.getValue().toString(), clusterItem);
//                    listaClusterItems.add(clusterItem);
//                }
//
//                //Configura o Cluster
//                mapView.onResume();
//                setUpClusterer();
//                addClusterItems();
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("FBase", "Failed to read value.", error.toException());
//            }
//        });

    }

    private void addClusterItems(){
        //Constrói o cluster de ocorrências no mapa

        mClusterManager.addItems(listaClusterItems);
        mClusterManager.cluster();
    }


    private void setUpClusterer() {

        mClusterManager = new ClusterManager<>(getActivity(), map);

        mClusterManager.setRenderer(new MyClusterRenderer(getActivity(), map,
                mClusterManager));
        map.setOnCameraIdleListener(mClusterManager);


        map.setOnInfoWindowClickListener(mClusterManager);
        map.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(
                new MyCustomAdapterForClusters());
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(
                new MyCustomAdapterForItems());

        map.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        mClusterManager
                .setOnClusterClickListener(cluster -> false);

        mClusterManager
                .setOnClusterItemClickListener(item -> {

                    return false;
                });
        mClusterManager.setOnClusterItemInfoWindowClickListener(ocorrenciaClusterItem -> {
            clickedClusterItem = ocorrenciaClusterItem;
            Intent intent = new Intent(getContext(), OcorrenciaDetails.class);
            intent.putExtra("ocorrencia_id", ocorrenciaClusterItem.getmId());
            getActivity().startActivity(intent);
        });

    }

    public class MyCustomAdapterForItems implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyCustomAdapterForItems() {
            myContentsView = getLayoutInflater().inflate(
                    R.layout.marker_layout, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            infoWindowOcorrenciaClusterItem = ocorrenciaMarkerIdMap.get(marker.getId());
            TextView markertitulo =  myContentsView.findViewById(R.id.marker_titulo);
            ImageView markerimagem = myContentsView.findViewById(R.id.marker_foto);
            markertitulo.setText(marker.getTitle());
            Glide.with(getContext()).
                    load(infoWindowOcorrenciaClusterItem.getUrlImagem()).
                    apply(new RequestOptions().override(180, 120).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)).
                    into(markerimagem);

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }
    }
    // class for Main Clusters.

    public class MyCustomAdapterForClusters implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyCustomAdapterForClusters() {
            myContentsView = null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub


            return myContentsView;
        }
    }


    @Override
    public void onClusterItemInfoWindowClick(OcorrenciaClusterItem item) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onClusterItemClick(OcorrenciaClusterItem item) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<OcorrenciaClusterItem> cluster) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onClusterClick(Cluster<OcorrenciaClusterItem> cluster) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLocation, 15);
        map.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class MyClusterRenderer extends DefaultClusterRenderer<OcorrenciaClusterItem> {

        public MyClusterRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
            super(context, map, clusterManager);
            ocorrenciaMarkerIdMap = new HashMap<>();
        }

        @Override
        protected void onBeforeClusterItemRendered(OcorrenciaClusterItem item, MarkerOptions markerOptions) {
            super.onBeforeClusterItemRendered(item, markerOptions);

            markerOptions.title(item.getTitle());
            markerOptions.snippet(item.getSnippet());


        }

        @Override
        protected void onClusterItemRendered(OcorrenciaClusterItem clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
            ocorrenciaMarkerIdMap.put(marker.getId(), clusterItem);
            //here you have access to the marker itself
        }

    }
}

