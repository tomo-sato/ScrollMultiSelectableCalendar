package jp.dcworks.android.views.scrollmultiselectablecalendar.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import jp.dcworks.android.views.scrollmultiselectablecalendar.R;
import jp.dcworks.android.views.scrollmultiselectablecalendar.consts.ScheduleMode;
import jp.dcworks.android.views.scrollmultiselectablecalendar.entity.AvailableSchedule;
import jp.dcworks.android.views.scrollmultiselectablecalendar.entity.SimpleDate;
import jp.dcworks.android.views.scrollmultiselectablecalendar.list.MonthListAdapter;

/**
 * ScrollMultiSelectableCalendarViewメイン処理クラス。
 *
 * @author tomo-sato
 * @since 1.0.0
 */
public class ScrollMultiSelectableCalendarView extends LinearLayout implements MonthListAdapter.OnDateClickListener {

    /** TAG */
    private static final String TAG = ScrollMultiSelectableCalendarView.class.getSimpleName();


    /** コンテキスト */
    private Context mContext;

    /** スケジュールモード */
    private ScheduleMode mScheduleMode = ScheduleMode.SINGLE;


    /** 表示カレンダー：開始カレンダー */
    private Calendar mViewFromCalendar;
    /** 表示カレンダー：終了カレンダー */
    private Calendar mViewToCalendar;

    // attributes ---------------
    /** xml属性：色設定：週バックグラウンドカラー */
    private int mWeekBackgroundColor;
    /** xml属性：色設定：週テキストカラー */
    private int mWeekTextColor;

    /** xml属性：色設定：年月バックグラウンドカラー */
    private int mMonthBackgroundColor;
    /** xml属性：色設定：年月テキストカラー */
    private int mMonthTextColor;

    /** xml属性：色設定：日バックグラウンドカラー */
    private int mDayBackgroundColor;
    /** xml属性：色設定：日テキストカラー */
    private int mDayTextColor;

    /** xml属性：色設定：日（選択）バックグラウンドカラー */
    private int mAvailableDayBackgroundColor;
    /** xml属性：色設定：日（選択）テキストカラー */
    private int mAvailableDayTextColor;

    /** xml属性：色設定：日（除外）バックグラウンドカラー */
    private int mUnavailableDayBackgroundColor;
    /** xml属性：色設定：日（除外）テキストカラー */
    private int mUnavailableDayTextColor;

    /** xml属性：色設定：日（選択不可）テキストカラー */
    private int mDisableDayTextColor;


    /**
     * 日付クリック時のイベントリスナー。
     *
     * @author tomo-sato
     * @since 1.0.0
     */
    private interface OnDateClickListener {

        /**
         * 日付クリック時のイベントを通知する。
         *
         * @param view クリックされたViewを通知する。
         * @param calendar クリックされたViewのカレンダーを通知する。
         * @author tomo-sato
         * @since 1.0.0
         */
        void onDateClick(View view, Calendar calendar);
    };

    /** 日付クリック時のイベントリスナーのメンバ変数。 */
    private OnDateClickListener mOnDateClickListener;

    /**
     * 日付クリック時のイベントリスナーをセットする。
     *
     * @param listener OnDateClickListener
     * @author tomo-sato
     * @since 1.0.0
     */
    public void setOnDateClickListener(OnDateClickListener listener) {
        this.mOnDateClickListener = listener;
    }


    /**
     * コンストラクタ。
     *
     * @param context Context
     * @author tomo-sato
     * @since 1.0.0
     */
    public ScrollMultiSelectableCalendarView(Context context) {
        this(context, null);
    }

