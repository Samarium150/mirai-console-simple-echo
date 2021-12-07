package com.github.samarium150

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object MiraiConsoleSimpleEchoConfig : AutoSavePluginConfig("config") {

    @ValueDescription("复读阈值，默认为2，同一条消息超过该次数才复读，设置为0则不复读")
    val threshold: Int by value(2)

    @ValueDescription("复读过滤器，只复读其中的消息，默认为空（不过滤）")
    val filter: List<String> by value()

}
