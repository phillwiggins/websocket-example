package com.purewowstudio.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

internal class ConnectionManagerImpl<T>(
    private val client: OkHttpClient,
    private var dispatcher: CoroutineDispatcher,
    private val url: String
) : ConnectionManager<T> {

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    private var webSocket: WebSocket? = null
    private val eventStream = MutableStateFlow<SocketEvent<T>>(SocketEvent.Loading)

    override suspend fun start(): StateFlow<SocketEvent<T>> = withContext(dispatcher) {
        webSocket = client.newWebSocket(
            Request.Builder().url(url).build(),
            ConnectionListener<T> { eventStream.value = it }
        )
        return@withContext eventStream.asStateFlow()
    }

    override suspend fun subscribe(subscibe: Subscribe) {
        val adapter = moshi.adapter(Subscribe::class.java)
        webSocket?.send(adapter.toJson(subscibe))
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

    override suspend fun subscribe(clazz: Class<T>, subscibe: Subscribe) {
        TODO("Not yet implemented")
    }
}
