package com.purewowstudio.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

internal class ConnectionManagerImpl<T>(
    private val client: OkHttpClient,
    private var dispatcher: CoroutineDispatcher,
    private val url: String
) : ConnectionManager<T> {

    private var webSocket: WebSocket? = null
    private val eventStream = MutableStateFlow<SocketEvent<T>>(SocketEvent.Loading)

    override suspend fun start(): StateFlow<SocketEvent<T>> = withContext(dispatcher) {
        webSocket = client.newWebSocket(
            Request.Builder().url(url).build(),
            ConnectionListener<T> { eventStream.value = it }
        )
        return@withContext eventStream.asStateFlow()
    }

    override suspend fun <S> subscribe(subscription: SubscribeRQ<S>) {
        val sub = Json.encodeToString(subscription)
        webSocket?.send(sub)
    }

    override suspend fun send(text: String) {
        webSocket?.send(text)
    }

    override suspend fun close(
        code: Int?,
        reason: String?
    ): Unit = withContext(dispatcher) {
        webSocket?.close(code ?: CLOSE_NORMAL, reason)
    }

    companion object {
        const val CLOSE_NORMAL = 1000

        /* indicates that an endpoint is "going away", such as a server
        going down or a browser having navigated away from a page. */
        const val CLOSE_EXIT = 1001

        /* indicates that an endpoint is terminating the connection due
        to a protocol error. */
        const val CLOSE_PROTOCOL_ERROR = 1002

        /* indicates that an endpoint is terminating the connection
        because it has received a type of data it cannot accept (e.g., an
        endpoint that understands only text data MAY send this if it
        receives a binary message). */
        const val CLOSE_DATA_ERROR = 1003
    }

}
