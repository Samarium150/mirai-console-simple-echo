package io.github.samarium150.mirai.plugin

import io.github.samarium150.mirai.plugin.config.PluginConfig
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

object MiraiConsoleSimpleEcho : KotlinPlugin(
    JvmPluginDescription(
        id = "io.github.samarium150.mirai.plugin.mirai-console-simple-echo",
        name = "Simple Echo",
        version = "1.1.1",
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

        PluginConfig.reload()
        val threshold = PluginConfig.threshold
        val filter = PluginConfig.filter

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
            } else if (contents != prev) {
                counter = 1
                echoed = false
            }
            prev = contents
            prevSender = sender.user?.id
            if (threshold in 1..counter && !echoed &&
                (filter.isEmpty() || contents in filter)) {
                sender.sendMessage(message)
                logger.info("已复读消息：${contents}")
                counter = 1
                echoed = true
            }
        }

        logger.info { "Plugin loaded" }
    }

    override fun onDisable() {
        listener.cancel()
        logger.info { "Plugin unloaded" }
    }
}
