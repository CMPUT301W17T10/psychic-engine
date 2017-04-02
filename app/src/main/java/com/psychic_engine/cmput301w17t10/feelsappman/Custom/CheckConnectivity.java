package com.psychic_engine.cmput301w17t10.feelsappman.Custom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ElasticParticipantController;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;

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
        }
    }
}
