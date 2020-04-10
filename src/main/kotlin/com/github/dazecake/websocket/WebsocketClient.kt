package com.github.dazecake.websocket

import com.github.dazecake.CmdMap
import com.github.dazecake.bot.BotClient
import com.github.dazecake.data.Incoming
import com.github.dazecake.data.Outgoing
import com.github.dazecake.data.ServerInfo
import com.github.dazecake.util.BDXJson
import com.github.dazecake.util.KeyGenerator
import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.channels.SendChannel
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.stringify

@KtorExperimentalAPI
@UnstableDefault
@ImplicitReflectionSerializer
class WebsocketClient(val serverInfo: ServerInfo) {

    private val client = HttpClient {
        install(WebSockets)
    }

    private lateinit var outgoing: SendChannel<Frame>
    internal var life = serverInfo.retryTime


    suspend fun connect() {
        try {
            client.ws(
                method = HttpMethod.Post,
                host = serverInfo.host,
                port = serverInfo.port,
                path = serverInfo.path
            ) {

                this@WebsocketClient.outgoing = outgoing

                BotClient.notifyConnect()
                life = serverInfo.retryTime // 重置尝试次数

                while (true) {
                    when (val frame = incoming.receive()) {
                        is Frame.Text -> {
                            BotClient.onReceive(BDXJson.json.parse(Incoming.serializer(), frame.readText()))
                        }
                    }
                }


            }
        } catch (e: Exception) {
            e.printStackTrace()
            BotClient.notifyDrop()
        }
    }

    suspend fun sendCmd(cmd: String) {

        outgoing.send(
            Frame.Text(
                BDXJson.json.stringify(Outgoing(
                    passwd = KeyGenerator(serverInfo.basePwd),
                    cmd = cmd
                ))
            )
        )
    }
}