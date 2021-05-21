package com.demon.fmodsound

import java.lang.Exception

/**
 * @author DeMon
 * Created on 2020/12/31.
 * E-mail 757454343@qq.com
 * Desc:
 */
object FmodSound {
    //音效的类型
    const val MODE_NORMAL = 0 //正常

    const val MODE_FUNNY = 1 //搞笑

    const val MODE_UNCLE = 2 //大叔

    const val MODE_LOLITA = 3 //萝莉

    const val MODE_ROBOT = 4 //机器人

    const val MODE_ETHEREAL = 5 //空灵

    const val MODE_CHORUS = 6 //混合

    const val MODE_HORROR = 7 //恐怖

    init {
        System.loadLibrary("fmodL")
        System.loadLibrary("fmod")
        System.loadLibrary("FmodSound")
    }


    /**
     * 变声并播放，耗时，需要在子线程中执行
     *
     * @param path 音频路径，只支持WAV格式
     * @param type 变声音效类型，默认=0即普通播放无变声效果
     */
    external fun playSound(path: String, type: Int = MODE_NORMAL): Int

    external fun stopPlay()
    external fun resumePlay()
    external fun pausePlay()
    external fun isPlaying(): Boolean

    /**
     * 变声保存，耗时，需要在子线程中执行
     *
     * @param path 音频路径，只支持WAV格式
     * @param type 变声音效类型，默认=0即普通播放无变声效果
     * @param savePath 变声后保存的路径，输出为WAV格式
     */
    external fun saveSound(path: String, type: Int, savePath: String): Int

    /**
     * 变声保存
     * @param path 音频路径，只支持WAV格式
     * @param type 变声音效类型，默认=0即普通播放无变声效果
     * @param savePath 变声后保存的路径，输出为WAV格式
     * @param listener 变声结果监听，根据回调可以在变声成功后播放
     */
    fun saveSoundAsync(path: String, type: Int, savePath: String, listener: ISaveSoundListener? = null) {
        try {
            if (isPlaying()) {
                stopPlay()
            }
            val result = saveSound(path, type, savePath)
            if (result == 0) {
                listener?.onFinish(path, savePath, type)
            } else {
                listener?.onError("error")
            }
        } catch (e: Exception) {
            listener?.onError(e.message)
        }
    }

    interface ISaveSoundListener {
        //成功
        fun onFinish(path: String, savePath: String, type: Int)

        //出错
        fun onError(msg: String?)
    }

}