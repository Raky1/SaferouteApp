package me.saferoute.saferouteapp.CustomCluster;

import android.content.Context;
import android.util.Log;

import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import me.saferoute.saferouteapp.Model.Ocorrencia;

public class CustomClickCluster implements ClusterManager.OnClusterClickListener<Ocorrencia> {

    private Context context;

    public CustomClickCluster(Context context) {
        this.context = context;
    }

    @Override
    public boolean onClusterClick(Cluster<Ocorrencia> cluster) {
        Log.d("INFO", "click cluster");
        return false;
    }
}
