package jp.dcworks.android.views.scrollmultiselectablecalendar.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import jp.dcworks.android.views.scrollmultiselectablecalendar.R;
import jp.dcworks.android.views.scrollmultiselectablecalendar.consts.ScheduleMode;

/**
 * ScrollMultiSelectableCalendarViewメイン処理クラス。
 *
 * @author tomo-sato
 * @since 1.0.0
 */
public class ScrollMultiSelectableCalendarView extends LinearLayout implements View.OnClickListener {

    /** コンテキストのメンバ変数。 */
    private Context mContext;

    /** スケジュールモードのメンバ変数。 */
    private ScheduleMode mScheduleMode = ScheduleMode.SINGLE;

    /**
     * 日付クリック時のイベントリスナー。
     *
     * @author tomo-sato
     * @since 1.0.0
     */
    private interface OnDateClickListener {};

    /** 日付クリック時のイベントリスナーのメンバ変数。 */
    private OnDateClickListener mOnDateClickListener;

    /**
     * コンストラクタ。
     *
     * @param context Context
     * @param attrs AttributeSet
     * @author tomo-sato
     * @since 1.0.0
     */
    public ScrollMultiSelectableCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        createViews();
    }

    /**
     * Viewを生成する。
     *
     * @author tomo-sato
     * @since 1.0.0
     */
    private void createViews() {
        View layout = LayoutInflater.from(this.mContext).inflate(R.layout.scroll_multi_selectable_calendar_view, this);

        // TODO tomo-sato 検証用暫定対応 クリックイベントセット
        setOnClickListenerMonth(layout, R.id.include_month_1);
        setOnClickListenerMonth(layout, R.id.include_month_2);
        setOnClickListenerMonth(layout, R.id.include_month_3);
        setOnClickListenerMonth(layout, R.id.include_month_4);
        setOnClickListenerMonth(layout, R.id.include_month_5);

        return;
    }

    /**
     * TODO tomo-sato 検証用暫定対応
     *
     * @param layout
     * @param monthResourcesid
     */
    private void setOnClickListenerMonth(View layout, int monthResourcesid) {
        View monthView = layout.findViewById(monthResourcesid);
        setOnClickListenerWeek(monthView, R.id.include_week_1);
        setOnClickListenerWeek(monthView, R.id.include_week_2);
        setOnClickListenerWeek(monthView, R.id.include_week_3);
        setOnClickListenerWeek(monthView, R.id.include_week_4);
        setOnClickListenerWeek(monthView, R.id.include_week_5);
        setOnClickListenerWeek(monthView, R.id.include_week_6);
    }

    /**
     * TODO tomo-sato 検証用暫定対応
     *
     * @param monthView
     * @param weekResourcesid
     */
    private void setOnClickListenerWeek(View monthView, int weekResourcesid) {
        View weekView = monthView.findViewById(weekResourcesid);
        TextView textViewDay1 = (TextView) weekView.findViewById(R.id.day1);
        TextView textViewDay2 = (TextView) weekView.findViewById(R.id.day2);
        TextView textViewDay3 = (TextView) weekView.findViewById(R.id.day3);
        TextView textViewDay4 = (TextView) weekView.findViewById(R.id.day4);
        TextView textViewDay5 = (TextView) weekView.findViewById(R.id.day5);
        TextView textViewDay6 = (TextView) weekView.findViewById(R.id.day6);
        TextView textViewDay7 = (TextView) weekView.findViewById(R.id.day7);

        textViewDay1.setOnClickListener(this);
        textViewDay2.setOnClickListener(this);
        textViewDay3.setOnClickListener(this);
        textViewDay4.setOnClickListener(this);
        textViewDay5.setOnClickListener(this);
        textViewDay6.setOnClickListener(this);
        textViewDay7.setOnClickListener(this);
    }

    /**
     * onClick実装。
     * @param view View
     * @author tomo-sato
     * @since 1.0.0
     * @see android.view.View.OnClickListener#onClick(View)
     */
    @Override
    public void onClick(View view) {
        Log.d("tomo-sato", "クリックされました。");

        if(this.mScheduleMode == ScheduleMode.SINGLE) {
            onClickAtSingleMode(view);
        } else if(this.mScheduleMode == ScheduleMode.RANGE) {
            onClickAsRangeMode(view);
        } else if(this.mScheduleMode == ScheduleMode.DISPLAY) {
            return;
        }
    }

    /**
     * シングル選択時のタップ動作。
     *
     * @param view
     * @author tomo-sato
     * @since 1.0.0
     */
    private void onClickAtSingleMode(View view) {
        TextView dayText = (TextView) view;
        dayText.setBackgroundColor(ContextCompat.getColor(mContext, R.color.available_day_background));
        dayText.setTextColor(ContextCompat.getColor(mContext, R.color.white));
    }

    /**
     * マルチ選択時のタップ動作。
     *
     * @param view
     * @author tomo-sato
     * @since 1.0.0
     */
    private void onClickAsRangeMode(View view) {
    }

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
}
