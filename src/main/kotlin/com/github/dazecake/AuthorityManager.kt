package com.github.dazecake

import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import net.mamoe.mirai.console.plugins.ConfigSection

@KtorExperimentalAPI
@OptIn(UnstableDefault::class)
@ImplicitReflectionSerializer
object AuthorityManager {

    private const val DEFAULT_AUTHORITY = "default"

    private val authoritiesMap: MutableMap<Long, String> = mutableMapOf()
    private val authoritiesCmd: MutableMap<String, MutableList<String>> = mutableMapOf()

    fun load(section: ConfigSection) {
        authoritiesMap.clear()
        authoritiesCmd.clear()

        section.keys.forEach {
            val config = BDXWebSocketPlugin.loadConfig(section.getString(it))

            // 默认组不配置id
            if (it != DEFAULT_AUTHORITY) {
                config.getLongList("id").forEach { id ->
                    authoritiesMap[id] = it
                }
            }

            authoritiesCmd[it] = mutableListOf<String>().apply {
                addAll(config.getStringList("cmd"))
            }
        }
    }

    fun checkAuthority(id: Long, cmd: String): Boolean {
        val ls = authoritiesCmd[authoritiesMap[id] ?: DEFAULT_AUTHORITY] ?: return false

        ls.forEach {
            if (it == "*" || cmd.startsWith(it)) return true
        }

        return false
    }
}