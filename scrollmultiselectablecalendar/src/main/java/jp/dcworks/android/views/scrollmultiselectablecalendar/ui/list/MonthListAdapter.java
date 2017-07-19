package jp.dcworks.android.views.scrollmultiselectablecalendar.ui.list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
        this.mOnDateClickListener = listener;
    }


    /**
     * コンストラクタ。
     *
     * @param context Context
     * @author tomo-sato
     * @since 1.0.0
     */
    public MonthListAdapter(Context context) {
        super();
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mCalendarList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return (this.mCalendarList != null) ? this.mCalendarList.size() : 0;
    }

    @Override
    public Calendar getItem(int position) {
        if (this.mCalendarList.isEmpty() || this.mCalendarList.size() <= position) {
            return null;
        }
        return this.mCalendarList.get(position);
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

//        View view;
//        ViewHolder viewHolder;

// TODO tomo-sato リサイクルできるように調整の余地あり。
//        if (convertView == null) {
            View view = this.mLayoutInflater.inflate(R.layout.inc_month, parent, false);

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

            view.setTag(viewHolder);
//        } else {
//            view = convertView;
//            viewHolder = (ViewHolder) view.getTag();
//        }

        final Calendar calendar = getItem(position);
        if (calendar == null) {
            return view;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.mYearMonthFormat);

        // 年月をセット
        viewHolder.monthTextView.setText(simpleDateFormat.format(calendar.getTime()));

        // 日をセット
        setWeekView(calendar, viewHolder);

        return view;
    }

    /**
     * 日をセットする。
     *
     * @param calendar 表示するカレンダー
     * @param viewHolder Viewホルダー
     * @author tomo-sato
     * @since 1.0.0
     */
    private void setWeekView(Calendar calendar, ViewHolder viewHolder) {

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
                        viewGroup.removeView(weekView);
                    }
                }
                continue;
            }

            // 日
            for (int j = 0; j < 7; j++) {
                if (weekView != null) {
                    TextView dayTextView = weekViewSet.dayTextViewList.get(j);

                    // 余った日はViewを非表示にする。また1日未満の日を非表示にする。
                    if (dayOfMonth < day) {
                        dayTextView.setVisibility(View.INVISIBLE);
                        continue;
                    }

                    // 1日未満の日を非表示にする。
                    if ((day == 1 && oneOfWeek > (j + 1))) {
                        dayTextView.setVisibility(View.INVISIBLE);
                        continue;
                    }

                    // 日付をセットする。
                    dayTextView.setText(String.valueOf(day));

                    // 曜日毎にテキストカラーをセットする。
                    switch (j) {
                        // 日曜
                        case 0:
                            dayTextView.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                            break;

                        // 土曜
                        case 6:
                            dayTextView.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
                            break;

                        default:
                            dayTextView.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    }

                    // 日付をカレンダーにセットする。
                    Calendar targetCalendar = Calendar.getInstance();
                    targetCalendar.setTime(calendar.getTime());
                    targetCalendar.set(Calendar.DATE, day);

                    // 選択状態（個別選択）をセットする。
                    if (this.mAvailableSchedule != null) {
                        if (this.mAvailableSchedule.selectedCalendarList != null && !this.mAvailableSchedule.selectedCalendarList.isEmpty()) {
                            for (Calendar selectedCalendar : this.mAvailableSchedule.selectedCalendarList) {
                                if (selectedCalendar.compareTo(targetCalendar) == 0) {
                                    dayTextView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.available_day_background));
                                    dayTextView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                }
                            }
                        }

                        // 選択状態（範囲選択）をセットする。
                        if (this.mAvailableSchedule.selectedFromCalendar != null && this.mAvailableSchedule.selectedToCalendar != null) {

                            if ((this.mAvailableSchedule.selectedFromCalendar != null && this.mAvailableSchedule.selectedFromCalendar.compareTo(targetCalendar) <= 0)
                                    && (this.mAvailableSchedule.selectedToCalendar != null && this.mAvailableSchedule.selectedToCalendar.compareTo(targetCalendar) >= 0)) {

                                dayTextView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.available_day_background));
                                dayTextView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                            }

                        } else if (this.mAvailableSchedule.selectedFromCalendar != null && this.mAvailableSchedule.selectedToCalendar == null) {

                            if (this.mAvailableSchedule.selectedFromCalendar.compareTo(targetCalendar) == 0) {
                                dayTextView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey));
                                dayTextView.setTextColor(ContextCompat.getColor(mContext, R.color.available_day_background));
                            }
                        }

                        // クリックイベントをセットする。
                        if ((this.mAvailableSchedule.selectableFromCalendar != null && this.mAvailableSchedule.selectableFromCalendar.compareTo(targetCalendar) <= 0)
                                && (this.mAvailableSchedule.selectableToCalendar != null && this.mAvailableSchedule.selectableToCalendar.compareTo(targetCalendar) >= 0)) {

                            dayTextView.setOnClickListener(this);

                        } else if ((this.mAvailableSchedule.selectableFromCalendar != null && this.mAvailableSchedule.selectableFromCalendar.compareTo(targetCalendar) <= 0)
                                && (this.mAvailableSchedule.selectableToCalendar == null)) {

                            dayTextView.setOnClickListener(this);

                        } else if ((this.mAvailableSchedule.selectableFromCalendar == null)
                                && (this.mAvailableSchedule.selectableToCalendar != null && this.mAvailableSchedule.selectableToCalendar.compareTo(targetCalendar) >= 0)) {

                            dayTextView.setOnClickListener(this);

                        } else {
                            dayTextView.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
                        }
                    }
                    day++;
                }
            }
        }
        return;
    }

    @Override
    public void onClick(View view) {
        TextView dayText = (TextView) view;

        // タップされた年月を取得する。
        // TODO tomo-sato 親を辿っているためレイアウトに依存している。他に方法が無いか要検討。
        ViewGroup viewWeekGroup = (ViewGroup) view.getParent();
        ViewGroup viewLinearLayoutGroup = (ViewGroup) viewWeekGroup.getParent();
        ViewGroup viewMonthLinearLayoutGroup = (ViewGroup) viewLinearLayoutGroup.getParent();
        String yearMonthStr = ((TextView) viewMonthLinearLayoutGroup.findViewById(R.id.month_text_view)).getText().toString();

        // TODO tomo-sato 年月取得暫定
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.mYearMonthFormat);
        Date date = null;
        try {
            date = simpleDateFormat.parse(yearMonthStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, Integer.parseInt(dayText.getText().toString()));

        // リスナーがセットされている場合、クリック時のイベントを通知する。
        if (this.mOnDateClickListener != null) {
            this.mOnDateClickListener.onDateClick(view, calendar);
        }
    }

    /**
     * リストクリア処理。
     *
     * @author tomo-sato
     * @since 1.0.0
     */
    public void clear() {
        this.mCalendarList.clear();
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
        this.mCalendarList.addAll(list);
    }

    /**
     * 利用可能スケジュールをセットする。
     *
     * @param availableSchedule 利用可能スケジュール
     * @author tomo-sato
     * @since 1.0.0
     */
    public void setAvailableSchedule(AvailableSchedule availableSchedule) {
        this.mAvailableSchedule = availableSchedule;
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
}
