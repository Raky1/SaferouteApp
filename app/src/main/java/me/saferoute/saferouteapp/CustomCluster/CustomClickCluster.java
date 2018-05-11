package me.saferoute.saferouteapp.CustomCluster;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import me.saferoute.saferouteapp.Model.Ocorrencia;

public class CustomClickCluster implements ClusterManager.OnClusterClickListener<Ocorrencia> {

    private Context context;
    private GoogleMap mMap;

    public CustomClickCluster(Context context, GoogleMap mMap) {
        this.context = context;
        this.mMap = mMap;
    }

    @Override
    public boolean onClusterClick(Cluster<Ocorrencia> cluster) {
        Log.d("INFO", "click cluster");
        mMap.moveCamera(CameraUpdateFactory.zoomIn());
        return false;
    }
}
