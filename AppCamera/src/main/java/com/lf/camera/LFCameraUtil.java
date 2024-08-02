package com.lf.camera;

import static com.lf.camera.constants.LFCameraConstants.LFCAMERA_PICK_FILE_CHOOSER_REQUEST_CODE;
import static com.lf.camera.constants.LFCameraConstants.LFCAMERA_PICK_IMAGE_CHOOSER_REQUEST_CODE;
import static com.lf.camera.constants.LFCameraConstants.LFCAMERA_PICK_VIDEO_CHOOSER_REQUEST_CODE;
import static com.lf.camera.constants.LFCameraConstants.LFCAMERA_TAKE_CAMERA_REQUEST_CODE;
import static com.lf.camera.constants.LFCameraConstants.LFCAMERA_TAKE_VIDEO_CHOOSER_REQUEST_CODE;

import android.content.Context;

import com.lf.camera.listener.LFCameraListener;
import com.lf.camera.util.LFCameraFileUtil;


/**
 * 调用系统拍照、系统相册
 * 使用前请自行进行权限申请
 * 拍照图片会保存在/storage/emulated/0/Android/data/<应用包名>/files/Pictures
 *  * @date: 2024/7/18
 */
public class LFCameraUtil {

    private static volatile LFCameraUtil instance = null;

    private LFCameraListener callback;

    /**
     * 输出的文件路径
     */
    private String outputFilePath;

    private static LFCameraUtil getInstance() {
        if (instance == null) {
            synchronized (LFCameraUtil.class) {
                if (instance == null)
                    instance = new LFCameraUtil();
            }
        }
        return instance;
    }

    /**
     * 清除图片和视频的缓存
     * @param context
     * @return
     */
    public static boolean clearCatch(Context context) {
        return LFCameraFileUtil.clearCatch(context);
    }

    /**
     * 启动照相机(Activity)
     */
    public static void startPhotoCamera(Context context, LFCameraListener callback) {
        getInstance().callback = callback;
        LFCameraProxyActivity.runActivity(context, LFCAMERA_TAKE_CAMERA_REQUEST_CODE);
    }

    /**
     * 拍视频
     * @param context
     * @param callback
     */
    public static void startVideoCamera(Context context, int duration, LFCameraListener callback) {
        getInstance().callback = callback;
        LFCameraProxyActivity.runActivity(context, LFCAMERA_TAKE_VIDEO_CHOOSER_REQUEST_CODE, duration);
    }

    /**
     * 启动图库选择器(Activity)
     */
    public static void startPhotoGallery(Context context, LFCameraListener callback) {
        getInstance().callback = callback;
        LFCameraProxyActivity.runActivity(context, LFCAMERA_PICK_IMAGE_CHOOSER_REQUEST_CODE);
    }


    public static void startVideoGallery(Context context, LFCameraListener callback) {
        getInstance().callback = callback;
        LFCameraProxyActivity.runActivity(context, LFCAMERA_PICK_VIDEO_CHOOSER_REQUEST_CODE);
    }



    /**
     * 启动文件选择器(Activity)
     */
    public static void startFileChooser(Context context, LFCameraListener callback) {
        getInstance().callback = callback;
        LFCameraProxyActivity.runActivity(context, LFCAMERA_PICK_FILE_CHOOSER_REQUEST_CODE);
    }




    public static void setOutputFilePath(String outputFilePath) {
        getInstance().outputFilePath = outputFilePath;
    }

    public static String getOutputFilePath() {
        return getInstance().outputFilePath;
    }

    public static LFCameraListener getCallback() {
        return getInstance().callback;
    }
}
