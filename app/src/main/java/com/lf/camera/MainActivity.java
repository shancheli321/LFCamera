package com.lf.camera;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lf.AppCamera;
import com.lf.appcamera.CaptureMode;

/**
 * @date: 2024/7/4
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCamera.from(MainActivity.this).setMode(CaptureMode.All).forResult(10001);
            }
        });
    }
}
