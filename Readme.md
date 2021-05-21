### DeMon_Sound

[![](https://jitpack.io/v/iDeMonnnnnn/DeMon_Sound.svg)](https://jitpack.io/#iDeMonnnnnn/DeMon_Sound)

Android端基于[FMOD](https://www.fmod.com/)/[SoundTouch](https://gitlab.com/soundtouch/soundtouch)的简单音频变声解决方案。

1. FmodSound库提供了基于FMOD的7中变声方案。
2. SoundTouch库提供了音调&变声自由设置的变声方案。
3. SoundCoding库提供了```AMR<-->PCM<-->WAV```互转方法。

#### 开始使用

##### 添加依赖

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
[latest_version](https://github.com/iDeMonnnnnn/DeMon_Sound/releases)
```
dependencies {
      //使用FMOD变声，请添加此库
	  implementation 'com.github.iDeMonnnnnn.DeMon_Sound:FmodSound:$latest_version'
	  //使用SoundTouch变声，请添加此库
	  implementation 'com.github.iDeMonnnnnn.DeMon_Sound:SoundTouch:$latest_version'
	  //使用视频转码功能，请添加此库
      implementation 'com.github.iDeMonnnnnn.DeMon_Sound:SoundCoding:$latest_version'
}
```

##### FmodSound

1.7种音效类型

[FmodSound](https://github.com/iDeMonnnnnn/DeMon_Sound/blob/master/FmodSound/src/main/java/com/demon/fmodsound/FmodSound.kt)

```java
    //音效的类型
    const val MODE_NORMAL = 0 //正常

    const val MODE_FUNNY = 1 //搞笑

    const val MODE_UNCLE = 2 //大叔

    const val MODE_LOLITA = 3 //萝莉

    const val MODE_ROBOT = 4 //机器人

    const val MODE_ETHEREAL = 5 //空灵

    const val MODE_CHORUS = 6 //混合

    const val MODE_HORROR = 7 //恐怖
```

2.初始化FMOD

```java
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //初始化
        FMOD.init(this)
}

override fun onDestroy() {
        super.onDestroy()
        //释放
        FMOD.close()
}
```

3.变声播放

```java
   /**
     * 变声并播放，耗时，需要在子线程中执行
     *
     * @param path 音频路径，只支持WAV格式
     * @param type 变声音效类型，默认=0即普通播放无变声效果
     */
    external fun playSound(path: String, type: Int = MODE_NORMAL): Int
```

4.变声保存

```java
    /**
     * 变声保存，耗时，需要在子线程中执行
     *
     * @param path 音频路径，只支持WAV格式
     * @param type 变声音效类型，默认=0即普通播放无变声效果
     * @param savePath 变声后保存的路径，输出为WAV格式
     */
    external fun saveSound(path: String, type: Int, savePath: String): Int

    /**
     * 变声保存
     * @param path 音频路径，只支持WAV格式
     * @param type 变声音效类型，默认=0即普通播放无变声效果
     * @param savePath 变声后保存的路径，输出为WAV格式
     * @param listener 变声结果监听，根据回调可以在变声成功后播放
     */
    fun saveSoundAsync(path: String, type: Int, savePath: String, listener: ISaveSoundListener? = null) {
        try {
            if (isPlaying()) {
                stopPlay()
            }
            val result = saveSound(path, type, savePath)
            if (result == 0) {
                listener?.onFinish(path, savePath, type)
            } else {
                listener?.onError("error")
            }
        } catch (e: Exception) {
            listener?.onError(e.message)
        }
    }
```



##### 实现原理
[Android端音频变声库解决方案](https://demon.blog.csdn.net/article/details/113585385)





