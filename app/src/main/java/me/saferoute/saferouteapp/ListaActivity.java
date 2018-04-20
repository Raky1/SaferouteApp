package me.saferoute.saferouteapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.saferoute.saferouteapp.DAO.AsyncResponse;
import me.saferoute.saferouteapp.DAO.RequestData;
import me.saferoute.saferouteapp.Model.Ocorrencia;

public class ListaActivity extends Activity implements AsyncResponse {

    private static final String URL_OCORRENCIA = "http://saferoute.me/php/mExecutor/mExecOcorrencia.php";

    private RequestData requestData;
    private AsyncResponse asyncResponse;

    private int comando;
    private Ocorrencia selectedItem = null;

    private ListView listView;
    private Button btnEditar, btnExcluir;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list_ocorrencia);

        screen_init();

        init();
    }

    private void screen_init() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.9));
    }

    private void init() {
        asyncResponse = this;

        listView = (ListView) findViewById(R.id.listView);
        btnEditar = (Button) findViewById(R.id.btnEditar);
        btnExcluir = (Button) findViewById(R.id.btnExcluir);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = (Ocorrencia) listView.getAdapter().getItem(i);
                btnEditar.setEnabled(true);
                btnExcluir.setEnabled(true);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedItem != null) {

                    getIntent().putExtra("ocorrencia", (Serializable) selectedItem);
                    setResult(RESULT_OK, getIntent());
                    finish();

                    btnEditar.setEnabled(false);
                    btnExcluir.setEnabled(false);
                }
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedItem != null) {
                    requestData = new RequestData();
                    requestData.delegate = asyncResponse;
                    String parametro = "action=deleta&id="+selectedItem.getId();
                    comando = 1;
                    requestData.execute(URL_OCORRENCIA, parametro);
                    btnEditar.setEnabled(false);
                    btnExcluir.setEnabled(false);
                }
            }
        });

        listar();

    }

    private void listar() {
        requestData = new RequestData();
        requestData.delegate = this;
        String parametro = "action=getAllId&id="+getIntent().getIntExtra("id", 0);
        comando = 0;
        requestData.execute(URL_OCORRENCIA, parametro);
        selectedItem = null;
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
    }


    @Override
    public void processFinish(String result) {
        //Log.d("INFO", result);
        if (comando == 0) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                List<Ocorrencia> ocorrencias = new ArrayList<Ocorrencia>();

                if (jsonObject.getBoolean("resultado")) {
                    JSONArray jsonOcorrencias = jsonObject.getJSONArray("ocorrencias");

                    for (int i = 0; i < jsonOcorrencias.length(); i++) {
                        JSONObject jsonOcor = jsonOcorrencias.getJSONObject(i);
                        Ocorrencia ocor = new Ocorrencia();

                        ocor.setFromJSON(jsonOcor);

                        ocorrencias.add(ocor);
                    }

                    ArrayAdapter<Ocorrencia> adapter = new ArrayAdapter<Ocorrencia>(this,
                            android.R.layout.simple_list_item_1, ocorrencias);

                    listView.setAdapter(adapter);

                } else {
                    if (jsonObject.getString("erro").contains("error: nada encontrado")) {

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_list_item_1);

                        adapter.add("Nenhuma ocorrencia encontrada");

                        listView.setAdapter(adapter);

                        btnEditar.setEnabled(false);
                        btnExcluir.setEnabled(false);
                        Log.d("ERROR", "naddaaaaaaaaaa");
                    }
                    Log.d("ERROR", jsonObject.getString("erro"));

                }
            } catch (Exception e) {
                Log.d("ERROR", e.getMessage());
            }
        } else if (comando == 1) {
            if(result.contains("Delete_Ok")) {
                Toast.makeText(this, "Deletado", Toast.LENGTH_SHORT).show();
                listar();
            } else {
                Toast.makeText(this, "Erro ao excluir", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
