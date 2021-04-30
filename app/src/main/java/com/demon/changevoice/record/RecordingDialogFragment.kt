package com.demon.changevoice.record

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.demon.changevoice.R
import com.demon.changevoice.databinding.FragmentDialogRecordingBinding
import com.demon.changevoice.getRecordFilePath
import com.demon.changevoice.showToast
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 开始录音的 DialogFragment
 *
 *
 * Created by DeMon on 2018/7/19.
 */
class RecordingDialogFragment : DialogFragment() {
    private var _binding: FragmentDialogRecordingBinding? = null

    private val binding
        get() = _binding!!

    private var isRecording = false
    private var intent: Intent? = null
    private var mListener: Listener? = null
    private var filePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDialogRecordingBinding.bind(inflater.inflate(R.layout.fragment_dialog_recording, container, false))
        return _binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recordAudioFabRecord.setOnClickListener {
            isRecording = !isRecording
            onRecord(isRecording)
        }
        binding.recordAudioIvClose.setOnClickListener {
            if (isRecording) {
                showToast("录音取消...")
                mListener?.onCancel()
            }
            dismissAllowingStateLoss()
        }
    }


    private fun onRecord(flag: Boolean) {
        intent = Intent(activity, RecordingService::class.java)
        if (flag) {
            val type = arguments?.getInt("type", 0) ?: 0
            filePath = getRecordFilePath(type)
            intent?.putExtra("FilePath", filePath)
            intent?.putExtra("Type", type)
            binding.recordAudioFabRecord.setImageResource(R.drawable.base_recording_stop)
            showToast("开始录音...")
            binding.recordAudioChronometerTime.base = SystemClock.elapsedRealtime()
            binding.recordAudioChronometerTime.start()
            requireActivity().startService(intent)
        } else {
            binding.recordAudioFabRecord.setImageResource(R.drawable.base_recording_start)
            binding.recordAudioChronometerTime.stop()
            mListener?.onFinish(filePath)
            showToast("录音结束...")
            activity?.stopService(intent)
            dismissAllowingStateLoss()
        }
    }

    fun showAllowingStateLoss(manager: FragmentManager) {
        if (!isVisible) {
            manager.beginTransaction().add(this, tag).commitAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isRecording) {
            val file = File(filePath)
            if (file.exists()) {
                file.delete()
            }
            requireActivity().stopService(intent)
        }
        _binding = null
    }

    interface Listener {
        fun onCancel()
        fun onFinish(path: String?)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    companion object {
        private const val TAG = "RecordAudioDialogFragme"
        fun newInstance(type: Int): RecordingDialogFragment {
            val fragment = RecordingDialogFragment()
            val bundle = Bundle()
            bundle.putInt("type", type)
            fragment.arguments = bundle
            return fragment
        }

    }
}