package com.psychic_engine.cmput301w17t10.feelsappman.Fragments;

/**
 * Created by jordi on 2017-03-09.
 *
 */

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.psychic_engine.cmput301w17t10.feelsappman.Custom.DateConverter;
import com.psychic_engine.cmput301w17t10.feelsappman.Custom.DayAxisValueFormatter;
import com.psychic_engine.cmput301w17t10.feelsappman.Custom.MutableInteger;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodColor;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Mood;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.MoodEvent;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.Participant;
import com.psychic_engine.cmput301w17t10.feelsappman.Models.ParticipantSingleton;
import com.psychic_engine.cmput301w17t10.feelsappman.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.graphics.Color.parseColor;

/**
 * @Author Jennifer Yuen
 * @References:
 * https://github.com/PhilJay/MPAndroidChart/issues/789
 * http://stackoverflow.com/questions/17192776/get-value-of-day-month-form-date-object-in-android
 * http://stackoverflow.com/questions/27979187/number-of-days-between-two-dates-in-android
 * http://blog.pengyifan.com/most-efficient-way-to-increment-a-map-value-in-java-only-search-the-key-once/
 *
 */


public class SummaryTabFragment extends Fragment implements
        OnChartGestureListener, OnChartValueSelectedListener {

    private static final String REFERENCE_DATE = "2017-01-01";
    private static final int BOUND_DAY = 1822;

    private ArrayList<MoodEvent> moodEventList;

    private ScatterChart mChart;
    private TextView chartTitle;
    private Spinner spinnerRange;
    private ImageButton datePicker;
    private ImageButton prev;
    private ImageButton next;

    int daysSince, range;

    Map<Integer, MutableInteger> sadDayToCountMap;
    Map<Integer, MutableInteger> happyDayToCountMap;
    Map<Integer, MutableInteger> shameDayToCountMap;
    Map<Integer, MutableInteger> fearDayToCountMap;
    Map<Integer, MutableInteger> angerDayToCountMap;
    Map<Integer, MutableInteger> disgustDayToCountMap;
    Map<Integer, MutableInteger> confusedDayToCountMap;
    Map<Integer, MutableInteger> surprisedDayToCountMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.summary, container, false);

        Participant participant = ParticipantSingleton.getInstance().getSelfParticipant();
        moodEventList = participant.getMoodList();

        spinnerRange = (Spinner) rootView.findViewById(R.id.spinnerTime1);
        datePicker = (ImageButton) rootView.findViewById(R.id.datePicker);
        prev = (ImageButton) rootView.findViewById(R.id.previous);
        next = (ImageButton) rootView.findViewById(R.id.next);

        mChart = (ScatterChart) rootView.findViewById(R.id.chart);
        chartTitle = (TextView) rootView.findViewById(R.id.chartTitle);

        // initialize start of graph view to current date
        Date now = new Date();
        daysSince = daysDiff(now);

        // Set chart, axis, and legend properties
        setChartProperties();
        setAxis();
        setLegend();

        //set day to count hash maps
        setDayToCountMaps();

        List<String> rangeSpinnerItems = new ArrayList<String>();
        rangeSpinnerItems.add("week");
        rangeSpinnerItems.add("month");
        rangeSpinnerItems.add("year");

        ArrayAdapter<String> rangeSpinnerAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, rangeSpinnerItems);

        spinnerRange.setAdapter(rangeSpinnerAdapter);

        spinnerRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    range = 7;
                } else if (position == 1) {
                    // Set graph view to the beginning of the month
                    int month = DateConverter.determineMonth(daysSince);
                    if (month == 3 || month == 5 || month == 8 || month == 10)
                        range = 30;
                    else
                        range = 31;

                    int year = DateConverter.determineYear(daysSince);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String formatedDate = sdf.format(new Date(year - 1900, month + 1, 1));
                    Date date = parseDate(formatedDate);
                    daysSince = daysDiff(date);

                } else if (position == 2) {
                    range = 365;
                }

                setData(range, daysSince);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //
            }

        });

        // TODO
        // limit how far back and front you can go (jan1,2017 - end of dateconverter)
        // fix prev and next
        // put repeated code into function
        // fix number of labels on x-axis (week - 7/8, month - ?, year - 12)
        // get rid of 0 values
        // make sure no off by 1s
        // fix UI clearer colors
        // set y axis label
        // fix UI spinners etc
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerRange.getSelectedItemPosition() == 1) {
                    daysSince = daysSince - 1; // get the previous month
                    int month = DateConverter.determineMonth(daysSince);
                    if (month == 3 || month == 5 || month == 8 || month == 10)
                        range = 30;
                    else
                        range = 31;

                    int year = DateConverter.determineYear(daysSince);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String formatedDate = sdf.format(new Date(year - 1900, month + 1, 1));
                    Date date = parseDate(formatedDate);
                    daysSince = daysDiff(date);
                }
                else {
                    daysSince -= range;
                }

                // check limits
                if (daysSince >= 0) {           // >= REFERENCE_DATE
                    setData(range, daysSince);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerRange.getSelectedItemPosition() == 1) {
                    daysSince = daysSince + range; // get the next month
                    int month = DateConverter.determineMonth(daysSince);
                    if (month == 3 || month == 5 || month == 8 || month == 10)
                        range = 30;
                    else
                        range = 31;

                    int year = DateConverter.determineYear(daysSince);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String formatedDate = sdf.format(new Date(year - 1900, month + 1, 1));
                    Date date = parseDate(formatedDate);
                    daysSince = daysDiff(date);
                }
                else {
                    daysSince += range;
                }

                // check limits
                if (daysSince <= BOUND_DAY - range) {
                    setData(range, daysSince);
                }
            }
        });


        datePicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();

                DatePickerDialog mDatePicker=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                       // standardize to days since 01/01/2017, used by the x-axis formatter
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String formatedDate = sdf.format(new Date(year - 1900, month, day));
                        Date viewDate = parseDate(formatedDate);
                        daysSince = daysDiff(viewDate);

                        setData(range, daysSince);
                    }
                }, mcurrentDate.get(Calendar.YEAR), mcurrentDate.get(Calendar.MONTH), mcurrentDate.get(Calendar.DAY_OF_MONTH));
                mDatePicker.setTitle("Select date");
                mDatePicker.show();  }
        });


        return rootView;
    }



    /**
     * Set the data to be displayed in the chart
     * @param num is the number of days to display
     * @param startDay is the start day to display
     */
    private void setData(int num, int startDay) {

        int m = DateConverter.determineMonth(startDay);
        int y = DateConverter.determineYear(startDay);
        int d = DateConverter.determineDayOfMonth(startDay, m + 12 * (y - 2017));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatedDate = sdf.format(new Date(y - 1900, m, d));
        chartTitle.setText(formatedDate);

        // Create an array of data points for each mood
        ArrayList<Entry> yValsSad = new ArrayList<Entry>();
        ArrayList<Entry> yValsHappy = new ArrayList<Entry>();
        ArrayList<Entry> yValsShame = new ArrayList<Entry>();
        ArrayList<Entry> yValsFear = new ArrayList<Entry>();
        ArrayList<Entry> yValsAnger = new ArrayList<Entry>();
        ArrayList<Entry> yValsDisgust = new ArrayList<Entry>();
        ArrayList<Entry> yValsConfused = new ArrayList<Entry>();
        ArrayList<Entry> yValsSurprised = new ArrayList<Entry>();


        // the start of the window
        //float start = 0;
        //float start = mSeekBarStart.getProgress() + 1;

        // the density of the window
        for (int i = (int) startDay; i < startDay + num + 1; i++) {

            addEntry(yValsSad, sadDayToCountMap, i);
            addEntry(yValsHappy, happyDayToCountMap, i);
            addEntry(yValsShame, shameDayToCountMap, i);
            addEntry(yValsFear, fearDayToCountMap, i);
            addEntry(yValsAnger, angerDayToCountMap, i);
            addEntry(yValsDisgust, disgustDayToCountMap, i);
            addEntry(yValsConfused, confusedDayToCountMap, i);
            addEntry(yValsSurprised, surprisedDayToCountMap, i);

        }


        ScatterDataSet setSad;
        ScatterDataSet setHappy;
        ScatterDataSet setShame;
        ScatterDataSet setFear;
        ScatterDataSet setAnger;
        ScatterDataSet setDisgust;
        ScatterDataSet setConfused;
        ScatterDataSet setSurprised;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {

            setSad = (ScatterDataSet) mChart.getData().getDataSetByIndex(0);
            setSad.setValues(yValsSad);

            setHappy = (ScatterDataSet) mChart.getData().getDataSetByIndex(1);
            setHappy.setValues(yValsHappy);

            setShame = (ScatterDataSet) mChart.getData().getDataSetByIndex(2);
            setShame.setValues(yValsShame);

            setFear = (ScatterDataSet) mChart.getData().getDataSetByIndex(3);
            setFear.setValues(yValsFear);

            setAnger = (ScatterDataSet) mChart.getData().getDataSetByIndex(4);
            setAnger.setValues(yValsAnger);

            setDisgust = (ScatterDataSet) mChart.getData().getDataSetByIndex(5);
            setDisgust.setValues(yValsDisgust);

            setConfused = (ScatterDataSet) mChart.getData().getDataSetByIndex(6);
            setConfused.setValues(yValsConfused);

            setSurprised = (ScatterDataSet) mChart.getData().getDataSetByIndex(7);
            setSurprised.setValues(yValsSurprised);

            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();

        } else {

            setSad = new ScatterDataSet(yValsSad, "sad");
            setHappy = new ScatterDataSet(yValsHappy, "happy");
            setShame = new ScatterDataSet(yValsShame, "shame");
            setFear = new ScatterDataSet(yValsFear, "fear");
            setAnger = new ScatterDataSet(yValsAnger, "anger");
            setDisgust = new ScatterDataSet(yValsDisgust, "disgust");
            setConfused = new ScatterDataSet(yValsConfused, "confused");
            setSurprised = new ScatterDataSet(yValsSurprised, "surprised");

            // Styling
            setSad.setColor(parseColor(MoodColor.BLUE.getBGColor()));
            setSad.setScatterShapeSize(50f);
            //setSad.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);

            setHappy.setColor(parseColor(MoodColor.GREEN.getBGColor()));
            setHappy.setScatterShapeSize(50f);
            setHappy.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);

            setShame.setColor(parseColor(MoodColor.PURPLE.getBGColor()));
            setShame.setScatterShapeSize(50f);
            setShame.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);

            setFear.setColor(parseColor(MoodColor.ORANGE.getBGColor()));
            setFear.setScatterShapeSize(50f);
            setFear.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);

            setAnger.setColor(parseColor(MoodColor.RED.getBGColor()));
            setAnger.setScatterShapeSize(50f);
            setAnger.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);

            setDisgust.setColor(parseColor(MoodColor.BROWN.getBGColor()));
            setDisgust.setScatterShapeSize(50f);
            setDisgust.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);

            setConfused.setColor(parseColor(MoodColor.YELLOW.getBGColor()));
            setConfused.setScatterShapeSize(50f);
            setConfused.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);

            setSurprised.setColor(parseColor(MoodColor.PINK.getBGColor()));
            setSurprised.setScatterShapeSize(50f);
            setSurprised.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);


            ArrayList<IScatterDataSet> dataSets = new ArrayList<IScatterDataSet>();
            dataSets.add(setSad);
            dataSets.add(setHappy);
            dataSets.add(setShame);
            dataSets.add(setFear);
            dataSets.add(setAnger);
            dataSets.add(setDisgust);
            dataSets.add(setConfused);
            dataSets.add(setSurprised);

            ScatterData data = new ScatterData(dataSets);
            data.setValueTextSize(10f);

            mChart.setData(data);
        }

        mChart.invalidate();

    }

    private void setDayToCountMaps() {

        sadDayToCountMap = new HashMap();
        happyDayToCountMap = new HashMap();
        shameDayToCountMap = new HashMap();
        fearDayToCountMap = new HashMap();
        angerDayToCountMap = new HashMap();
        disgustDayToCountMap = new HashMap();
        confusedDayToCountMap = new HashMap();
        surprisedDayToCountMap = new HashMap();

        int days;
        MutableInteger count;

        for (MoodEvent moodEvent : moodEventList) {

            // standardize days to reference start day
            days = daysDiff(moodEvent.getDate());

            switch (moodEvent.getMood().getMood()) {

                // For each mood, createMoodEvent mappings from days to mood count for that day
                case SAD:
                    if (sadDayToCountMap.containsKey(days)) {
                        count = sadDayToCountMap.get(days);
                        count.set(count.get() + 1);
                    }
                    else {
                        sadDayToCountMap.put(days, new MutableInteger(1));
                    }
                    break;
                case HAPPY:
                    if (happyDayToCountMap.containsKey(days)) {
                        count = happyDayToCountMap.get(days);
                        count.set(count.get() + 1);
                    }
                    else {
                        happyDayToCountMap.put(days, new MutableInteger(1));
                    }
                    break;
                case SHAME:
                    if (shameDayToCountMap.containsKey(days)) {
                        count = shameDayToCountMap.get(days);
                        count.set(count.get() + 1);
                    }
                    else {
                        shameDayToCountMap.put(days, new MutableInteger(1));
                    }
                    break;
                case FEAR:
                    if (fearDayToCountMap.containsKey(days)) {
                        count = fearDayToCountMap.get(days);
                        count.set(count.get() + 1);
                    }
                    else {
                        fearDayToCountMap.put(days, new MutableInteger(1));
                    }
                    break;
                case ANGER:
                    if (angerDayToCountMap.containsKey(days)) {
                        count = angerDayToCountMap.get(days);
                        count.set(count.get() + 1);
                    }
                    else {
                        angerDayToCountMap.put(days, new MutableInteger(1));
                    }
                    break;
                case DISGUST:
                    if (disgustDayToCountMap.containsKey(days)) {
                        count = disgustDayToCountMap.get(days);
                        count.set(count.get() + 1);
                    }
                    else {
                        disgustDayToCountMap.put(days, new MutableInteger(1));
                    }
                    break;
                case CONFUSED:
                    if (confusedDayToCountMap.containsKey(days)) {
                        count = confusedDayToCountMap.get(days);
                        count.set(count.get() + 1);
                    }
                    else {
                        confusedDayToCountMap.put(days, new MutableInteger(1));
                    }
                    break;
                case SURPRISED:
                    if (surprisedDayToCountMap.containsKey(days)) {
                        count = surprisedDayToCountMap.get(days);
                        count.set(count.get() + 1);
                    }
                    else {
                        surprisedDayToCountMap.put(days, new MutableInteger(1));
                    }
                    break;
                default:
                    // pass
            }
        }
    }

    /**
     * Adds an entry to yVals where the x-value corresponds to the day and
     * the y-value corresponds to the mood frequency on that day.
     * @param yVals stores the entries for a dataset of the line graph
     * @param moodCountMap stores the x and y values for the line graph
     * @param day corresponds to the day the mood was felt, used as a key in moodCountMap
     */
    private void addEntry(ArrayList<Entry> yVals,  Map<Integer, MutableInteger> moodCountMap, int day) {

        MutableInteger val = moodCountMap.get(day);

        if (val != null)
            yVals.add(new Entry(day, val.toFloat()));
        else
            yVals.add(new Entry(day, 0));   // TODO: not have this

    }

    /**
     * Set chart properties
     */
    private void setChartProperties() {
        // TODO add title, x, y axis description
        mChart.getDescription().setEnabled(false);
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.setOnChartValueSelectedListener(this);
        //mChart.setVisibleXRangeMaximum(7f);
        //mChart.setVisibleXRangeMinimum(7f);
        mChart.setVisibleXRange(7f, 7f);

    }

    /**
     * Set axis properties
     */
    private void setAxis() {

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        //xAxis.setLabelCount(7);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(5f);
        leftAxis.setGranularity(1f);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

    }

    /**
     * Set legend properties
     */
    private void setLegend() {

        Legend legend = mChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(true);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(9f);
        legend.setTextSize(10f);
        legend.setXEntrySpace(4f);

    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOWHIGH", "low: " + mChart.getLowestVisibleX() + ", high: " + mChart.getHighestVisibleX());
        Log.i("MIN MAX", "xmin: " + mChart.getXChartMin() + ", xmax: " + mChart.getXChartMax() + ", ymin: " + mChart.getYChartMin() + ", ymax: " + mChart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }


    private static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Finds the difference in days since the beginning of the year
     * @param date
     * @return
     */
    private int daysDiff(Date date) {
        Date refDate = parseDate(REFERENCE_DATE);
        long diff = date.getTime() - refDate.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;    // DayAxisFormatter begins at day 0, 01/01/2017 is day 1
    }

}


         /*~********************************************************
        /                         TEST DATA                       /
        *********************************************************/


