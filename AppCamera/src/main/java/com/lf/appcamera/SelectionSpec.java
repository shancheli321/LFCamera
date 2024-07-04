/*
 * Copyright (C) 2014 nohana, Inc.
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lf.appcamera;

import android.content.pm.ActivityInfo;

import androidx.annotation.StyleRes;

import java.util.List;
import java.util.Set;

public final class SelectionSpec {

    public boolean mediaTypeExclusive;
    public boolean showSingleMediaType = true;
    @StyleRes
    public int themeId;
    public int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    public boolean countable = true;
    public int maxSelectable = 9;
    public int maxImageSelectable;
    public int maxVideoSelectable;
    public boolean capture;
    public int spanCount;
    public int gridExpectedSize;
    public float thumbnailScale;
    public boolean hasInited;
    public boolean originalable;
    public boolean autoHideToobar = true;
    public int originalMaxSize = Integer.MAX_VALUE;
    public CaptureMode captureMode = CaptureMode.Image;//默认可以

    private SelectionSpec() {
    }

    public static SelectionSpec getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static SelectionSpec getCleanInstance() {
        SelectionSpec selectionSpec = getInstance();
        selectionSpec.reset();
        return selectionSpec;
    }

    private void reset() {
        mediaTypeExclusive = true;
        showSingleMediaType = true;
        orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        countable = true;
        maxSelectable = 9;
        maxImageSelectable = 0;
        maxVideoSelectable = 0;
        capture = false;
        spanCount = 3;
        gridExpectedSize = 0;
        thumbnailScale = 0.5f;
        hasInited = true;
        originalable = false;
        autoHideToobar = true;
        originalMaxSize = Integer.MAX_VALUE;
        captureMode =  CaptureMode.Image;
    }

    public boolean singleSelectionModeEnabled() {
        return !countable && (maxSelectable == 1 || (maxImageSelectable == 1 && maxVideoSelectable == 1));
    }

    public boolean needOrientationRestriction() {
        return orientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }



    private static final class InstanceHolder {
        private static final SelectionSpec INSTANCE = new SelectionSpec();
    }
}
