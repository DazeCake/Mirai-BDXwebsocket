package com.github.dazecake.util

import net.mamoe.mirai.console.plugins.ConfigSection
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.QQ
import net.mamoe.mirai.contact.nameCardOrNick

object Template {

    const val SENDER_NAME = "\${senderName}"

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

    fun replaceCmd(cmd: String, sender: Member) = cmd.replace(SENDER_NAME, sender.nameCardOrNick)

    fun replaceCmd(cmd: String, sender: QQ) = cmd.replace(SENDER_NAME, sender.nick)
}