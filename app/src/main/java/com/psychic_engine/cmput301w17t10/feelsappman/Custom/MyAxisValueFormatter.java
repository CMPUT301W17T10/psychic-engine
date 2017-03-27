package com.psychic_engine.cmput301w17t10.feelsappman.Custom;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import java.text.DecimalFormat;

/**
 * Created by jyuen1 on 3/27/17.
 */

/**
 * Taken from https://github.com/PhilJay/MPAndroidChart on 3/26/2017
 */

public class MyAxisValueFormatter implements IAxisValueFormatter
{

    private DecimalFormat mFormat;

    public MyAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + " $";
    }
}