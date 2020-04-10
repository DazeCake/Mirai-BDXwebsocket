package com.github.dazecake

import com.github.dazecake.bot.BotClient
import com.github.dazecake.data.ServerInfo
import com.github.dazecake.util.Template
import com.github.dazecake.websocket.WebsocketClient
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import net.mamoe.mirai.console.command.registerCommand
import net.mamoe.mirai.console.plugins.PluginBase
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.GroupMessage

@UnstableDefault
@KtorExperimentalAPI
@ImplicitReflectionSerializer
object BDXWebSocketPlugin : PluginBase() {

    internal val bots: MutableList<Long> = mutableListOf()
    internal val pushGroup: MutableList<Long> = mutableListOf()
    internal val pushFriend: MutableList<Long> = mutableListOf()
    private val serverInfo: ServerInfo

    private val setting = loadConfig("base.yml")
    private lateinit var manager: AuthorityManager
    private lateinit var websocket: WebsocketClient

    init {
        bots.addAll(setting.getLongList("bots"))

        setting.getConfigSection("server").apply {
            serverInfo = ServerInfo(
                host = getString("host"),
                port = getInt("port"),
                path = getString("path"),
                basePwd = getString("password"),
                retryTime = getInt("retry_time")
            )
        }

        val push = setting.getConfigSection("push")
        pushGroup.addAll(push.getLongList("group"))
        pushFriend.addAll(push.getLongList("friend"))

        Template.load(setting.getConfigSection("template"))

        loadConf()
    }

    override fun onLoad() {
        websocket = WebsocketClient(serverInfo)

        registerCommand {
            name = "BDX"
            onCommand {
                if (it[0] == "reload") {
                    loadConf()
                    true
                } else {
                    false
                }
            }
        }
    }

    override fun onEnable() {

        runBlocking {
            launchWebsocket()
        }

        subscribeMessages {
            startsWith(Template.prefix, trim = true) {

                val cmd = message.contentToString()

                if (manager.checkAuthority(sender.id, cmd)) {

                    var realCmd: String? = null
                    for (key in CmdMap.keys) {
                        if (cmd.startsWith(key)) {
                            realCmd = cmd.replaceFirst(key, CmdMap[key]!!)
                            break
                        }
                    }

                    if (realCmd == null) realCmd = cmd

                    realCmd = Template.replaceCmd(realCmd, when(this) {
                        is GroupMessage -> sender // as Member
                        else -> sender            // as QQ
                    })

                    websocket.sendCmd(realCmd)
                }
            }

            case(Template.rebootCmd) {
                launchWebsocket()
            }
        }
    }

    internal suspend fun launchWebsocket() {
        if (websocket.life > 0) {
            launch {
                websocket.life--
                websocket.connect()
            }
        } else {
            BotClient.notifyClose()
        }
    }

    private fun loadConf() {

        AuthorityManager.load(setting.getConfigSection("authorities"))

        val plugins = setting.getConfigSection("plugins")
        CmdMap.load(loadConfig(plugins.getString("cmd_map")))
    }
}