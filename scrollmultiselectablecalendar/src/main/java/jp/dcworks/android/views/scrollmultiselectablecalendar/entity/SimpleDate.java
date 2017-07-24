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