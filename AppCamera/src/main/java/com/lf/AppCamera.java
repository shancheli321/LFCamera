package com.lf;



import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lf.appcamera.AppCameraSpec;
import com.lf.appcamera.CaptureMode;
import com.lf.appcamera.MatisseConst;
import com.lf.appcamera.MimeType;
import com.lf.appcamera.activity.CameraActivity;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Set;

/**
 * @date: 2024/7/3
 */
public class AppCamera {

    private final WeakReference<Activity> mContext;
    private final WeakReference<Fragment> mFragment;

    private AppCamera(Activity activity) {
        this(activity, null);
    }

    private AppCamera(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private AppCamera(Activity activity, Fragment fragment) {
        mContext = new WeakReference<>(activity);
        mFragment = new WeakReference<>(fragment);
        AppCameraSpec.getCleanInstance();
    }

    /**
     * Start Matisse from an Activity.
     * <p>
     * This Activity's {@link Activity#onActivityResult(int, int, Intent)} will be called when user
     * finishes selecting.
     *
     * @param activity Activity instance.
     * @return Matisse instance.
     */
    public static AppCamera from(Activity activity) {
        return new AppCamera(activity);
    }

    /**
     * Start Matisse from a Fragment.
     * <p>
     * This Fragment's {@link Fragment#onActivityResult(int, int, Intent)} will be called when user
     * finishes selecting.
     *
     * @param fragment Fragment instance.
     * @return Matisse instance.
     */
    public static AppCamera from(Fragment fragment) {
        return new AppCamera(fragment);
    }

    /**
     * 获取拍照或者录制的结果，如果是录制视频的话，则返回的是第一帧
     * @param data
     * @return
     */
    public static String obtainCaptureImageResult(Intent data) {
        return data.getStringExtra(MatisseConst.EXTRA_RESULT_CAPTURE_IMAGE_PATH);
    }

    /**
     * 获取录制视频的地址
     * @param data
     * @return
     */
    public static String obtainCaptureVideoResult(Intent data) {
        return data.getStringExtra(MatisseConst.EXTRA_RESULT_CAPTURE_VIDEO_PATH);
    }

    /**
     * Obtain state whether user decide to use selected media in original
     *
     * @param data Intent passed by {@link Activity#onActivityResult(int, int, Intent)} or
     *             {@link Fragment#onActivityResult(int, int, Intent)}.
     * @return Whether use original photo
     */
    public static boolean obtainOriginalState(Intent data) {
        return data.getBooleanExtra(MatisseConst.EXTRA_RESULT_ORIGINAL_ENABLE, false);
    }

    @Nullable
    Activity getActivity() {
        return mContext.get();
    }

    @Nullable
    Fragment getFragment() {
        return mFragment != null ? mFragment.get() : null;
    }

    public AppCamera setMode(CaptureMode mode) {
        AppCameraSpec.getInstance().captureMode = mode;
        return this;
    }

    /**
     * Start to select media and wait for result.
     *
     * @param requestCode Identity of the request Activity or Fragment.
     */
    public void forResult(final int requestCode) {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        //自动进行权限检查
        //录制所需权限
        String[] permissions;
        if (AppCameraSpec.getCleanInstance().captureMode == CaptureMode.Image) {
            permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"};
        } else {
            permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA", "android.permission.RECORD_AUDIO"};
        }

        if (!XXPermissions.isGranted(mContext.get(), permissions)) {
            XXPermissions.with(mContext.get())
                    .permission(permissions)
                    .request(new OnPermissionCallback() {
                        @Override
                        public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                            if (!allGranted) {
                                Toast.makeText(activity, "没有权限，无法使用该功能", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            gotoCamera(requestCode);
                        }

                        @Override
                        public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                            if (doNotAskAgain) {
                                // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                XXPermissions.startPermissionActivity(mContext.get(), permissions);
                            } else {

                            }
                        }
                    });
        } else {
            gotoCamera(requestCode);
        }
    }

    private void gotoCamera(int requestCode) {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        Fragment fragment = getFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            getActivity().startActivityForResult(intent, requestCode);
        }
    }
}
