# autoPackage
一键自动化打release包，并实现加固且生成渠道包（360加固+walle多渠道打包）

## 介绍
该工具使用 360加固宝进行加固 ，walle进行多渠道打包，并使用gradle plugin进行封装成自动化脚本，实现 一键 完成 release + 加固 + 渠道包 全流程 

## 接入
1. 将demo中的 tools文件夹下载下来放到自己 app module下（和src同级）如图
![如图](https://github.com/NiLuogege/autoPackage/blob/master/Demo/screenshots/1.jpg)

2. 根目录下的build.gradle 中添加
```
classpath 'com.niluogege.auto-package:plugin:1.0.0'
```

3. gradle.properties 中间行 必要参数的配置（具体含义看截图或者demo）

4. 执行 autoPackage 就可以 一键  release + 加固 + 渠道包 了



## License
```
Copyright 2016 Zheng Zibin

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
