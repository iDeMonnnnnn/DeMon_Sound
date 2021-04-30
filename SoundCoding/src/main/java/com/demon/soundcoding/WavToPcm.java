package com.demon.soundcoding;

import android.util.Log;

/**
 * @author DeMon
 * Created on 2021/4/28.
 * E-mail 757454343@qq.com
 * Desc:
 */
public class WavToPcm {
    private static final String TAG = "WavToPcm";

    /**
     * WAV转PCM，模式使用WAV文件夹及文件名
     *
     * @param wavPath     wav文件路径
     * @param isDeleteWav 转码成功后是否删除wav文件
     * @return 转码后的pcm路径
     */
    public static String makeWavToPcm(String wavPath, boolean isDeleteWav) {
        //建立输出文件
        String pcmPath = "";
        if (wavPath.endsWith(".wav")) {
            wavPath = wavPath.replace(".wav", ".pcm");
        } else {
            wavPath = wavPath + ".pcm";
        }
        return makeWavToPcm(wavPath, pcmPath, isDeleteWav);
    }


    /**
     * WAV转PCM
     *
     * @param wavPath     wav文件路径
     * @param pcmPath     pcm文件路径
     * @param isDeleteWav 转码成功后是否删除wav文件
     * @return 转码后的pcm路径
     */
    public static String makeWavToPcm(String wavPath, String pcmPath, boolean isDeleteWav) {
        Log.i(TAG, "makeAmrToPcm: wavPath=" + wavPath + ",pcmPath=" + pcmPath + ",isDeleteWav=" + isDeleteWav);
        String amrPath = WavToAmr.makeWavToAmr(wavPath, isDeleteWav);
        return AmrToPcm.makeAmrToPcm(amrPath, pcmPath, true);
    }
}
