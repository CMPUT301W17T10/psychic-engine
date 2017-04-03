package com.psychic_engine.cmput301w17t10.feelsappman.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;

import com.psychic_engine.cmput301w17t10.feelsappman.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.TilesOverlay;

import java.util.ArrayList;

/**
 * Created by Pierre Lin on 3/28/2017.
 */

public class FilterMapActivity extends Activity {
    //Initializing Arraylist of OverlayItems
    private ArrayList<OverlayItem> sadEvents;
    private ArrayList<OverlayItem> happyEvents;
    private ArrayList<OverlayItem> shameEvents;
    private ArrayList<OverlayItem> fearEvents;
    private ArrayList<OverlayItem> angerEvents;
    private ArrayList<OverlayItem> surprisedEvents;
    private ArrayList<OverlayItem> disgustEvents;
    private ArrayList<OverlayItem> confusedEvents;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();

        setContentView(R.layout.activity_filter_map);

        MapView map = (MapView) findViewById(R.id.mapview);

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        IMapController mController = map.getController();

        TilesOverlay x=map.getOverlayManager().getTilesOverlay();
        x.setOvershootTileCache(x.getOvershootTileCache() * 2);

        ArrayList<MoodEvent> moodList = (ArrayList<MoodEvent>) getIntent().getExtras().getSerializable("moodEventList");
        

        //Initializing arrays of OverlayItems
        //each item in the array will be a moodEvent with the corresponding moodState
        sadEvents = new ArrayList<OverlayItem>();
        happyEvents = new ArrayList<OverlayItem>();
        shameEvents = new ArrayList<OverlayItem>();
        fearEvents = new ArrayList<OverlayItem>();
        angerEvents = new ArrayList<OverlayItem>();
        surprisedEvents = new ArrayList<OverlayItem>();
        disgustEvents = new ArrayList<OverlayItem>();
        confusedEvents = new ArrayList<OverlayItem>();

        //For each item in the filteredMoodList
        for (MoodEvent mood : moodList) {
            //if has location
            if (mood.getLocation() != null) {
                //get moodstate
                String moodType = mood.getMood().getMood().toString();

                if (moodType == "Sad") {
                    //Taken from http://stackoverflow.com/questions/10533071/osmdroid-add-custom-icons-to-itemizedoverlay
                    //March 29. 2017
                    //Initialize the item with the moodType, trigger and set a new Geopoint for it
                    OverlayItem item = new OverlayItem(moodType, mood.getTrigger(), mood.getLocation().getLoc());
                    //gets custom icon
                    Drawable sadMarker = this.getResources().getDrawable(R.drawable.sadmarker);
                    //set the icon of the overlayItem as the custom icon
                    item.setMarker(sadMarker);
                    //puts it in the arrayList of OverlayItems for this mood
                    sadEvents.add(item);
                }
                if (moodType == "Happy") {

                    OverlayItem item = new OverlayItem(moodType, mood.getTrigger(), mood.getLocation().getLoc());
                    Drawable happyMarker = this.getResources().getDrawable(R.drawable.happymarker);
                    item.setMarker(happyMarker);
                    happyEvents.add(item);
                }
                if (moodType == "Shame") {

                    OverlayItem item = new OverlayItem(moodType, mood.getTrigger(), mood.getLocation().getLoc());
                    Drawable shameMarker = this.getResources().getDrawable(R.drawable.shamemarker);
                    item.setMarker(shameMarker);
                    shameEvents.add(item);
                }
                if (moodType == "Fear") {

                    OverlayItem item = new OverlayItem(moodType, mood.getTrigger(), mood.getLocation().getLoc());
                    Drawable fearMarker = this.getResources().getDrawable(R.drawable.fearmarker);
                    item.setMarker(fearMarker);
                    fearEvents.add(item);
                }
                if (moodType == "Anger") {

                    OverlayItem item = new OverlayItem(moodType, mood.getTrigger(), mood.getLocation().getLoc());
                    Drawable angerMarker = this.getResources().getDrawable(R.drawable.angermarker);
                    item.setMarker(angerMarker);
                    angerEvents.add(item);
                }
                if (moodType == "Surprised") {

                    OverlayItem item = new OverlayItem(moodType, mood.getTrigger(), mood.getLocation().getLoc());
                    Drawable surprisedMarker = this.getResources().getDrawable(R.drawable.surprisedmarker);
                    item.setMarker(surprisedMarker);
                    surprisedEvents.add(item);
                }
                if (moodType == "Disgust") {

                    OverlayItem item = new OverlayItem(moodType, mood.getTrigger(), mood.getLocation().getLoc());
                    Drawable disgustMarker = this.getResources().getDrawable(R.drawable.disgustmarker);
                    item.setMarker(disgustMarker);
                    disgustEvents.add(item);
                }
                if (moodType == "Confused") {

                    OverlayItem item = new OverlayItem(moodType, mood.getTrigger(), mood.getLocation().getLoc());
                    Drawable confusedMarker = this.getResources().getDrawable(R.drawable.confusedmarker);
                    item.setMarker(confusedMarker);
                    confusedEvents.add(item);
                }
            }
        }
        //find current location using GPS provider
        Location coords = new Location("GPS");
        coords = getCurrentLocation(coords);

