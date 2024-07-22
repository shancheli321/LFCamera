package com.lf.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 调用系统拍照、系统相册
 * 使用前请自行进行权限申请
 * 拍照图片会保存在/storage/emulated/0/Android/data/<应用包名>/files/Pictures
 *  * @date: 2024/7/18
 */
public class LFCameraUtil {

    /**
     * 拍照
     */
    private static final int PICK_CAMERA_REQUEST_CODE = 33003;

    /**
     * 选视频
     */
    private static final int PICK_VIDEO_CHOOSER_REQUEST_CODE = 33004;

    /**
     * 选文件
     */
    private static final int PICK_FILE_CHOOSER_REQUEST_CODE = 33005;

    /**
     * 选图片
     */
    private static final int PICK_IMAGE_CHOOSER_REQUEST_CODE = 33006;

    private static volatile LFCameraUtil instance = null;

    private LFCameraListener callback;

    /**
     * 输出的文件路径
     */
    private String outputFilePath;

    public static LFCameraUtil getInstance() {
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
    public boolean clearCatch(Context context) {
        return LFCameraFileUtil.clearCatch(context);
    }

    /**
     * 启动照相机(Activity)
     */
    public void startPhotoCamera(Activity activity, LFCameraListener callback) {
        this.callback = callback;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            outputFilePath = createImagePath(activity);

            Uri imageUri = FileProvider.getUriForFile(activity, getProvider(activity), new File(outputFilePath));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            activity.startActivityForResult(intent, PICK_CAMERA_REQUEST_CODE);
        }
    }

