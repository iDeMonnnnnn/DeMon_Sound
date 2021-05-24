### DeMon_Sound

[![](https://jitpack.io/v/iDeMonnnnnn/DeMon_Sound.svg)](https://jitpack.io/#iDeMonnnnnn/DeMon_Sound)

Android端基于[FMOD](https://www.fmod.com/)/[SoundTouch](https://gitlab.com/soundtouch/soundtouch)的简单音频变声解决方案。

1. FmodSound库提供了基于FMOD的7种变声方案。
2. SoundTouch库提供了音调&变速设置的变声方案。
3. SoundCoding库提供了```AMR<-->PCM<-->WAV```互转方案。

#### 开始使用

##### 使用文档

[WIKI](https://github.com/iDeMonnnnnn/DeMon_Sound/wiki)

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

##### 示例代码

[示例App ](https://raw.githubusercontent.com/iDeMonnnnnn/DeMon_Sound/master/app-release.apk)

[示例代码](https://github.com/iDeMonnnnnn/DeMon_Sound/tree/master/app)

##### 截图

![](https://github.com/iDeMonnnnnn/DeMon_Sound/blob/master/screen/111.jpg?raw=true)
![](https://github.com/iDeMonnnnnn/DeMon_Sound/blob/master/screen/222.jpg?raw=true)
![](https://github.com/iDeMonnnnnn/DeMon_Sound/blob/master/screen/333.jpg?raw=true)

### MIT License

```
Copyright (c) 2021 DeMon

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```




