package com.lf.camera;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @date: 2024/7/18
 */
public class LFCameraFileUtil {

    public static String getRootPath(Context context) {
        String rootPath = context.getExternalFilesDir("").getPath() + File.separator + "LFCamera";
        File file = new File(rootPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return rootPath;
    }

    public static String saveBitmap(Context context, Bitmap bitmap) {
        String rootPath = getRootPath(context);

        long dataTake = System.currentTimeMillis();
        String picPath = rootPath + File.separator + "picture_" + dataTake + ".png";
        File picFile = new File(picPath);

        try {
            FileOutputStream fout = new FileOutputStream(picFile);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
            return picPath;
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * 清除缓存
     * @param context
     */
    public static boolean clearCatch(Context context) {
        File dir = new File(getRootPath(context));
        boolean isClear = deleteDir(dir);

        return isClear;
    }

    /**
     * 删除文件夹及其所有内容
     *
     * @param dir 需要删除的文件夹
     * @return 删除成功返回true，否则返回false
     */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

}
