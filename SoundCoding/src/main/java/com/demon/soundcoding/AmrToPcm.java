package com.demon.soundcoding;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * AMR转PCM
 */
public class AmrToPcm {
    private final static int SAMPLE_RATE = 8000;
    private final static int BIT_RATE = 64;
    private static final String TAG = "SoundCoding_AmrToPcm";

    /**
     * AMR转PCM，默认使用amr所在文件夹及文件名
     *
     * @param amrPath     amr文件路径
     * @param isDeleteAmr 转码成功后是否删除amr文件
     * @return 转码后的pcm路径
     */
    public static String makeAmrToPcm(String amrPath, boolean isDeleteAmr) {
        //建立输出文件
        String pcmPath = "";
        if (amrPath.endsWith(".amr")) {
            pcmPath = amrPath.replace(".amr", ".pcm");
        } else {
            pcmPath = amrPath + ".pcm";
        }

        return makeAmrToPcm(amrPath, pcmPath, isDeleteAmr);
    }

    /**
     * AMR转PCM
     *
     * @param amrPath     amr文件路径
     * @param pcmPath     pcm文件路径
     * @param isDeleteAmr 转码成功后是否删除amr文件
     * @return 转码后的pcm路径
     */
    public static String makeAmrToPcm(String amrPath, String pcmPath, boolean isDeleteAmr) {
        try {
            Log.i(TAG, "makeAmrToPcm: amrPath=" + amrPath + ",pcmPath=" + pcmPath + ",isDeleteAmr=" + isDeleteAmr);
            //校验文件合法性
            File amrFile = new File(amrPath);
            if (!amrFile.exists()) {
                throw new IllegalArgumentException("amr file not found : " + amrFile.getAbsolutePath());
            }
            if (!isLegalFile(amrFile)) {
                throw new IllegalArgumentException("amr file is not a legal file : " + amrFile.getAbsolutePath());
            }
            if (TextUtils.isEmpty(pcmPath)) {
                throw new IllegalArgumentException("pcmPath can not empty!");
            }
            //建立输出文件
            File pcmFile = new File(pcmPath);
            FileOutputStream pcmOutPutStream = new FileOutputStream(pcmFile);
            //获取文件信息
            MediaExtractor mediaExtractor = new MediaExtractor();
            mediaExtractor.setDataSource(amrPath);
            for (int i = 0; i < mediaExtractor.getTrackCount(); i++) {
                MediaFormat format = mediaExtractor.getTrackFormat(i);
                if (format.getString(MediaFormat.KEY_MIME).startsWith("audio/")) {
                    mediaExtractor.selectTrack(i);
                    break;
                }
            }
            //设置解码器
            MediaCodec decoder = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_AUDIO_AMR_NB);
            MediaFormat format = new MediaFormat();
            format.setString(MediaFormat.KEY_MIME, MediaFormat.MIMETYPE_AUDIO_AMR_NB);
            format.setInteger(MediaFormat.KEY_SAMPLE_RATE, SAMPLE_RATE);
            format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
            format.setInteger(MediaFormat.KEY_BIT_RATE, BIT_RATE);
            decoder.configure(format, null, null, 0);
            decoder.start();
            //获取输入输出队列
            ByteBuffer[] inputBuffers = decoder.getInputBuffers();
            ByteBuffer[] outputBuffers = decoder.getOutputBuffers();
            //获取当前缓冲信息
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            for (; ; ) {
                int inIndex = decoder.dequeueInputBuffer(1000);
                if (inIndex >= 0) {
                    //入队输入数据
                    ByteBuffer buffer = inputBuffers[inIndex];
                    int sampleSize = mediaExtractor.readSampleData(buffer, 0);
                    if (sampleSize < 0) {
                        Log.d(TAG, "InputBuffer BUFFER_FLAG_END_OF_STREAM");
                        decoder.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                    } else {
                        decoder.queueInputBuffer(inIndex, 0, sampleSize, mediaExtractor.getSampleTime(), 0);
                        mediaExtractor.advance();
                    }
                    //出队输出数据
                    int outIndex = decoder.dequeueOutputBuffer(bufferInfo, 1000);
                    switch (outIndex) {
                        case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED: {
                            Log.d(TAG, "INFO_OUT_PUT_BUFFERS_CHANGED");
                            outputBuffers = decoder.getOutputBuffers();
                            break;
                        }
                        case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED: {
                            MediaFormat mediaFormat = decoder.getOutputFormat();
                            Log.d(TAG, "MediaFormat=" + mediaFormat);
                            break;
                        }
                        case MediaCodec.INFO_TRY_AGAIN_LATER: {
                            Log.d(TAG, "Decoding timeout");
                            break;
                        }
                        default: {
                            ByteBuffer outBuffer = outputBuffers[outIndex];
                            final byte[] chunk = new byte[bufferInfo.size];
                            outBuffer.get(chunk);
                            outBuffer.clear();
                            pcmOutPutStream.write(chunk, 0, chunk.length);
                            decoder.releaseOutputBuffer(outIndex, false);
                            break;
                        }
                    }
                    if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        Log.d(TAG, "End of parsing");
                        break;
                    }
                }
            }
            mediaExtractor.release();
            decoder.stop();
            decoder.release();
            if (isDeleteAmr) {
                amrFile.delete();
            }
            return pcmFile.getAbsolutePath();
        } catch (IOException e) {
            Log.e(TAG, "makeAmrToPcm: ", e);
            return "";
        }
    }


    public static final boolean isLegalFile(File file) {
        return file != null && file.exists() && file.canRead() && file.isFile() && file.length() > 0L;
    }

}
