package me.saferoute.saferouteapp;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import me.saferoute.saferouteapp.Model.Usuario;
import me.saferoute.saferouteapp.Tools.Validacao;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Login persist
    private Usuario user = null;

    private MapsFragment mapsFragment;
    private FragmentManager fragmentManager;

    //scroll and mapbtn
    private LinearLayout linearLayout;
    private ImageView btnBicicleta, btnCartao, btnCel, btnDocumento, btnMochila, btnMoney, btnPc, btnPlus;
    private ImageView btnGps;
    private AutoCompleteTextView txtSearch;

    //Dialogs
    private AlertDialog dialogLogin, dialogOcorrencia, dialogDicas, dialogPrivacidade, dialogSobre;

    //login screen
    private EditText txtEmail_login,txtSenha_login;
    private Button btnLogar_login;

    //ocorrencia Screen
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Spinner spinnerTipo, spinnerBoletim, spinnerAgrecao;
    private EditText txtPertences_Ocor, txtComplemento_Ocor;
    private Button btnRegistrar_Ocor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Verifica conectividade com internet
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected())
            finish();

        //searchs
        txtSearch = (AutoCompleteTextView) findViewById(R.id.txtSearch);
        btnGps = (ImageView) findViewById(R.id.icon_gps);

        //Botões filtro
        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutScrollFiltro);
        btnBicicleta = (ImageView) findViewById(R.id.btnBicicleta);
        btnCartao = (ImageView) findViewById(R.id.btnCartao);
        btnCel = (ImageView) findViewById(R.id.btnCel);
        btnDocumento = (ImageView) findViewById(R.id.btnDocumento);
        btnMochila = (ImageView) findViewById(R.id.btnMochila);
        btnMoney = (ImageView) findViewById(R.id.btnMoney);
        btnPc = (ImageView) findViewById(R.id.btnPc);
        btnPlus = (ImageView) findViewById(R.id.btnPlus);

        this.configButton(btnBicicleta, btnCartao, btnCel,btnDocumento, btnMochila, btnMoney, btnPc, btnPlus);

        init();
    }

    private void init() {
        //Barra lateral de navegação...
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        //config Login screen
        View view = getLayoutInflater().inflate(R.layout.dialog_login, null);
        builder.setView(view);
        dialogLogin = builder.create();

        //tela login
        txtEmail_login = (EditText) view.findViewById(R.id.txtEmail);
        txtSenha_login = (EditText) view.findViewById(R.id.txtSenha);
        btnLogar_login = (Button) view.findViewById(R.id.btnLogar);

        btnLogar_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validacao.CheckEmail(txtEmail_login.getText().toString())) {
                    if (Validacao.CheckSenha(txtSenha_login.getText().toString())) {

                        dialogLogin.dismiss();
                    } else {
                        txtSenha_login.requestFocus();
                    }
                } else {
                    txtEmail_login.requestFocus();
                }
            }
        });

        //-----Ocorrencia Dialog
        view = getLayoutInflater().inflate(R.layout.dialog_ocorrencia, null);
        builder.setView(view);
        dialogOcorrencia = builder.create();

        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        spinnerTipo = (Spinner) view.findViewById(R.id.spinnerTipo);
        spinnerBoletim = (Spinner) view.findViewById(R.id.spinnerBoletim);
        spinnerAgrecao = (Spinner) view.findViewById(R.id.spinnerAgrecao);
        txtPertences_Ocor = (EditText) view.findViewById(R.id.txtPertences);
        txtComplemento_Ocor = (EditText) view.findViewById(R.id.txtComplemento);
        btnRegistrar_Ocor = (Button) view.findViewById(R.id.btnRegistrar);

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -1);
        datePicker.setMinDate(c.getTime().getTime());

        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(this,
                R.array.ocorrencia_tipos, android.R.layout.simple_spinner_item);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipo);

        ArrayAdapter<CharSequence> adapterYN = ArrayAdapter.createFromResource(this,
                R.array.ocorrencia_yes_no, android.R.layout.simple_spinner_item);
        adapterYN.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBoletim.setAdapter(adapterYN);
        spinnerAgrecao.setAdapter(adapterYN);

        btnRegistrar_Ocor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOcorrencia.dismiss();
            }
        });

        //dialog Dicas de seguraça
        view = getLayoutInflater().inflate(R.layout.dialog_dicas, null);
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //@SuppressWarnings("StatementWithEmptyBody") // não sei pra que esta aqui...
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_logar) {
            dialogLogin.show();

        } else if (id == R.id.nav_fui_roubado) {
            dialogOcorrencia.show();

        } else if (id == R.id.nav_dicas) {
            dialogDicas.show();

        } else if (id == R.id.nav_privacidade) {
            dialogPrivacidade.show();

        } else if (id == R.id.nav_sobre) {
            dialogSobre.show();

        } else if (id == R.id.nav_personal) {
            mapsFragment.getDeviceLocation();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //configura os botões
    private void configButton(ImageView ...btns) {
        for (ImageView btn : btns){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int i = 0; i < linearLayout.getChildCount(); i++)
                        linearLayout.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

                    view.setBackgroundColor(Color.rgb(100,100,100));
                }
            });
        }

    }
}
