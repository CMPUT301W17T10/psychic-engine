package com.psychic_engine.cmput301w17t10.feelsappman.Custom;

/**
 * Created by Jen on 4/2/2017.
 */

/**
 * Taken from https://github.com/PhilJay/MPAndroidChart on 3/26/2017
 */

public class DateConverter {

    public static int getDaysForMonth(int month, int year) {

        // month is 0-based

        if (month == 1) {
            boolean is29Feb = false;

            if (year < 1582)
                is29Feb = (year < 1 ? year + 1 : year) % 4 == 0;
            else if (year > 1582)
                is29Feb = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);

            return is29Feb ? 29 : 28;
        }

        if (month == 3 || month == 5 || month == 8 || month == 10)
            return 30;
        else
            return 31;
    }

    public static int determineMonth(int dayOfYear) {

        int month = -1;
        int days = 0;

        while (days < dayOfYear) {
            month = month + 1;

            if (month >= 12)
                month = 0;

            int year = determineYear(days);
            days += getDaysForMonth(month, year);
        }

        return Math.max(month, 0);
    }

    public static int determineDayOfMonth(int days, int month) {

        int count = 0;
        int daysForMonths = 0;

        while (count < month) {

            int year = determineYear(daysForMonths);
            daysForMonths += getDaysForMonth(count % 12, year);
            count++;
        }

        return days - daysForMonths;
    }

    public static int determineYear(int days) {

        if (days <= 366)
            return 2017;
        else if (days <= 730)
            return 2018;
        else if (days <= 1094)
            return 2019;
        else if (days <= 1458)
            return 2020;
        else
            return 2021;

    }

}
