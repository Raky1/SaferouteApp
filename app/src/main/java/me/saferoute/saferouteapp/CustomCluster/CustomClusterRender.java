package me.saferoute.saferouteapp.CustomCluster;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.Collection;
import java.util.List;

import me.saferoute.saferouteapp.MapsFragment;
import me.saferoute.saferouteapp.Model.Ocorrencia;

public class CustomClusterRender extends DefaultClusterRenderer{

    private final Context mContext;

    //private final int[] COLORS = {Color.BLUE, Color.RED, Color.GREEN, Color.GRAY, Color.YELLOW, Color.MAGENTA, Color.BLACK, Color.CYAN, Color.WHITE};

    public CustomClusterRender(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(ClusterItem item, MarkerOptions markerOptions) {
        final BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);

        //markerOptions.visible(((Ocorrencia) item).isVisible());
        markerOptions.icon(markerDescriptor).snippet(((Ocorrencia) item).toString());

    }

    @Override
    protected void onClusterRendered(Cluster cluster, Marker marker) {
        super.onClusterRendered(cluster, marker);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        return cluster.getSize() > 2;
    }

}
