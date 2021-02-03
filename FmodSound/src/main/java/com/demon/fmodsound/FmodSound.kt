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


    external fun saveSound(path: String, type: Int, savePath: String): Int
    external fun playSound(path: String, type: Int = MODE_NORMAL): Int

    external fun stopPlay()
    external fun resumePlay()
    external fun pausePlay()
    external fun isPlaying(): Boolean

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