    /**
     * コンストラクタ。
     *
     * @param context Context
     * @param attrs AttributeSet
     * @author tomo-sato
     * @since 1.0.0
     */
    public ScrollMultiSelectableCalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.originalViewStyle);
    }

    /**
     * コンストラクタ。
     *
     * @param context Context
     * @param attrs AttributeSet
     * @param defStyleAttr int
     * @author tomo-sato
     * @since 1.0.0
     */
    public ScrollMultiSelectableCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 初期化処理
        this.mContext = context;
        setAttributes(attrs);
        createViews();
    }

    /**
     * 属性をセットする。
     *
     * @param attrs 属性（{@code AttributeSet}が{@code null}の場合、デフォルト値で初期化する。）
     * @author tomo-sato
     * @since 1.0.0
     */
    private void setAttributes(AttributeSet attrs) {
        // 属性デフォルト設定
        mWeekBackgroundColor               = ContextCompat.getColor(this.mContext, R.color.white);
        mWeekTextColor                     = ContextCompat.getColor(this.mContext, R.color.text_color);
        mMonthBackgroundColor              = ContextCompat.getColor(this.mContext, R.color.default_background_month);
        mMonthTextColor                    = ContextCompat.getColor(this.mContext, R.color.text_color);
        mDayBackgroundColor                = ContextCompat.getColor(this.mContext, R.color.white);
        mDayTextColor                      = ContextCompat.getColor(this.mContext, R.color.text_color);
        mAvailableDayBackgroundColor       = ContextCompat.getColor(this.mContext, R.color.available_day_background);
        mAvailableDayTextColor             = ContextCompat.getColor(this.mContext, R.color.white);
        mUnavailableDayBackgroundColor     = ContextCompat.getColor(this.mContext, R.color.unavailable_day_background);
        mUnavailableDayTextColor           = ContextCompat.getColor(this.mContext, R.color.text_color);
        mDisableDayTextColor               = ContextCompat.getColor(this.mContext, R.color.grey);

        if (attrs != null) {
            TypedArray typedArray          = this.mContext.obtainStyledAttributes(attrs, R.styleable.ScrollMultiSelectableCalendarView);
            mWeekBackgroundColor           = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_weekBackgroundColor,           ContextCompat.getColor(this.mContext, R.color.white));
            mWeekTextColor                 = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_weekTextColor,                 ContextCompat.getColor(this.mContext, R.color.text_color));
            mMonthBackgroundColor          = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_monthBackgroundColor,          ContextCompat.getColor(this.mContext, R.color.default_background_month));
            mMonthTextColor                = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_monthTextColor,                ContextCompat.getColor(this.mContext, R.color.text_color));
            mDayBackgroundColor            = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_dayBackgroundColor,            ContextCompat.getColor(this.mContext, R.color.white));
            mDayTextColor                  = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_dayTextColor,                  ContextCompat.getColor(this.mContext, R.color.text_color));
            mAvailableDayBackgroundColor   = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_availableDayBackgroundColor,   ContextCompat.getColor(this.mContext, R.color.available_day_background));
            mAvailableDayTextColor         = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_availableDayTextColor,         ContextCompat.getColor(this.mContext, R.color.white));
            mUnavailableDayBackgroundColor = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_unavailableDayBackgroundColor, ContextCompat.getColor(this.mContext, R.color.unavailable_day_background));
            mUnavailableDayTextColor       = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_unavailableDayTextColor,       ContextCompat.getColor(this.mContext, R.color.text_color));
            mDisableDayTextColor           = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_disableDayTextColor,           ContextCompat.getColor(this.mContext, R.color.grey));
            typedArray.recycle();
        }
        return;
    }

    /**
     * Viewを生成する。
     *
     * @author tomo-sato
     * @since 1.0.0
     */
    private void createViews() {
        View layout = LayoutInflater.from(this.mContext).inflate(R.layout.scroll_multi_selectable_calendar_view, this);

        // 表示するカレンダーのリストを生成する。
        final ArrayList<Calendar> listItem = new ArrayList<>();
        listItem.add(getCalendar(2017, 1));
        listItem.add(getCalendar(2017, 2));
        listItem.add(getCalendar(2017, 3));
        listItem.add(getCalendar(2017, 4));
        listItem.add(getCalendar(2017, 5));
        listItem.add(getCalendar(2017, 6));
        listItem.add(getCalendar(2017, 7));
        listItem.add(getCalendar(2017, 8));
        listItem.add(getCalendar(2017, 9));
        listItem.add(getCalendar(2017, 10));
        listItem.add(getCalendar(2017, 11));
        listItem.add(getCalendar(2017, 12));

        AvailableSchedule availableSchedule = new AvailableSchedule();
        availableSchedule.selectableFromCalendar = getCalendar(2017, 1, 10);
        availableSchedule.selectableToCalendar = getCalendar(2017, 9, 10);

        availableSchedule.selectedCalendarList.add(getCalendar(2017, 1, 13));
        availableSchedule.selectedCalendarList.add(getCalendar(2017, 2, 14));
        availableSchedule.selectedCalendarList.add(getCalendar(2017, 3, 15));

        availableSchedule.selectedFromCalendar = getCalendar(2017, 4, 10);
        availableSchedule.selectedToCalendar = getCalendar(2017, 5, 10);


        ListView listView = (ListView) findViewById(R.id.month_list);
        final MonthListAdapter monthListAdapter = new MonthListAdapter(layout.getContext());
        monthListAdapter.setOnDateClickListener(this);
        listView.setAdapter(monthListAdapter);

        monthListAdapter.clear();
        monthListAdapter.addAll(listItem);
        monthListAdapter.setAvailableSchedule(availableSchedule);
        monthListAdapter.notifyDataSetChanged();

        // TODO tomo-sato テスト
        View view = layout.findViewById(R.id.dayOfWeek1);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AvailableSchedule availableSchedule = new AvailableSchedule();
                availableSchedule.selectableFromCalendar = getCalendar(2017, 1, 10);
                availableSchedule.selectableToCalendar = getCalendar(2017, 9, 10);

                availableSchedule.selectedCalendarList.add(getCalendar(2017, 4, 13));
                availableSchedule.selectedCalendarList.add(getCalendar(2017, 5, 14));
                availableSchedule.selectedCalendarList.add(getCalendar(2017, 6, 15));

                availableSchedule.selectedFromCalendar = getCalendar(2017, 1, 12);
                availableSchedule.selectedToCalendar = getCalendar(2017, 2, 10);

                monthListAdapter.clear();
                monthListAdapter.addAll(listItem);
                monthListAdapter.setAvailableSchedule(availableSchedule);
                monthListAdapter.notifyDataSetChanged();
            }
        });

        return;
    }

    @Override
    public void onDateClick(View view, Calendar calendar) {
        Log.d(TAG, SimpleDate.toSimpleDate(calendar).toString());

        // リスナーがセットされている場合、クリック時のイベントを通知する。
        if (this.mOnDateClickListener != null) {
            this.mOnDateClickListener.onDateClick(view, calendar);
        }
        return;
    }

    /**
     * 引数{@code year}年、{@code month}月で指定したCalendarオブジェクトを生成する。
     *
     * @param year 年
     * @param month 月（1〜12月）
     * @return Calendarオブジェクト
     */
    private static Calendar getCalendar(int year, int month) {
        return getCalendar(year, month, 1);
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
