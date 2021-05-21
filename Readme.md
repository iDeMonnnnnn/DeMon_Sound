### DeMon_Sound

[![](https://jitpack.io/v/iDeMonnnnnn/DeMon_Sound.svg)](https://jitpack.io/#iDeMonnnnnn/DeMon_Sound)

Android端基于[FMOD](https://www.fmod.com/)/[SoundTouch](https://gitlab.com/soundtouch/soundtouch)的音频变声库.

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


##### 实现原理
[Android端音频变声库解决方案](https://demon.blog.csdn.net/article/details/113585385)





