# **本项目停止维护，有缘再见**

# Mirai-BDXwebsocket

通过websocket链接Bedrockx实现qq联动远控 支持全平台运行

### 原理

根据[MCYneos](https://github.com/MCYneos)提供的BDX端[websocket](https://github.com/WangYneos/BDXWebSocket)插件API使得[Mirai](https://github.com/mamoe/mirai)连接到服务器，并实现在qq群内进行服务器指令操作

### 什么是Mirai

----

[Mirai](https://github.com/mamoe/mirai) 是一个在全平台下运行，提供 QQ Android 和 TIM PC 协议支持的高效率机器人框架

这个项目的名字来源于
     <p><a href = "http://www.kyotoanimation.co.jp/">京都动画</a>作品<a href = "https://zh.moegirl.org/zh-hans/%E5%A2%83%E7%95%8C%E7%9A%84%E5%BD%BC%E6%96%B9">《境界的彼方》</a>的<a href = "https://zh.moegirl.org/zh-hans/%E6%A0%97%E5%B1%B1%E6%9C%AA%E6%9D%A5">栗山未来(Kuriyama <b>Mirai</b>)</a></p>
     <p><a href = "https://www.crypton.co.jp/">CRYPTON</a>以<a href = "https://www.crypton.co.jp/miku_eng">初音未来</a>为代表的创作与活动<a href = "https://magicalmirai.com/2019/index_en.html">(Magical <b>Mirai</b>)</a></p>
**QQ Android** 协议支持库与高效率的机器人框架   
纯 Kotlin 实现协议和支持框架    
mirai 既可以作为项目中的 QQ 协议支持库, 也可以作为单独的应用程序与插件承载 QQ 机器人服务。  

### 如何安装

首先我们需要安装mirai来运行本插件 使用[mirai-console](https://github.com/mamoe/mirai-console)可以快速安装mirai

#### Linux用户

方案1：使用[一键脚本](https://github.com/cyanray/mirai-linux-deployment)进行安装

方案2：手动安装openjdk-11-jdk


[下载(download)](https://github.com/mamoe/mirai-console/releases)
请下载最新的 mirai-console-wrapper-x.x.x-all.jar

运行指令：
```
java -jar mirai-console-wrapper-x.x.x-all.jar

```
请自行修改相应版号到命令中

#### Windows用户


请使用[一键安装包](https://suihou-my.sharepoint.com/:f:/g/personal/user18_5tb_site/ErWGr97FpPVDjkboIDmDAJkBID-23ZMNbTPggGajf1zvGw?e=51NZWM)


---
#### 初次运行

初次运行后，将会提示**选择版本** 请选择
```
Pure
```

依照引导进行登录

此时所在目录下将会生成相关文件
按下 **ctrl+c** 来结束运行

下载 **Action** 内最新的包 解压将会得到一个jar文件

将jar上传到 **plugins** 文件夹内 再次运行mirai


此时相关配置已经生成 关闭mirai即可开始修改配置文件

### 一些疑问

---

Q：似乎coolq上已近有人实现了这个功能，这个插件有什么存在的必要？

A：由于coolq的限制，只能在win平台运行，这给很多linux用户造成了困扰

---
Q：相比于同类插件，本插件有何优势？

A：本插件引入用户组的概念，可以有效管理服主、op、玩家可以操作的命令，所有操作的命令均需注册后再使用，支持中文封装，**配置文件简洁易懂**等等 
