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

[App示例代码](https://github.com/iDeMonnnnnn/DeMon_Sound/tree/master/app)

[Apk体验](https://raw.githubusercontent.com/iDeMonnnnnn/DeMon_Sound/master/app-release.apk)

##### 截图

![](https://github.com/iDeMonnnnnn/DeMon_Sound/blob/master/screen/111.jpg?raw=true)
![](https://github.com/iDeMonnnnnn/DeMon_Sound/blob/master/screen/222.jpg?raw=true)
![](https://github.com/iDeMonnnnnn/DeMon_Sound/blob/master/screen/333.jpg?raw=true)

### License

```
   Copyright [2022] [DeMon]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

```




