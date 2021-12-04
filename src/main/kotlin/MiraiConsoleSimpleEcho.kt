package com.github.samarium150

import com.github.samarium150.MiraiConsoleSimpleEchoConfig.filter
import com.github.samarium150.MiraiConsoleSimpleEchoConfig.threshold
import kotlinx.coroutines.CoroutineExceptionHandler
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.commandPrefix
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.contentsSequence
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
    private var counter = 1
    private var prev = ""
    private var prevSender: Long? = null
    private var echoed = false
    private lateinit var listener: Listener<MessageEvent>

    override fun onEnable() {
        MiraiConsoleSimpleEchoConfig.reload()
        listener = globalEventChannel().subscribeAlways(
            MessageEvent::class,
            CoroutineExceptionHandler { _, throwable ->
                logger.error(throwable)
            },
            priority = EventPriority.MONITOR
        ) call@{
            val sender = kotlin.runCatching {
                this.toCommandSender()
            }.getOrNull() ?: return@call
            val contents = message.contentsSequence().joinToString("")
            if (contents.startsWith(commandPrefix)) return@call
            if (contents == prev && sender.user?.id != prevSender) {
                counter++
            } else {
                echoed = false
            }
            prev = contents
            prevSender = sender.user?.id
            if (threshold in 1..counter && !echoed &&
                (filter.isEmpty() || (filter.isNotEmpty() && contents in filter))) {
                sender.sendMessage(message)
                logger.info("已复读消息：${contents}")
                counter = 1
                echoed = true
            }
        }
        logger.info { "Plugin mirai-console-simple-echo loaded" }
    }

    override fun onDisable() {
        listener.cancel()
        logger.info { "Plugin mirai-console-simple-echo unloaded" }
    }
}
