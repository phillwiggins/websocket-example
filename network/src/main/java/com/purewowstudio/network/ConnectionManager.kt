package com.purewowstudio.network

import com.purewowstudio.network.ConnectionManagerImpl.Companion.CLOSE_NORMAL
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import okhttp3.OkHttpClient

interface ConnectionManager<T> {

    suspend fun start(): StateFlow<SocketEvent<T>>
    suspend fun close(code: Int? = CLOSE_NORMAL, reason: String? = "")

    data class Builder<T>(
            private var client: OkHttpClient = OkHttpClient(),
            private var dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) {
        fun client(client: OkHttpClient) = apply { this.client = client }

        fun dispatcher(dispatcher: CoroutineDispatcher) = apply { this.dispatcher = dispatcher }

        fun build(url: String): ConnectionManager<T> =
                ConnectionManagerImpl<T>(
                        client = client,
                        dispatcher = dispatcher,
                        url = url
                )
    }

    suspend fun send(text: String)
    suspend fun subscribe(clazz: Class<T>, subscibe: Subscribe)
    suspend fun subscribe(subscibe: Subscribe)
}