package me.saferoute.saferouteapp.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import me.saferoute.saferouteapp.Model.Ocorrencia;
import me.saferoute.saferouteapp.R;

public class CustomListViewAdapter extends BaseAdapter {

    private final List<Ocorrencia> ocorrencias;
    private final Activity activity;

    public CustomListViewAdapter(List<Ocorrencia> ocorrencias, Activity activity) {
        this.ocorrencias = ocorrencias;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return ocorrencias.size();
    }

    @Override
    public Object getItem(int i) {
        return ocorrencias.get(i);
    }

    @Override
    public long getItemId(int i) {
        return ocorrencias.get(i).getId();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        @SuppressLint("ViewHolder")
        View v = activity.getLayoutInflater().inflate(R.layout.list_info_ocorrencias, viewGroup, false);

        Ocorrencia ocorrencia = ocorrencias.get(i);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dia = format.format(ocorrencia.getData());

        format = new SimpleDateFormat("HH:mm");
        String tempo = format.format(ocorrencia.getHora());

        ((TextView)v.findViewById(R.id.list_text_data)).setText(dia);
        ((TextView)v.findViewById(R.id.list_text_hora)).setText(tempo);

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        if(!ocorrencia.isDinheiro())
            ((ImageView)v.findViewById(R.id.list_img_dinheiro)).setColorFilter(filter);
        if(!ocorrencia.isCelular())
            ((ImageView)v.findViewById(R.id.list_img_celular)).setColorFilter(filter);
        if(!ocorrencia.isVeiculo())
            ((ImageView)v.findViewById(R.id.list_img_veiculo)).setColorFilter(filter);
        if(!ocorrencia.isCartao())
            ((ImageView)v.findViewById(R.id.list_img_cartao)).setColorFilter(filter);
        if(!ocorrencia.isCarteira())
            ((ImageView)v.findViewById(R.id.list_img_carteira)).setColorFilter(filter);
        if(!ocorrencia.isBolsa())
            ((ImageView)v.findViewById(R.id.list_img_bolsa)).setColorFilter(filter);
        if(!ocorrencia.isBicicleta())
            ((ImageView)v.findViewById(R.id.list_img_bicicleta)).setColorFilter(filter);
        if(!ocorrencia.isDocumentos())
            ((ImageView)v.findViewById(R.id.list_img_documentos)).setColorFilter(filter);
        if(!ocorrencia.isOutros())
            ((ImageView)v.findViewById(R.id.list_img_outros)).setColorFilter(filter);

        int numeroHora = Integer.parseInt((tempo.split(":"))[0]);
        if(numeroHora > 5 && numeroHora < 18)
            ((ImageView)v.findViewById(R.id.list_img_dia_noite)).setImageResource(R.drawable.ic_dia);
        else
            ((ImageView)v.findViewById(R.id.list_img_dia_noite)).setImageResource(R.drawable.ic_noite);

        return v;
    }
}
