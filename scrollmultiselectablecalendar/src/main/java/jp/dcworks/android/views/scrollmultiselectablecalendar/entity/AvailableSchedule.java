package jp.dcworks.android.views.scrollmultiselectablecalendar.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * スケジュールエンティティクラス。
 *
 * @author tomo-sato
 * @since 1.0.0
 */
public class AvailableSchedule {

    /** 選択可能範囲：開始カレンダー */
    public Calendar selectableFromCalendar;
    /** 選択可能範囲：終了カレンダー */
    public Calendar selectableToCalendar;

    /** 選択中のカレンダー：選択状態を保持。（※{@code ScheduleMode.SINGLE} 時利用。） */
    public List<Calendar> selectedCalendarList = new ArrayList<>();

    /** 選択中のカレンダー：選択開始。（※{@code ScheduleMode.RANGE} 時利用。） */
    public Calendar selectedFromCalendar;
    /** 選択中のカレンダー：選択終了。（※{@code ScheduleMode.RANGE} 時利用。） */
    public Calendar selectedToCalendar;
}