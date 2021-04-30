package com.demon.changevoice.record

import android.media.MediaRecorder
import android.os.Handler
import android.util.Log
import java.io.File

/**
 * @author DeMon
 * Created on 2021/4/29.
 * E-mail 757454343@qq.com
 * Desc: 录制AMR
 */
class AudioRecordAmr : IAudioRecord {
    private val TAG = "AudioRecordAmr"
    private var mFilePath: String = ""
    private var mRecorder: MediaRecorder? = null
    private var listener: IAudioRecord.RecordListener? = null
    var handler: Handler = Handler()

    private var runnable: Runnable = object : Runnable {
        override fun run() {
            mRecorder?.run {
                //获取音量大小
                Log.i(TAG, "run: 音量：$maxAmplitude")
                listener?.volume(maxAmplitude)
                //声音波形计算
                //val db = 30 * log10((maxAmplitude / 100).toDouble())
            }
            handler.postDelayed(this, 200)
        }
    }

    init {
        if (mRecorder == null) {
            mRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
                setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
                setAudioSamplingRate(8000)
            }
        }
    }

    override fun setRecordListener(listener: IAudioRecord.RecordListener?) {
        this.listener = listener
    }

    override fun startRecording(outFile: String) {
        mFilePath = outFile
        mRecorder?.run {
            listener?.start()
            val file = File(mFilePath)
            if (!file.exists()) {
                if (!file.parentFile.exists()) {
                    file.parentFile.mkdirs()
                }
                file.createNewFile()
            }
            setOutputFile(mFilePath)
            prepare()
            start()
        }
    }

    override fun stopRecording() {
        listener?.stop(mFilePath)
        mRecorder?.stop()
        mRecorder?.release()
        mRecorder = null
    }

    override fun getOutFilePath(): String = mFilePath
}