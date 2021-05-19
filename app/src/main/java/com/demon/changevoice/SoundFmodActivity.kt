package com.demon.changevoice

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.demon.changevoice.databinding.ActivitySoundFmodBinding
import com.demon.changevoice.widget.FNRadioGroup
import com.demon.fmodsound.FmodSound
import com.demon.soundcoding.AmrToPcm
import com.demon.soundcoding.AmrToWav
import com.demon.soundcoding.WavToPcm
import kotlinx.coroutines.GlobalScope
import org.fmod.FMOD
import java.io.File

class SoundFmodActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivitySoundFmodBinding

    private var type: Int = FmodSound.MODE_NORMAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FMOD.init(this)
        binding = ActivitySoundFmodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var path = intent.getStringExtra("path") ?: ""
        val file = File(path)
        if (!file.exists()) {
            showToast("录音文件不存在，请重新录制！")
            finish()
        } else {
            if (path.endsWith(".amr")) {
                path = AmrToWav.makeAmrToWav(path, false)
            }
            binding.tvPath.text = "音频文件:$path"
        }

        binding.fnRG.setOnCheckedChangeListener { group, checkedId ->
            val pos = group.indexOfChild(group.findViewById(checkedId))
            Log.i(TAG, "onCreate: $pos")
            type = pos
        }
        binding.btnPlay.setOnClickListener {
            GlobalScope.launchIO {
                FmodSound.playSound(path, type)
            }
        }
        binding.btnSave.setOnClickListener {
            binding.tvSave.text = "开始变声..."
            GlobalScope.launchIO {
                FmodSound.saveSoundAsync(path, type, getRecordFilePath(1), object : FmodSound.ISaveSoundListener {
                    override fun onFinish(path: String, savePath: String, type: Int) {
                        runOnUiThread {
                            binding.tvSave.text = "变声输出文件路径:$savePath"
                        }
                        FmodSound.playSound(savePath)
                    }

                    override fun onError(msg: String?) {
                        Log.e(TAG, "onError: $msg")
                        runOnUiThread {
                            binding.tvSave.text = "变声失败:$msg"
                        }
                    }
                })
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        FMOD.close()
    }
}