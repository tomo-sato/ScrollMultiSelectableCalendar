/*
 * Copyright (C) 2017 tomo-sato
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.dcworks.android.views.scrollmultiselectablecalendar.ui.list;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.dcworks.android.views.scrollmultiselectablecalendar.R;
import jp.dcworks.android.views.scrollmultiselectablecalendar.entity.AvailableSchedule;
import jp.dcworks.android.views.scrollmultiselectablecalendar.entity.ColorSet;

/**
 * 月リストのアダプタ。
 *
 * @author tomo-sato
 * @since 1.0.0
 */
public class MonthListAdapter extends BaseAdapter implements View.OnClickListener {

    /** TAG */
    private static final String TAG = MonthListAdapter.class.getSimpleName();

    /** 週ResourcesID配列 */
    private static final int[] INCLUDE_WEEK_RESOURCES_ID_ARRAY = {
            R.id.include_week_1,
            R.id.include_week_2,
            R.id.include_week_3,
            R.id.include_week_4,
            R.id.include_week_5,
            R.id.include_week_6,
    };

    /** 日（１週間）ResourcesID配列 */
    private static final int[] INCLUDE_DAY_RESOURCES_ID_ARRAY = {
            R.id.day1,
            R.id.day2,
            R.id.day3,
            R.id.day4,
            R.id.day5,
            R.id.day6,
            R.id.day7,
    };


    /** コンテキスト */
    private Context mContext;

    /** LayoutInflater */
    private LayoutInflater mLayoutInflater;

    /** Calendarリストオブジェクト（※表示するカレンダーのリストを保持。） */
    private List<Calendar> mCalendarList;

    /** カレンダーの状態を保持 */
    private AvailableSchedule mAvailableSchedule;

    /** 年月フォーマット */
    private String mYearMonthFormat = "yyyy年MM月";

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
     * @param colorSet カレンダー色情報
     * @author tomo-sato
     * @since 1.0.0
     */
    public MonthListAdapter(Context context, ColorSet colorSet) {
        super();
        mContext = context;
        mColorSet = colorSet;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCalendarList = new ArrayList<>();

        // リソースの色を設定する。
        setDrawableResourceBackgroundColor(R.drawable.layer_available_day,       colorSet.availableDayBackgroundColor);
        setDrawableResourceBackgroundColor(R.drawable.layer_available_day_from,  colorSet.availableDayBackgroundColor);
        setDrawableResourceBackgroundColor(R.drawable.layer_available_day_to,    colorSet.availableDayBackgroundColor);
        setDrawableResourceBackgroundColor(R.drawable.layer_available_day_alpha, colorSet.availableDayBackgroundColorAlpha);
        setDrawableResourceBackgroundColor(R.drawable.layer_clicked_day,         colorSet.clickedDayBackgroundColor);
    }

    @Override
    public int getCount() {
        return (mCalendarList != null) ? mCalendarList.size() : 0;
    }

    @Override
    public Calendar getItem(int position) {
        if (mCalendarList.isEmpty() || mCalendarList.size() <= position) {
            return null;
        }
        return mCalendarList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Calendar calendar = getItem(position);
        if (calendar == null) {
            return null;
        }

        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.inc_month, parent, false);
            viewHolder = initViewHolder(view);
            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();

