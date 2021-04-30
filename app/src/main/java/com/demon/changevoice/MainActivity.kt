package com.demon.changevoice


import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.demon.changevoice.databinding.ActivityMainBinding
import com.demon.changevoice.record.PlayDialogFragment
import com.demon.changevoice.record.RecordingDialogFragment
import com.permissionx.guolindev.PermissionX
import java.io.File

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    private var allGranted: Boolean = false

    private var recordType = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rgGroup.setOnCheckedChangeListener { group, checkedId ->
            val rb = group.findViewById<RadioButton>(checkedId)
            recordType = group.indexOfChild(rb)
            deleteRecord()
        }

        val savePath = SPUtil.get("record_path", "") as String;
        if (File(savePath).exists()) {
            binding.tvPath.text = savePath
        }
        binding.btnRecord.setOnClickListener {
            if (!allGranted) {
                showToast("Permission is missing!")
            }
            deleteRecord()
            RecordingDialogFragment.newInstance(recordType).apply {
                setListener(object : RecordingDialogFragment.Listener {
                    override fun onCancel() {

                    }

                    override fun onFinish(path: String?) {
                        binding.tvPath.text = path
                        SPUtil.save("record_path", path ?: "")
                    }
                })
                showAllowingStateLoss(supportFragmentManager)
            }
        }

        binding.btnPlay.setOnClickListener {
            val path = binding.tvPath.text.toString()
            if (path.isEmpty()) {
                showToast("请先录音！")
            } else {
                PlayDialogFragment.newInstance(path).showAllowingStateLoss(supportFragmentManager)
            }
        }

        binding.bntFmod.setOnClickListener {
            val path = binding.tvPath.text.toString()
            if (path.isEmpty()) {
                showToast("请先录音！")
            } else {
                startActivity(Intent(this, SoundFmodActivity::class.java).putExtra("path", path))
            }
        }
        binding.bntSoundTouch.setOnClickListener {
            val path = binding.tvPath.text.toString()
            if (path.isEmpty()) {
                showToast("请先录音！")
            } else {
                startActivity(Intent(this, SoundTouchActivity::class.java).putExtra("path", path))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        deleteRecord()
    }

    //删除上一个录音文件
    private fun deleteRecord() {
        val file = File(binding.tvPath.text.toString())
        if (file.exists()) {
            file.delete()
        }
        binding.tvPath.text = ""
    }


    private fun initPermission() {
        PermissionX.init(this)
            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
            .request { allGranted, grantedList, deniedList ->
                this.allGranted = allGranted
                if (allGranted) {
                    showToast("Permissions all Granted!")
                } else {
                    showToast("These permissions are denied: $deniedList")
                }
            }
    }


}