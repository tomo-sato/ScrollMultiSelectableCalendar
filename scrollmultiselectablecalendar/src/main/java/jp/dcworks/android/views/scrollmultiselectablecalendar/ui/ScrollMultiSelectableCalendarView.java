package jp.dcworks.android.views.scrollmultiselectablecalendar.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.dcworks.android.views.scrollmultiselectablecalendar.R;
import jp.dcworks.android.views.scrollmultiselectablecalendar.consts.ScheduleMode;
import jp.dcworks.android.views.scrollmultiselectablecalendar.entity.AvailableSchedule;
import jp.dcworks.android.views.scrollmultiselectablecalendar.entity.ColorSet;
import jp.dcworks.android.views.scrollmultiselectablecalendar.ui.list.MonthListAdapter;

/**
 * ScrollMultiSelectableCalendarViewメイン処理クラス。
 *
 * @author tomo-sato
 * @since 1.0.0
 */
public class ScrollMultiSelectableCalendarView extends LinearLayout implements MonthListAdapter.OnDateClickListener {

    /** コンテキスト */
    private Context mContext;

    /** スケジュールモード：デフォルト範囲選択 */
    private ScheduleMode mScheduleMode = ScheduleMode.RANGE;

    /** カレンダーの状態 */
    private AvailableSchedule mAvailableSchedule = new AvailableSchedule();

    /** 表示するカレンダーのリスト */
    private List<Calendar> mViewCalendar = new ArrayList<>();

    /** 月リストアダプター */
    private MonthListAdapter mMonthListAdapter;


    // attributes ---------------
    /** カレンダー色情報 */
    private ColorSet mColorSet;


    /**
     * 日付クリック時のイベントリスナー。
     *
     * @author tomo-sato
     * @since 1.0.0
     */
    public interface OnDateClickListener {

        /**
         * 日付クリック時のイベントを通知する。
         *
         * @param view クリックされたViewを通知する。
         * @param calendar クリックされたViewのカレンダーを通知する。
         * @author tomo-sato
         * @since 1.0.0
         */
        void onDateClick(View view, Calendar calendar);