            // ある月が何週あるか（週の数）
            int weekOfMonth = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);

            // 使いまわしているため、週が足りない場合がある。
            ViewGroup viewGroup = (ViewGroup) viewHolder.weekViewSetList.get(0).weekView.getParent();
            int childCount = viewGroup.getChildCount();
            // 週が足りなかった場合、ViewHolderを再構成する。
            if (weekOfMonth > childCount) {
                view = mLayoutInflater.inflate(R.layout.inc_month, parent, false);
                viewHolder = initViewHolder(view);
                view.setTag(viewHolder);
            }
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(mYearMonthFormat);

        // 年月をセット
        viewHolder.monthTextView.setText(simpleDateFormat.format(calendar.getTime()));
        viewHolder.monthTextView.setTextColor(mColorSet.monthTextColor);

        this.setViewHolderWeekView(calendar, viewHolder);

        return view;
    }

    /**
     * ViewHolderを初期化する。
     *
     * @param view View
     * @return ViewHolderを返す。
     * @author tomo-sato
     * @since 1.0.0
     */
    private ViewHolder initViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        // 年月テキスト
        viewHolder.monthTextView = (TextView) view.findViewById(R.id.month_text_view);
        // 各週
        for (int weekResId : INCLUDE_WEEK_RESOURCES_ID_ARRAY) {
            ViewHolder.WeekViewSet weekViewSet = new ViewHolder.WeekViewSet();
            weekViewSet.weekView = view.findViewById(weekResId);

            for (int dayResId : INCLUDE_DAY_RESOURCES_ID_ARRAY) {
                weekViewSet.dayTextViewList.add((TextView) weekViewSet.weekView.findViewById(dayResId));
            }

            viewHolder.weekViewSetList.add(weekViewSet);
        }
        return viewHolder;
    }

    /**
     * 日をセットする。
     *
     * @param calendar 表示するカレンダー
     * @param viewHolder Viewホルダー（※この参照に対して操作を行う。）
     * @author tomo-sato
     * @since 1.0.0
     */
    private void setViewHolderWeekView(Calendar calendar, ViewHolder viewHolder) {

        // 1日の曜日を取得する。
        int oneOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // ある月が何週あるか（週の数）
        int weekOfMonth = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);

        // ある月が何日あるか（日数）
        int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 日付
        int day = 1;

        // 週
        for (int i = 0; i < 6; i++) {
            ViewHolder.WeekViewSet weekViewSet = viewHolder.weekViewSetList.get(i);
            View weekView = weekViewSet.weekView;

            // 余った週はViewから削除する。
            if (weekOfMonth < (i + 1)) {
                if (weekView != null) {
                    ViewGroup viewGroup = (ViewGroup) weekView.getParent();
                    if (viewGroup != null) {

                        // 参照に対して操作を行う。
                        viewGroup.removeView(weekView);
                    }
                }
                continue;
            }

            // 日
            for (int j = 0; j < 7; j++) {
                if (weekView != null) {
                    TextView dayTextView = weekViewSet.dayTextViewList.get(j);

                    // リサイクルしてる都合、初期化処理が必要。
                    dayTextView.setVisibility(View.VISIBLE);
                    dayTextView.setOnClickListener(null);
                    dayTextView.setBackgroundResource(R.drawable.border_top);
                    dayTextView.setTextColor(mColorSet.dayTextColor);

                    // 余った日はViewを非表示にする。また1日未満の日を非表示にする。
                    if (dayOfMonth < day) {
                        dayTextView.setVisibility(View.INVISIBLE);

                        // ViewHolderにセットし直す。
                        weekViewSet.dayTextViewList.set(j, dayTextView);
                        continue;
                    }

                    // 1日未満の日を非表示にする。
                    if ((day == 1 && oneOfWeek > (j + 1))) {
                        dayTextView.setVisibility(View.INVISIBLE);

                        // ViewHolderにセットし直す。
                        weekViewSet.dayTextViewList.set(j, dayTextView);
                        continue;
                    }

                    // 日付をセットする。
                    dayTextView.setText(String.valueOf(day));

                    // 曜日毎にテキストカラーをセットする。
                    switch (j) {
                        // 日曜
                        case 0:
                            dayTextView.setTextColor(mColorSet.daySundayTextColor);
                            break;

                        // 土曜
                        case 6:
                            dayTextView.setTextColor(mColorSet.dayWeekendTextColor);
                            break;

                        // 平日
                        default:
                            dayTextView.setTextColor(mColorSet.dayTextColor);
                    }

                    // 日付をカレンダーにセットする。
                    Calendar targetCalendar = Calendar.getInstance();
                    targetCalendar.setTime(calendar.getTime());
                    targetCalendar.set(Calendar.DATE, day);

                    if (mAvailableSchedule != null) {
                        // 選択状態（個別選択）をセットする。
                        if (mAvailableSchedule.selectedCalendarList != null && !mAvailableSchedule.selectedCalendarList.isEmpty()) {
                            for (Calendar selectedCalendar : mAvailableSchedule.selectedCalendarList) {
                                if (selectedCalendar.compareTo(targetCalendar) == 0) {
                                    dayTextView.setBackgroundResource(R.drawable.layer_available_day);
                                    dayTextView.setTextColor(mColorSet.availableDayTextColor);
                                }
                            }
                        }

                        // 選択状態（範囲選択）をセットする。
                        if (mAvailableSchedule.selectedFromCalendar != null && mAvailableSchedule.selectedToCalendar != null) {

                            // 範囲選択が決定したときの色をセットする。
                            if ((mAvailableSchedule.selectedFromCalendar != null && mAvailableSchedule.selectedFromCalendar.compareTo(targetCalendar) <= 0)
                                    && (mAvailableSchedule.selectedToCalendar != null && mAvailableSchedule.selectedToCalendar.compareTo(targetCalendar) >= 0)) {

                                // 開始日、終了日の間の色をセットする。
                                if (!(mAvailableSchedule.selectedFromCalendar.compareTo(targetCalendar) == 0
                                        || mAvailableSchedule.selectedToCalendar.compareTo(targetCalendar) == 0)) {

                                    dayTextView.setBackgroundResource(R.drawable.layer_available_day_alpha);

                                // 開始日、終了日が同日の色をセットする。
                                } else if ((mAvailableSchedule.selectedFromCalendar.compareTo(targetCalendar) == 0
                                        && mAvailableSchedule.selectedToCalendar.compareTo(targetCalendar) == 0)) {

                                    dayTextView.setBackgroundResource(R.drawable.layer_available_day);

                                // 開始日、終了日の色をセットする。
                                } else {
                                    // 開始日の色をセットする。
                                    if (mAvailableSchedule.selectedFromCalendar.compareTo(targetCalendar) == 0) {
                                        dayTextView.setBackgroundResource(R.drawable.layer_available_day_from);
                                    }
                                    // 終了日の色をセットする。
                                    if (mAvailableSchedule.selectedToCalendar.compareTo(targetCalendar) == 0) {
                                        dayTextView.setBackgroundResource(R.drawable.layer_available_day_to);
                                    }
                                }
                                dayTextView.setTextColor(mColorSet.availableDayTextColor);
                            }

                        } else if (mAvailableSchedule.selectedFromCalendar != null && mAvailableSchedule.selectedToCalendar == null) {

                            // 範囲選択開始が決まったときの色をセットする。
                            if (mAvailableSchedule.selectedFromCalendar.compareTo(targetCalendar) == 0) {
                                dayTextView.setBackgroundResource(R.drawable.layer_clicked_day);
                                dayTextView.setTextColor(mColorSet.clickedDayTextColor);
                            }
                        }

                        // クリックイベントをセットする。
                        if ((mAvailableSchedule.selectableFromCalendar != null && mAvailableSchedule.selectableFromCalendar.compareTo(targetCalendar) <= 0)
                                && (mAvailableSchedule.selectableToCalendar != null && mAvailableSchedule.selectableToCalendar.compareTo(targetCalendar) >= 0)) {

                            dayTextView.setOnClickListener(this);

                        } else if ((mAvailableSchedule.selectableFromCalendar != null && mAvailableSchedule.selectableFromCalendar.compareTo(targetCalendar) <= 0)
                                && (mAvailableSchedule.selectableToCalendar == null)) {

                            dayTextView.setOnClickListener(this);

                        } else if ((mAvailableSchedule.selectableFromCalendar == null)
                                && (mAvailableSchedule.selectableToCalendar != null && mAvailableSchedule.selectableToCalendar.compareTo(targetCalendar) >= 0)) {

                            dayTextView.setOnClickListener(this);

                        } else {
                            dayTextView.setTextColor(mColorSet.disableDayTextColor);
                        }
                    }
                    day++;
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        TextView dayText = (TextView) view;

        // タップされた年月を取得する。
        ViewGroup viewWeekGroup = (ViewGroup) view.getParent();
        ViewGroup viewLinearLayoutGroup = (ViewGroup) viewWeekGroup.getParent();
        ViewGroup viewMonthLinearLayoutGroup = (ViewGroup) viewLinearLayoutGroup.getParent();
        String yearMonthStr = ((TextView) viewMonthLinearLayoutGroup.findViewById(R.id.month_text_view)).getText().toString();
        Log.d(TAG, "yearMonthStr=" + yearMonthStr + ", dayText=" + dayText.getText().toString());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(mYearMonthFormat);
        Date date = null;
        try {
            date = simpleDateFormat.parse(yearMonthStr);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, Integer.parseInt(dayText.getText().toString()));

        // リスナーがセットされている場合、クリック時のイベントを通知する。
        if (mOnDateClickListener != null) {
            mOnDateClickListener.onDateClick(view, calendar);
        }
    }

    /**
     * リストクリア処理。
     *
     * @author tomo-sato
     * @since 1.0.0
     */
    public void clear() {
        mCalendarList.clear();
        notifyDataSetChanged();
    }

    /**
     * 引数に指定されたリストを追加する。
     *
     * @param list 表示項目リスト
     * @author tomo-sato
     * @since 1.0.0
     */
    public void addAll(List<Calendar> list) {
        mCalendarList.addAll(list);
    }

    /**
     * 利用可能スケジュールをセットする。
     *
     * @param availableSchedule 利用可能スケジュール
     * @author tomo-sato
     * @since 1.0.0
     */
    public void setAvailableSchedule(AvailableSchedule availableSchedule) {
        mAvailableSchedule = availableSchedule;
    }

    /**
     * ViewHolderクラス。
     *
     * @author tomo-sato
     * @since 1.0.0
     */
    private static class ViewHolder {

        /** 年月 */
        TextView monthTextView;
        /** 各週 */
        List<WeekViewSet> weekViewSetList = new ArrayList<>();

        /**
         * 1週間のView情報を保持するクラス。
         *
         * @author tomo-sato
         * @since 1.0.0
         */
        private static class WeekViewSet {
            /** 週 */
            View weekView;
            /** 1週間の日リスト */
            List<TextView> dayTextViewList = new ArrayList<>();
        }
    }

    /**
     * リソースの色を設定する。
     *
     * @param resourceId リソースID
     * @param colorResourceId カラーリソースID
     * @author tomo-sato
     * @since 1.0.0
     */
    private void setDrawableResourceBackgroundColor(int resourceId, int colorResourceId) {
        LayerDrawable layerDrawable = (LayerDrawable) this.getDrawableResource(resourceId);
        GradientDrawable strokeDrawable = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.item_background_color);
        strokeDrawable.setColor(colorResourceId);
    }

    /**
     * Drawableを取得する。
     *
     * @param resourceId リソースID
     * @return Drawable
     * @author tomo-sato
     * @since 1.0.0
     */
    @SuppressWarnings("deprecation")
    private Drawable getDrawableResource(int resourceId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return mContext.getDrawable(resourceId);
        } else {
            return mContext.getResources().getDrawable(resourceId);
        }
    }
}
