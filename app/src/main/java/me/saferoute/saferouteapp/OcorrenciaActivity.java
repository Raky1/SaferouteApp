package me.saferoute.saferouteapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.saferoute.saferouteapp.DAO.AsyncResponse;
import me.saferoute.saferouteapp.DAO.RequestData;
import me.saferoute.saferouteapp.Model.Ocorrencia;

public class OcorrenciaActivity extends Activity implements AsyncResponse{

    private static final String URL_OCORRENCIA = "http://saferoute.me/php/mExecutor/mExecOcorrencia.php";

    private RequestData requestData;
    private AsyncResponse asyncResponse;

    private int mode = 0;

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Spinner spinnerBoletim, spinnerAgrecao;
    private EditText txtComplemento_Ocor;
    private Button btnRegistrar_Ocor;

    private boolean[] pertences = {true, true, true, true, true, true, true, true, true};
    //private boolean sDinheiro, sCelular, sVeiculo, sCartao, sCarteira, sBolsa, sBicicleta, sDocumentos, sOutros;
    //private ImageView imgDinheiro, imgCelular, imgVeiculo, imgCartao, imgCarteira, imgBolsa, imgBicicleta, imgDocumentos, imgOutros;
    //private TextView txtDinheiro, txtCelular, txtVeiculo, txtCartao, txtCarteira, txtBolsa, txtBicicleta, txtDocumentos, txtOutros;

    private Ocorrencia ocorrencia;

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
        mode = getIntent().getIntExtra("mode",0);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        spinnerBoletim = (Spinner) findViewById(R.id.spinnerBoletim);
        spinnerAgrecao = (Spinner) findViewById(R.id.spinnerAgrecao);
        txtComplemento_Ocor = (EditText) findViewById(R.id.txtComplemento);
        btnRegistrar_Ocor = (Button) findViewById(R.id.btnRegistrar);


        configSelects();


