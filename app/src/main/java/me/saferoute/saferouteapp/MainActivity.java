package me.saferoute.saferouteapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MapsFragment mapsFragment;
    private FragmentManager fragmentManager;

    private LinearLayout linearLayout;
    private ImageView btnBicicleta, btnCartao, btnCel, btnDocumento, btnMochila, btnMoney, btnPc, btnPlus;
    private ImageView btnGps;
    private TextView txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //searchs
        txtSearch = (TextView) findViewById(R.id.txtSearch);
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

        init();

        /* menu "..."
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/
    }

    private void init() {
        //Barra lateral de navegação...
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.configButtons();

        //set fragment Map
        mapsFragment = new MapsFragment();
        mapsFragment.setTxtSearch(txtSearch);
        mapsFragment.setBtnGps(btnGps);

        //change fragment
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transation = fragmentManager.beginTransaction();
        transation.add(R.id.container, mapsFragment, "MapsFragment");
        transation.commitAllowingStateLoss();

        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if (actionID == EditorInfo.IME_ACTION_SEARCH
                        || actionID == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    //procura localização
                    mapsFragment.geoLocate();
                }

                return false;
            }
        });



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

    /* menu "..." no canto
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    //@SuppressWarnings("StatementWithEmptyBody") // não sei pra que esta aqui...
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_personal) {
            mapsFragment.getDeviceLocation();
        }/* else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //configura os botões
    private void configButtons() {
        btnBicicleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < linearLayout.getChildCount(); i++)
                    linearLayout.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

                view.setBackgroundColor(Color.rgb(100,100,100));
            }
        });

        btnCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < linearLayout.getChildCount(); i++)
                    linearLayout.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

                view.setBackgroundColor(Color.rgb(100,100,100));
            }
        });

        btnCel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < linearLayout.getChildCount(); i++)
                    linearLayout.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

                view.setBackgroundColor(Color.rgb(100,100,100));
            }
        });

        btnDocumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < linearLayout.getChildCount(); i++)
                    linearLayout.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

                view.setBackgroundColor(Color.rgb(100,100,100));
            }
        });

        btnMochila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < linearLayout.getChildCount(); i++)
                    linearLayout.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

                view.setBackgroundColor(Color.rgb(100,100,100));
            }
        });

        btnMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < linearLayout.getChildCount(); i++)
                    linearLayout.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

                view.setBackgroundColor(Color.rgb(100,100,100));
            }
        });

        btnPc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < linearLayout.getChildCount(); i++)
                    linearLayout.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

                view.setBackgroundColor(Color.rgb(100,100,100));
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < linearLayout.getChildCount(); i++)
                    linearLayout.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

                view.setBackgroundColor(Color.rgb(100,100,100));
            }
        });
    }
}
