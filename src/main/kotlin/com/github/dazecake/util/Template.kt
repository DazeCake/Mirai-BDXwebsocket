package com.github.dazecake.util

import net.mamoe.mirai.console.plugins.ConfigSection
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.QQ
import net.mamoe.mirai.contact.nameCardOrNick

object Template {

    private const val SENDER_NAME = "\${senderName}"

    lateinit var prefix: String
    lateinit var prefixMc: String
    lateinit var rebootCmd: String
    lateinit var connectMsg: String
    lateinit var dropMsg: String
    lateinit var closeMsg: String

    fun load(section: ConfigSection) {
        prefix = section.getString("prefix")
        prefixMc = section.getString("mc_prefix")
        rebootCmd = section.getString("reboot_cmd")
        connectMsg = section.getString("connect_msg")
        dropMsg = section.getString("drop_msg")
        closeMsg = section.getString("close_msg")
    }

    fun replaceCmdWithMember(cmd: String, sender: Member) = cmd.replace(SENDER_NAME, sender.nameCardOrNick)

    fun replaceCmdWithFriend(cmd: String, sender: QQ) = cmd.replace(SENDER_NAME, sender.nick)
}