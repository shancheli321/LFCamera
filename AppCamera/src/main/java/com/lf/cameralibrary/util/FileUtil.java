package com.lf.cameralibrary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * =====================================
 * 作    者: 陈嘉桐
 * 版    本：1.1.4
 * 创建日期：2017/4/25
 * 描    述：
 * =====================================
 */
public class FileUtil {

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

    public static boolean deleteFile(String url) {
        boolean result = false;
        File file = new File(url);
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }
}
