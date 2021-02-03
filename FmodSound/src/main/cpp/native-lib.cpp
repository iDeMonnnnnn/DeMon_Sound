#include <jni.h>
#include "inc/fmod.hpp"
#include <string>
#include <android/log.h>
#include <unistd.h>

using namespace FMOD;

#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"FmodSound",FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR,"FmodSound",FORMAT,##__VA_ARGS__);

#define MODE_NORMAL 0
#define MODE_FUNNY 1
#define MODE_UNCLE 2
#define MODE_LOLITA 3
#define MODE_ROBOT 4
#define MODE_ETHEREAL 5
#define MODE_CHORUS 6
#define MODE_HORROR 7

Channel *channel;

extern "C"
JNIEXPORT jint JNICALL
Java_com_demon_fmodsound_FmodSound_saveSound(JNIEnv *env, jobject cls, jstring path_jstr, jint type, jstring save_jstr) {
    Sound *sound;
    DSP *dsp;
    bool playing = true;
    float frequency = 0;
    System *mSystem;
    JNIEnv *mEnv = env;
    int code = 0;
    System_Create(&mSystem);
    const char *path_cstr = mEnv->GetStringUTFChars(path_jstr, NULL);
    LOGI("saveAiSound-%s", path_cstr)
    const char *save_cstr;
    if (save_jstr != NULL) {
        save_cstr = mEnv->GetStringUTFChars(save_jstr, NULL);
        LOGI("saveAiSound-save_path=%s", save_cstr)
    }
    try {
        if (save_jstr != NULL) {
            char cDest[200];
            strcpy(cDest, save_cstr);
            mSystem->setSoftwareFormat(8000, FMOD_SPEAKERMODE_MONO, 0); //设置采样率为8000，channel为1
            mSystem->setOutput(FMOD_OUTPUTTYPE_WAVWRITER); //保存文件格式为WAV
            mSystem->init(32, FMOD_INIT_NORMAL, cDest);
            mSystem->recordStart(0, sound, true);
        }
        //创建声音
        mSystem->createSound(path_cstr, FMOD_DEFAULT, NULL, &sound);
        mSystem->playSound(sound, 0, false, &channel);
        LOGI("saveAiSound-%s", "save_start")
        switch (type) {
            case MODE_NORMAL:
                LOGI("saveAiSound-%s", "save MODE_NORMAL")
                break;
            case MODE_FUNNY:
                LOGI("saveAiSound-%s", "save MODE_FUNNY")
                mSystem->createDSPByType(FMOD_DSP_TYPE_NORMALIZE, &dsp);
                channel->getFrequency(&frequency);
                frequency = frequency * 1.6;
                channel->setFrequency(frequency);
                break;
            case MODE_UNCLE:
                LOGI("saveAiSound-%s", "save MODE_UNCLE")
                mSystem->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
                dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH, 0.8);
                channel->addDSP(0, dsp);
                break;
            case MODE_LOLITA:
                LOGI("saveAiSound-%s", "save MODE_LOLITA")
                mSystem->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
                dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH, 1.8);
                channel->addDSP(0, dsp);
                break;
            case MODE_ROBOT:
                LOGI("saveAiSound-%s", "save MODE_ROBOT")
                mSystem->createDSPByType(FMOD_DSP_TYPE_ECHO, &dsp);
                dsp->setParameterFloat(FMOD_DSP_ECHO_DELAY, 50);
                dsp->setParameterFloat(FMOD_DSP_ECHO_FEEDBACK, 60);
                channel->addDSP(0, dsp);
                break;
            case MODE_ETHEREAL:
                LOGI("saveAiSound-%s", "save MODE_ETHEREAL")
                mSystem->createDSPByType(FMOD_DSP_TYPE_ECHO, &dsp);
                dsp->setParameterFloat(FMOD_DSP_ECHO_DELAY, 300);
                dsp->setParameterFloat(FMOD_DSP_ECHO_FEEDBACK, 20);
                channel->addDSP(0, dsp);
                break;
            case MODE_CHORUS:
                LOGI("saveAiSound-%s", "save MODE_CHORUS")
                mSystem->createDSPByType(FMOD_DSP_TYPE_ECHO, &dsp);
                dsp->setParameterFloat(FMOD_DSP_ECHO_DELAY, 100);
                dsp->setParameterFloat(FMOD_DSP_ECHO_FEEDBACK, 50);
                channel->addDSP(0, dsp);
                break;
            case MODE_HORROR:
                LOGI("saveAiSound-%s", "save MODE_HORROR")
                mSystem->createDSPByType(FMOD_DSP_TYPE_TREMOLO, &dsp);
                dsp->setParameterFloat(FMOD_DSP_TREMOLO_SKEW, 0.8);
                channel->addDSP(0, dsp);
                break;
            default:
                break;
        }
        mSystem->update();
    } catch (...) {
        LOGE("saveAiSound-%s", "save error!")
        code = 1;
        goto end;
    }
    while (playing) {
        usleep(1000);
        channel->isPlaying(&playing);
    }
    LOGI("saveAiSound-%s", "save over!")
    goto end;
    end:
    if (path_jstr != NULL) {
        mEnv->ReleaseStringUTFChars(path_jstr, path_cstr);
    }
    if (save_jstr != NULL) {
        mEnv->ReleaseStringUTFChars(save_jstr, save_cstr);
    }
    sound->release();
    mSystem->close();
    mSystem->release();
    return code;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_demon_fmodsound_FmodSound_playSound(JNIEnv *env, jobject cls, jstring path_jstr, jint type) {
    Sound *sound;
    DSP *dsp;
    bool playing = true;
    float frequency = 0;
    System *mSystem;
    JNIEnv *mEnv = env;
    int code = 0;
    System_Create(&mSystem);
    const char *path_cstr = mEnv->GetStringUTFChars(path_jstr, NULL);
    LOGI("playAiSound-%s", path_cstr)
    try {
        mSystem->init(32, FMOD_INIT_NORMAL, NULL);
        //创建声音
        mSystem->createSound(path_cstr, FMOD_DEFAULT, NULL, &sound);
        mSystem->playSound(sound, 0, false, &channel);
        LOGI("playAiSound-%s", "play_start")
        switch (type) {
            case MODE_NORMAL:
                LOGI("playAiSound-%s", "play MODE_NORMAL")
                break;
            case MODE_FUNNY:
                LOGI("playAiSound-%s", "play MODE_FUNNY")
                mSystem->createDSPByType(FMOD_DSP_TYPE_NORMALIZE, &dsp);
                channel->getFrequency(&frequency);
                frequency = frequency * 1.6;
                channel->setFrequency(frequency);
                break;
            case MODE_UNCLE:
                LOGI("playAiSound-%s", "play MODE_UNCLE")
                mSystem->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
                dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH, 0.8);
                channel->addDSP(0, dsp);
                break;
            case MODE_LOLITA:
                LOGI("playAiSound-%s", "play MODE_LOLITA")
                mSystem->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
                dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH, 1.8);
                channel->addDSP(0, dsp);
                break;
            case MODE_ROBOT:
                LOGI("playAiSound-%s", "play MODE_ROBOT")
                mSystem->createDSPByType(FMOD_DSP_TYPE_ECHO, &dsp);
                dsp->setParameterFloat(FMOD_DSP_ECHO_DELAY, 50);
                dsp->setParameterFloat(FMOD_DSP_ECHO_FEEDBACK, 60);
                channel->addDSP(0, dsp);
                break;
            case MODE_ETHEREAL:
                LOGI("playAiSound-%s", "play MODE_ETHEREAL")
                mSystem->createDSPByType(FMOD_DSP_TYPE_ECHO, &dsp);
                dsp->setParameterFloat(FMOD_DSP_ECHO_DELAY, 300);
                dsp->setParameterFloat(FMOD_DSP_ECHO_FEEDBACK, 20);
                channel->addDSP(0, dsp);
                break;
            case MODE_CHORUS:
                LOGI("playAiSound-%s", "play MODE_CHORUS")
                mSystem->createDSPByType(FMOD_DSP_TYPE_ECHO, &dsp);
                dsp->setParameterFloat(FMOD_DSP_ECHO_DELAY, 100);
                dsp->setParameterFloat(FMOD_DSP_ECHO_FEEDBACK, 50);
                channel->addDSP(0, dsp);
                break;
            case MODE_HORROR:
                LOGI("playAiSound-%s", "play MODE_HORROR")
                mSystem->createDSPByType(FMOD_DSP_TYPE_TREMOLO, &dsp);
                dsp->setParameterFloat(FMOD_DSP_TREMOLO_SKEW, 0.8);
                channel->addDSP(0, dsp);
                break;
            default:
                break;
        }
        mSystem->update();
    } catch (...) {
        LOGE("playAiSound-%s", "play error!")
        code = 1;
        goto end;
    }
    while (playing) {
        usleep(1000);
        channel->isPlaying(&playing);
    }
    LOGI("playAiSound-%s", "play over！")
    goto end;
    end:
    if (path_jstr != NULL) {
        mEnv->ReleaseStringUTFChars(path_jstr, path_cstr);
    }
    sound->release();
    mSystem->close();
    mSystem->release();
    return code;
}


extern "C" JNIEXPORT void JNICALL
Java_com_demon_fmodsound_FmodSound_stopPlay(JNIEnv *env, jobject jcls) {
    LOGI("%s", "stopPlay")
    channel->stop();
}

extern "C" JNIEXPORT void JNICALL
Java_com_demon_fmodsound_FmodSound_resumePlay(JNIEnv *env, jobject jcls) {
    LOGI("%s", "resumePlay")
    channel->setPaused(false);

}

extern "C" JNIEXPORT void JNICALL
Java_com_demon_fmodsound_FmodSound_pausePlay(JNIEnv *env, jobject jcls) {
    LOGI("%s", "pausePlay")
    channel->setPaused(true);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_demon_fmodsound_FmodSound_isPlaying(JNIEnv *env, jobject jcls) {
    LOGI("%s", "isPlaying")
    bool isPlaying = true;
    return !channel->isPlaying(&isPlaying);

}


