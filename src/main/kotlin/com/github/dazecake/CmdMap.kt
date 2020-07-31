package com.github.dazecake

import net.mamoe.mirai.console.plugins.Config
import net.mamoe.mirai.console.plugins.ToBeRemoved

object CmdMap : HashMap<String, String>() {

    @ToBeRemoved
    fun load(section: Config) {
        clear()

        section.asMap().forEach{
            put(it.key, it.value as String)
        }
    }
}