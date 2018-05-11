package me.saferoute.saferouteapp.CustomCluster;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.maps.android.clustering.ClusterManager;

import java.text.SimpleDateFormat;

import me.saferoute.saferouteapp.MainActivity;
import me.saferoute.saferouteapp.Model.Ocorrencia;
import me.saferoute.saferouteapp.R;

public class CustomClickInfoWindow implements ClusterManager.OnClusterItemInfoWindowClickListener<Ocorrencia> {

    private final LayoutInflater inflater;
    private AlertDialog dialog;
    AlertDialog.Builder builder;
    private final View view;

    public CustomClickInfoWindow(LayoutInflater inflater) {
        this.inflater = inflater;

        builder = new AlertDialog.Builder(inflater.getContext());
        view = inflater.inflate(R.layout.dialog_ocorrencia_info, null);
        builder.setView(view);
        dialog = builder.create();
    }
    @Override
    public void onClusterItemInfoWindowClick(Ocorrencia ocorrencia) {
        //Log.d("INFO", "click info window");

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dia = format.format(ocorrencia.getData());

        format = new SimpleDateFormat("HH:mm");
        String tempo = format.format(ocorrencia.getHora());

        ((TextView)view.findViewById(R.id.info_window_text_data)).setText(dia);
        ((TextView)view.findViewById(R.id.info_window_text_hora)).setText(tempo);

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filterNS = new ColorMatrixColorFilter(matrix);
        matrix.setSaturation(1);
        ColorMatrixColorFilter filterFS = new ColorMatrixColorFilter(matrix);

        if(ocorrencia.isDinheiro())
            ((ImageView)view.findViewById(R.id.info_window_img_dinheiro)).setColorFilter(filterFS);
        else
            ((ImageView)view.findViewById(R.id.info_window_img_dinheiro)).setColorFilter(filterNS);

        if(ocorrencia.isCelular())
            ((ImageView)view.findViewById(R.id.info_window_img_celular)).setColorFilter(filterFS);
        else
            ((ImageView)view.findViewById(R.id.info_window_img_celular)).setColorFilter(filterNS);

        if(ocorrencia.isVeiculo())
            ((ImageView)view.findViewById(R.id.info_window_img_veiculo)).setColorFilter(filterFS);
        else
            ((ImageView)view.findViewById(R.id.info_window_img_veiculo)).setColorFilter(filterNS);

        if(ocorrencia.isCartao())
            ((ImageView)view.findViewById(R.id.info_window_img_cartao)).setColorFilter(filterFS);
        else
            ((ImageView)view.findViewById(R.id.info_window_img_cartao)).setColorFilter(filterNS);

        if(ocorrencia.isCarteira())
            ((ImageView)view.findViewById(R.id.info_window_img_carteira)).setColorFilter(filterFS);
        else
            ((ImageView)view.findViewById(R.id.info_window_img_carteira)).setColorFilter(filterNS);

        if(ocorrencia.isBolsa())
            ((ImageView)view.findViewById(R.id.info_window_img_bolsa)).setColorFilter(filterFS);
        else
            ((ImageView)view.findViewById(R.id.info_window_img_bolsa)).setColorFilter(filterNS);

        if(ocorrencia.isBicicleta())
            ((ImageView)view.findViewById(R.id.info_window_img_bicicleta)).setColorFilter(filterFS);
        else
            ((ImageView)view.findViewById(R.id.info_window_img_bicicleta)).setColorFilter(filterNS);

        if(ocorrencia.isDocumentos())
            ((ImageView)view.findViewById(R.id.info_window_img_documentos)).setColorFilter(filterFS);
        else
            ((ImageView)view.findViewById(R.id.info_window_img_documentos)).setColorFilter(filterNS);

        if(ocorrencia.isOutros())
            ((ImageView)view.findViewById(R.id.info_window_img_outros)).setColorFilter(filterFS);
        else
            ((ImageView)view.findViewById(R.id.info_window_img_outros)).setColorFilter(filterNS);

        int numeroHora = Integer.parseInt((tempo.split(":"))[0]);
        if(numeroHora > 5 && numeroHora < 18)
            ((ImageView)view.findViewById(R.id.info_window_img_dia_noite)).setImageResource(R.drawable.ic_dia);
        else
            ((ImageView)view.findViewById(R.id.info_window_img_dia_noite)).setImageResource(R.drawable.ic_noite);

        if(ocorrencia.isAgrecao())
            ((TextView)view.findViewById(R.id.info_window_txt_agrecao)).setText(R.string.info_window_agracao_sim);
        else
            ((TextView)view.findViewById(R.id.info_window_txt_agrecao)).setText(R.string.info_window_agracao_nao);

        if(ocorrencia.isBoletim())
            ((TextView)view.findViewById(R.id.info_window_txt_boletim)).setText(R.string.info_window_boletim_sim);
        else
            ((TextView)view.findViewById(R.id.info_window_txt_boletim)).setText(R.string.info_window_boletim_nao);

        if(ocorrencia.getComplemento().isEmpty())
            ((TextView)view.findViewById(R.id.info_window_txt_complemento)).setVisibility(View.GONE);
        else
            ((TextView)view.findViewById(R.id.info_window_txt_complemento)).setText(ocorrencia.getComplemento());


        dialog.show();
    }
}
