package io.github.samarium150.mirai.plugin.echo.data

import io.github.samarium150.mirai.plugin.echo.config.PluginConfig

val threshold = PluginConfig.threshold

val filter = PluginConfig.filter

data class Record(
    var user: Long,
    var contents: String,
    var counter: Int = 1
) {
    fun determine(user: Long, contents: String): Boolean {
        if (this.contents == contents && this.user != user)
            counter++
        else if (this.contents != contents)
            counter = 1
        this.contents = contents
        this.user = user
        return threshold in 1..counter && (filter.isEmpty() || contents in filter)
    }
}
