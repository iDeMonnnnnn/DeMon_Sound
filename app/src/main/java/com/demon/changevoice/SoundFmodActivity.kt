package com.demon.changevoice

import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.demon.changevoice.databinding.ActivitySoundFmodBinding
import com.demon.fmodsound.FmodSound
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
        val dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath
        val path = "$dir/aaa.wav"
        val file = File(path)
        if (!file.exists()) {
            Toast.makeText(this, "请添加录音文件！", Toast.LENGTH_SHORT).show()
        }
        binding.bnt0.setOnClickListener {
            GlobalScope.launchIO {
                FmodSound.playSound(path, FmodSound.MODE_NORMAL)
            }
        }
        binding.bnt1.setOnClickListener {
            GlobalScope.launchIO {
                FmodSound.saveSoundAsync(path, FmodSound.MODE_FUNNY, "$dir/aaa1.wav", object : FmodSound.ISaveSoundListener {
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