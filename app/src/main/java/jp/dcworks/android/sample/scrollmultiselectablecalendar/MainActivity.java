package jp.dcworks.android.sample.scrollmultiselectablecalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

import jp.dcworks.android.views.scrollmultiselectablecalendar.consts.ScheduleMode;
import jp.dcworks.android.views.scrollmultiselectablecalendar.entity.AvailableSchedule;
import jp.dcworks.android.views.scrollmultiselectablecalendar.entity.SimpleDate;
import jp.dcworks.android.views.scrollmultiselectablecalendar.ui.ScrollMultiSelectableCalendarView;

/**
 * ScrollMultiSelectableCalendarViewサンプルActivity。
 *
 * @author tomo-sato
 * @since 1.0.0
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // カレンダーView生成
        ScrollMultiSelectableCalendarView calendarView = (ScrollMultiSelectableCalendarView) findViewById(R.id.calendar_view);

        // スケジュールモードをセットする。（※デフォルト：範囲指定）
        calendarView.setScheduleMode(ScheduleMode.RANGE);

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

        // クリックイベントをセットする。
        calendarView.setOnDateClickListener(new ScrollMultiSelectableCalendarView.OnDateClickListener() {
            @Override
            public void onDateClick(View view, Calendar calendar, boolean isClickFixed) {
                Toast.makeText(MainActivity.this,
                        SimpleDate.toSimpleDate(calendar).toString() + ", isClickFixed=" + isClickFixed,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFixedRange(Calendar fromCalendar, Calendar toCalendar) {
                Toast.makeText(MainActivity.this,
                        SimpleDate.toSimpleDate(fromCalendar).toString() + "〜" + SimpleDate.toSimpleDate(toCalendar).toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
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
