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

public final class AppCameraSpec {

    public CaptureMode captureMode = CaptureMode.All;//默认可以

    private AppCameraSpec() {

    }

    private static final class InstanceHolder {
        private static final AppCameraSpec INSTANCE = new AppCameraSpec();
    }

    public static AppCameraSpec getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static AppCameraSpec getCleanInstance() {
        AppCameraSpec selectionSpec = getInstance();
        selectionSpec.reset();
        return selectionSpec;
    }

    private void reset() {
        captureMode =  CaptureMode.All;
    }


}
