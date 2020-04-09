package com.github.dazecake.util

import net.mamoe.mirai.console.plugins.ConfigSection

object Template {

    lateinit var prefix: String
    lateinit var rebootCmd: String
    lateinit var connectMsg: String
    lateinit var dropMsg: String
    lateinit var closeMsg: String

    fun load(section: ConfigSection) {
        prefix = section.getString("prefix")
        prefix = section.getString("reboot_cmd")
        prefix = section.getString("connect_msg")
        prefix = section.getString("drop_msg")
        prefix = section.getString("close_msg")
    }
}