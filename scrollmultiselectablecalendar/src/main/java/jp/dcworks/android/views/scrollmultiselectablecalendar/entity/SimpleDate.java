package jp.dcworks.android.views.scrollmultiselectablecalendar.entity;

import java.util.Calendar;

/**
 * 日付エンティティクラス。
 *
 * @author tomo-sato
 * @since 1.0.0
 */
public class SimpleDate {

    /** 年 */
    public int year;
    /** 月 */
    public int month;
    /** 日 */
    public int day;

    /**
     * コンストラクタ。
     *
     * @param year 年
     * @param month 月
     * @param day 日
     * @author tomo-sato
     * @since 1.0.0
     */
    public SimpleDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * このクラスをカレンダーオブジェクトに変換する。
     *
     * @return このクラスのカレンダーオブジェクトを返す。
     * @author tomo-sato
     * @since 1.0.0
     */
    public Calendar toCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(this.year, this.month - 1, this.day);
        return calendar;
    }

    /**
     * 引数で指定されたカレンダーオブジェクトを{@code SimpleDate}クラスのオブジェクトに変換する。
     *
     * @param calendar カレンダーオブジェクト
     * @return SimpleDateオブジェクト
     * @author tomo-sato
     * @since 1.0.0
     */
    public static SimpleDate toSimpleDate(Calendar calendar) {
        return new SimpleDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
    }

    @Override
    public String toString() {
        return this.year + "/" + this.month + "/" + this.day;
    }
}