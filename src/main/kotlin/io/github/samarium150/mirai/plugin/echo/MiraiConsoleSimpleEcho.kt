package io.github.samarium150.mirai.plugin.echo

import io.github.samarium150.mirai.plugin.echo.config.PluginConfig
import io.github.samarium150.mirai.plugin.echo.data.Record
import kotlinx.coroutines.CoroutineExceptionHandler
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.contentsSequence

object MiraiConsoleSimpleEcho : KotlinPlugin(
    JvmPluginDescription(
        id = "io.github.samarium150.mirai.plugin.mirai-console-simple-echo",
        name = "Simple Echo",
        version = "1.1.4",
    ) {
        author("Samarium")
        info("简单复读插件")
    }
) {
    private lateinit var listener: Listener<MessageEvent>
    private val recordMap: MutableMap<Long, Record> = mutableMapOf()

    override fun onEnable() {

        PluginConfig.reload()

        listener = GlobalEventChannel.parentScope(this).subscribeAlways(
            MessageEvent::class,
            CoroutineExceptionHandler { _, throwable ->
                logger.error(throwable)
            },
            priority = EventPriority.MONITOR
        ) call@{
            val sender = kotlin.runCatching {
                this.toCommandSender()
            }.getOrNull() ?: return@call
            val subject = sender.subject ?: return@call
            val contents = message.contentsSequence().joinToString("")
            if (contents.startsWith(CommandManager.INSTANCE.commandPrefix)) return@call
            CommandManager.INSTANCE.allRegisteredCommands.forEach {
                if (contents.startsWith(it.primaryName)) return@call
                for (name in it.secondaryNames) if (contents.startsWith(name)) return@call
            }
            val user = sender.user!!.id
            val record = recordMap[subject.id]
            if (record != null) {
                if (!record.determine(user, contents)) return@call
                recordMap.remove(subject.id)
                sender.sendMessage(message)
                logger.info("已复读消息：${contents}")
            } else
                recordMap[subject.id] = Record(user, contents)
        }

        logger.info("Plugin loaded")
    }

    override fun onDisable() {
        listener.cancel()
        logger.info("Plugin unloaded")
    }
}
