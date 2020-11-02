package com.purewowstudio.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class ConnectionManager {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    private fun start() {
        val request: Request = Request.Builder().url(URL).build()
        val listener = ConnectionListener()
        webSocket = client.newWebSocket(request, listener)
        client.dispatcher.executorService.shutdown()
    }

    companion object {
        private const val PUBLIC_KEY = "GKY7SHF80BVZCL95K5SC"
        private const val URL = "wss://stream.cryptowat.ch/connect?apikey=$PUBLIC_KEY"
    }
}