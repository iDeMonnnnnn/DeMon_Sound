package com.demon.changevoice

import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.demon.changevoice.databinding.ActivitySoundFmodBinding
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
                path = AmrToPcm.makeAmrToPcm(path, false)
            }
            binding.tvPath.text = "音频文件:$path"
        }
        binding.bnt0.setOnClickListener {
            GlobalScope.launchIO {
                FmodSound.playSound(path, FmodSound.MODE_NORMAL)
            }
        }
        binding.bnt1.setOnClickListener {
            GlobalScope.launchIO {
                FmodSound.saveSoundAsync(path, FmodSound.MODE_FUNNY, "${getRecordFilePath()}/${System.currentTimeMillis()}", object : FmodSound.ISaveSoundListener {
                    override fun onFinish(path: String, savePath: String, type: Int) {
                        FmodSound.playSound(savePath)
                    }

                    override fun onError(msg: String?) {

                    }
                })
            }
        }
        binding.bnt2.setOnClickListener {
            GlobalScope.launchIO {
                FmodSound.playSound(path, FmodSound.MODE_UNCLE)
            }
        }
        binding.bnt3.setOnClickListener {
            GlobalScope.launchIO {
                FmodSound.playSound(path, FmodSound.MODE_LOLITA)
            }
        }
        binding.bnt4.setOnClickListener {
            GlobalScope.launchIO {
                FmodSound.playSound(path, FmodSound.MODE_ROBOT)
            }
        }
        binding.bnt5.setOnClickListener {
            GlobalScope.launchIO {
                FmodSound.playSound(path, FmodSound.MODE_ETHEREAL)
            }
        }
        binding.bnt6.setOnClickListener {
            GlobalScope.launchIO {
                FmodSound.playSound(path, FmodSound.MODE_CHORUS)
            }
        }
        binding.bnt7.setOnClickListener {
            GlobalScope.launchIO {
                FmodSound.playSound(path, FmodSound.MODE_HORROR)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        FMOD.close()
    }
}