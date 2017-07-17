package jp.dcworks.android.views.scrollmultiselectablecalendar.list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import jp.dcworks.android.views.scrollmultiselectablecalendar.R;
import jp.dcworks.android.views.scrollmultiselectablecalendar.consts.ScheduleMode;

/**
 * 月リストのアダプタ。
 *
 * @author tomo-sato
 * @since 1.0.0
 */
public class MonthListAdapter extends ArrayAdapter<Calendar> implements View.OnClickListener {

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

    /** スケジュールモード */
    private ScheduleMode mScheduleMode = ScheduleMode.SINGLE;


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
         * @param view クリックされたView。
         * @param date クリックされたViewの日付。
         * @author tomo-sato
         * @since 1.0.0
         */
        public void onClick(View view, Date date);
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
     * @param resourceId ResourcesID
     * @param calendarList カレンダーリスト
     * @author tomo-sato
     * @since 1.0.0
     */
    public MonthListAdapter(Context context, int resourceId, ArrayList<Calendar> calendarList, ScheduleMode mScheduleMode) {
        super(context, resourceId, calendarList);
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * リストビューの表示処理。
     *
     * @param position ポジション
     * @param convertView View
     * @param parent ViewGroup
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
        if (null == convertView) {
            convertView = this.mLayoutInflater.inflate(R.layout.inc_month, null);
        }

        Calendar calendar = getItem(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月");

        // 年月をセット
        TextView monthTextView = (TextView) convertView.findViewById(R.id.month_text_view);
        monthTextView.setText(simpleDateFormat.format(calendar.getTime()));

        // 日をセット
        setWeekView(calendar, convertView);

        return convertView;
    }

    /**
     * 日をセットする。
     *
     * @param calendar 表示するカレンダー
     * @param convertView 表示対象のView
     */
    private void setWeekView(Calendar calendar, View convertView) {

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
            View weekView = convertView.findViewById(INCLUDE_WEEK_RESOURCES_ID_ARRAY[i]);

            // 余った週はViewから削除する。
            if (weekOfMonth < (i + 1)) {
                if (weekView != null) {
                    ViewGroup viewGroup = (ViewGroup) weekView.getParent();
                    viewGroup.removeView(weekView);
                }
                continue;
            }

            // 日
            for (int j = 0; j < 7; j++) {
                if (weekView != null) {
                    TextView textViewDay = (TextView) weekView.findViewById(INCLUDE_DAY_RESOURCES_ID_ARRAY[j]);

                    // 余った日はViewを非表示にする。また1日未満の日を非表示にする。
                    if ((dayOfMonth < day)
                            || (day == 1 && oneOfWeek > (j + 1))) {
                        Log.d(TAG, "dayOfMonth=" + dayOfMonth + ", day=" + day + ", oneOfWeek=" + oneOfWeek);


//                        textViewDay.setVisibility(View.INVISIBLE);
                        textViewDay.setText(String.valueOf(day));
                        textViewDay.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                        continue;
                    }

                    textViewDay.setText(String.valueOf(day));

                    // TODO tomo-sato 【バグ】非表示部分に表示部分のクリックイベントがセットされている。
                    //textViewDay.setOnClickListener(this);

                    day++;
                }
            }
        }
        return;
    }

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

        // リスナーがセットされている場合、クリック時のイベントを通知する。
        if (this.mOnDateClickListener != null) {
            this.mOnDateClickListener.onClick(view, null);
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
        dayText.setBackgroundColor(ContextCompat.getColor(this.mContext, R.color.available_day_background));
        dayText.setTextColor(ContextCompat.getColor(this.mContext, R.color.white));
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
}
