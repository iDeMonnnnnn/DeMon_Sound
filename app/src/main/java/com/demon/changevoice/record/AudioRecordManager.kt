package com.demon.changevoice.record

/**
 * @author DeMon
 * Created on 2021/4/29.
 * E-mail 757454343@qq.com
 * Desc:
 */
object AudioRecordManager {

    const val AMR = 0
    const val PCM = 1

    fun getInstance(mode: Int): IAudioRecord {
        return when (mode) {
            AMR -> AudioRecordAmr()
            PCM -> AudioRecordWav()
            else -> AudioRecordAmr()
        }
    }
}