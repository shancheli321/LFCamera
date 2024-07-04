# LFCamera


仿微信相机 拍照片和视频

###接入方法

1. 接入SDK

	```
	repositories {
		mavenCentral()
		maven { url 'https://jitpack.io' }
	}
	
	dependencies {
        implementation 'com.github.shancheli321:LFCamera:0.0.1'
	}
		
	```

2. 使用
	
	```
	AppCamera.from(MainActivity.this).setMode(CaptureMode.All).forResult(10001);
	
	```
	
	回调
	
	
	```
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
	```


#### 接入遇到的问题
1. 在接入一个第三方SDK的时候（他们使用androidx），本身项目有support，出现了如题的错误 

	```
	More than one file was found with OS independent path 'META-INF/xxx
	```

	在build.gradle文件中加入

	```
	packagingOptions {
		exclude 'META-INF/xxx'
	}
	```

	exclude ：打包时移除项目中的相关文件，不打入apk文件中

	pickFirst : 有多个匹配时只匹配第一个