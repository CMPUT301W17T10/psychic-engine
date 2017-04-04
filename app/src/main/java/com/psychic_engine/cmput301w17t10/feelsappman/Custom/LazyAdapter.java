package com.psychic_engine.cmput301w17t10.feelsappman.Custom;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.R;

import static android.graphics.Color.parseColor;

public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater=null;
    private ArrayList<MoodEvent> data;

    public LazyAdapter(Activity a, ArrayList<MoodEvent> moodList) {
        activity = a;
        data = moodList;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.item_history, null);

        TextView state = (TextView)vi.findViewById(R.id.title); // title
        TextView name = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        RelativeLayout background = (RelativeLayout)vi.findViewById(R.id.background);

        MoodEvent moodEvent = data.get(position);

        // Setting all values in listview
        state.setText(moodEvent.getTrigger());
        name.setText(moodEvent.getMoodOwner());
        int color = parseColor(moodEvent.getMood().getColor().getBGColor());
        background.setBackgroundColor(color);
        duration.setText(moodEvent.getDate().toString());

        String defaultImage = moodEvent.getMood().getIconName();
        int drawableResourceId = activity.getResources().getIdentifier(defaultImage, "drawable", activity.getPackageName());
        thumb_image.setImageResource(drawableResourceId);

        //imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
        return vi;
    }
}