        //This while loop is to keep trying to get a location from the GPS provider
        //prevents null pointer exception if you momentarily lose GPS
        while (coords == null) {
            coords = getCurrentLocation(coords);
        }

        double lat = coords.getLatitude();
        double lon = coords.getLongitude();

        //Create new geopoint from current location
        GeoPoint center = new GeoPoint(lat, lon);

        //Center mapview on current location
        mController.setCenter(center);
        mController.setZoom(17);

        //Taken from http://stackoverflow.com/questions/41090639/add-marker-to-osmdroid-5-5-map
        //March 29, 2017

        //Creates our custom GestureListener so that when we tap on a marker, it displays
        //the trigger and the mood
        ItemizedIconOverlay.OnItemGestureListener customGlistener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                //do something
                AlertDialog.Builder dialog = new AlertDialog.Builder(FilterMapActivity.this);
                dialog.setTitle(item.getTitle());
                dialog.setMessage(item.getSnippet());
                dialog.show();
                return true;
            }
            @Override
            public boolean onItemLongPress(final int index, final OverlayItem item) {
                return false;
            }
        };

        //Each mood is represented by an ItemizedOverlayWithFocus
        //this is because each overlay can only have one icon
        ItemizedOverlayWithFocus<OverlayItem> sadOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this, sadEvents,  //  <--------- added Context this as first parameter
                customGlistener);
        ItemizedOverlayWithFocus<OverlayItem> happyOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this, happyEvents,
                customGlistener);
        ItemizedOverlayWithFocus<OverlayItem> shameOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this, shameEvents,
                customGlistener);
        ItemizedOverlayWithFocus<OverlayItem> fearOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this, fearEvents,
                customGlistener);
        ItemizedOverlayWithFocus<OverlayItem> angerOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this, angerEvents,
                customGlistener);
        ItemizedOverlayWithFocus<OverlayItem> surprisedOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this, surprisedEvents,
                customGlistener);
        ItemizedOverlayWithFocus<OverlayItem> disgustOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this, disgustEvents,
                customGlistener);
        ItemizedOverlayWithFocus<OverlayItem> confusedOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this, confusedEvents,
                customGlistener);


        //add the overlays for each mood to the mapView
        map.getOverlays().add(sadOverlay);
        map.getOverlays().add(happyOverlay);
        map.getOverlays().add(shameOverlay);
        map.getOverlays().add(fearOverlay);
        map.getOverlays().add(angerOverlay);
        map.getOverlays().add(surprisedOverlay);
        map.getOverlays().add(disgustOverlay);
        map.getOverlays().add(confusedOverlay);
        //so that the markers will editMoodEvent
        map.invalidate();
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

    //Taken from https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library
    //March 29, 2017
    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }
}