package com.lf.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lf.AppCamera;
import com.lf.appcamera.CaptureMode;
import com.lf.camera.listener.LFCameraListener;
import com.lf.camera.util.LFCameraFileUtil;
import com.lf.camera.picture.GlideEngine;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;

import java.util.ArrayList;

/**
 * @date: 2024/7/4
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvPath;


    private ImageView ivContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tvPath = findViewById(R.id.tv_path);

        ivContent = findViewById(R.id.iv_content);
        findViewById(R.id.tv_toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCamera.from(MainActivity.this).setMode(CaptureMode.All).forResult(10001);
            }
        });


        findViewById(R.id.tv_toast2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LFCameraUtil.startPhotoCamera(MainActivity.this, new LFCameraListener() {
                    @Override
                    public void onCanceled() {

                    }

                    @Override
                    public void onPicked(@Nullable String filePath, Object object) {
                        tvPath.setText("拍照路径："+ filePath);
                    }
                });
            }
        });

        findViewById(R.id.tv_toast3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LFCameraUtil.startVideoCamera(MainActivity.this,  10,new LFCameraListener() {
                    @Override
                    public void onCanceled() {

                    }

                    @Override
                    public void onPicked(@Nullable String filePath, Object object) {
                        tvPath.setText("拍照路径："+ filePath);
                    }
                });
            }
        });

        findViewById(R.id.tv_toast4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LFCameraFileUtil.clearCatch(MainActivity.this);
                LFCameraUtil.startPhotoGallery(MainActivity.this, new LFCameraListener() {
                    @Override
                    public void onCanceled() {

                    }

                    @Override
                    public void onPicked(@Nullable String filePath, Object object) {
                        ivContent.setImageBitmap((Bitmap) object);
                        tvPath.setText("拍照路径："+ filePath);
                    }
                });
            }
        });

        findViewById(R.id.tv_toast5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LFCameraFileUtil.clearCatch(MainActivity.this);
                LFCameraUtil.startVideoGallery(MainActivity.this, new LFCameraListener() {
                    @Override
                    public void onCanceled() {

                    }

                    @Override
                    public void onPicked(@Nullable String filePath, Object object) {
                        tvPath.setText("拍照路径："+ filePath);
                    }
                });
            }
        });

        findViewById(R.id.tv_toast6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LFCameraFileUtil.clearCatch(MainActivity.this);
                LFCameraUtil.startFileChooser(MainActivity.this, new LFCameraListener() {
                    @Override
                    public void onCanceled() {

                    }

                    @Override
                    public void onPicked(@Nullable String filePath, Object object) {
                        tvPath.setText("拍照路径："+ filePath);
                    }
                });
            }
        });


        findViewById(R.id.tv_toast7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelector.create(MainActivity.this)
                        .openGallery(SelectMimeType.ofImage())
                        .setImageEngine(GlideEngine.createGlideEngine())
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(ArrayList<LocalMedia> result) {

                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });

        findViewById(R.id.tv_toast8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelector.create(MainActivity.this)
                        .openSystemGallery(SelectMimeType.ofImage())
                        .forSystemResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(ArrayList<LocalMedia> result) {

                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });


        findViewById(R.id.tv_toast9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelector.create(MainActivity.this)
                        .openCamera(SelectMimeType.ofImage())
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(ArrayList<LocalMedia> result) {

                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10001 && resultCode == RESULT_OK) {
            String capturePath = null;
            String videoPath = null;

            if ((videoPath = AppCamera.obtainCaptureVideoResult(data))!=null) {
                //录制的视频
                capturePath = AppCamera.obtainCaptureImageResult(data);
                tvPath.setText("视频路径："+videoPath
                        +" \n 第一帧图片："+capturePath);
                Log.d("111---", videoPath);

            } else if((capturePath = AppCamera.obtainCaptureImageResult(data))!=null) {
                tvPath.setText("拍照路径："+capturePath);
                Log.d("111---", capturePath);
            }
        }
    }




}
