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
import android.widget.Button;
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
import com.psychic_engine.cmput301w17t10.feelsappman.Controllers.ParticipantController;
import com.psychic_engine.cmput301w17t10.feelsappman.Custom.DateConverter;
import com.psychic_engine.cmput301w17t10.feelsappman.Custom.DayAxisValueFormatter;
import com.psychic_engine.cmput301w17t10.feelsappman.Custom.MutableInteger;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodColor;
import com.psychic_engine.cmput301w17t10.feelsappman.Enums.MoodState;
import com.psychic_engine.cmput301w17t10.feelsappman.Exceptions.EmptyMoodException;
import com.psychic_engine.cmput301w17t10.feelsappman.Exceptions.TriggerTooLongException;
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
    private static final int BOUND_DAY = 366 + 8;

    private ArrayList<MoodEvent> moodEventList;

    private ScatterChart mChart;
    private TextView chartTitle;
    private Spinner spinnerRange;
    private ImageButton datePicker;
    private ImageButton prev;
    private ImageButton next;

    // TODO: demo button
    private Button demo;

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
        //rangeSpinnerItems.add("year");

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
                    daysSince = dayToBeginningOfMonth();
                } else if (position == 2) {
                    range = 365;
                }

                setData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //
            }

        });

        //TODO demo
        demo = (Button) rootView.findViewById(R.id.demo);
        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demo();
                setDayToCountMaps();
                setData();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerRange.getSelectedItemPosition() == 1) {
                    daysSince = daysSince - 1; // get the previous month
                    daysSince = dayToBeginningOfMonth();
                }
                else {
                    daysSince -= range;
                }

                // check limits
                if (daysSince > 0) {           // > REFERENCE_DATE
                    setData();
                }
                else {
                    daysSince += range;
                }
            }
        });

        // TODO: make dates futureproof, resolve looping date issue
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerRange.getSelectedItemPosition() == 1) {
                    daysSince = daysSince + range; // get the next month
                    daysSince = dayToBeginningOfMonth();
                }
                else {
                    daysSince += range;
                }

                // check limits
                if (daysSince <= BOUND_DAY - range) {
                    setData();
                } else {
                    daysSince -= range;
                }
            }
        });


        datePicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Calendar mcurrentDate = Calendar.getInstance();
                int m = DateConverter.determineMonth(daysSince);
                int y = DateConverter.determineYear(daysSince);
                int d = DateConverter.determineDayOfMonth(daysSince, m + 12 * (y - 2017));

                DatePickerDialog mDatePicker=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                       // standardize to days since 01/01/2017, used by the x-axis formatter
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String formatedDate = sdf.format(new Date(year - 1900, month, day));
                        Date viewDate = parseDate(formatedDate);
                        daysSince = daysDiff(viewDate);

                        setData();
                    }
                }, //mcurrentDate.get(Calendar.YEAR), mcurrentDate.get(Calendar.MONTH), mcurrentDate.get(Calendar.DAY_OF_MONTH));
                    y, m, d);
                mDatePicker.setTitle("Select date");

                mDatePicker.getDatePicker().setMinDate(parseDate("2017-01-01").getTime());
                mDatePicker.getDatePicker().setMaxDate(parseDate("2017-12-31").getTime());

                mDatePicker.show();  }
        });


        return rootView;
    }



    /**
     * Set the data to be displayed in the chart
     */
    private void setData() {

        int m = DateConverter.determineMonth(daysSince);
        int y = DateConverter.determineYear(daysSince);
        int d = DateConverter.determineDayOfMonth(daysSince, m + 12 * (y - 2017));
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


        // the density of the window
        for (int i = (int) daysSince; i < daysSince + range + 1; i++) {

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
            setSad.setColor(parseColor(MoodColor.BLUE.getChartColor()));
            setSad.setScatterShapeSize(80f);
            setSad.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);

            setHappy.setColor(parseColor(MoodColor.GREEN.getChartColor()));
            setHappy.setScatterShapeSize(80f);
            setHappy.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);

            setShame.setColor(parseColor(MoodColor.PURPLE.getChartColor()));
            setShame.setScatterShapeSize(80f);
            setShame.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);

            setFear.setColor(parseColor(MoodColor.ORANGE.getChartColor()));
            setFear.setScatterShapeSize(80f);
            setFear.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);

            setAnger.setColor(parseColor(MoodColor.RED.getChartColor()));
            setAnger.setScatterShapeSize(80f);
            setAnger.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);

            setDisgust.setColor(parseColor(MoodColor.BROWN.getChartColor()));
            setDisgust.setScatterShapeSize(80f);
            setDisgust.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);

            setConfused.setColor(parseColor(MoodColor.YELLOW.getChartColor()));
            setConfused.setScatterShapeSize(80f);
            setConfused.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);

            setSurprised.setColor(parseColor(MoodColor.PINK.getChartColor()));
            setSurprised.setScatterShapeSize(80f);
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
        mChart.getDescription().setEnabled(false);
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.setOnChartValueSelectedListener(this);
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
        leftAxis.setAxisMinimum(0.5f);
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

    private int dayToBeginningOfMonth() {
        int month = DateConverter.determineMonth(daysSince);
        int year = DateConverter.determineYear(daysSince);
        range = DateConverter.getDaysForMonth(month, year);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatedDate = sdf.format(new Date(year - 1900, month, 1));
        Date date = parseDate(formatedDate);
        return daysDiff(date);
    }

    /**
     * Finds the difference in days since the beginning of the year
     * @param date
     * @return
     */
    private int daysDiff(Date date) {
        Date refDate = parseDate(REFERENCE_DATE);
        return DateConverter.getDaysBetween(refDate, date) + 1; // DayAxisFormatter begins at day 0, 01/01/2017 is day 1

    }

    // TODO demo
    private void demo() {
        Participant participant = ParticipantSingleton.getInstance().getSelfParticipant();

        Mood testSadMood = new Mood(MoodState.SAD);
        Mood testHappyMood = new Mood(MoodState.HAPPY);
        Mood testShameMood = new Mood(MoodState.SHAME);
        Mood testFearMood = new Mood(MoodState.FEAR);
        Mood testAngerMood = new Mood(MoodState.ANGER);
        Mood testSurprisedMood = new Mood(MoodState.SURPRISED);
        Mood testDisgustMood = new Mood(MoodState.DISGUST);
        Mood testConfusedMood = new Mood(MoodState.CONFUSED);

        try {
            MoodEvent testSad1 = new MoodEvent(testSadMood, null, "", null, null);
            MoodEvent testSad2 = new MoodEvent(testSadMood, null, "", null, null);
            MoodEvent testSad3 = new MoodEvent(testSadMood, null, "", null, null);
            MoodEvent testSad4 = new MoodEvent(testSadMood, null, "", null, null);
            MoodEvent testSad5 = new MoodEvent(testSadMood, null, "", null, null);
            MoodEvent testSad6 = new MoodEvent(testSadMood, null, "", null, null);
            MoodEvent testSad7 = new MoodEvent(testSadMood, null, "", null, null);
            MoodEvent testSad8 = new MoodEvent(testSadMood, null, "", null, null);

            testSad1.setDate(parseDate("2017-03-25"));
            testSad2.setDate(parseDate("2017-03-25"));
            testSad3.setDate(parseDate("2017-04-05"));
            testSad4.setDate(parseDate("2017-05-01"));
            testSad5.setDate(parseDate("2017-05-15"));
            testSad6.setDate(parseDate("2017-05-15"));
            testSad7.setDate(parseDate("2017-05-25"));
            testSad8.setDate(parseDate("2017-05-30"));

            ParticipantController.addMoodEvent(testSad1);
            ParticipantController.addMoodEvent(testSad2);
            ParticipantController.addMoodEvent(testSad3);
            ParticipantController.addMoodEvent(testSad4);
            ParticipantController.addMoodEvent(testSad5);
            ParticipantController.addMoodEvent(testSad6);
            ParticipantController.addMoodEvent(testSad7);
            ParticipantController.addMoodEvent(testSad8);

            //////////////////////////////////////////////////////////////////////////////

            MoodEvent testHappy1 = new MoodEvent(testHappyMood, null, "", null, null);
            MoodEvent testHappy2 = new MoodEvent(testHappyMood, null, "", null, null);
            MoodEvent testHappy3 = new MoodEvent(testHappyMood, null, "", null, null);
            MoodEvent testHappy4 = new MoodEvent(testHappyMood, null, "", null, null);
            MoodEvent testHappy5 = new MoodEvent(testHappyMood, null, "", null, null);
            MoodEvent testHappy6 = new MoodEvent(testHappyMood, null, "", null, null);
            MoodEvent testHappy7 = new MoodEvent(testHappyMood, null, "", null, null);
            MoodEvent testHappy8 = new MoodEvent(testHappyMood, null, "", null, null);

            testHappy1.setDate(parseDate("2017-03-27"));
            testHappy2.setDate(parseDate("2017-03-28"));
            testHappy3.setDate(parseDate("2017-03-30"));
            testHappy4.setDate(parseDate("2017-04-25"));
            testHappy5.setDate(parseDate("2017-04-25"));
            testHappy6.setDate(parseDate("2017-04-28"));
            testHappy7.setDate(parseDate("2017-05-05"));
            testHappy8.setDate(parseDate("2017-05-07"));

            ParticipantController.addMoodEvent(testHappy1);
            ParticipantController.addMoodEvent(testHappy2);
            ParticipantController.addMoodEvent(testHappy3);
            ParticipantController.addMoodEvent(testHappy4);
            ParticipantController.addMoodEvent(testHappy5);
            ParticipantController.addMoodEvent(testHappy6);
            ParticipantController.addMoodEvent(testHappy7);
            ParticipantController.addMoodEvent(testHappy8);

            //////////////////////////////////////////////////////////////////////////////

            MoodEvent testShame1 = new MoodEvent(testShameMood, null, "", null, null);
            MoodEvent testShame2 = new MoodEvent(testShameMood, null, "", null, null);
            MoodEvent testShame3 = new MoodEvent(testShameMood, null, "", null, null);
            MoodEvent testShame4 = new MoodEvent(testShameMood, null, "", null, null);
            MoodEvent testShame5 = new MoodEvent(testShameMood, null, "", null, null);
            MoodEvent testShame6 = new MoodEvent(testShameMood, null, "", null, null);
            MoodEvent testShame7 = new MoodEvent(testShameMood, null, "", null, null);
            MoodEvent testShame8 = new MoodEvent(testShameMood, null, "", null, null);

            testShame1.setDate(parseDate("2017-04-01"));
            testShame2.setDate(parseDate("2017-04-03"));
            testShame3.setDate(parseDate("2017-04-04"));
            testShame4.setDate(parseDate("2017-04-14"));
            testShame5.setDate(parseDate("2017-04-14"));
            testShame6.setDate(parseDate("2017-04-14"));
            testShame7.setDate(parseDate("2017-05-01"));
            testShame8.setDate(parseDate("2017-05-02"));

            ParticipantController.addMoodEvent(testShame1);
            ParticipantController.addMoodEvent(testShame2);
            ParticipantController.addMoodEvent(testShame3);
            ParticipantController.addMoodEvent(testShame4);
            ParticipantController.addMoodEvent(testShame5);
            ParticipantController.addMoodEvent(testShame6);
            ParticipantController.addMoodEvent(testShame7);
            ParticipantController.addMoodEvent(testShame8);

            //////////////////////////////////////////////////////////////////////////////

            MoodEvent testFear1 = new MoodEvent(testFearMood, null, "", null, null);
            MoodEvent testFear2 = new MoodEvent(testFearMood, null, "", null, null);
            MoodEvent testFear3 = new MoodEvent(testFearMood, null, "", null, null);
            MoodEvent testFear4 = new MoodEvent(testFearMood, null, "", null, null);
            MoodEvent testFear5 = new MoodEvent(testFearMood, null, "", null, null);
            MoodEvent testFear6 = new MoodEvent(testFearMood, null, "", null, null);
            MoodEvent testFear7 = new MoodEvent(testFearMood, null, "", null, null);
            MoodEvent testFear8 = new MoodEvent(testFearMood, null, "", null, null);

            testFear1.setDate(parseDate("2017-03-03"));
            testFear2.setDate(parseDate("2017-03-10"));
            testFear3.setDate(parseDate("2017-03-10"));
            testFear4.setDate(parseDate("2017-03-28"));
            testFear5.setDate(parseDate("2017-04-19"));
            testFear6.setDate(parseDate("2017-04-20"));
            testFear7.setDate(parseDate("2017-04-20"));
            testFear8.setDate(parseDate("2017-05-03"));

            ParticipantController.addMoodEvent(testFear1);
            ParticipantController.addMoodEvent(testFear2);
            ParticipantController.addMoodEvent(testFear3);
            ParticipantController.addMoodEvent(testFear4);
            ParticipantController.addMoodEvent(testFear5);
            ParticipantController.addMoodEvent(testFear6);
            ParticipantController.addMoodEvent(testFear7);
            ParticipantController.addMoodEvent(testFear8);

            //////////////////////////////////////////////////////////////////////////////

            MoodEvent testAnger1 = new MoodEvent(testAngerMood, null, "", null, null);
            MoodEvent testAnger2 = new MoodEvent(testAngerMood, null, "", null, null);
            MoodEvent testAnger3 = new MoodEvent(testAngerMood, null, "", null, null);
            MoodEvent testAnger4 = new MoodEvent(testAngerMood, null, "", null, null);
            MoodEvent testAnger5 = new MoodEvent(testAngerMood, null, "", null, null);
            MoodEvent testAnger6 = new MoodEvent(testAngerMood, null, "", null, null);
            MoodEvent testAnger7 = new MoodEvent(testAngerMood, null, "", null, null);
            MoodEvent testAnger8 = new MoodEvent(testAngerMood, null, "", null, null);

            testAnger1.setDate(parseDate("2017-03-02"));
            testAnger2.setDate(parseDate("2017-03-12"));
            testAnger3.setDate(parseDate("2017-03-15"));
            testAnger4.setDate(parseDate("2017-03-15"));
            testAnger5.setDate(parseDate("2017-04-12"));
            testAnger6.setDate(parseDate("2017-04-22"));
            testAnger7.setDate(parseDate("2017-04-22"));
            testAnger8.setDate(parseDate("2017-05-23"));

            ParticipantController.addMoodEvent(testAnger1);
            ParticipantController.addMoodEvent(testAnger2);
            ParticipantController.addMoodEvent(testAnger3);
            ParticipantController.addMoodEvent(testAnger4);
            ParticipantController.addMoodEvent(testAnger5);
            ParticipantController.addMoodEvent(testAnger6);
            ParticipantController.addMoodEvent(testAnger7);
            ParticipantController.addMoodEvent(testAnger8);

            //////////////////////////////////////////////////////////////////////////////

            MoodEvent testSurprised1 = new MoodEvent(testSurprisedMood, null, "", null, null);
            MoodEvent testSurprised2 = new MoodEvent(testSurprisedMood, null, "", null, null);
            MoodEvent testSurprised3 = new MoodEvent(testSurprisedMood, null, "", null, null);
            MoodEvent testSurprised4 = new MoodEvent(testSurprisedMood, null, "", null, null);
            MoodEvent testSurprised5 = new MoodEvent(testSurprisedMood, null, "", null, null);
            MoodEvent testSurprised6 = new MoodEvent(testSurprisedMood, null, "", null, null);
            MoodEvent testSurprised7 = new MoodEvent(testSurprisedMood, null, "", null, null);
            MoodEvent testSurprised8 = new MoodEvent(testSurprisedMood, null, "", null, null);

            testSurprised1.setDate(parseDate("2017-03-18"));
            testSurprised2.setDate(parseDate("2017-03-19"));
            testSurprised3.setDate(parseDate("2017-03-20"));
            testSurprised4.setDate(parseDate("2017-04-27"));
            testSurprised5.setDate(parseDate("2017-04-27"));
            testSurprised6.setDate(parseDate("2017-04-27"));
            testSurprised7.setDate(parseDate("2017-05-11"));
            testSurprised8.setDate(parseDate("2017-05-11"));

            ParticipantController.addMoodEvent(testSurprised1);
            ParticipantController.addMoodEvent(testSurprised2);
            ParticipantController.addMoodEvent(testSurprised3);
            ParticipantController.addMoodEvent(testSurprised4);
            ParticipantController.addMoodEvent(testSurprised5);
            ParticipantController.addMoodEvent(testSurprised6);
            ParticipantController.addMoodEvent(testSurprised7);
            ParticipantController.addMoodEvent(testSurprised8);

            //////////////////////////////////////////////////////////////////////////////

            MoodEvent testDisgust1 = new MoodEvent(testDisgustMood, null, "", null, null);
            MoodEvent testDisgust2 = new MoodEvent(testDisgustMood, null, "", null, null);
            MoodEvent testDisgust3 = new MoodEvent(testDisgustMood, null, "", null, null);
            MoodEvent testDisgust4 = new MoodEvent(testDisgustMood, null, "", null, null);
            MoodEvent testDisgust5 = new MoodEvent(testDisgustMood, null, "", null, null);
            MoodEvent testDisgust6 = new MoodEvent(testDisgustMood, null, "", null, null);
            MoodEvent testDisgust7 = new MoodEvent(testDisgustMood, null, "", null, null);
            MoodEvent testDisgust8 = new MoodEvent(testDisgustMood, null, "", null, null);

            testDisgust1.setDate(parseDate("2017-05-20"));
            testDisgust2.setDate(parseDate("2017-05-20"));
            testDisgust3.setDate(parseDate("2017-05-20"));
            testDisgust4.setDate(parseDate("2017-05-20"));
            testDisgust5.setDate(parseDate("2017-05-28"));
            testDisgust6.setDate(parseDate("2017-05-29"));
            testDisgust7.setDate(parseDate("2017-03-26"));
            testDisgust8.setDate(parseDate("2017-04-26"));

            ParticipantController.addMoodEvent(testDisgust1);
            ParticipantController.addMoodEvent(testDisgust2);
            ParticipantController.addMoodEvent(testDisgust3);
            ParticipantController.addMoodEvent(testDisgust4);
            ParticipantController.addMoodEvent(testDisgust5);
            ParticipantController.addMoodEvent(testDisgust6);
            ParticipantController.addMoodEvent(testDisgust7);
            ParticipantController.addMoodEvent(testDisgust8);

            //////////////////////////////////////////////////////////////////////////////

            MoodEvent testConfused1 = new MoodEvent(testConfusedMood, null, "", null, null);
            MoodEvent testConfused2 = new MoodEvent(testConfusedMood, null, "", null, null);
            MoodEvent testConfused3 = new MoodEvent(testConfusedMood, null, "", null, null);
            MoodEvent testConfused4 = new MoodEvent(testConfusedMood, null, "", null, null);
            MoodEvent testConfused5 = new MoodEvent(testConfusedMood, null, "", null, null);
            MoodEvent testConfused6 = new MoodEvent(testConfusedMood, null, "", null, null);
            MoodEvent testConfused7 = new MoodEvent(testConfusedMood, null, "", null, null);
            MoodEvent testConfused8 = new MoodEvent(testConfusedMood, null, "", null, null);

            testConfused1.setDate(parseDate("2017-03-30"));
            testConfused2.setDate(parseDate("2017-03-30"));
            testConfused3.setDate(parseDate("2017-04-07"));
            testConfused4.setDate(parseDate("2017-04-07"));
            testConfused5.setDate(parseDate("2017-04-09"));
            testConfused6.setDate(parseDate("2017-04-19"));
            testConfused7.setDate(parseDate("2017-04-19"));
            testConfused8.setDate(parseDate("2017-04-19"));

            ParticipantController.addMoodEvent(testConfused1);
            ParticipantController.addMoodEvent(testConfused1);
            ParticipantController.addMoodEvent(testConfused1);
            ParticipantController.addMoodEvent(testConfused1);
            ParticipantController.addMoodEvent(testConfused1);
            ParticipantController.addMoodEvent(testConfused1);
            ParticipantController.addMoodEvent(testConfused1);
            ParticipantController.addMoodEvent(testConfused1);

        } catch (EmptyMoodException e) {
            //pass
        } catch (TriggerTooLongException e) {
            //pass
        }
    }

}



