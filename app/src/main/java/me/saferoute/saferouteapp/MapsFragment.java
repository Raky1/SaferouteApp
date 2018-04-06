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
import android.view.WindowManager;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback,
    GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapClickListener {

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

    private int mode = 0;


    private AutoCompleteTextView txtSearch;
    private ImageView btnGps;

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


    }

    //verifica se google api falha
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("INFO", "onConnectionFailed: falha");
    }

    //------click map
    @Override
    public void onMapClick(LatLng latLng) {
        Log.d("INFO", "click on map");
        if(mode==1) {
            MainActivity activity = (MainActivity) getActivity();
            activity.showROcorrencia(latLng.latitude, latLng.longitude);
            setMode(0);
        } else if (mode==2) {
            MainActivity activity = (MainActivity) getActivity();
            activity.showEditOcorrencia(latLng.latitude, latLng.longitude);
            setMode(0);
        }
    }

    //-----------------------------------------------move camera
    private void moveCamera(LatLng latLng, float zoom) {
        Log.d("INFO", "moveCamera: moving the camera to : lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        //txtSearch.clearFocus();

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

    //-----------------------------------------------get device location
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
                    Toast.makeText(this.getContext(),"Ative o GPS", Toast.LENGTH_SHORT);
            } else
                getLocationPermission();


        } catch(SecurityException e) {
            Log.e("INFO", "getDeviceLocation: Exception: " + e.getMessage());
        }
    }

    //-------------------------------0=show Ocorrencia / 1=get Coordenadas--------
    private void changeMode() {
        if(mode == 0) {

        } else if(mode == 1) {
            //limpa marcadores no mapa
            //esconde scroll

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

    //----------------------------------------------get requests permissions
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

    //-----------------------------esconde teclado... era pelo menos...
    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager)this.getContext().getSystemService(this.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);
    }

    //-------------------------------------------propriedades
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
}
