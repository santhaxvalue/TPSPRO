package com.panelManagement.fragment;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.panelManagement.listener.OnDateListener;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment {
    public static String monthfinal;
    public static String yearfinal;
    public static String dayfinal;
    OnDateSetListener ondateSet;
    OnDateListener listener;
    OnDateSetListener ondate = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String yr = String.valueOf(year);
            String month = getMonth(monthOfYear);
            String day = String.valueOf(dayOfMonth);
            monthfinal = month;
            yearfinal = yr;
            dayfinal = day;
            if (listener != null) {
                listener.onDateSet(month + " " + day + "," + yr);
                dismiss();
            }
        }
    };
    private int year, month, day;

    public static String getMonth(int month) {
        return new DateFormatSymbols().getShortMonths()[month];
    }

    public static int getMonthInnumber(String monthName) {
        int monthInt = 0;
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(new SimpleDateFormat("MMM").parse(monthName));
            monthInt = cal.get(Calendar.MONTH) + 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return monthInt;
    }

    public static int getmonthfinal() {

        return getMonthInnumber(monthfinal);
    }

    public static int getDay() {
        return Integer.parseInt(dayfinal);
    }

    public static int getYear() {
        return Integer.parseInt(yearfinal);
    }

    public void setCallBack(OnDateListener ondate) {
        this.listener = ondate;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        Date maxDate = new Date();
        DatePickerDialog d = new DatePickerDialog(getActivity(), ondate, year,
                month, day);

        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        try {
            maxDate = new Date();
            maxDate.setYear(maxDate.getYear() - 17);
            // maxDate.setDate(31);
            // maxDate.setMonth(0);
            d.getDatePicker().setMaxDate(maxDate.getTime());
            String string = "January 1, 1947";
            SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy",
                    Locale.ENGLISH);
            Date minDate;

            minDate = format.parse(string);
            d.getDatePicker().setMinDate(minDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public class DatePickerDialogWithMaxMinRange extends DatePickerDialog {

        int minYear = 1947;
        int minMonth = 0;
        int minDay = 1;
        private int maxYear = 1996;
        private int maxMonth = 12;
        private int maxDay = 31;

        public DatePickerDialogWithMaxMinRange(Context context,
                                               OnDateSetListener callBack, int maxYear, int maxMonth,
                                               int maxDay) {
            super(context, callBack, maxYear, maxMonth, maxDay);
            this.maxDay = maxDay;
            this.maxMonth = maxMonth;
            this.maxYear = maxYear;
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
            super.onDateChanged(view, year, monthOfYear, dayOfMonth);

            if (year > maxYear || monthOfYear > maxMonth && year == maxYear
                    || dayOfMonth > maxDay && year == maxYear
                    && monthOfYear == maxMonth) {
                view.updateDate(maxYear, maxMonth, maxDay);
            } else if (year < minYear || monthOfYear < minMonth
                    && year == minYear || dayOfMonth < minDay
                    && year == minYear && monthOfYear == minMonth) {
                view.updateDate(minYear, minMonth, minDay);
            }
        }

    }
}