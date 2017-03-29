package com.psychic_engine.cmput301w17t10.feelsappman.Activities;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.psychic_engine.cmput301w17t10.feelsappman.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

/**
 * Created by Pierre Lin on 3/28/2017.
 */

public class FilterMapActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        //Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_filter_map);

        MapView map = (MapView) findViewById(R.id.mapview);

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        IMapController mController = map.getController();

        Location coords = new Location("GPS");
        //This while loop is to keep trying to get a location from the GPS provider
        //prevents null pointer exception if you momentarily lose GPS


        coords = getCurrentLocation(coords);

        double lat = coords.getLatitude();
        double lon = coords.getLongitude();


        GeoPoint center = new GeoPoint(lat, lon);

        mController.setCenter(center);


        mController.setZoom(15);
    }


    public Location getCurrentLocation(Location coords) {
        //Taken from http://stackoverflow.com/questions/17584374/check-if-gps-and-or-mobile-network-location-is-enabled
        //March 27, 2017
        LocationManager lm;
        LocationListener locationListener;

        lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        //Create new Location object using provider
        Boolean gps = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Boolean network = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        //GPS service gets FINE location
        //Network provider gets COARSE location
        if (gps) {
            //Ignore warnings, permissions checked when activity starts
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            if (lm != null) {
                coords = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }

        }
        if (!gps && network) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            if (lm != null) {
                coords = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

        }
        return coords;
    }
}