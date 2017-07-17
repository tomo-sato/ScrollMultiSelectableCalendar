package jp.dcworks.android.views.scrollmultiselectablecalendar.entity;

import java.util.Calendar;

public class SimpleDate {
    private static final String TAG = SimpleDate.class.getSimpleName();

    public int year;
    public int month;
    public int day;

    public SimpleDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(this.year, this.month - 1, this.day);
        return calendar;
    }

    @Override
    public String toString() {
        return this.year + "/" + this.month + "/" + this.day;
    }
}