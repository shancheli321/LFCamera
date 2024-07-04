package com.lf.appcamera.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.lf.R;
import com.lf.appcamera.CaptureMode;
import com.lf.appcamera.MatisseConst;
import com.lf.appcamera.AppCameraSpec;
import com.lf.cameralibrary.JCameraView;
import com.lf.cameralibrary.listener.ClickListener;
import com.lf.cameralibrary.listener.ErrorListener;
import com.lf.cameralibrary.listener.JCameraListener;
import com.lf.cameralibrary.util.FileUtil;

import java.io.File;

public class CameraActivity extends AppCompatActivity {
    private JCameraView jCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Matisse_Zhihu);
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_camera);
        jCameraView = findViewById(R.id.jcameraview);
        init();
    }

    private int getFeature(){
        if(AppCameraSpec.getInstance().captureMode== CaptureMode.All){
            return JCameraView.BUTTON_STATE_BOTH;
        }else if(AppCameraSpec.getInstance().captureMode== CaptureMode.Image){
            return JCameraView.BUTTON_STATE_ONLY_CAPTURE;
        }else {
            return JCameraView.BUTTON_STATE_ONLY_RECORDER;
        }
    }

    private void init(){
        //设置视频保存路径
        jCameraView.setSaveVideoPath(getCacheDir() + File.separator + "matisse");
        jCameraView.setFeatures(getFeature());
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //错误监听
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
            @Override
            public void AudioPermissionError() {
                Toast.makeText(CameraActivity.this, "没有录音权限", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
        //JCameraView监听
        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                String path = FileUtil.saveBitmap("matisse", bitmap);
                Intent intent = new Intent();
                intent.putExtra(MatisseConst.EXTRA_RESULT_CAPTURE_IMAGE_PATH, path);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                String path = FileUtil.saveBitmap("matisse", firstFrame);
//                Log.i("CJT", "url = " + url + ", Bitmap = " + path);
                Intent intent = new Intent();
                intent.putExtra(MatisseConst.EXTRA_RESULT_CAPTURE_IMAGE_PATH, path);
                intent.putExtra(MatisseConst.EXTRA_RESULT_CAPTURE_VIDEO_PATH, url);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                CameraActivity.this.finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }
}
