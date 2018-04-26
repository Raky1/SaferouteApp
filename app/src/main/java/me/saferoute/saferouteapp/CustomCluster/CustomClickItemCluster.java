package me.saferoute.saferouteapp.CustomCluster;

import android.content.Context;
import android.util.Log;

import com.google.maps.android.clustering.ClusterManager;

import me.saferoute.saferouteapp.Model.Ocorrencia;

public class CustomClickItemCluster implements ClusterManager.OnClusterItemClickListener<Ocorrencia> {

    private Context context;

    public CustomClickItemCluster(Context context) {
        this.context = context;
    }

    @Override
    public boolean onClusterItemClick(Ocorrencia ocorrencia) {
        Log.d("INFO", "click marker");
        return false;
    }
}
