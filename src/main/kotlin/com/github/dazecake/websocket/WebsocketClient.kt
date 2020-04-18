package com.github.dazecake.websocket

import com.github.dazecake.BDXWebSocketPlugin
import com.github.dazecake.bot.BotClient
import com.github.dazecake.data.Incoming
import com.github.dazecake.data.Outgoing
import com.github.dazecake.data.ServerInfo
import com.github.dazecake.util.BDXJson
import com.github.dazecake.util.KeyGenerator
import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.DefaultClientWebSocketSession
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.stringify

@KtorExperimentalAPI
@UnstableDefault
@ImplicitReflectionSerializer
class WebsocketClient(private val serverInfo: ServerInfo) {

    private val client = HttpClient {
        install(WebSockets)
    }

    private lateinit var outgoing: SendChannel<Frame>
    private lateinit var session: DefaultClientWebSocketSession
    internal var life = serverInfo.retryTime

    suspend fun connect() {

        BDXWebSocketPlugin.launch {
            client.ws(
                method = HttpMethod.Post,
                host = serverInfo.host,
                port = serverInfo.port,
                path = serverInfo.path
            ) {
                process(this)
            }
        }
    }

    private suspend fun process(session: DefaultClientWebSocketSession) {

        try {

            // 结束上一个session
            this.session.close()

            this.session = session
            outgoing = session.outgoing

            BotClient.notifyConnect()
            life = serverInfo.retryTime // 重置尝试次数

            for (message in session.incoming) {
                when (message) {
                    is Frame.Text -> {
                        BotClient.onReceive(BDXJson.json.parse(Incoming.serializer(), message.readText()))
                    }
                }
            }
        } catch (cancel: CancellationException) {

            // reboot
            BDXWebSocketPlugin.logger.info("BDX reboot")

        } catch (e: Exception) {
            e.printStackTrace()
            BotClient.notifyDrop()
        }
    }

    suspend fun sendCmd(cmd: String) {

        outgoing.send(
            Frame.Text(
                BDXJson.json.stringify(
                    Outgoing(
                        passwd = KeyGenerator(serverInfo.basePwd),
                        cmd = cmd
                    )
                )
            )
        )

        BDXWebSocketPlugin.logger.info(
            "Sent BDX command -> $cmd"
        )
    }
}