package com.github.dazecake

import net.mamoe.mirai.console.plugins.Config

object CmdMap : HashMap<String, String>() {

    fun load(section: Config) {
        clear()

        section.asMap().forEach{
            put(it.key, it.value as String)
        }
    }
}