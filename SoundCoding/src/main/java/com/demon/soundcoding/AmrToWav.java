package com.demon.soundcoding;

import android.util.Log;

/**
 * @author DeMon
 * Created on 2021/4/28.
 * E-mail 757454343@qq.com
 * Desc:
 */
public class AmrToWav {

    private static final String TAG = "SoundCoding_AmrToWav";

    /**
     * AMR转WAV，默认使用amr所在文件夹及文件名
     *
     * @param amrPath     amr文件路径
     * @param isDeleteAmr 转码成功后是否删除amr文件
     * @return 转码后的wav路径
     */
    public static String makeAmrToWav(String amrPath, boolean isDeleteAmr) {
        //建立输出文件
        String wavPath = "";
        if (amrPath.endsWith(".amr")) {
            wavPath = amrPath.replace(".amr", ".wav");
        } else {
            wavPath = amrPath + ".wav";
        }
        return makeAmrToWav(amrPath, wavPath, isDeleteAmr);
    }


    /**
     * AMR转WAV
     *
     * @param amrPath     amr文件路径
     * @param wavPath     wav文件路径
     * @param isDeleteAmr 转码成功后是否删除amr文件
     * @return 转码后的wav路径
     */
    public static String makeAmrToWav(String amrPath, String wavPath, boolean isDeleteAmr) {
        Log.i(TAG, "makeAmrToPcm: amrPath=" + amrPath + ",wavPath=" + wavPath + ",isDeleteAmr=" + isDeleteAmr);
        String pcmPath = AmrToPcm.makeAmrToPcm(amrPath, isDeleteAmr);
        return PcmToWav.makePcmToWav(pcmPath, wavPath, true);
    }
}
