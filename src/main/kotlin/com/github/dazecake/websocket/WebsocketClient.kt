package com.github.dazecake.websocket

import com.github.dazecake.BDXWebSocketPlugin
import com.github.dazecake.bot.BotClient
import com.github.dazecake.data.Incoming
import com.github.dazecake.data.Outgoing
import com.github.dazecake.data.RunCmd
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
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.stringify
import java.net.ConnectException

@KtorExperimentalAPI
@UnstableDefault
@ImplicitReflectionSerializer
class WebsocketClient(private val serverInfo: ServerInfo) {

    private val client = HttpClient {
        install(WebSockets)
    }

    private lateinit var outgoing: SendChannel<Frame>
    private var session: DefaultClientWebSocketSession? = null
    internal var life = serverInfo.retryTime

    suspend fun connect() {

        BDXWebSocketPlugin.launch {
            try {
                client.ws(
                    method = HttpMethod.Post,
                    host = serverInfo.host,
                    port = serverInfo.port,
                    path = serverInfo.path
                ) {
                    process(this)
                }

            } catch (e: ClosedReceiveChannelException) {
                BotClient.notifyDrop()
            } catch (e: ConnectException) {
                BotClient.notifyDrop()
            } catch (e: Exception) {
                BotClient.notifyClose()
                e.printStackTrace()
            }
        }
    }

    private suspend fun process(session: DefaultClientWebSocketSession) {

        try {

            // 结束上一个session
            this.session?.close()

            this.session = session
            outgoing = session.outgoing

            BotClient.notifyConnect()
            life = serverInfo.retryTime // 重置尝试次数

            while (true) {
                when (val frame = session.incoming.receive()) {
                    is Frame.Text -> {
                        BotClient.onReceive(BDXJson.json.parse(Incoming.serializer(), frame.readText()))
                    }
                }
            }

        } catch (cancel: CancellationException) {

            // reboot
            BDXWebSocketPlugin.logger.info("BDX reboot")
        }
    }

    suspend fun sendCmd(cmd: String) {

        outgoing.send(
            Frame.Text(
                BDXJson.json.stringify(
                    RunCmd(passwd = "", cmd = cmd).apply {
                        passwd = KeyGenerator(serverInfo.basePwd, BDXJson.json.stringify(this))
                    }
                )
            )
        )

        BDXWebSocketPlugin.logger.info(
            "Sent BDX command -> $cmd"
        )
    }
}