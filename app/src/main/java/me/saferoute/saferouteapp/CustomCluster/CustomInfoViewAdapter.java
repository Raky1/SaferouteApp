package me.saferoute.saferouteapp.CustomCluster;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import me.saferoute.saferouteapp.Model.Ocorrencia;
import me.saferoute.saferouteapp.R;

public class CustomInfoViewAdapter implements GoogleMap.InfoWindowAdapter {

    private final LayoutInflater inflater;

    public CustomInfoViewAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        final View popup = inflater.inflate(R.layout.info_marker_screen, null);

        String[] parametros = marker.getSnippet().split("\\*");


        ((TextView)popup.findViewById(R.id.info_text_data)).setText(parametros[0]);
        ((TextView)popup.findViewById(R.id.info_text_hora)).setText(parametros[1]);

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        if(parametros[2].equals("0"))
            ((ImageView)popup.findViewById(R.id.info_img_dinheiro)).setColorFilter(filter);
        if(parametros[3].equals("0"))
            ((ImageView)popup.findViewById(R.id.info_img_celular)).setColorFilter(filter);
        if(parametros[4].equals("0"))
            ((ImageView)popup.findViewById(R.id.info_img_veiculo)).setColorFilter(filter);
        if(parametros[5].equals("0"))
            ((ImageView)popup.findViewById(R.id.info_img_cartao)).setColorFilter(filter);
        if(parametros[6].equals("0"))
            ((ImageView)popup.findViewById(R.id.info_img_carteira)).setColorFilter(filter);
        if(parametros[7].equals("0"))
            ((ImageView)popup.findViewById(R.id.info_img_bolsa)).setColorFilter(filter);
        if(parametros[8].equals("0"))
            ((ImageView)popup.findViewById(R.id.info_img_bicicleta)).setColorFilter(filter);
        if(parametros[9].equals("0"))
            ((ImageView)popup.findViewById(R.id.info_img_documentos)).setColorFilter(filter);
        if(parametros[10].equals("0"))
            ((ImageView)popup.findViewById(R.id.info_img_outros)).setColorFilter(filter);

        if(parametros[11].equals("1"))
            ((ImageView)popup.findViewById(R.id.info_img_dia_noite)).setImageResource(R.drawable.ic_noite);



        return popup;
    }

    @Override
    public View getInfoContents(Marker marker) {
        //final View popup = inflater.inflate(R.layout.info_marker_screen, null);

        //((TextView) popup.findViewById(R.id.title)).setText(marker.getSnippet());

        return null;
    }
}
