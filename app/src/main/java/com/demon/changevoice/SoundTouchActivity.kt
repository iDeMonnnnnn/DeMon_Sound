package com.demon.changevoice

import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.demon.changevoice.databinding.ActivitySoundTouchBinding
import com.demon.fmodsound.FmodSound
import com.pinealgland.soundtouch.SoundTouch
import kotlinx.coroutines.GlobalScope
import org.fmod.FMOD
import java.io.File

class SoundTouchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySoundTouchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoundTouchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FMOD.init(this)
        val dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath
        val path = "$dir/aaa.wav"
        val file = File(path)
        if (!file.exists()) {
            Toast.makeText(this, "请添加录音文件！", Toast.LENGTH_SHORT).show()
        }
        binding.buttonProcess.setOnClickListener {
            GlobalScope.launchIO {
                try {
                    val pitch = binding.editTextPitch.text.toString().toFloat()
                    // button "process" pushed
                    process(path, "$dir/${System.currentTimeMillis()}.wav", pitch)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun process(path: String, savePath: String, pitch: Float) {
        try {
            val st = SoundTouch()
            st.setTempo(1.0f)
            st.setPitchSemiTones(pitch)
            val res = st.processFile(path, savePath)
            if (res == 0) {
                FmodSound.playSound(savePath, FmodSound.MODE_NORMAL)
            } else {
                val err = SoundTouch.getErrorString()
                GlobalScope.launchUI {
                    Toast.makeText(this, err, Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FMOD.close()
    }
}