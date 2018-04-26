package me.saferoute.saferouteapp.CustomCluster;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.google.maps.android.clustering.ClusterManager;

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
        view = inflater.inflate(R.layout.dialog_dicas, null);

    }
    @Override
    public void onClusterItemInfoWindowClick(Ocorrencia ocorrencia) {
        if(!ocorrencia.isDinheiro())
            //view.findViewById(R.id.lis)

        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }
}
