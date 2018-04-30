package me.saferoute.saferouteapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import me.saferoute.saferouteapp.DAO.AsyncResponse;
import me.saferoute.saferouteapp.DAO.RequestData;
import me.saferoute.saferouteapp.Tools.Validacao;

public class CadastroActivity extends Activity implements AsyncResponse{

    private static final String URL_CADASTRO = "http://saferoute.me/php/mExecutor/mExecUsuario.php";

    private RequestData requestData;
    private AsyncResponse asyncResponse;

    private EditText txtEmail, txtSenha, txtSenhaR;
    private Spinner spinnerGenero;
    private DatePicker datePicker;
    private Button btnCadastrar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cadastro);

        screen_config();

        init();
    }

    private void screen_config() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));
    }

    private void init() {

        asyncResponse = this;

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        txtSenhaR = (EditText) findViewById(R.id.txtSenhaR);
        spinnerGenero = (Spinner) findViewById(R.id.spinnerGenero);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);

        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(this,
                R.array.cadastro_M_F, android.R.layout.simple_spinner_item);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(adapterTipo);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validacao.CheckEmail(txtEmail.getText().toString())) {
                    if (Validacao.CheckSenha(txtSenha.getText().toString())) {
                        if(txtSenha.getText().toString().equals(txtSenhaR.getText().toString())) {
                            requestData = new RequestData();
                            requestData.delegate = asyncResponse;
                            requestData.execute(URL_CADASTRO, getParametros());
                            btnCadastrar.setEnabled(false);
                        } else {
                            Toast.makeText(view.getContext(), "Senhas diferentes", Toast.LENGTH_SHORT).show();
                            txtSenhaR.requestFocus();
                        }
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
    }

    private String getParametros() {
        return "email=" + txtEmail.getText().toString() +
                "&senha=" + txtSenha.getText().toString() +
                "&genero=" + spinnerGenero.getSelectedItem().toString() +
                "&data_nasc=" +
                datePicker.getYear() + "-" +
                (datePicker.getMonth() < 9 ? "0" : "") + (datePicker.getMonth()+1) + "-" +
                (datePicker.getDayOfMonth() < 10 ? "0" : "") + datePicker.getDayOfMonth() +
                "&action=inserir";
    }

    @Override
    public void processFinish(String result) {
        btnCadastrar.setEnabled(true);
        if(result.contains("Cadastro_OK")) {
            int id = Integer.parseInt(result.split("\\*")[1]);

            getIntent().putExtra("id", id);
            getIntent().putExtra("email", txtEmail.getText().toString());
            getIntent().putExtra("senha", txtSenha.getText().toString());
            setResult(RESULT_OK, getIntent());
            Toast.makeText(this, "Cadastrado", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro no cadastramento", Toast.LENGTH_SHORT).show();
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
}
