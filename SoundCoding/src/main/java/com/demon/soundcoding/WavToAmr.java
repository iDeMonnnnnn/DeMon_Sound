package com.demon.soundcoding;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author DeMon
 * Created on 2021/4/28.
 * E-mail 757454343@qq.com
 * Desc:
 */
public class WavToAmr {
    private static final String TAG = "WavToAmr";
    final private static byte[] header = new byte[]{0x23, 0x21, 0x41, 0x4D, 0x52, 0x0A};

    /**
     * WAV转AMR转，默认使用wav所在文件夹及文件名
     *
     * @param wavPath     wav文件路径
     * @param isDeleteWav 转码成功后是否删除wav
     * @return 转码后的amr路径
     */
    public static String makeWavToAmr(String wavPath, boolean isDeleteWav) {
        //建立输出文件
        String amrPath = "";
        if (amrPath.endsWith(".wav")) {
            amrPath = amrPath.replace(".wav", ".amr");
        } else {
            amrPath = amrPath + ".amr";
        }
        return makeWavToAmr(wavPath, amrPath, isDeleteWav);
    }

    /**
     * 通过反射调用android系统自身AmrInputStream类进行转换
     *
     * @param wavPath wav源文件
     * @param amrPath amr目标文件
     */
    public static String makeWavToAmr(String wavPath, String amrPath, boolean isDeleteWav) {
        try {
            Log.i(TAG, "makeAmrToPcm: wavPath=" + wavPath + ",amrPath=" + amrPath + ",isDeleteWav=" + isDeleteWav);
            File wavFile = new File(wavPath);
            if (!wavFile.exists()) {
                return "";
            }
            FileInputStream fileInputStream = new FileInputStream(wavFile);
            FileOutputStream fileoutputStream = new FileOutputStream(amrPath);
            // 获得Class
            Class<?> cls = Class.forName("android.media.AmrInputStream");
            // 通过Class获得所对应对象的方法
            Method[] methods = cls.getMethods();
            // 输出每个方法名
            fileoutputStream.write(header);
            Constructor<?> con = cls.getConstructor(InputStream.class);
            Object obj = con.newInstance(fileInputStream);
            for (Method method : methods) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if ("read".equals(method.getName())
                        && parameterTypes.length == 3) {
                    byte[] buf = new byte[1024];
                    int len = 0;
                    while ((len = (int) method.invoke(obj, buf, 0, 1024)) > 0) {
                        fileoutputStream.write(buf, 0, len);
                    }
                    break;
                }
            }
            for (Method method : methods) {
                if ("close".equals(method.getName())) {
                    method.invoke(obj);
                    break;
                }
            }
            fileoutputStream.close();
            if (isDeleteWav) {
                wavFile.delete();
            }
            Log.i(TAG, "makeWavToAmr: end!");
            return amrPath;
        } catch (Exception e) {
            Log.e(TAG, "makeWavToAmr: ", e);
            e.printStackTrace();
            return "";
        }
    }
}
