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

package jp.dcworks.android.views.scrollmultiselectablecalendar.consts;

/**
 * スケジュールモード。
 * <ul>
 *     <li>{@code SINGLE}：単一選択</li>
 *     <li>{@code RANGE}：範囲選択</li>
 *     <li>{@code DISPLAY}：表示のみ（※選択制御無し。）</li>
 * </ul>
 *
 * @author tomo-sato
 * @since 1.0.0
 */
public enum ScheduleMode {
    SINGLE,
    RANGE,
    DISPLAY
}
