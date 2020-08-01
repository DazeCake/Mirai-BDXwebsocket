package com.github.dazecake.bot

import com.github.dazecake.BDXWebSocketPlugin
import com.github.dazecake.data.*
import com.github.dazecake.util.Template
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import net.mamoe.mirai.Bot

@KtorExperimentalAPI
@OptIn(UnstableDefault::class)
@ImplicitReflectionSerializer
object BotClient {

    suspend fun onReceive(pkg: Incoming) {
        when (pkg) {
            is MemberMessage -> onMemberMessage(pkg)
            is MemberJoin -> onMemberJoin(pkg)
            is MemberLeave -> onMemberLeave(pkg)
            is MemberCmd -> onMemberCmd(pkg)
            is CmdResp -> onCmdResp(pkg)
            is ServerCrash -> onServerCrash(pkg)
        }
    }

    internal suspend fun notifyConnect() {
        pushMessage(Template.connectMsg)
    }

    internal suspend fun notifyDrop() {
        pushMessage(Template.dropMsg)
        BDXWebSocketPlugin.launchWebsocket()
    }

    internal suspend fun notifyClose() {
        pushMessage(Template.closeMsg)
    }

    private suspend fun onMemberMessage(pkg: MemberMessage) {
        // filter mc chat msg
        if (pkg.text.startsWith(Template.prefixMc)) {
            pushMessage(Template.BDXTemplate.onMsg(pkg.target, pkg.text.removePrefix(Template.prefix)))
        }
    }

    private suspend fun onMemberJoin(pkg: MemberJoin) {
        pushMessage(Template.BDXTemplate.onJoin(pkg.target))
    }

    private suspend fun onMemberLeave(pkg: MemberLeave) {
        pushMessage(Template.BDXTemplate.onLeave(pkg.target))
    }

    private suspend fun onMemberCmd(pkg: MemberCmd) {
//        pushMessage("${pkg.target}执行了命令: ${pkg.CMD}")
    }

    private suspend fun onCmdResp(pkg: CmdResp) {
        pushMessage(pkg.text)
    }

    private suspend fun onServerCrash(pkg: ServerCrash) {
        pushMessage(pkg.reason)
    }


    private suspend fun pushMessage(msg: String) {
        if(msg.isEmpty()) return

        val bdx = BDXWebSocketPlugin
        bdx.bots.forEach {
            getBotOrNull(it)?.apply {
                pushFriendMessage(msg, bdx.pushFriend)
                pushGroupMessage(msg, bdx.pushGroup)
            }
        }
    }

    private suspend fun Bot.pushFriendMessage(msg: String, friends: List<Long>) {
        friends.forEach {
            try {
                this.getFriend(it).sendMessage(msg)
            } catch (e: NoSuchElementException) {
                BDXWebSocketPlugin.logger.info("Bot($id) 没有好友 $it")
            }
        }
    }

    private suspend fun Bot.pushGroupMessage(msg: String, groups: List<Long>) {
        groups.forEach {
            try {
                this.getGroup(it).sendMessage(msg)
            } catch (e: NoSuchElementException) {
                BDXWebSocketPlugin.logger.info("Bot($id) 没有群 $it")
            }
        }
    }

    private fun getBotOrNull(id: Long): Bot? = Bot.getInstanceOrNull(id)
}