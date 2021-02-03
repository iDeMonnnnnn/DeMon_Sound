### ChangeVoice

Android端音频变声库FMOD&SoundTouch使用Demo.

[Android端音频变声库解决方案]（https://demon.blog.csdn.net/article/details/113585385）


#### 注意
运行Demo请在作用域```Environment.DIRECTORY_DOCUMENTS```下添加命名为aaa.wav的音频文件。

```js
val dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath
val path = "$dir/aaa.wav"
```

#### 参考文档

##### FMOD
<http://blackchy.com/2018/12/10/2018-12-10-Fmod-Voice-Change/>

<https://www.jianshu.com/p/2e1fd3035ae1>

##### SoundTouch

<http://www.surina.net/soundtouch/README-SoundTouch-Android.html>

<https://gitlab.com/soundtouch/soundtouch>

