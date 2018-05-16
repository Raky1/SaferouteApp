package me.saferoute.saferouteapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import me.saferoute.saferouteapp.Model.Ocorrencia;
import me.saferoute.saferouteapp.Model.Usuario;
import me.saferoute.saferouteapp.Tools.Validacao;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationView.OnCreateContextMenuListener {

    //Login persist
    private Usuario user = null;
    private Ocorrencia ocor4Edit = null;

    private MapsFragment mapsFragment;
    private FragmentManager fragmentManager;

    //scroll e mapbtn
    private MenuItem navLogar;
    private HorizontalScrollView scrollViewFiltro;
    private LinearLayout linearLayout;
    private Button btnConfirmaLoc;
    private ImageView btnGps, btnThermo;
    private AutoCompleteTextView txtSearch;

    //Dialogs
    private AlertDialog dialogDicas, dialogPrivacidade, dialogSobre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        //Botões filtro
        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutScrollFiltro);
        this.configButton();

        //Log.d("Diretório", getCacheDir().getAbsolutePath());
    }

    @Override
    protected void onDestroy() {
        mapsFragment.saveLastLocation();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        mapsFragment.saveLastLocation();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    /**
     * inicialização
     */
    private void init() {

        ImageView btnListDrawer = (ImageView) findViewById(R.id.icon_list_drawer);
        btnListDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                //drawer.setDrawerLockMode(DrawerLayout.SCREEN_STATE_ON);
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        //searchs
        txtSearch = (AutoCompleteTextView) findViewById(R.id.txtSearch);
        btnGps = (ImageView) findViewById(R.id.icon_gps);
        btnThermo = (ImageView) findViewById(R.id.icon_thermo);

        btnThermo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mapsFragment.getMode() != MapsFragment.MODE_THERMAL_VIEW)
                    mapsFragment.setMode(MapsFragment.MODE_THERMAL_VIEW);
                else
                    mapsFragment.setMode(MapsFragment.MODE_NORMAL_VIEW);
            }
        });

        //Barra lateral de navegação...
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navLogar = (MenuItem)navigationView.getMenu().findItem(R.id.nav_logar);

        //set fragment Map
        mapsFragment = new MapsFragment();
        mapsFragment.setTxtSearch(txtSearch);
        mapsFragment.setBtnGps(btnGps);

        //change fragment
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, mapsFragment, "MapsFragment");
        fragmentTransaction.commitAllowingStateLoss();

        //****************************************************************************************************************************
        //Configura Dialogs
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        //dialog Dicas de seguraça
        View view = getLayoutInflater().inflate(R.layout.dialog_dicas, null);
        builder.setView(view);
        dialogDicas = builder.create();

        //dialog Politica de Privacidade
        view = getLayoutInflater().inflate(R.layout.dialog_privacidade, null);
        builder.setView(view);
        dialogPrivacidade = builder.create();

        //dialog Sobre
        view = getLayoutInflater().inflate(R.layout.dialog_sobre, null);
        builder.setView(view);
        dialogSobre = builder.create();

        //****************************************************************************************************************************

    }

    /**configura os botões
     *
     */
    private void configButton() {

        scrollViewFiltro = (HorizontalScrollView) findViewById(R.id.scrollFiltro);
        btnConfirmaLoc = (Button) findViewById(R.id.btnConfirmaLoc);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idFiltro = 0;

                if(view.getId() == R.id.btnFiltroDinheiro)
                    idFiltro = 0;
                else if (view.getId() == R.id.btnFiltroCelular)
                    idFiltro = 1;
                else if (view.getId() == R.id.btnFiltroVeiculo)
                    idFiltro = 2;
                else if (view.getId() == R.id.btnFiltroCartao)
                    idFiltro = 3;
                else if (view.getId() == R.id.btnFiltroCarteiro)
                    idFiltro = 4;
                else if (view.getId() == R.id.btnFiltroBolsa)
                    idFiltro = 5;
                else if (view.getId() == R.id.btnFiltroBicicleta)
                    idFiltro = 6;
                else if (view.getId() == R.id.btnFiltroDocumentos)
                    idFiltro = 7;
                else if (view.getId() == R.id.btnFiltroOutros)
                    idFiltro = 8;

                mapsFragment.filtro[idFiltro] = !mapsFragment.filtro[idFiltro];

                ColorMatrix matrix = new ColorMatrix();

                if(mapsFragment.filtro[idFiltro]) {
                    matrix.setSaturation(1);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                    ((ImageView)view).setColorFilter(filter);
                } else {
                    matrix.setSaturation(0);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                    ((ImageView)view).setColorFilter(filter);
                }

                mapsFragment.showMarkers();
            }
        };

        findViewById(R.id.btnFiltroDinheiro).setOnClickListener(onClickListener);
        findViewById(R.id.btnFiltroCelular).setOnClickListener(onClickListener);
        findViewById(R.id.btnFiltroVeiculo).setOnClickListener(onClickListener);
        findViewById(R.id.btnFiltroCartao).setOnClickListener(onClickListener);
        findViewById(R.id.btnFiltroCarteiro).setOnClickListener(onClickListener);
        findViewById(R.id.btnFiltroBolsa).setOnClickListener(onClickListener);
        findViewById(R.id.btnFiltroBicicleta).setOnClickListener(onClickListener);
        findViewById(R.id.btnFiltroDocumentos).setOnClickListener(onClickListener);
        findViewById(R.id.btnFiltroOutros).setOnClickListener(onClickListener);

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        ((ImageView)findViewById(R.id.btnFiltroDinheiro)).setColorFilter(filter);
        ((ImageView)findViewById(R.id.btnFiltroCelular)).setColorFilter(filter);
        ((ImageView)findViewById(R.id.btnFiltroVeiculo)).setColorFilter(filter);
        ((ImageView)findViewById(R.id.btnFiltroCartao)).setColorFilter(filter);
        ((ImageView)findViewById(R.id.btnFiltroCarteiro)).setColorFilter(filter);
        ((ImageView)findViewById(R.id.btnFiltroBolsa)).setColorFilter(filter);
        ((ImageView)findViewById(R.id.btnFiltroBicicleta)).setColorFilter(filter);
        ((ImageView)findViewById(R.id.btnFiltroDocumentos)).setColorFilter(filter);
        ((ImageView)findViewById(R.id.btnFiltroOutros)).setColorFilter(filter);
    }

    public void setViewScroll(int mode) {
        scrollViewFiltro.setVisibility(mode);
    }

    public void setBtnConfirmLoc(int mode) {
        btnConfirmaLoc.setVisibility(mode);
    }

    public void setEnableBtnConfirmLoc(boolean enable, int mode) {
        btnConfirmaLoc.setEnabled(enable);
        if(enable)
            btnConfirmaLoc.setText("Confirmar Localização");
        else
            btnConfirmaLoc.setText("Selecione a Localização");

        if(mode == 0) {
            btnConfirmaLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mapsFragment.getLocationMarker() != null)
                        showAddOcorrencia(mapsFragment.getLocationMarker().latitude, mapsFragment.getLocationMarker().longitude);
                    mapsFragment.setMode(MapsFragment.MODE_NORMAL_VIEW);
                }
            });
        } else if(mode==1) {
            btnConfirmaLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mapsFragment.getLocationMarker() != null)
                        showEditOcorrencia(mapsFragment.getLocationMarker().latitude, mapsFragment.getLocationMarker().longitude);
                    mapsFragment.setMode(MapsFragment.MODE_NORMAL_VIEW);
                }
            });
        }
    }

    /**
     *  seleção de item da barra deslizante lateral
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_logar) {
            if(item.getTitle().toString() == getResources().getString(R.string.nav_logar))
                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), 901);
            else {
                user = null;
                item.setTitle(getResources().getString(R.string.nav_logar));
            }

        } else if (id == R.id.nav_fui_roubado) {
            if(user != null) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                mapsFragment.setMode(MapsFragment.MODE_CLICK_VIEW);//mode de captura de coordenadas
            } else
                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), 901);

        } else if (id == R.id.nav_suas_ocorrencias) {
            if(user != null) {
                Intent intent = new Intent(MainActivity.this, ListaActivity.class);
                intent.putExtra("id", user.getId());
                startActivityForResult(intent, 902);
            } else
                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), 901);

        } else if (id == R.id.nav_dicas) {
            dialogDicas.show();

        } else if (id == R.id.nav_privacidade) {
            dialogPrivacidade.show();

        } else if (id == R.id.nav_sobre) {
            dialogSobre.show();

        } else if (id == R.id.nav_personal) {
            mapsFragment.getDeviceLocation();
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /***************chamado quando cadastrar  nova ocorrencia
     *
     * @param latitude
     * @param longitude
     */
    public void showAddOcorrencia(double latitude, double longitude) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        Intent intent = new Intent(MainActivity.this, OcorrenciaActivity.class);
        intent.putExtra("mode", 0);
        intent.putExtra("id", user.getId());
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivityForResult(intent,903);
    }

    /***************chamado quando se coleta a posição no mode de click do fragment map
     *
     * @param latitude
     * @param longitude
     */
    public void showEditOcorrencia(double latitude, double longitude) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        ocor4Edit.setLatitude(latitude);
        ocor4Edit.setLongitude(longitude);

        Intent intent = new Intent(MainActivity.this, OcorrenciaActivity.class);
        intent.putExtra("mode", 1);
        intent.putExtra("id", user.getId());
        intent.putExtra("ocorrencia", ocor4Edit);
        startActivityForResult(intent, 904);
    }


    /****************resposta das activits
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        //Login ok
        if (requestCode == 901 && resultCode == RESULT_OK) {
            user = new Usuario();
            user.setId(data.getIntExtra("id", 0));
            user.setEmail(data.getStringExtra("email"));
            user.setSenha(data.getStringExtra("senha"));
            navLogar.setTitle(getResources().getString(R.string.nav_logado));
        } else {

            //Edit Ocorrencia
            if (requestCode == 902) {
                //mapsFragment.updateMarkers();
                if (resultCode == RESULT_OK) {
                    ocor4Edit = (Ocorrencia) data.getSerializableExtra("ocorrencia");

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                    mapsFragment.setMode(MapsFragment.MODE_CLICK_EDIT_VIEW);
                } else {
                    mapsFragment.updateMarkers();
                }
            }

            //Add Ocorrencia
            if (requestCode == 903 && resultCode == RESULT_OK) {
                mapsFragment.addMarker((Ocorrencia) data.getSerializableExtra("ocorrencia"));
            }

            //Atualiza pos edit ocorrencia
            if (requestCode == 904 && resultCode == RESULT_OK) {
                mapsFragment.updateMarkers();
            }
        }

    }

}