//        participant.getMoodList().clear();
//
//        Mood testSadMood = new Mood(MoodState.SAD);
//        MoodEvent testSad1 = new MoodEvent(testSadMood, null, "", null, null);
//        MoodEvent testSad2 = new MoodEvent(testSadMood, null, "", null, null);
//        MoodEvent testSad3 = new MoodEvent(testSadMood, null, "", null, null);
//        MoodEvent testSad4 = new MoodEvent(testSadMood, null, "", null, null);
//        MoodEvent testSad5 = new MoodEvent(testSadMood, null, "", null, null);
//        MoodEvent testSad6 = new MoodEvent(testSadMood, null, "", null, null);
//        MoodEvent testSad7 = new MoodEvent(testSadMood, null, "", null, null);
//        MoodEvent testSad8 = new MoodEvent(testSadMood, null, "", null, null);
//
//        testSad1.setDate(parseDate("2017-03-25"));
//        testSad2.setDate(parseDate("2017-03-25"));
//        testSad3.setDate(parseDate("2017-04-01"));
//        testSad4.setDate(parseDate("2017-05-01"));
//        testSad5.setDate(parseDate("2017-05-15"));
//        testSad6.setDate(parseDate("2018-03-25"));
//        testSad7.setDate(parseDate("2019-03-25"));
//        testSad8.setDate(parseDate("2020-03-25"));
//
//        participant.addMoodEvent(testSad1);
//        participant.addMoodEvent(testSad2);
//        participant.addMoodEvent(testSad3);
//        participant.addMoodEvent(testSad4);
//        participant.addMoodEvent(testSad5);
//        participant.addMoodEvent(testSad6);
//        participant.addMoodEvent(testSad7);
//        participant.addMoodEvent(testSad8);
//
//
//
//
//
//        Mood testHappyMood = new Mood(MoodState.HAPPY);
//        MoodEvent testHappy1 = new MoodEvent(testHappyMood, null, "", null, null);
//        MoodEvent testHappy2 = new MoodEvent(testHappyMood, null, "", null, null);
//        MoodEvent testHappy3 = new MoodEvent(testHappyMood, null, "", null, null);
//        MoodEvent testHappy4 = new MoodEvent(testHappyMood, null, "", null, null);
//        MoodEvent testHappy5 = new MoodEvent(testHappyMood, null, "", null, null);
//        MoodEvent testHappy6 = new MoodEvent(testHappyMood, null, "", null, null);
//        MoodEvent testHappy7 = new MoodEvent(testHappyMood, null, "", null, null);
//        MoodEvent testHappy8 = new MoodEvent(testHappyMood, null, "", null, null);
//
//        testHappy1.setDate(parseDate("2017-03-27"));
//        testHappy2.setDate(parseDate("2017-03-27"));
//        testHappy3.setDate(parseDate("2017-03-27"));
//        testHappy4.setDate(parseDate("2017-04-25"));
//        testHappy5.setDate(parseDate("2017-04-25"));
//        testHappy6.setDate(parseDate("2017-04-28"));
//        testHappy7.setDate(parseDate("2019-03-23"));
//        testHappy8.setDate(parseDate("2020-03-25"));
//
//        participant.addMoodEvent(testHappy1);
//        participant.addMoodEvent(testHappy2);
//        participant.addMoodEvent(testHappy3);
//        participant.addMoodEvent(testHappy4);
//        participant.addMoodEvent(testHappy5);
//        participant.addMoodEvent(testHappy6);
//        participant.addMoodEvent(testHappy7);
//        participant.addMoodEvent(testHappy8);
//
//
//
//        Mood testShameMood = new Mood(MoodState.SHAME);
//
//        MoodEvent testShame1 = new MoodEvent(testShameMood, null, "", null, null);
//
//        testShame1.setDate(parseDate("2017-03-29"));
//
//        participant.addMoodEvent(testShame1);


        /*~*********************************************************
         *                         TEST DATA                       /
         *********************************************************/
