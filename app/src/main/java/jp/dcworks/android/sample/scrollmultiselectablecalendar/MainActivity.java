package jp.dcworks.android.sample.scrollmultiselectablecalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;
import java.util.TimeZone;

import jp.dcworks.android.views.scrollmultiselectablecalendar.entity.AvailableSchedule;
import jp.dcworks.android.views.scrollmultiselectablecalendar.ui.ScrollMultiSelectableCalendarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // カレンダーView生成
        ScrollMultiSelectableCalendarView calendarView = (ScrollMultiSelectableCalendarView) findViewById(R.id.calendar_view);

        // カレンダーの表示範囲を設定する。
        calendarView.addViewCalendar(getCalendar(2017, 1, 1));
        calendarView.addViewCalendar(getCalendar(2017, 2, 1));
        calendarView.addViewCalendar(getCalendar(2017, 3, 1));
        calendarView.addViewCalendar(getCalendar(2017, 4, 1));
        calendarView.addViewCalendar(getCalendar(2017, 5, 1));
        calendarView.addViewCalendar(getCalendar(2017, 6, 1));

        // カレンダーの選択範囲を設定する。
        AvailableSchedule availableSchedule = calendarView.getAvailableSchedule();
        availableSchedule.selectableFromCalendar = getCalendar(2017, 1, 10);
        availableSchedule.selectableToCalendar = getCalendar(2017, 5, 10);
        calendarView.setAvailableSchedule(availableSchedule);
    }

    /**
     * 引数{@code year}年、{@code month}月、{@code day}で指定したCalendarオブジェクトを生成する。
     *
     * @param year 年
     * @param month 月（1〜12月）
     * @param day 日
     * @return Calendarオブジェクト
     */
    private static Calendar getCalendar(int year, int month, int day) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Tokyo");

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeZone(timeZone);
        calendar.set(Calendar.YEAR, year);

        // 0から始まる。
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }
}