        /**
         * 範囲選択時、開始終了が決定したタイミングで通知する。
         *
         * @param fromCalendar 選択開始カレンダー
         * @param toCalendar 選択終了カレンダー
         * @author tomo-sato
         * @since 1.0.0
         */
        void onFixedRange(Calendar fromCalendar, Calendar toCalendar);
    }

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
        mOnDateClickListener = listener;
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
        mContext = context;
        mColorSet = new ColorSet(context);
        setAttributes(attrs);
        createViews();
    }

    /**
     * スケジュールエンティティクラスを返す。
     *
     * @return スケジュールエンティティクラスを返す。
     * @author tomo-sato
     * @since 1.0.0
     */
    public AvailableSchedule getAvailableSchedule() {
        return mAvailableSchedule;
    }

    /**
     * スケジュールエンティティクラスをセットする。
     *
     * @param availableSchedule スケジュールエンティティクラス。
     * @author tomo-sato
     * @since 1.0.0
     */
    public void setAvailableSchedule(AvailableSchedule availableSchedule) {
        mAvailableSchedule = availableSchedule;

        // 再描画処理
        this.redraw();
    }

    /**
     * 表示するカレンダーをセットする。
     *
     * @param calendar 表示するカレンダーをセットする。
     * @author tomo-sato
     * @since 1.0.0
     */
    public void addViewCalendar(Calendar calendar) {
        mViewCalendar.add(calendar);

        // 再描画処理
        this.redraw();
    }

    /**
     * スケジュールモードをセットする。
     *
     * @param scheduleMode スケジュールモード
     * @author tomo-sato
     * @since 1.0.0
     */
    public void setScheduleMode(ScheduleMode scheduleMode) {
        mScheduleMode = scheduleMode;

        // 再描画処理
        this.redraw();
    }

    /**
     * 属性をセットする。
     *
     * @param attrs 属性（{@code AttributeSet}が{@code null}の場合、デフォルト値で初期化する。）
     * @author tomo-sato
     * @since 1.0.0
     */
    private void setAttributes(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray                      = this.mContext.obtainStyledAttributes(attrs, R.styleable.ScrollMultiSelectableCalendarView);
            mColorSet.monthTextColor                   = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_monthTextColor,                   ContextCompat.getColor(mContext, R.color.default_text_color));
            mColorSet.dayTextColor                     = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_dayTextColor,                     ContextCompat.getColor(mContext, R.color.default_text_color));
            mColorSet.dayWeekendTextColor              = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_dayWeekendTextColor,              ContextCompat.getColor(mContext, R.color.blue));
            mColorSet.daySundayTextColor               = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_daySundayTextColor,               ContextCompat.getColor(mContext, R.color.red));

            mColorSet.availableDayBackgroundColor      = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_availableDayBackgroundColor,      ContextCompat.getColor(mContext, R.color.available_day_background));
            mColorSet.availableDayBackgroundColorAlpha = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_availableDayBackgroundColorAlpha, ContextCompat.getColor(mContext, R.color.available_day_background_alpha));
            mColorSet.availableDayTextColor            = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_availableDayTextColor,            ContextCompat.getColor(mContext, R.color.white));

            mColorSet.clickedDayBackgroundColor        = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_clickedDayBackgroundColor,        ContextCompat.getColor(mContext, R.color.clicked_day_background));
            mColorSet.clickedDayTextColor              = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_clickedDayTextColor,              ContextCompat.getColor(mContext, R.color.default_text_color));

            mColorSet.disableDayTextColor              = typedArray.getColor(R.styleable.ScrollMultiSelectableCalendarView_disableDayTextColor,              ContextCompat.getColor(mContext, R.color.grey));
            typedArray.recycle();
        }
    }

    /**
     * Viewを生成する。
     *
     * @author tomo-sato
     * @since 1.0.0
     */
    private void createViews() {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.scroll_multi_selectable_calendar_view, this);

        // ListView初期化。
        ListView listView = (ListView) findViewById(R.id.month_list);
        listView.setCacheColorHint(Color.BLACK);
        mMonthListAdapter = new MonthListAdapter(layout.getContext(), mColorSet);
        mMonthListAdapter.setOnDateClickListener(this);
        listView.setAdapter(mMonthListAdapter);

        // 再描画処理
        this.redraw();
    }

    @Override
    public void onDateClick(View view, Calendar calendar) {
        if (mScheduleMode == ScheduleMode.SINGLE) {
            onClickAtSingleMode(calendar);
        } else if (mScheduleMode == ScheduleMode.RANGE) {
            onClickAsRangeMode(calendar);
        } else if (mScheduleMode == ScheduleMode.DISPLAY) {
            return;
        }

        // リスナーがセットされている場合、クリック時のイベントを通知する。
        if (mOnDateClickListener != null) {
            mOnDateClickListener.onDateClick(view, calendar);
        }
    }

    /**
     * 単一選択モード時のクリック処理。
     *
     * @param calendar クリックされたカレンダーオブジェクト
     * @author tomo-sato
     * @since 1.0.0
     */
    private void onClickAtSingleMode(Calendar calendar) {
        if (mAvailableSchedule.selectedCalendarList.contains(calendar)) {
            mAvailableSchedule.selectedCalendarList.remove(calendar);
        } else {
            mAvailableSchedule.selectedCalendarList.add(calendar);
        }

        // 再描画処理
        this.redraw();
    }

    /**
     * 範囲選択モード時のクリック処理。
     *
     * @param calendar クリックされたカレンダーオブジェクト
     * @author tomo-sato
     * @since 1.0.0
     */
    private void onClickAsRangeMode(Calendar calendar) {
        // 初回タップの場合
        if (mAvailableSchedule.selectedFromCalendar == null) {
            mAvailableSchedule.selectedFromCalendar = calendar;

            // 2回目以降かつ、範囲が決まっていない場合
        } else if (mAvailableSchedule.selectedToCalendar == null) {

            // 初回タップより過去の場合セットし直し
            if (mAvailableSchedule.selectedFromCalendar.compareTo(calendar) > 0) {
                mAvailableSchedule.selectedFromCalendar = calendar;

                // 初回タップ以降未来の場合（同じ場所をタップした場合も含む）
            } else {
                mAvailableSchedule.selectedToCalendar = calendar;

                // リスナーがセットされている場合、範囲選択完了時のイベントを通知する。
                if (mOnDateClickListener != null) {
                    mOnDateClickListener.onFixedRange(
                            mAvailableSchedule.selectedFromCalendar, mAvailableSchedule.selectedToCalendar);
                }
            }

            // 範囲が決まっている場合
        } else {
            mAvailableSchedule.selectedFromCalendar = calendar;
            mAvailableSchedule.selectedToCalendar = null;
        }

        // 再描画処理
        this.redraw();
    }

    /**
     * 再描画処理。
     *
     * @author tomo-sato
     * @since 1.0.0
     */
    private void redraw() {
        mMonthListAdapter.clear();
        mMonthListAdapter.addAll(mViewCalendar);
        mMonthListAdapter.setAvailableSchedule(mAvailableSchedule);
        mMonthListAdapter.notifyDataSetChanged();
    }
}
