package com.demon.soundcoding;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 将pcm文件转化为wav文件
 */
public class PcmToWav {
    private static final String TAG = "PcmToWav";

    /**
     * 将一个pcm文件转化为wav文件,默认使用pcm所在文件夹及文件名
     *
     * @param pcmPath       pcm文件路径
     * @param deletePcmFile 是否删除源文件
     */
    public static String makePcmToWav(String pcmPath, boolean deletePcmFile) {
        //建立输出文件
        String wavPath = "";
        if (pcmPath.endsWith(".pcm")) {
            pcmPath = pcmPath.replace(".pcm", ".wav");
        } else {
            pcmPath = pcmPath + ".wav";
        }
        return makePcmToWav(pcmPath, wavPath, deletePcmFile);
    }

    /**
     * 将一个pcm文件转化为wav文件
     *
     * @param pcmPath     pcm文件路径
     * @param wavPath     目标文件路径(wav)
     * @param isDeletePcm 是否删除源文件
     */
    public static String makePcmToWav(String pcmPath, String wavPath, boolean isDeletePcm) {
        try {
            Log.i(TAG, "makeAmrToPcm: pcmPath=" + pcmPath + ",wavPath=" + wavPath + ",isDeletePcm=" + isDeletePcm);
            File file = new File(pcmPath);
            if (!file.exists()) {
                return "";
            }
            int TOTAL_SIZE = (int) file.length();
            // 填入参数，比特率等等。这里用的是16位单声道 8000 hz
            WaveHeader header = new WaveHeader();
            // 长度字段 = 内容的大小（TOTAL_SIZE) +
            // 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
            header.fileLength = TOTAL_SIZE + (44 - 8);
            header.FmtHdrLeth = 16;
            header.BitsPerSample = 16;
            header.Channels = 1;
            header.FormatTag = 0x0001;
            header.SamplesPerSec = 8000;
            header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);
            header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
            header.DataHdrLeth = TOTAL_SIZE;
            byte[] h = header.getHeader();
            if (h.length != 44) // WAV标准，头部应该是44字节,如果不是44个字节则不进行转换文件
                return "";
            //合成所有的pcm文件的数据，写到目标文件
            byte[] buffer = new byte[1024 * 4]; // Length of All Files, Total Size
            OutputStream ouStream = new BufferedOutputStream(new FileOutputStream(wavPath));
            ouStream.write(h, 0, h.length);
            InputStream inStream = new BufferedInputStream(new FileInputStream(file));
            int size = inStream.read(buffer);
            while (size != -1) {
                ouStream.write(buffer);
                size = inStream.read(buffer);
            }
            inStream.close();
            ouStream.close();
            if (isDeletePcm) {
                file.delete();
            }
            Log.i(TAG, "makePcmToWav  success!");
            return wavPath;
        } catch (Exception e) {
            Log.e(TAG, "makePcmToWav: ", e);
            return "";
        }
    }

    /**
     * 合并多个pcm文件为一个wav文件
     *
     * @param filePathList pcm文件路径集合
     * @param wavPath      目标wav文件路径
     * @return true|false
     */

    public static boolean mergePcmsToWav(List<String> filePathList, String wavPath) {
        File[] file = new File[filePathList.size()];
        byte buffer[] = null;

        int TOTAL_SIZE = 0;
        int fileNum = filePathList.size();

        for (int i = 0; i < fileNum; i++) {
            file[i] = new File(filePathList.get(i));
            TOTAL_SIZE += file[i].length();
        }

        // 填入参数，比特率等等。这里用的是16位单声道 8000 hz
        WaveHeader header = new WaveHeader();
        // 长度字段 = 内容的大小（TOTAL_SIZE) +
        // 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
        header.fileLength = TOTAL_SIZE + (44 - 8);
        header.FmtHdrLeth = 16;
        header.BitsPerSample = 16;
        header.Channels = 2;
        header.FormatTag = 0x0001;
        header.SamplesPerSec = 8000;
        header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);
        header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
        header.DataHdrLeth = TOTAL_SIZE;

        byte[] h = null;
        try {
            h = header.getHeader();
        } catch (IOException e1) {
            Log.e(TAG, e1.getMessage());
            return false;
        }

        if (h.length != 44) // WAV标准，头部应该是44字节,如果不是44个字节则不进行转换文件
            return false;

        //先删除目标文件
        File destfile = new File(wavPath);
        if (destfile.exists())
            destfile.delete();

        //合成所有的pcm文件的数据，写到目标文件
        try {
            buffer = new byte[1024 * 4]; // Length of All Files, Total Size
            InputStream inStream = null;
            OutputStream ouStream = null;

            ouStream = new BufferedOutputStream(new FileOutputStream(
                    wavPath));
            ouStream.write(h, 0, h.length);
            for (int j = 0; j < fileNum; j++) {
                inStream = new BufferedInputStream(new FileInputStream(file[j]));
                int size = inStream.read(buffer);
                while (size != -1) {
                    ouStream.write(buffer);
                    size = inStream.read(buffer);
                }
                inStream.close();
            }
            ouStream.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
//        clearFiles(filePathList);
        Log.i(TAG, "mergePCMFilesToWAVFile  success!");
        return true;
    }

}


