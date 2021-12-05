# mirai-console-simple-echo
[![GitHub top language](https://img.shields.io/github/languages/top/Samarium150/mirai-console-simple-echo?style=flat)](https://kotlinlang.org/)
[![Gradle CI](https://github.com/Samarium150/mirai-console-simple-echo/actions/workflows/Gradle%20CI.yml/badge.svg)](https://github.com/Samarium150/mirai-console-simple-echo/actions/workflows/Gradle%20CI.yml)
[![GitHub](https://img.shields.io/github/license/Samarium150/mirai-console-simple-echo?style=flat)](https://github.com/Samarium150/mirai-console-simple-echo/blob/master/LICENSE)

简单复读插件，当某条消息重复次数超过阈值时，将自动复读一次该消息。

**不同的用户发送同一条消息才会计算重复次数**

## 使用方法

1. 从`Releases`中下载`jar`并放入`plugins`目录下。
2. 在`config.yml`中配置阈值和过滤器。<br>
   (1). 阈值默认为2，超过才会复读。<br>
   (2). 过滤器默认为空，即任何消息都可能会被复读。
