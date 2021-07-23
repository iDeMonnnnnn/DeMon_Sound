package com.demon.changevoice

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

/**
 * @author DeMon
 * Created on 2020/10/23.
 * E-mail 757454343@qq.com
 * Desc:
 */

/**
 * io线程
 */
fun CoroutineScope.launchIO(block: suspend () -> Unit): Job {
    return this.launch(Dispatchers.IO) { block() }
}

/**
 * 主线程
 */
fun CoroutineScope.launchUI(block: suspend () -> Unit): Job {
    return this.launch(Dispatchers.Main) { block() }
}

fun Fragment.showToast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Fragment.getRecordFilePath(type: Int = 0) = this.requireContext().getRecordFilePath(type)

fun Context.getRecordFilePath(type: Int = 0): String {
    val suffix = when (type) {
        1 -> ".wav"
        2 -> ".pcm"
        else -> ".amr"
    }
    val file = File(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath + "/Record")
    if (!file.exists()) {
        file.mkdirs()
    }
    val path = File(file, "${System.currentTimeMillis()}$suffix")
    if (!path.exists()) {
        file.createNewFile()
    }
    return path.absolutePath
}


