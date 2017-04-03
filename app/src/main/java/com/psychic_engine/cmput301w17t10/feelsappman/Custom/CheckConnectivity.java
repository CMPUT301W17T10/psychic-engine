package com.psychic_engine.cmput301w17t10.feelsappman.Custom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ElasticMoodController;
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ElasticParticipantController;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;

import java.util.ArrayList;

//http://stackoverflow.com/questions/21254555/how-to-check-internet-connectivity-continuously-in-background-while-android-appl
//obtained April 1, 2017
//by Ajay Venugopal

/**
 * Constant connectivity status checker that will determine when the app has available connection. At
 * this time, we can start to sync up the mood events. Upon disconnection, it will bring a popup
 * stating so. At this point on, mood events could still be done locally for yourself, but it will
 * not be saved in the server, thus other areas of the app will be unavailable.
 */
public class CheckConnectivity extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent arg1) {
        ParticipantSingleton instance = ParticipantSingleton.getInstance();
        boolean isConnected = arg1.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if(isConnected){
            Toast.makeText(context, "Internet Connection Lost", Toast.LENGTH_LONG).show();
            Log.i("Internet", "Internet connection lost");
        }
        else{
            Log.i("Internet", "Internet connection lost");
            Toast.makeText(context, "Internet Connected", Toast.LENGTH_LONG).show();

            Toast.makeText(context, "Attempting to sync your profile", Toast.LENGTH_LONG).show();
            Participant updateSelf = instance.getSelfParticipant();
            ElasticParticipantController.UpdateParticipantTask update = new ElasticParticipantController
                    .UpdateParticipantTask();
            update.execute(updateSelf);

            ArrayList<MoodEvent> deleteMoodList = instance.getOfflineDeleteList();
            ElasticMoodController.DeleteOfflineMoodEventTask delete = new ElasticMoodController.DeleteOfflineMoodEventTask();
            delete.execute(deleteMoodList);

            // add to server
            ArrayList<MoodEvent> addMoodList = instance.getOfflineCreatedList();
            ElasticMoodController.AddOfflineMoodEventTask add = new ElasticMoodController.AddOfflineMoodEventTask();
            add.execute(addMoodList);

            // update edits
            ArrayList<MoodEvent> editMoodsList = instance.getOfflineEditList();
            ElasticMoodController.UpdateOfflineMoodTask updateList = new ElasticMoodController.UpdateOfflineMoodTask();
            updateList.execute(editMoodsList);


        }
    }
}
