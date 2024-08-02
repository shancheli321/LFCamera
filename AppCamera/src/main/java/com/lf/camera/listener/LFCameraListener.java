package com.lf.camera.listener;

import android.net.Uri;

import androidx.annotation.Nullable;

/**
 * @date: 2024/7/18
 */
public interface LFCameraListener {

    /**
     * 用户取消回调
     */
    public void onCanceled();

    /**
     * 图片返回回调
     */
    public void onPicked(@Nullable String filePath, Object object);

}
