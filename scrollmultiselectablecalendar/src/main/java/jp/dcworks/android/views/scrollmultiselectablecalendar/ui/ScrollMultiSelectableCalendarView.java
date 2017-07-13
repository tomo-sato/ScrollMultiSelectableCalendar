package jp.dcworks.android.views.scrollmultiselectablecalendar.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import jp.dcworks.android.views.scrollmultiselectablecalendar.R;

public class ScrollMultiSelectableCalendarView extends LinearLayout {

    public ScrollMultiSelectableCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View layout = LayoutInflater.from(context).inflate(R.layout.sample_my_view, this);
    }
}
