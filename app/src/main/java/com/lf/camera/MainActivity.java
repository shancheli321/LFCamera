package com.lf.camera;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lf.AppCamera;
import com.lf.appcamera.CaptureMode;

/**
 * @date: 2024/7/4
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tvPath = findViewById(R.id.tv_path);
        findViewById(R.id.tv_toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCamera.from(MainActivity.this).setMode(CaptureMode.All).forResult(10001);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10001 && resultCode == RESULT_OK) {
            String capturePath = null;
            String videoPath = null;

            if((videoPath = AppCamera.obtainCaptureVideoResult(data))!=null){
                //录制的视频
                capturePath = AppCamera.obtainCaptureImageResult(data);
                tvPath.setText("视频路径："+videoPath
                        +" \n 第一帧图片："+capturePath);
                Log.d("111---", videoPath);

            }else if((capturePath = AppCamera.obtainCaptureImageResult(data))!=null){
                tvPath.setText("拍照路径："+capturePath);
                Log.d("111---", capturePath);
            }
        }
    }
}
