package com.psychic_engine.cmput301w17t10.feelsappman.Fragments;

/**
 * Created by jordi on 2017-03-09.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.psychic_engine.cmput301w17t10.feelsappman.Custom.DayAxisValueFormatter;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.R;

import java.util.ArrayList;


public class SummaryTabFragment extends Fragment {
    //https://github.com/PhilJay/MPAndroidChart/issues/789

    protected LineChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;

    Participant participant;
    ArrayList<MoodEvent> moodEventList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.summary, container, false);

        tvX = (TextView) getActivity().findViewById(R.id.tvXMax);
        tvY = (TextView) getActivity().findViewById(R.id.tvYMax);

        seekBarX = (SeekBar) getActivity().findViewById(R.id.seekBar1);
        seekBarY = (SeekBar) getActivity().findViewById(R.id.seekBar2);

        chart = (LineChart) getActivity().findViewById(R.id.chart);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);   // intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMaximum(10f);
        leftAxis.setAxisMinimum(0f);

        chart.getAxisRight().setEnabled(false);





        /*
        participant = ParticipantSingleton.getInstance().getSelfParticipant();
        moodEventList = participant.getMoodList();


        // Defining data for the chart
        Map<String, Integer> moodCountMap = new HashMap<String, Integer>();
        for (MoodState moodState : MoodState.values())
            moodCountMap.put(moodState.toString(), new Integer(0));


        // calculating frequency of each mood
        for (MoodEvent moodEvent  : moodEventList)
            moodCountMap.put(moodEvent.toString(), moodCountMap.get(moodEvent.toString() + 1));


        List<Entry> entries = new ArrayList<Entry>();
        for (String mood : moodCountMap.keySet()) {

            entries.add(new Entry(mood, (float) moodCountMap.get(mood)));

        }



        BarChart chart = new BarChart(getContext());

        // Set the bar char title
        String name = participant.getLogin();
        String title = name.toUpperCase() + "'s mood history".toUpperCase();
        BarDataSet dataset = new BarDataSet(entries, title);

        // define the charts data
        BarData data = new BarData(dataset);
        chart.setData(data);

        // Display bar chart
        getActivity().setContentView(chart);
*/






        return rootView;
    }
}
