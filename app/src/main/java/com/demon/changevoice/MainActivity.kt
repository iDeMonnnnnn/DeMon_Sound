package com.demon.changevoice


import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.demon.changevoice.databinding.ActivityMainBinding
import com.permissionx.guolindev.PermissionX
import java.io.File

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PermissionX.init(this)
            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, "All permissions are granted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                }
            }
        val dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath
        val path = "$dir/aaa.wav"
        val file = File(path)
        if (!file.exists()) {
            Toast.makeText(this, "请添加录音文件！", Toast.LENGTH_SHORT).show()
        }
        binding.bnt0.setOnClickListener {
            startActivity(Intent(this, SoundFmodActivity::class.java))
        }
        binding.bnt1.setOnClickListener {
            startActivity(Intent(this, SoundTouchActivity::class.java))
        }
    }


}