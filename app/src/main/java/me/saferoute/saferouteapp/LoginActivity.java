package me.saferoute.saferouteapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import me.saferoute.saferouteapp.DAO.AsyncResponse;
import me.saferoute.saferouteapp.DAO.RequestData;
import me.saferoute.saferouteapp.Model.Usuario;
import me.saferoute.saferouteapp.Tools.CacheData;
import me.saferoute.saferouteapp.Tools.Validacao;

public class LoginActivity extends Activity implements AsyncResponse{

    private static final String URL_LOGIN = "http://saferoute.me/php/mExecutor/mExecLogin.php";

    private RequestData requestData;
    private AsyncResponse asyncResponse;

    private TextView txtCadastro;
    private EditText txtEmail, txtSenha;
    private Button  btnLogar;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);

        screen_init();

        init();
    }

    private void screen_init() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        //int height = dm.heightPixels;

        LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayoutLogin);

        getWindow().setLayout((int)(width*.8),420);//a altura ta setado mas testada no meu cel, não sei como fica nos outros.. qualquer coisa só colocar (int)(height*.5)
    }

    private void init() {
        asyncResponse  = this;

        //tela login
        txtCadastro = (TextView) findViewById(R.id.linkCadastrar);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        btnLogar = (Button) findViewById(R.id.btnLogar);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestData = new RequestData();
                requestData.delegate = asyncResponse;
                requestData.execute(URL_LOGIN, getParametros());
                btnLogar.setEnabled(false);
                txtCadastro.setTextColor(Color.RED);
                txtCadastro.setEnabled(false);

                if(Validacao.CheckEmail(txtEmail.getText().toString())) {
                    if (Validacao.CheckSenha(txtSenha.getText().toString())) {
                        //qualquer coisa
                    } else {

                        Toast.makeText(view.getContext(), "Senha invalida", Toast.LENGTH_SHORT).show();
                        txtSenha.requestFocus();
                    }
                } else {
                    Toast.makeText(view.getContext(), "Login invalido", Toast.LENGTH_SHORT).show();
                    txtEmail.requestFocus();
                }
            }
        });

        txtCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogar.setEnabled(false);
                txtCadastro.setTextColor(Color.RED);
                txtCadastro.setEnabled(false);

                startActivityForResult(new Intent(LoginActivity.this, CadastroActivity.class), 901);
            }
        });

        txtEmail.setText(CacheData.GetEmail(this));
    }

    private String getParametros() {
        return "email=" + txtEmail.getText().toString() +
                "&senha=" + txtSenha.getText().toString() +
                "&action=logar";
    }

    @Override
    public void processFinish(String result) {
        //Log.d("INFO", result);
        btnLogar.setEnabled(true);
        txtCadastro.setTextColor(getResources().getColor(R.color.link));
        txtCadastro.setEnabled(true);
        if(result.contains("Login_OK")) {
            int id = Integer.parseInt(result.split("\\*")[1]);

            getIntent().putExtra("id", id);
            getIntent().putExtra("email", txtEmail.getText().toString());
            getIntent().putExtra("senha", txtSenha.getText().toString());
            setResult(RESULT_OK, getIntent());

            CacheData.SaveEmail(txtEmail.getText().toString(), this);

            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(requestData != null)
                requestData.cancel(true);
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }

        setResult(RESULT_CANCELED);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        //Cadastro ok
        if (requestCode == 901 && resultCode == RESULT_OK) {

            getIntent().putExtra("id", data.getIntExtra("id", 0));
            getIntent().putExtra("email", data.getStringExtra("email"));
            getIntent().putExtra("senha", data.getStringExtra("senha"));
            setResult(RESULT_OK, getIntent());
            finish();
        } else if (requestCode == 901 && resultCode == RESULT_CANCELED) {
            txtCadastro.setEnabled(true);
            txtCadastro.setTextColor(getResources().getColor(R.color.link));
            btnLogar.setEnabled(true);
        }

    }
}