        if(mode == 0) { // mode de cadastro

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.set(Calendar.YEAR, 2010);
            datePicker.setMinDate(c.getTime().getTime());

            ArrayAdapter<CharSequence> adapterYN = ArrayAdapter.createFromResource(this,
                    R.array.ocorrencia_yes_no, android.R.layout.simple_spinner_item);
            adapterYN.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerBoletim.setAdapter(adapterYN);
            spinnerAgrecao.setAdapter(adapterYN);

        } else if (mode == 1) { //modo de edição
            Ocorrencia ocor = (Ocorrencia) getIntent().getSerializableExtra("ocorrencia");

            btnRegistrar_Ocor.setText(R.string.ocorrencia_atualizar);

            //seta data
            Calendar c = Calendar.getInstance();
            c.setTime(ocor.getData());
            datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), null);

            //seta Time
            c.setTime(ocor.getHora());
            if(Build.VERSION.SDK_INT < 23) {
                timePicker.setCurrentHour(c.get(Calendar.HOUR));
                timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
            } else {
                timePicker.setHour(c.get(Calendar.HOUR));
                timePicker.setMinute(c.get(Calendar.MINUTE));
            }


            if(ocor.isDinheiro())
                pertences[0] = changeStatePertence((ImageView) findViewById(R.id.select_img_dinheiro), (TextView) findViewById(R.id.select_txt_dinheiro), pertences[0]);
            if(ocor.isCelular())
                pertences[1] = changeStatePertence((ImageView) findViewById(R.id.select_img_celular), (TextView) findViewById(R.id.select_txt_celular), pertences[1]);
            if(ocor.isVeiculo())
                pertences[2] = changeStatePertence((ImageView) findViewById(R.id.select_img_veiculo), (TextView) findViewById(R.id.select_txt_veiculo), pertences[2]);
            if(ocor.isCartao())
                pertences[3] = changeStatePertence((ImageView) findViewById(R.id.select_img_cartao), (TextView) findViewById(R.id.select_txt_cartao), pertences[3]);
            if(ocor.isCarteira())
                pertences[4] = changeStatePertence((ImageView) findViewById(R.id.select_img_carteira), (TextView) findViewById(R.id.select_txt_carteira), pertences[4]);
            if(ocor.isBolsa())
                pertences[5] = changeStatePertence((ImageView) findViewById(R.id.select_img_bolsa), (TextView) findViewById(R.id.select_txt_bolsa), pertences[5]);
            if(ocor.isBicicleta())
                pertences[6] = changeStatePertence((ImageView) findViewById(R.id.select_img_bicicleta), (TextView) findViewById(R.id.select_txt_bicicleta), pertences[6]);
            if(ocor.isDocumentos())
                pertences[7] = changeStatePertence((ImageView) findViewById(R.id.select_img_documentos), (TextView) findViewById(R.id.select_txt_documentos), pertences[7]);
            if(ocor.isOutros())
                pertences[8] = changeStatePertence((ImageView) findViewById(R.id.select_img_outros), (TextView) findViewById(R.id.select_txt_outros), pertences[8]);


            ArrayAdapter<CharSequence> adapterYN = ArrayAdapter.createFromResource(this,
                    R.array.ocorrencia_yes_no, android.R.layout.simple_spinner_item);
            adapterYN.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerBoletim.setAdapter(adapterYN);
            spinnerBoletim.setSelection(adapterYN.getPosition((ocor.isBoletim()?"Sim":"Não")));

            spinnerAgrecao.setAdapter(adapterYN);
            spinnerAgrecao.setSelection(adapterYN.getPosition((ocor.isAgrecao()?"Sim":"Não")));

            txtComplemento_Ocor.setText(ocor.getComplemento());
        }

        btnRegistrar_Ocor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pertences[0] || pertences[1] || pertences[2] || pertences[3] || pertences[4] || pertences[5] || pertences[6] || pertences[7] || pertences[8]) {
                    Log.d("INFO", (pertences[0] ? "1" : "0") + "|" +
                            (pertences[1] ? "1" : "0") + "|" +
                            (pertences[2] ? "1" : "0") + "|" +
                            (pertences[3] ? "1" : "0") + "|" +
                            (pertences[4] ? "1" : "0") + "|" +
                            (pertences[5] ? "1" : "0") + "|" +
                            (pertences[6] ? "1" : "0") + "|" +
                            (pertences[7] ? "1" : "0") + "|" +
                            (pertences[8] ? "1" : "0"));
                    requestData = new RequestData();
                    requestData.delegate = asyncResponse;

                    requestData.execute(URL_OCORRENCIA, getParametros());
                    btnRegistrar_Ocor.setEnabled(false);
                } else {
                    Toast.makeText(view.getContext(), "Selecione pelo menos uma opção dos pertences", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void configSelects() {

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.select_img_dinheiro || view.getId() == R.id.select_txt_dinheiro)
                    pertences[0] = changeStatePertence((ImageView) findViewById(R.id.select_img_dinheiro),
                            (TextView) findViewById(R.id.select_txt_dinheiro),
                            pertences[0]);

                else if (view.getId() == R.id.select_img_celular || view.getId() == R.id.select_txt_celular)
                    pertences[1] = changeStatePertence((ImageView) findViewById(R.id.select_img_celular),
                            (TextView) findViewById(R.id.select_txt_celular),
                            pertences[1]);

                else if (view.getId() == R.id.select_img_veiculo || view.getId() == R.id.select_txt_veiculo)
                    pertences[2] = changeStatePertence((ImageView) findViewById(R.id.select_img_veiculo),
                            (TextView) findViewById(R.id.select_txt_veiculo),
                            pertences[2]);

                else if (view.getId() == R.id.select_img_cartao || view.getId() == R.id.select_txt_cartao)
                    pertences[3] =  changeStatePertence((ImageView) findViewById(R.id.select_img_cartao),
                            (TextView) findViewById(R.id.select_txt_cartao),
                            pertences[3]);

                else if (view.getId() == R.id.select_img_carteira || view.getId() == R.id.select_txt_carteira)
                    pertences[4] = changeStatePertence((ImageView) findViewById(R.id.select_img_carteira),
                            (TextView) findViewById(R.id.select_txt_carteira),
                            pertences[4]);

                else if (view.getId() == R.id.select_img_bolsa || view.getId() == R.id.select_txt_bolsa)
                    pertences[5] = changeStatePertence((ImageView) findViewById(R.id.select_img_bolsa),
                            (TextView) findViewById(R.id.select_txt_bolsa),
                            pertences[5]);

                else if (view.getId() == R.id.select_img_bicicleta || view.getId() == R.id.select_txt_bicicleta)
                    pertences[6] = changeStatePertence((ImageView) findViewById(R.id.select_img_bicicleta),
                            (TextView) findViewById(R.id.select_txt_bicicleta),
                            pertences[6]);

                else if (view.getId() == R.id.select_img_documentos || view.getId() == R.id.select_txt_documentos)
                    pertences[7] = changeStatePertence((ImageView) findViewById(R.id.select_img_documentos),
                            (TextView) findViewById(R.id.select_txt_documentos),
                            pertences[7]);

                else if (view.getId() == R.id.select_img_outros || view.getId() == R.id.select_txt_outros)
                    pertences[8] = changeStatePertence((ImageView) findViewById(R.id.select_img_outros),
                            (TextView) findViewById(R.id.select_txt_outros),
                            pertences[8]);
            }
        };

        //setting up click selects
        findViewById(R.id.select_img_dinheiro).setOnClickListener(clickListener);
        findViewById(R.id.select_txt_dinheiro).setOnClickListener(clickListener);
        pertences[0] = changeStatePertence((ImageView) findViewById(R.id.select_img_dinheiro),
                (TextView) findViewById(R.id.select_txt_dinheiro),
                pertences[0]);

        findViewById(R.id.select_img_celular).setOnClickListener(clickListener);
        findViewById(R.id.select_txt_celular).setOnClickListener(clickListener);
        pertences[1] = changeStatePertence((ImageView) findViewById(R.id.select_img_celular),
                (TextView) findViewById(R.id.select_txt_celular),
                pertences[1]);

        findViewById(R.id.select_img_veiculo).setOnClickListener(clickListener);
        findViewById(R.id.select_txt_veiculo).setOnClickListener(clickListener);
        pertences[2] = changeStatePertence((ImageView) findViewById(R.id.select_img_veiculo),
                (TextView) findViewById(R.id.select_txt_veiculo),
                pertences[2]);

        findViewById(R.id.select_img_cartao).setOnClickListener(clickListener);
        findViewById(R.id.select_txt_cartao).setOnClickListener(clickListener);
        pertences[3] = changeStatePertence((ImageView) findViewById(R.id.select_img_cartao),
                (TextView) findViewById(R.id.select_txt_cartao),
                pertences[3]);

        findViewById(R.id.select_img_carteira).setOnClickListener(clickListener);
        findViewById(R.id.select_txt_carteira).setOnClickListener(clickListener);
        pertences[4] = changeStatePertence((ImageView) findViewById(R.id.select_img_carteira),
                (TextView) findViewById(R.id.select_txt_carteira),
                pertences[4]);

        findViewById(R.id.select_img_bolsa).setOnClickListener(clickListener);
        findViewById(R.id.select_txt_bolsa).setOnClickListener(clickListener);
        pertences[5] = changeStatePertence((ImageView) findViewById(R.id.select_img_bolsa),
                (TextView) findViewById(R.id.select_txt_bolsa),
                pertences[5]);

        findViewById(R.id.select_img_bicicleta).setOnClickListener(clickListener);
        findViewById(R.id.select_txt_bicicleta).setOnClickListener(clickListener);
        pertences[6] = changeStatePertence((ImageView) findViewById(R.id.select_img_bicicleta),
                (TextView) findViewById(R.id.select_txt_bicicleta),
                pertences[6]);

        findViewById(R.id.select_img_documentos).setOnClickListener(clickListener);
        findViewById(R.id.select_txt_documentos).setOnClickListener(clickListener);
        pertences[7] = changeStatePertence((ImageView) findViewById(R.id.select_img_documentos),
                (TextView) findViewById(R.id.select_txt_documentos),
                pertences[7]);

        findViewById(R.id.select_img_outros).setOnClickListener(clickListener);
        findViewById(R.id.select_txt_outros).setOnClickListener(clickListener);
        pertences[8] = changeStatePertence((ImageView) findViewById(R.id.select_img_outros),
                (TextView) findViewById(R.id.select_txt_outros),
                pertences[8]);
    }

    private boolean changeStatePertence(ImageView img, TextView txt, boolean state) {
        state = !state;
        ColorMatrix matrix = new ColorMatrix();

        if(state) {
            matrix.setSaturation(1);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            img.setColorFilter(filter);
            txt.setTextColor(Color.BLACK);
        } else {
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            img.setColorFilter(filter);
            txt.setTextColor(Color.GRAY);
        }

        return state;
    }


    @SuppressLint("SimpleDateFormat")
    private String getParametros() {
        ocorrencia = new Ocorrencia();
        //ocorrencia.setId();
        ocorrencia.setLatitude(getIntent().getDoubleExtra("latitude", 0));
        ocorrencia.setLongitude(getIntent().getDoubleExtra("longitude", 0));


        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            ocorrencia.setData(formato.parse(datePicker.getDayOfMonth()+"/"+(datePicker.getMonth() + 1)+"/"+datePicker.getYear()));
            formato = new SimpleDateFormat("HH:mm");
            String hora;
            if (Build.VERSION.SDK_INT < 23) {
                hora =(timePicker.getCurrentHour() < 9 ? "0" : "") + timePicker.getCurrentHour() + ":" +
                        (timePicker.getCurrentHour() < 9 ? "0" : "") + timePicker.getCurrentMinute();
            } else {
                hora = (timePicker.getHour() < 9 ? "0" : "") + timePicker.getHour() + ":" +
                        (timePicker.getMinute() < 9 ? "0" : "") + timePicker.getMinute();
            }

            ocorrencia.setHora(new Time(formato.parse(hora).getTime()));

        }catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
        ocorrencia.setDinheiro(pertences[0]);
        ocorrencia.setCelular(pertences[1]);
        ocorrencia.setVeiculo(pertences[2]);
        ocorrencia.setCartao(pertences[3]);
        ocorrencia.setCarteira(pertences[4]);
        ocorrencia.setBolsa(pertences[5]);
        ocorrencia.setBicicleta(pertences[6]);
        ocorrencia.setDocumentos(pertences[7]);
        ocorrencia.setOutros(pertences[8]);

        String parametro = "";

        if(mode==0) {

            parametro = "usuario=" + getIntent().getIntExtra("id", 0) +
                    "&latitude=" + ocorrencia.getLatitude() +
                    "&longitude=" + ocorrencia.getLongitude();

            parametro += "&dia=" + datePicker.getYear() + "-" +
                    (datePicker.getMonth() < 9 ? "0" : "") + (datePicker.getMonth() + 1) + "-" +
                    (datePicker.getDayOfMonth() < 10 ? "0" : "") + datePicker.getDayOfMonth();

            if (Build.VERSION.SDK_INT < 23) {
                parametro += "&hora=" + (timePicker.getCurrentHour() < 9 ? "0" : "") + timePicker.getCurrentHour() + ":" +
                        (timePicker.getCurrentHour() < 9 ? "0" : "") + timePicker.getCurrentMinute();
            } else {
                parametro += "&hora=" + (timePicker.getHour() < 9 ? "0" : "") + timePicker.getHour() + ":" +
                        (timePicker.getMinute() < 9 ? "0" : "") + timePicker.getMinute();
            }

            parametro += "&dinheiro=" + (pertences[0] ? "1" : "0") +
                    "&celular=" + (pertences[1] ? "1" : "0") +
                    "&veiculo=" + (pertences[2] ? "1" : "0") +
                    "&cartao=" + (pertences[3] ? "1" : "0") +
                    "&carteira=" + (pertences[4] ? "1" : "0") +
                    "&bolsa=" + (pertences[5] ? "1" : "0") +
                    "&bicicleta=" + (pertences[6] ? "1" : "0") +
                    "&documentos=" + (pertences[7] ? "1" : "0") +
                    "&outros=" + (pertences[8] ? "1" : "0") +

                    "&boletim=" + (spinnerBoletim.getSelectedItem().toString().equals("Sim") ? "1" : "0") +
                    "&agrecao=" + (spinnerAgrecao.getSelectedItem().toString().equals("Sim") ? "1" : "0") +
                    "&complemento=" + txtComplemento_Ocor.getText().toString() +
                    "&action=inserir";
            //Log.d("INFO", parametro);
        } else if(mode == 1){

            Ocorrencia ocor = (Ocorrencia) getIntent().getSerializableExtra("ocorrencia");

            parametro = "id=" + ocor.getId() +
                    "&usuario=" + getIntent().getIntExtra("id", 0) +
                    "&latitude=" + ocor.getLatitude() +
                    "&longitude=" + ocor.getLongitude();

            parametro += "&dia=" + datePicker.getYear() + "-" +
                    (datePicker.getMonth() < 9 ? "0" : "") + (datePicker.getMonth() + 1) + "-" +
                    (datePicker.getDayOfMonth() < 10 ? "0" : "") + datePicker.getDayOfMonth();

            if (Build.VERSION.SDK_INT < 23) {
                parametro += "&hora=" + (timePicker.getCurrentHour() < 9 ? "0" : "") + timePicker.getCurrentHour() + ":" +
                        (timePicker.getCurrentHour() < 9 ? "0" : "") + timePicker.getCurrentMinute();
            } else {
                parametro += "&hora=" + (timePicker.getHour() < 9 ? "0" : "") + timePicker.getHour() + ":" +
                        (timePicker.getMinute() < 9 ? "0" : "") + timePicker.getMinute();
            }

            parametro += "&dinheiro=" + (pertences[0] ? "1" : "0") +
                    "&celular=" + (pertences[1] ? "1" : "0") +
                    "&veiculo=" + (pertences[2] ? "1" : "0") +
                    "&cartao=" + (pertences[3] ? "1" : "0") +
                    "&carteira=" + (pertences[4] ? "1" : "0") +
                    "&bolsa=" + (pertences[5] ? "1" : "0") +
                    "&bicicleta=" + (pertences[6] ? "1" : "0") +
                    "&documentos=" + (pertences[7] ? "1" : "0") +
                    "&outros=" + (pertences[8] ? "1" : "0") +

                    "&boletim=" + (spinnerBoletim.getSelectedItem().toString().equals("Sim") ? 1 : 0) +
                    "&agrecao=" + (spinnerAgrecao.getSelectedItem().toString().equals("Sim") ? 1 : 0) +
                    "&complemento=" + txtComplemento_Ocor.getText().toString();

            parametro += "&action=update";
        }

        return parametro;
    }

    //
    @Override
    public void processFinish(String result) {
        if(mode == 0)
            if(result.contains("Registro_OK")) {
                Toast.makeText(this, "Ocorrencia Registrada", Toast.LENGTH_SHORT).show();

                ocorrencia.setId(Integer.parseInt(result.split("\\*")[1]));

                Intent intent = new Intent();
                intent.putExtra("ocorrencia", ocorrencia);
                setResult(RESULT_OK, intent);

                finish();
            } else {
                Toast.makeText(this, "Erro ao registrar a ocorrencia", Toast.LENGTH_SHORT).show();
            }
        else if (mode == 1)
            if(result.contains("Update_Ok")) {
                Toast.makeText(this, "Ocorrencia Atualizada", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Erro ao atualizar a ocorrencia", Toast.LENGTH_SHORT).show();
            }

        btnRegistrar_Ocor.setEnabled(true);
    }
}
