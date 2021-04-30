package com.demon.changevoice

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.demon.changevoice.databinding.ActivitySoundTouchBinding
import com.demon.changevoice.record.PlayDialogFragment
import com.demon.soundcoding.AmrToPcm
import com.demon.soundcoding.AmrToWav
import com.pinealgland.soundtouch.SoundTouch
import kotlinx.coroutines.GlobalScope
import java.io.File

class SoundTouchActivity : AppCompatActivity() {
    private val TAG = "SoundTouchActivity"
    private lateinit var binding: ActivitySoundTouchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoundTouchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var path = intent.getStringExtra("path") ?: ""
        Log.i(TAG, "onCreate: $path")
        val file = File(path)
        if (!file.exists()) {
            showToast("录音文件不存在，请重新录制！")
            finish()
        } else {
            if (path.endsWith(".amr")) {
                path = AmrToPcm.makeAmrToPcm(path, false)
            }
            binding.tvPath.text = "音频文件:$path"
        }
        binding.buttonProcess.setOnClickListener {
            GlobalScope.launchIO {
                try {
                    val pitch = binding.editTextPitch.text.toString().toFloat()
                    process(path, "${getRecordFilePath()}/${System.currentTimeMillis()}", pitch)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun process(path: String, savePath: String, pitch: Float) {
        try {
            val st = SoundTouch()
            st.setTempo(1.0f)
            st.setSpeed(1.0f)
            st.setPitchSemiTones(pitch)
            val res = st.processFile(path, savePath)
            GlobalScope.launchUI {
                if (res == 0) {
                    binding.tvChange.text = "变声:$savePath"
                    PlayDialogFragment.newInstance(savePath).showAllowingStateLoss(supportFragmentManager)
                } else {
                    showToast(SoundTouch.getErrorString())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }
}