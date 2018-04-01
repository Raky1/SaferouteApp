package me.saferoute.saferouteapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class OcorrenciaActivity extends Activity {

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

            }
        });
    }

    private void screen_config() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));
    }
}
