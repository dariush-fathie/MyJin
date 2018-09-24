package myjin.pro.ahoora.myjin.customClasses;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class OwnIconRendered extends DefaultClusterRenderer<Clusters> {

    public OwnIconRendered(Context context, GoogleMap map,
                           ClusterManager<Clusters> clusterManager) {

        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(Clusters item, MarkerOptions markerOptions) {


        markerOptions.icon
                (BitmapDescriptorFactory
                        .fromBitmap(item.icon));

        markerOptions.snippet(item.getMSnippet());
        markerOptions.title(item.getMTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);



    }
}