    /**
     * 启动照相机(Fragment)
     */
    public void startPhotoCamera(Fragment fragment, LFCameraListener callback) {
        this.callback = callback;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
            outputFilePath = createImagePath(fragment.getContext());

            Uri imageUri = FileProvider.getUriForFile(fragment.getActivity(), getProvider(fragment.getActivity()), new File(outputFilePath));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            fragment.startActivityForResult(intent, PICK_CAMERA_REQUEST_CODE);
        }
    }


    /**
     * 拍视频
     * @param activity
     * @param callback
     */
    public void startVideoCamera(Activity activity, LFCameraListener callback) {
        this.callback = callback;
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        //这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            outputFilePath = createVideoPath(activity);

            Uri imageUri = FileProvider.getUriForFile(activity, getProvider(activity), new File(outputFilePath));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            activity.startActivityForResult(intent, PICK_CAMERA_REQUEST_CODE);
        }
    }

    /**
     * 拍视频
     * @param fragment
     * @param callback
     */
    public void startVideoCamera(Fragment fragment, LFCameraListener callback) {
        this.callback = callback;
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (intent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
            outputFilePath = createVideoPath(fragment.getContext());

            Uri imageUri = FileProvider.getUriForFile(fragment.getActivity(), getProvider(fragment.getActivity()), new File(outputFilePath));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            fragment.startActivityForResult(intent, PICK_CAMERA_REQUEST_CODE);
        }
    }

    /**
     * 启动图库选择器(Activity)
     */
    public void startPhotoGallery(Activity activity, LFCameraListener callback) {
        this.callback = callback;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        // 如果你想要限制到特定的图片类型（例如JPEG），可以这样设置
        // intent.setType("image/jpeg");

        activity.startActivityForResult(intent, PICK_IMAGE_CHOOSER_REQUEST_CODE);
    }

    /**
     * 启动图库选择器(Fragment)
     */
    public void startPhotoGallery(Fragment fragment, LFCameraListener callback) {
        this.callback = callback;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        // 如果你想要限制到特定的图片类型（例如JPEG），可以这样设置
        // intent.setType("image/jpeg");

        fragment.startActivityForResult(intent, PICK_IMAGE_CHOOSER_REQUEST_CODE);
    }

    public void startVideoGallery(Activity activity, LFCameraListener callback) {
        this.callback = callback;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");

        // 如果你想要限制到特定的图片类型（例如JPEG），可以这样设置
        // intent.setType("image/jpeg");

        activity.startActivityForResult(intent, PICK_VIDEO_CHOOSER_REQUEST_CODE);
    }

    public void startVideoGallery(Fragment fragment, LFCameraListener callback) {
        this.callback = callback;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");

        // 如果你想要限制到特定的图片类型（例如JPEG），可以这样设置
        // intent.setType("image/jpeg");

        fragment.startActivityForResult(intent, PICK_VIDEO_CHOOSER_REQUEST_CODE);
    }

    /**
     * 启动文件选择器(Activity)
     */
    public void startFileChooser(Activity activity, LFCameraListener callback) {
        this.callback = callback;
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        activity.startActivityForResult(intent, PICK_FILE_CHOOSER_REQUEST_CODE);
    }

    /**
     * 启动文件选择器(Fragment)
     */
    public void startFileChooser(Fragment fragment, LFCameraListener callback) {
        this.callback = callback;
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        fragment.startActivityForResult(intent, PICK_FILE_CHOOSER_REQUEST_CODE);
    }

    /**
     * 图片选择结果回调，在 {@link Activity#onActivityResult(int, int, Intent)} 中调用
     */
    @SuppressWarnings("JavadocReference")
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        onActivityResultInner(activity, null, requestCode, resultCode, data);
    }

    /**
     * 图片选择结果回调，在 {@link Fragment#onActivityResult(int, int, Intent)} 中调用
     */
    public void onActivityResult(Fragment fragment, int requestCode, int resultCode, Intent data) {
        onActivityResultInner(null, fragment, requestCode, resultCode, data);
    }

    /**
     * @param activity
     * @param fragment
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void onActivityResultInner(Activity activity, Fragment fragment, int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            if (callback != null)
                callback.onCanceled();
            return;
        }

        Context context;
        if (activity != null) {
            context = activity;
        } else {
            context = fragment.getContext();
        }

        if (context != null) {
            if (requestCode == PICK_CAMERA_REQUEST_CODE) { // 相机
                if (callback != null) {
                    callback.onPicked(outputFilePath, null);
                }
                outputFilePath = null;
            } else if (requestCode == PICK_IMAGE_CHOOSER_REQUEST_CODE) { // 相册
                if (data != null && data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    Bitmap bitmap = LFCameraUriUtil.getBitmapFromUri(context, selectedImageUri);
                    String path = LFCameraUriUtil.getPathFromUri(context, 1, selectedImageUri);
                    if (callback != null) {
                        callback.onPicked(path, bitmap);
                    }
                    outputFilePath = null;
                }
            } else if (requestCode == PICK_VIDEO_CHOOSER_REQUEST_CODE) {
                if (data != null && data.getData() != null) {
                    Uri selectedVideoUri = data.getData();
                    String path = LFCameraUriUtil.getPathFromUri(context, 2, selectedVideoUri);
                    if (callback != null) {
                        callback.onPicked(path, null);
                    }
                    outputFilePath = null;
                }
            } else if (requestCode == PICK_FILE_CHOOSER_REQUEST_CODE) {
                if (data != null && data.getData() != null) {
                    Uri selectedFileUri = data.getData();
                    String path = LFCameraUriUtil.getPathFromUri(context, selectedFileUri);
                    if (callback != null) {
                        callback.onPicked(path, null);
                    }
                    outputFilePath = null;
                }
            }
        }

    }

    /**
     * 获取一个图片的路径
     * @return
     * @throws IOException
     */
    private String createImagePath(Context context) {

        // 创建一个存储图片的目录
        String storageDir = LFCameraFileUtil.getRootPath(context);

        // 创建一个新的图片文件
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFilePath =  storageDir + "/PNG_" + timeStamp + ".png";

        return imageFilePath;
    }


    /**
     * 创建一个视频的路径
     * @return
     * @throws IOException
     */
    private String createVideoPath(Context context) {

        // 创建一个存储视频的目录
        String storageDir = LFCameraFileUtil.getRootPath(context);

        // 创建一个新的视频文件
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String videoFilePath = storageDir + "/MP4_" + timeStamp + ".mp4";

        // 返回新创建的文件
        return videoFilePath;
    }

    private String getProvider(Context context) {
        return  "com.lf.camera.fileProvider";
    }


}
