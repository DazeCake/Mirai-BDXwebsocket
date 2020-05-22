package com.github.dazecake.util

import net.mamoe.mirai.console.plugins.ConfigSection
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.contact.nameCardOrNick

object Template {

    private const val SENDER_NAME = "\${senderName}"

    lateinit var prefix: String
    lateinit var prefixMc: String
    lateinit var rebootCmd: String
    lateinit var connectMsg: String
    lateinit var dropMsg: String
    lateinit var closeMsg: String

    //BDX template
    object BDXTemplate {

        private const val TARGET = "\${target}"
        private const val MSG = "\${msg}"

        lateinit var onMsg: String
        lateinit var onJoin: String
        lateinit var onLeave: String

        fun onMsg(target: String, msg: String) = onMsg.replace(TARGET, target).replace(MSG, msg)
        fun onJoin(target: String) = onJoin.replace(TARGET, target)
        fun onLeave(target: String) = onLeave.replace(TARGET, target)
    }

    fun load(section: ConfigSection) {
        prefix = section.getString("prefix")
        prefixMc = section.getString("mc_prefix")
        rebootCmd = section.getString("reboot_cmd")
        connectMsg = section.getString("connect_msg")
        dropMsg = section.getString("drop_msg")
        closeMsg = section.getString("close_msg")

        BDXTemplate.onMsg = section.getString("on_msg_template")
        BDXTemplate.onJoin = section.getString("on_join_template")
        BDXTemplate.onLeave = section.getString("on_leave_template")
    }

    fun replaceCmdWithMember(cmd: String, sender: Member) = cmd.replace(SENDER_NAME, sender.nameCardOrNick)

    fun replaceCmdWithFriend(cmd: String, sender: User) = cmd.replace(SENDER_NAME, sender.nick)
}