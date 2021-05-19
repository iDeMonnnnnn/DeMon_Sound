package com.demon.changevoice

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.demon.changevoice.databinding.ActivitySoundTouchBinding
import com.demon.changevoice.record.PlayDialogFragment
import com.demon.soundcoding.AmrToWav
import kotlinx.coroutines.GlobalScope
import net.surina.soundtouch.SoundTouch
import java.io.File

/**
 * SoundTouch变声只支持.WAV
 */
class SoundTouchActivity : AppCompatActivity() {
    private val TAG = "SoundTouchActivity"
    private lateinit var binding: ActivitySoundTouchBinding

    private var pitch = 0.0f
    private var tempo = 1.0f
    private var speed = 1.0f
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
                path = AmrToWav.makeAmrToWav(path, false)
            }
            binding.tvPath.text = "音频文件:$path"
        }
        binding.seekPitch.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                pitch = (progress - 12).toFloat()
                binding.textPitch.text = "音调(-12~12)---当前:$pitch"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        binding.seekTempo.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tempo = progress.toFloat() / 10
                binding.textTempo.text = "时间(0~10)---当前:$tempo"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        binding.seekSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                speed = progress.toFloat()/10
                binding.textSpeed.text = "速度(0~10)---当前:$speed"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        binding.buttonProcess.setOnClickListener {
            GlobalScope.launchIO {
                try {
                    process(path, getRecordFilePath(1))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun process(path: String, savePath: String) {
        try {
            val st = SoundTouch()
            st.setTempo(tempo) //速度
            st.setSpeed(speed) //速度&变调
            st.setPitchSemiTones(pitch) //音调变调
            val res = st.processFile(path, savePath)
            GlobalScope.launchUI {
                if (res == 0) {
                    binding.tvChange.text = "变声输出文件路径:$savePath"
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