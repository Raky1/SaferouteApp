package me.saferoute.saferouteapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.saferoute.saferouteapp.DAO.AsyncResponse;
import me.saferoute.saferouteapp.DAO.RequestData;
import me.saferoute.saferouteapp.Model.Ocorrencia;
import me.saferoute.saferouteapp.Tools.CacheData;
import me.saferoute.saferouteapp.Tools.CustomClusterRender;
import me.saferoute.saferouteapp.Tools.PlaceAutoCompleteAdapter;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback,
    GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapClickListener, AsyncResponse {

    //modos
    public static final int MODE_NORMAL_VIEW = 0;
    public static final int MODE_CLICK_VIEW = 1;
    public static final int MODE_CLICK_EDIT_VIEW = 2;
    public static final int MODE_THERMAL_VIEW = 3;
    private int mode = 0;

    private static final String URL_OCORRENCIAS = "http://saferoute.me/php/mExecutor/mExecOcorrencia.php";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 18f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-85, -180),
            new LatLng(85, 180));


    private GoogleMap mMap;
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationManager mLocationManager;
    private PlaceAutoCompleteAdapter mPlaceAutoCompleteAdapter;
    private GeoDataClient mGeoDataClient;

    private RequestData requestData;
    private ClusterManager<Ocorrencia> mClusterManager;

    private AutoCompleteTextView txtSearch;
    private ImageView btnGps;

    //Ocorrencias e controle de filtro
    private List<Ocorrencia> ocorrencias;
    protected boolean[] filtro = {false, false, false, false, false, false, false, false, false};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getMapAsync(this);
    }

    //-----------------------------------------mapa carregado
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("INFO", "onMapReady: OK");

        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        //permissão
        getLocationPermission();

        // auto complete do search
        mGeoDataClient = Places.getGeoDataClient(this.getContext(), null);
        mPlaceAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this.getContext(),
                mGeoDataClient, LAT_LNG_BOUNDS, null);

        try {
            txtSearch.setAdapter(mPlaceAutoCompleteAdapter);
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }

        //ativando o botão de localização e se localizando....
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            //permissão obrigatório
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        //gerenciador de

        //Cluster manager
        mClusterManager = new ClusterManager<Ocorrencia>(getContext(), mMap);

        mClusterManager.setRenderer(new CustomClusterRender(getContext(), mMap, mClusterManager));
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        updateMarkers();

        loadtLastLocation();
    }

    /**
     * Eventos do mapa... click, movimento ativo da camera, geo localização do editText
     */
    @Override
    public void onMapClick(LatLng latLng) {
        Log.d("INFO", "click on map");
        if(mode==MODE_CLICK_VIEW) {
            MainActivity activity = (MainActivity) getActivity();
            activity.showROcorrencia(latLng.latitude, latLng.longitude);
            setMode(0);
        } else if (mode==MODE_CLICK_EDIT_VIEW) {
            MainActivity activity = (MainActivity) getActivity();
            activity.showEditOcorrencia(latLng.latitude, latLng.longitude);
            setMode(0);
        }
    }


    //-----------------------------------------------move camera
    private void moveCamera(LatLng latLng, float zoom) {
        Log.d("INFO", "moveCamera: moving the camera to : lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        hideSoftKeyboard();
    }

    //----------------------------------------------localização geografica
    public void geoLocate() {
        Log.d("INFO", "geoLocate: geoLocating");
        String searchString = txtSearch.getText().toString();

        Geocoder geocoder = new Geocoder(this.getContext());
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e("INFO", "IOException: " + e.getMessage());
        }

        if(list.size() > 0) {
            Address address = list.get(0);
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), 10);
        }
    }

    /**
     * Localização do dispositivo.... coleta e requisição de permissão
     */
    public void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getContext());

        mLocationManager = (LocationManager) this.getActivity().getSystemService(this.getContext().LOCATION_SERVICE);

        try {
            if(mLocationPermissionGranted) {
                if(mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER)) {
                    final Task location = mFusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Log.d("INFO", "onComplete: found location");
                                Location currentLocation = (Location) task.getResult();
                                if(currentLocation != null) // se não estiver procurando ainda
                                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            } else {
                                Log.d("INFO", "onComplete: found not location");
                            }
                        }
                    });
                } else
                    Toast.makeText(this.getContext(),"Ative o GPS para melhor experiencia", Toast.LENGTH_LONG).show();
            } else
                getLocationPermission();


        } catch(SecurityException e) {
            Log.e("INFO", "getDeviceLocation: Exception: " + e.getMessage());
        }
    }


    //----------------------------------------------gerenciamento de permissao de localização
    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                mLocationPermissionGranted = true;
            else
                ActivityCompat.requestPermissions(this.getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);

        } else
            ActivityCompat.requestPermissions(this.getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
    }

    /**
     * adiciona e atualiza markadores
     */
    public void addMarker(Ocorrencia ocorrencia) {
        ocorrencias.add(ocorrencia);
        showMarkers();
    }

    public void updateMarkers() {
        requestData = new RequestData();
        requestData.delegate = this;

        String parametros = "action=getAll";

        requestData.execute(URL_OCORRENCIAS, parametros);
    }

    public void showMarkers() {
        mClusterManager.clearItems();
        if(!filtro[0] && !filtro[1] && !filtro[2] && !filtro[3] && !filtro[4] && !filtro[5] && !filtro[6] && !filtro[7] && !filtro[8]) {
            mClusterManager.addItems(ocorrencias);
        } else {
            for(Ocorrencia o : ocorrencias) {

                if ((filtro[0] && o.isDinheiro()) ||
                        (filtro[1] && o.isCelular()) ||
                        (filtro[2] && o.isVeiculo()) ||
                        (filtro[3] && o.isCartao()) ||
                        (filtro[4] && o.isCarteira()) ||
                        (filtro[5] && o.isBolsa()) ||
                        (filtro[6] && o.isBicicleta()) ||
                        (filtro[7] && o.isDocumentos()) ||
                        (filtro[8] && o.isOutros()))
                    mClusterManager.addItem(o);
            }
        }

        mClusterManager.cluster();
    }

    @Override
    public void processFinish(String result) {
        Log.d("INFO", result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            this.ocorrencias = new ArrayList<Ocorrencia>();

            if (jsonObject.getBoolean("resultado")) {
                JSONArray jsonOcorrencias = jsonObject.getJSONArray("ocorrencias");

                for (int i = 0; i < jsonOcorrencias.length(); i++) {
                    JSONObject jsonOcor = jsonOcorrencias.getJSONObject(i);
                    Ocorrencia ocor = new Ocorrencia();

                    ocor.setPartialFromJSON(jsonOcor);

                    this.ocorrencias.add(ocor);
                    showMarkers();
                }
            } else {
                if (jsonObject.getString("erro").contains("error: nada encontrado")) {
                    Log.d("ERROR", "naddaaaaaaaaaa");
                }
                Log.d("ERROR", jsonObject.getString("erro"));
            }
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
    }


    /**
     * resultado da coleta de permissões
    **/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++)
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                            return;
                    mLocationPermissionGranted = true;
                }
            }
        }
    }






    /**##########################################################################Outros##########################################################################**/

    //-------------------------------0=show Ocorrencia / 1=get Coordenadas--------
    private void changeMode() {
        if(mode == MODE_NORMAL_VIEW) {

        } else if(mode == MODE_CLICK_VIEW) {
            //limpa marcadores no mapa
            //esconde scroll
        } else if(mode == MODE_THERMAL_VIEW) {

        }
    }

    public void setMode(int mode) {
        this.mode = mode;
        changeMode();
    }


    public void setTxtSearch(final AutoCompleteTextView txtSearch) {
        this.txtSearch = txtSearch;
        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if (actionID == EditorInfo.IME_ACTION_SEARCH
                        || actionID == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    //procura localização
                    geoLocate();
                }

                return false;
            }
        });
    }

    public void setBtnGps(ImageView btnGps) {
        this.btnGps = btnGps;
        this.btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
            }
        });
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager)this.getContext().getSystemService(this.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);
    }

    public void saveLastLocation() {
        CacheData.SaveLastLocation(mMap.getCameraPosition().target, getContext());
        CacheData.SaveLastZoom(mMap.getCameraPosition().zoom, getContext());
    }

    public void loadtLastLocation() {
        LatLng latLng;
        if((latLng = CacheData.GetLastLocation(getContext())) != null) {
            float zoom;
            if((zoom = CacheData.GetLastZoom(getContext())) == 0)
                moveCamera(latLng, DEFAULT_ZOOM);
            else
                moveCamera(latLng, zoom);
        }
    }

    /**
     * Ações da activiti ou sistema
     */
    //verifica se google api falha
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("INFO", "onConnectionFailed: falha");
    }

}
