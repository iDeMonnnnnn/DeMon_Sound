package com.demon.soundcoding;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

/**
 * PCM转AMR
 */
public class PcmToAmr {
    private static final String TAG = "SoundCoding_PcmToAmr";
    private static int SAMPLE_RATE = 8000;
    private static int BIT_RATE = 64;
    private static byte[] header = new byte[]{'#', '!', 'A', 'M', 'R', '\n'};

    /**
     * PCM转AMR转，默认使用pcm所在文件夹及文件名
     *
     * @param pcmPath     pcm文件路径
     * @param isDeletePcm 转码成功后是否删除pcm
     * @return 转码后的amr路径
     */
    public static String makePcmToAmr(String pcmPath, boolean isDeletePcm) {
        //建立输出文件
        String amrPath = "";
        if (amrPath.endsWith(".pcm")) {
            amrPath = amrPath.replace(".pcm", ".amr");
        } else {
            amrPath = amrPath + ".amr";
        }
        return makePcmToAmr(pcmPath, amrPath, isDeletePcm);
    }

    /**
     * PCM转AMR转
     *
     * @param pcmPath     pcm文件路径
     * @param amrPath     amr文件路径
     * @param isDeletePcm 转码成功后是否删除pcm
     * @return 转码后的amr路径
     */
    public static String makePcmToAmr(String pcmPath, String amrPath, boolean isDeletePcm) {
        try {
            Log.i(TAG, "makeAmrToPcm: pcmPath=" + pcmPath + ",amrPath=" + amrPath + ",isDeletePcm=" + isDeletePcm);
            MediaCodec encoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AMR_NB);
            MediaFormat format = new MediaFormat();
            format.setString(MediaFormat.KEY_MIME, MediaFormat.MIMETYPE_AUDIO_AMR_NB);
            format.setInteger(MediaFormat.KEY_SAMPLE_RATE, SAMPLE_RATE);
            format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
            format.setInteger(MediaFormat.KEY_BIT_RATE, BIT_RATE);


            encoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            File pcmFile = new File(pcmPath);
            FileInputStream fis = new FileInputStream(pcmFile);
            File armFIle = new File(amrPath);
            FileOutputStream fos = new FileOutputStream(armFIle);
            fos.write(header);
            encoder.start();

            ByteBuffer[] codecInputBuffers = encoder.getInputBuffers();
            ByteBuffer[] codecOutputBuffers = encoder.getOutputBuffers();
            byte[] tempBuffer = new byte[88200];
            boolean hasMoreData = true;
            MediaCodec.BufferInfo outBuffInfo = new MediaCodec.BufferInfo();
            double presentationTimeUs = 0;
            int totalBytesRead = 0;
            do {
                int inputBufIndex = 0;
                while (inputBufIndex != -1 && hasMoreData) {
                    inputBufIndex = encoder.dequeueInputBuffer(1000);
                    if (inputBufIndex >= 0) {
                        ByteBuffer dstBuf = codecInputBuffers[inputBufIndex];
                        dstBuf.clear();
                        int bytesRead = fis.read(tempBuffer, 0, dstBuf.limit());
                        if (bytesRead == -1) { // -1 implies EOS
                            hasMoreData = false;
                            encoder.queueInputBuffer(inputBufIndex, 0, 0, (long) presentationTimeUs, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                        } else {
                            totalBytesRead += bytesRead;
                            dstBuf.put(tempBuffer, 0, bytesRead);
                            encoder.queueInputBuffer(inputBufIndex, 0, bytesRead, (long) presentationTimeUs, 0);
                            presentationTimeUs = 1000000l * (totalBytesRead / 2) / SAMPLE_RATE;
                        }
                    }
                }
                // Drain audio
                int outputBufIndex = 0;
                while (outputBufIndex != MediaCodec.INFO_TRY_AGAIN_LATER) {
                    outputBufIndex = encoder.dequeueOutputBuffer(outBuffInfo, -1);
                    if (outputBufIndex >= 0) {
                        ByteBuffer encodedData = codecOutputBuffers[outputBufIndex];
                        encodedData.position(outBuffInfo.offset);
                        encodedData.limit(outBuffInfo.offset + outBuffInfo.size);
                        byte[] outData = new byte[outBuffInfo.size];
                        encodedData.get(outData, 0, outBuffInfo.size);
                        fos.write(outData, 0, outBuffInfo.size);
                        encoder.releaseOutputBuffer(outputBufIndex, false);
                    }
                }
            } while (outBuffInfo.flags != MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            if (fis != null) {
                fis.close();
                fis = null;
            }
            if (fos != null) {
                fos.flush();
                fos.close();
            }
            if (isDeletePcm) {
                pcmFile.delete();
            }
            Log.i(TAG, "makePcmToAmr: end!");
            return armFIle.getAbsolutePath();
        } catch (Exception ef) {
            ef.printStackTrace();
            Log.e(TAG, "makePcmToAmr: ", ef);
            return "";
        }
    }

}
