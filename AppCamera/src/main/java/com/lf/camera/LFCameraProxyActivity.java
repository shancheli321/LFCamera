package com.lf.camera;

import static com.lf.camera.LFCameraConstants.LFCAMERA_INTENT_TYPE;
import static com.lf.camera.LFCameraConstants.LFCAMERA_PICK_FILE_CHOOSER_REQUEST_CODE;
import static com.lf.camera.LFCameraConstants.LFCAMERA_PICK_IMAGE_CHOOSER_REQUEST_CODE;
import static com.lf.camera.LFCameraConstants.LFCAMERA_PICK_VIDEO_CHOOSER_REQUEST_CODE;
import static com.lf.camera.LFCameraConstants.LFCAMERA_TAKE_CAMERA_REQUEST_CODE;
import static com.lf.camera.LFCameraConstants.LFCAMERA_TAKE_VIDEO_CHOOSER_REQUEST_CODE;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @date: 2024/8/1
 */
public class LFCameraProxyActivity extends Activity {

    private Context mContext;

    public static void runActivity(Context context, int type) {
        Intent intent = new Intent(context, LFCameraProxyActivity.class);
        intent.putExtra(LFCAMERA_INTENT_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        if (savedInstanceState != null) {
            finish();
            return;
        }


        Intent intent = getIntent();
        int type = intent.getIntExtra(LFCAMERA_INTENT_TYPE, 0);

        if (type == LFCAMERA_TAKE_CAMERA_REQUEST_CODE) {
            startPhotoCamera();
        } else if (type == LFCAMERA_PICK_VIDEO_CHOOSER_REQUEST_CODE) {
            startVideoGallery();
        } else if (type == LFCAMERA_PICK_FILE_CHOOSER_REQUEST_CODE) {
            startFileChooser();
        } else if (type == LFCAMERA_PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            startPhotoGallery();
        } else if (type == LFCAMERA_TAKE_VIDEO_CHOOSER_REQUEST_CODE) {
            startVideoCamera();
        }

    }


    /**
     * 启动照相机(Activity)
     */
    private void startPhotoCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
        if (intent.resolveActivity(getPackageManager()) != null) {
            LFCameraUtil.setOutputFilePath(createImagePath(mContext));

            Uri imageUri = FileProvider.getUriForFile(this, getProvider(mContext), new File(LFCameraUtil.getOutputFilePath()));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, LFCAMERA_TAKE_CAMERA_REQUEST_CODE);
        }
    }


    public void startVideoCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        //这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
        if (intent.resolveActivity(getPackageManager()) != null) {
            LFCameraUtil.setOutputFilePath(createVideoPath(mContext));

            Uri imageUri = FileProvider.getUriForFile(mContext, getProvider(mContext), new File(LFCameraUtil.getOutputFilePath()));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, LFCAMERA_TAKE_VIDEO_CHOOSER_REQUEST_CODE);
        }
    }

    /**
     * 启动图库选择器(Activity)
     */
    public void startPhotoGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        // 如果你想要限制到特定的图片类型（例如JPEG），可以这样设置
        // intent.setType("image/jpeg");
        startActivityForResult(intent, LFCAMERA_PICK_IMAGE_CHOOSER_REQUEST_CODE);
    }


    /**
     * 启动视频选择器
     */
    public void startVideoGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");

        // 如果你想要限制到特定的图片类型（例如JPEG），可以这样设置
        // intent.setType("image/jpeg");

        startActivityForResult(intent, LFCAMERA_PICK_VIDEO_CHOOSER_REQUEST_CODE);
    }


    /**
     * 启动文件选择器(Activity)
     */
    public void startFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, LFCAMERA_PICK_FILE_CHOOSER_REQUEST_CODE);
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            if (LFCameraUtil.getCallback() != null) {
                LFCameraUtil.getCallback().onCanceled();
                finish();
            }
            return;
        }

        if (requestCode == LFCAMERA_TAKE_CAMERA_REQUEST_CODE || requestCode == LFCAMERA_TAKE_VIDEO_CHOOSER_REQUEST_CODE) { // 相机
            if (LFCameraUtil.getCallback() != null) {
                LFCameraUtil.getCallback().onPicked(LFCameraUtil.getOutputFilePath(), null);
            }
            LFCameraUtil.setOutputFilePath(null);

        } else if (requestCode == LFCAMERA_PICK_IMAGE_CHOOSER_REQUEST_CODE) { // 相册
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                Bitmap bitmap = LFCameraUriUtil.getBitmapFromUri(mContext, selectedImageUri);
                String path = LFCameraUriUtil.getPathFromUri(mContext, 1, selectedImageUri);
                if (LFCameraUtil.getCallback() != null) {
                    LFCameraUtil.getCallback().onPicked(path, bitmap);
                }
                LFCameraUtil.setOutputFilePath(null);
            }
        } else if (requestCode == LFCAMERA_PICK_VIDEO_CHOOSER_REQUEST_CODE) {
            if (data != null && data.getData() != null) {
                Uri selectedVideoUri = data.getData();
                String path = LFCameraUriUtil.getPathFromUri(mContext, 2, selectedVideoUri);
                if (LFCameraUtil.getCallback() != null) {
                    LFCameraUtil.getCallback().onPicked(path, null);
                }
                LFCameraUtil.setOutputFilePath(null);
            }
        } else if (requestCode == LFCAMERA_PICK_FILE_CHOOSER_REQUEST_CODE) {
            if (data != null && data.getData() != null) {
                Uri selectedFileUri = data.getData();
                String path = LFCameraUriUtil.getPathFromUri(mContext, selectedFileUri);
                if (LFCameraUtil.getCallback() != null) {
                    LFCameraUtil.getCallback().onPicked(path, null);
                }
                LFCameraUtil.setOutputFilePath(null);
            }
        }

        finish();
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
