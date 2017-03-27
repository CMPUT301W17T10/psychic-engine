package com.psychic_engine.cmput301w17t10.feelsappman.Fragments;

/**
 * Created by jordi on 2017-03-09.
 */

import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.psychic_engine.cmput301w17t10.feelsappman.Custom.DayAxisValueFormatter;
import com.psychic_engine.cmput301w17t10.feelsappman.Custom.MutableInteger;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author Jennifer Yuen
 *
 * @References:
 * https://github.com/PhilJay/MPAndroidChart/issues/789
 * http://stackoverflow.com/questions/17192776/get-value-of-day-month-form-date-object-in-android
 * http://stackoverflow.com/questions/27979187/number-of-days-between-two-dates-in-android
 * http://blog.pengyifan.com/most-efficient-way-to-increment-a-map-value-in-java-only-search-the-key-once/
 *
 */


public class SummaryTabFragment extends Fragment implements
        SeekBar.OnSeekBarChangeListener, OnChartValueSelectedListener {


    private BarChart mChart;
    private SeekBar mSeekBarDensity, mSeekBarStart; // TODO: 3rd seek bar for y axis range, right now its automatically scaling (which is fine)
    private TextView tvX, tvY;

    Participant participant;
    ArrayList<MoodEvent> moodEventList;
    int startDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.summary, container, false);

        participant = ParticipantSingleton.getInstance().getSelfParticipant();
        moodEventList = participant.getMoodList();

        tvX = (TextView) rootView.findViewById(R.id.tvXMax);
        tvY = (TextView) rootView.findViewById(R.id.tvYMax);

        mSeekBarDensity = (SeekBar) rootView.findViewById(R.id.seekBar1);
        mSeekBarStart = (SeekBar) rootView.findViewById(R.id.seekBar2);

        mChart = (BarChart) rootView.findViewById(R.id.chart);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        //IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        //leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        //rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
        //rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        mSeekBarStart.setProgress(0);        // TODO set to earliest mood/currently viewed mood/latest mood
        mSeekBarDensity.setProgress(0);      // TODO set to average / highest count

        mSeekBarStart.setOnSeekBarChangeListener(this);
        mSeekBarDensity.setOnSeekBarChangeListener(this);

        // mChart.setDrawLegend(false);

        return rootView;
    }

    /**
     * Called when the user adjusts the seek bar progress
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText("" + (mSeekBarDensity.getProgress() + 2));
        tvY.setText("" + (mSeekBarStart.getProgress()));

        setData(mSeekBarDensity.getProgress() + 1 , mSeekBarStart.getProgress());

        mChart.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    /**
     * Set the data to be displayed in the chart
     * @param num is the number of days to display
     * @param startDay is the start day to display
     */
    private void setData(int num, float startDay) {

        Map<Integer, MutableInteger> sadDayToCountMap = new HashMap();
        Map<Integer, MutableInteger> happyDayToCountMap = new HashMap();
        Map<Integer, MutableInteger> shameDayToCountMap = new HashMap();
        Map<Integer, MutableInteger> fearDayToCountMap = new HashMap();
        Map<Integer, MutableInteger> angerDayToCountMap = new HashMap();
        Map<Integer, MutableInteger> disgustDayToCountMap = new HashMap();
        Map<Integer, MutableInteger> confusedDayToCountMap = new HashMap();
        Map<Integer, MutableInteger> surprisedDayToCountMap = new HashMap();


         /*~********************************************************
        /                         TEST DATA                       /
        *********************************************************/

        Mood testSadMood = new Mood(MoodState.SAD);
        MoodEvent test1 = new MoodEvent(testSadMood, null, "", null, null);
        MoodEvent test2 = new MoodEvent(testSadMood, null, "", null, null);
        MoodEvent test3 = new MoodEvent(testSadMood, null, "", null, null);
        MoodEvent test4 = new MoodEvent(testSadMood, null, "", null, null);
        MoodEvent test5 = new MoodEvent(testSadMood, null, "", null, null);
        MoodEvent test6 = new MoodEvent(testSadMood, null, "", null, null);
        MoodEvent test7 = new MoodEvent(testSadMood, null, "", null, null);
        MoodEvent test8 = new MoodEvent(testSadMood, null, "", null, null);

        test1.setDate(parseDate("2017-03-25"));
        test2.setDate(parseDate("2017-03-25"));
        test3.setDate(parseDate("2017-04-01"));
        test4.setDate(parseDate("2017-05-01"));
        test5.setDate(parseDate("2017-05-15"));
        //test6.setDate(parseDate("2018-03-25"));
        //test7.setDate(parseDate("2019-03-25"));
        //test8.setDate(parseDate("2020-03-25"));

        participant.getMoodList().clear();
        participant.addMoodEvent(test1);
        participant.addMoodEvent(test2);
        participant.addMoodEvent(test3);
        participant.addMoodEvent(test4);
        participant.addMoodEvent(test5);
        //participant.addMoodEvent(test6);
        //participant.addMoodEvent(test7);
        //participant.addMoodEvent(test8);

        /*~*********************************************************
         *                         TEST DATA                       /
         *********************************************************/


        Date beginDate = parseDate("2017-01-01");
        long diff;
        int days;
        MutableInteger count;

        for (MoodEvent moodEvent : moodEventList) {

            diff = moodEvent.getDate().getTime() - beginDate.getTime();
            days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            switch (moodEvent.getMood().getMood()) {

                case SAD:
                    if (sadDayToCountMap.containsKey(days)) {
                        count = sadDayToCountMap.get(days);
                        count.set(count.get() + 1);
                    }
                    else {
                        sadDayToCountMap.put(days, new MutableInteger(1));
                    }
                case HAPPY:
                    if (happyDayToCountMap.containsKey(days)) {
                        count = happyDayToCountMap.get(days);
                        count.set(count.get() + 1);
                    }
                    else {
                        happyDayToCountMap.put(days, new MutableInteger(1));
                    }
                case SHAME:
                    if (shameDayToCountMap.containsKey(days)) {
                        count = shameDayToCountMap.get(days);
                        count.set(count.get() + 1);
                    }
                    else {
                        shameDayToCountMap.put(days, new MutableInteger(1));
                    }
                case FEAR:
                    if (fearDayToCountMap.containsKey(days)) {
                        count = fearDayToCountMap.get(days);
                        count.set(count.get() + 1);
                    }
                    else {
                        fearDayToCountMap.put(days, new MutableInteger(1));
                    }
                case ANGER:
                    if (angerDayToCountMap.containsKey(days)) {
                        count = angerDayToCountMap.get(days);
                        count.set(count.get() + 1);
                    }
                    else {
                        angerDayToCountMap.put(days, new MutableInteger(1));
                    }
                case DISGUST:
                    if (disgustDayToCountMap.containsKey(days)) {
                        count = disgustDayToCountMap.get(days);
                        count.set(count.get() + 1);
                    }
                    else {
                        disgustDayToCountMap.put(days, new MutableInteger(1));
                    }
                case CONFUSED:
                    if (confusedDayToCountMap.containsKey(days)) {
                        count = confusedDayToCountMap.get(days);
                        count.set(count.get() + 1);
                    }
                    else {
                        confusedDayToCountMap.put(days, new MutableInteger(1));
                    }
                case SURPRISED:
                    if (surprisedDayToCountMap.containsKey(days)) {
                        count = surprisedDayToCountMap.get(days);
                        count.set(count.get() + 1);
                    }
                    else {
                        surprisedDayToCountMap.put(days, new MutableInteger(1));
                    }
                default:
                    // pass
            }
        }


/*
        ArrayList<MoodEvent> copy = new ArrayList<MoodEvent>(moodEventList);
        Collections.sort(copy, new CustomComparator());
        MoodEvent earliestMoodEvent = copy.get(copy.size() - 1);

        diff = earliestMoodEvent.getDate().getTime() - beginDate.getTime();
        days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        startDay = (int) sadDayToCountMap.get(days).toFloat();

*/


        float start = mSeekBarStart.getProgress();

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = (int) start; i < start + num + 1; i++) {

            MutableInteger val = sadDayToCountMap.get(i);

            if (val != null)
                yVals1.add(new BarEntry(i, val.toFloat()));
            else
                yVals1.add(new BarEntry(i, 0));
        }


        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The year 2017");

            //set1.setDrawIcons(false);

            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            mChart.setData(data);
        }



    }

    protected RectF mOnValueSelectedRectF = new RectF();

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() { }



    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }


}
