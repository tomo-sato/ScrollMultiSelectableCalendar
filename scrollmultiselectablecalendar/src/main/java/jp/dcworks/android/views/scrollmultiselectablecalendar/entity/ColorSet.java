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

package jp.dcworks.android.views.scrollmultiselectablecalendar.entity;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import jp.dcworks.android.views.scrollmultiselectablecalendar.R;

/**
 * カレンダーで使用する色情報を保持するクラス。
 *
 * @author tomo-sato
 * @since 1.0.0
 */
public class ColorSet {

    /** xml属性：色設定：年月テキストカラー */
    public int monthTextColor;
    /** xml属性：色設定：日テキストカラー（平日） */
    public int dayTextColor;
    /** xml属性：色設定：日テキストカラー（土曜） */
    public int dayWeekendTextColor;
    /** xml属性：色設定：日テキストカラー（日曜） */
    public int daySundayTextColor;

    /** xml属性：色設定：日（選択）バックグラウンドカラー */
    public int availableDayBackgroundColor;
    /** xml属性：色設定：日（選択）バックグラウンドカラー */
    public int availableDayBackgroundColorAlpha;
    /** xml属性：色設定：日（選択）テキストカラー */
    public int availableDayTextColor;

    /** xml属性：色設定：日（クリック状態）バックグラウンドカラー */
    public int clickedDayBackgroundColor;
    /** xml属性：色設定：日（クリック状態）テキストカラー */
    public int clickedDayTextColor;

    /** xml属性：色設定：日（選択不可）テキストカラー */
    public int disableDayTextColor;

    /**
     * コンストラクタ。
     *
     * @param context Context
     * @author tomo-sato
     * @since 1.0.0
     */
    public ColorSet(Context context) {
        super();
        // 属性デフォルト設定
        this.monthTextColor                    = ContextCompat.getColor(context, R.color.default_text_color);
        this.dayTextColor                      = ContextCompat.getColor(context, R.color.default_text_color);
        this.dayWeekendTextColor               = ContextCompat.getColor(context, R.color.blue);
        this.daySundayTextColor                = ContextCompat.getColor(context, R.color.red);

        this.availableDayBackgroundColor       = ContextCompat.getColor(context, R.color.available_day_background);
        this.availableDayBackgroundColorAlpha  = ContextCompat.getColor(context, R.color.available_day_background_alpha);
        this.availableDayTextColor             = ContextCompat.getColor(context, R.color.white);

        this.clickedDayBackgroundColor         = ContextCompat.getColor(context, R.color.clicked_day_background);
        this.clickedDayTextColor               = ContextCompat.getColor(context, R.color.default_text_color);

        this.disableDayTextColor               = ContextCompat.getColor(context, R.color.grey);
    }
}