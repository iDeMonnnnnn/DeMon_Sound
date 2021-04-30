package com.demon.changevoice.record

import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.demon.changevoice.R
import com.demon.changevoice.databinding.FragmentDialogPlayBinding
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 播放录音的 DialogFragment
 *
 *
 * Created by DeMon on 2018/7/19.
 */
class PlayDialogFragment : DialogFragment() {
    private var _binding: FragmentDialogPlayBinding? = null
    private val binding
        get() = _binding!!
    private var mMediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private var filePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetStyle)
        filePath = arguments?.getString(FILE_PATH, "")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDialogPlayBinding.bind(inflater.inflate(R.layout.fragment_dialog_play, container, false))
        return _binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivClose.setOnClickListener { dismiss() }
        val filter: ColorFilter = LightingColorFilter(resources.getColor(R.color.teal_700), resources.getColor(R.color.teal_700))
        binding.seekbar.progressDrawable.colorFilter = filter
        binding.seekbar.thumb.colorFilter = filter
        binding.seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (mMediaPlayer != null && fromUser) {
                    mMediaPlayer?.seekTo(progress)
                    binding.root.removeCallbacks(mRunnable)
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer?.currentPosition?.toLong() ?: 0)
                    val seconds = (TimeUnit.MILLISECONDS.toSeconds(mMediaPlayer?.currentPosition?.toLong() ?: 0)
                            - TimeUnit.MINUTES.toSeconds(minutes))
                    binding.currentProgressTextView.text = String.format("%02d:%02d", minutes, seconds)
                    updateSeekBar()
                } else if (mMediaPlayer == null && fromUser) {
                    prepareMediaPlayerFromPoint(progress)
                    updateSeekBar()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                if (mMediaPlayer != null) {
                    binding.root.removeCallbacks(mRunnable)
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (mMediaPlayer != null) {
                    binding.root.removeCallbacks(mRunnable)
                    mMediaPlayer?.seekTo(seekBar.progress)
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer?.currentPosition?.toLong() ?: 0)
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(mMediaPlayer?.currentPosition?.toLong() ?: 0) - TimeUnit.MINUTES.toSeconds(minutes)
                    binding.currentProgressTextView.text = String.format("%02d:%02d", minutes, seconds)
                    updateSeekBar()
                }
            }
        })

        binding.fabPlay.setOnClickListener(View.OnClickListener {
            onPlay(isPlaying)
            isPlaying = !isPlaying
        })
        val file = File(filePath)
        binding.fileNameTextView.text = file.name
    }


    override fun onPause() {
        super.onPause()
        stopPlaying()

    }

    override fun onDestroy() {
        super.onDestroy()
        stopPlaying()
    }

    private fun onPlay(isPlaying: Boolean) {
        if (!isPlaying) {
            if (mMediaPlayer == null) {
                startPlaying()
            } else {
                resumePlaying()
            }
        } else {
            pausePlaying()
        }
    }

    private fun startPlaying() {
        binding.fabPlay.setImageResource(R.drawable.base_record_pause)
        mMediaPlayer = MediaPlayer()
        try {
            mMediaPlayer?.setDataSource(filePath)
            mMediaPlayer?.prepare()
            binding.seekbar.max = mMediaPlayer?.duration ?: 0
            mMediaPlayer?.setOnPreparedListener {
                mMediaPlayer?.start()
                val minutes = TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer?.duration?.toLong() ?: 0)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(mMediaPlayer?.duration?.toLong() ?: 0) - TimeUnit.MINUTES.toSeconds(minutes)
                binding.fileLengthTextView.text = String.format("%02d:%02d", minutes, seconds)
            }
        } catch (e: IOException) {
            Log.e(TAG, "prepare() failed")
        }
        mMediaPlayer?.setOnCompletionListener { stopPlaying() }
        updateSeekBar()
    }

    private fun prepareMediaPlayerFromPoint(progress: Int) {
        mMediaPlayer = MediaPlayer()
        try {
            mMediaPlayer?.setDataSource(filePath)
            mMediaPlayer?.prepare()
            binding.seekbar.max = mMediaPlayer?.duration ?: 0
            mMediaPlayer?.seekTo(progress)
            mMediaPlayer?.setOnCompletionListener { stopPlaying() }
        } catch (e: IOException) {
            Log.e(TAG, "prepare() failed")
        }

        //keep screen on while playing audio
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun pausePlaying() {
        if (mMediaPlayer != null) {
            binding.fabPlay.setImageResource(R.drawable.base_record_play)
            binding.root.removeCallbacks(mRunnable)
            mMediaPlayer?.pause()
        }
    }

    private fun resumePlaying() {
        if (mMediaPlayer != null) {
            binding.fabPlay.setImageResource(R.drawable.base_record_pause)
            binding.root.removeCallbacks(mRunnable)
            mMediaPlayer?.start()
            updateSeekBar()
        }
    }

    private fun stopPlaying() {
        if (mMediaPlayer != null) {
            binding.fabPlay.setImageResource(R.drawable.base_record_play)
            binding.root.removeCallbacks(mRunnable)
            mMediaPlayer?.stop()
            mMediaPlayer?.reset()
            mMediaPlayer?.release()
            mMediaPlayer = null
            binding.seekbar.progress = binding.seekbar.max
            isPlaying = !isPlaying
            binding.currentProgressTextView.text = binding.fileLengthTextView.text
            binding.seekbar.progress = binding.seekbar.max
        }
    }

    private val mRunnable = Runnable {
        if (mMediaPlayer != null) {
            val mCurrentPosition = mMediaPlayer?.currentPosition ?: 0
            binding.seekbar.progress = mCurrentPosition
            val minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition.toLong())
            val seconds = (TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition.toLong()) - TimeUnit.MINUTES.toSeconds(minutes))
            binding.currentProgressTextView.text = String.format("%02d:%02d", minutes, seconds)
            updateSeekBar()
        }
    }

    private fun updateSeekBar() {
        binding.root.postDelayed(mRunnable, 1000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mMediaPlayer?.release()
        mMediaPlayer = null
        _binding = null
    }

    fun showAllowingStateLoss(manager: FragmentManager) {
        if (!isVisible) {
            manager.beginTransaction().add(this, tag).commitAllowingStateLoss()
        }
    }

    companion object {
        private const val TAG = "PlayDialogFragment"
        const val FILE_PATH = "FilePath"
        fun newInstance(filePath: String): PlayDialogFragment {
            val f = PlayDialogFragment()
            val b = Bundle()
            b.putString(FILE_PATH, filePath)
            f.arguments = b
            return f
        }
    }
}