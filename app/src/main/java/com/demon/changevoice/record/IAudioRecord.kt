package com.demon.changevoice.record

/**
 * @author DeMon
 * Created on 2021/4/29.
 * E-mail 757454343@qq.com
 * Desc:
 */
interface IAudioRecord {

    fun setRecordListener(listener: RecordListener?)

    fun startRecording(outFile: String)

    fun stopRecording()

    fun getOutFilePath(): String


    interface RecordListener {
        fun start()

        fun volume(volume: Int)

        fun stop(outPath: String)
    }

}