package me.saferoute.saferouteapp;

import android.app.Activity;
import android.os.Build;
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
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import me.saferoute.saferouteapp.DAO.AsyncResponse;
import me.saferoute.saferouteapp.DAO.RequestData;

public class OcorrenciaActivity extends Activity implements AsyncResponse{

    private static final String URL_OCORRENCIA = "http://saferoute.me/php/mExecutor/mExecOcorrencia.php";

    private RequestData requestData;
    private AsyncResponse asyncResponse;

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Spinner spinnerTipo, spinnerBoletim, spinnerAgrecao;
    private EditText txtPertences_Ocor, txtComplemento_Ocor;
    private Button btnRegistrar_Ocor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ocorrencia);

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

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        spinnerTipo = (Spinner) findViewById(R.id.spinnerTipo);
        spinnerBoletim = (Spinner) findViewById(R.id.spinnerBoletim);
        spinnerAgrecao = (Spinner) findViewById(R.id.spinnerAgrecao);
        txtPertences_Ocor = (EditText) findViewById(R.id.txtPertences);
        txtComplemento_Ocor = (EditText) findViewById(R.id.txtComplemento);
        btnRegistrar_Ocor = (Button) findViewById(R.id.btnRegistrar);

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.YEAR, 2010);
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

                requestData = new RequestData();
                requestData.delegate = asyncResponse;

                requestData.execute(URL_OCORRENCIA, getParametros());
                btnRegistrar_Ocor.setEnabled(false);
            }
        });
    }

    private String getParametros() {
        String parametro = "usuario=" + getIntent().getIntExtra("id",0) +
                "&latitude=" + getIntent().getDoubleExtra("latitude",0) +
                "&longitude=" + getIntent().getDoubleExtra("longitude",0);

        parametro += "&dia=" + datePicker.getYear() + "-" +
                (datePicker.getMonth() < 9 ? "0" : "") + (datePicker.getMonth()+1) + "-" +
                (datePicker.getDayOfMonth() < 10 ? "0" : "") + datePicker.getDayOfMonth();

        if(Build.VERSION.SDK_INT < 23) {
            parametro += "&hora=" + (timePicker.getCurrentHour() < 9 ? "0" : "") + timePicker.getCurrentHour() + ":" +
                    (timePicker.getCurrentHour() < 9 ? "0" : "") + timePicker.getCurrentMinute();
        } else {
            parametro += "&hora=" + (timePicker.getHour() < 9 ? "0" : "") + timePicker.getHour() + ":" +
                    (timePicker.getMinute() < 9 ? "0" : "") + timePicker.getMinute();
        }

        parametro += "&tipo=" + spinnerTipo.getSelectedItem().toString() +
                "&boletim=" + (spinnerBoletim.getSelectedItem().toString().equals("Sim") ? 1 : 0) +
                "&agrecao=" + (spinnerAgrecao.getSelectedItem().toString().equals("Sim") ? 1 : 0) +
                "&pertences=" + txtPertences_Ocor.getText().toString() +
                "&complemento=" + txtComplemento_Ocor.getText().toString() +
                "&action=inserir";

        return parametro;
    }

    //
    @Override
    public void processFinish(String result) {
        Log.d("INFO", result);

        if(result.contains("Registro_Ok")) {
            Toast.makeText(this, "Ocorrencia Registrada", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao registrar a ocorrencia", Toast.LENGTH_SHORT).show();
        }

        btnRegistrar_Ocor.setEnabled(true);
    }
}
