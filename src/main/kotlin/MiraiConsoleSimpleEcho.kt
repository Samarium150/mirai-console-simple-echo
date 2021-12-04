package com.github.samarium150

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

object MiraiConsoleSimpleEcho: KotlinPlugin(
    JvmPluginDescription(
        id = "com.github.samarium150.mirai-console-simple-echo",
        name = "MiraiConsoleSimpleEcho",
        version = "1.0.0",
    ) {
        author("Samarium")
        info("简单复读插件")
    }
) {

    override fun onEnable() {
        logger.info { "Plugin mirai-console-simple-echo loaded" }
    }

    override fun onDisable() {
        logger.info { "Plugin mirai-console-simple-echo unloaded" }
    }
}
