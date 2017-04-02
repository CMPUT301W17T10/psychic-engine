package com.psychic_engine.cmput301w17t10.feelsappman.Custom;

/**
 * Created by Jen on 4/2/2017.
 */

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Taken from https://github.com/PhilJay/MPAndroidChart on 3/26/2017
 * Taken from http://stackoverflow.com/questions/7976989/java-get-days-between-two-dates
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

    public static int daysSince1900(Date date) {
        Calendar c = new GregorianCalendar();
        c.setTime(date);

        int year = c.get(Calendar.YEAR);
        if (year < 1900 || year > 2099) {
            throw new IllegalArgumentException("daysSince1900 - Date must be between 1900 and 2099");
        }
        year -= 1900;
        int month = c.get(Calendar.MONTH) + 1;
        int days = c.get(Calendar.DAY_OF_MONTH);

        if (month < 3) {
            month += 12;
            year--;
        }
        int yearDays = (int) (year * 365.25);
        int monthDays = (int) ((month + 1) * 30.61);

        return (yearDays + monthDays + days - 63);
    }

    public static Integer getDaysBetween(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return null;
        }

        int days1 = daysSince1900(date1);
        int days2 = daysSince1900(date2);

        if (days1 < days2) {
            return days2 - days1;
        } else {
            return days1 - days2;
        }
    }

}
