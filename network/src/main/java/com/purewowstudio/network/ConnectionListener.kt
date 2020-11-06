package com.purewowstudio.network

import com.purewowstudio.network.SocketEvent.Closed
import com.purewowstudio.network.SocketEvent.Closing
import com.purewowstudio.network.SocketEvent.Error
import com.purewowstudio.network.SocketEvent.Message
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

internal class ConnectionListener<T>(
    private val eventListener: (SocketEvent<T>) -> Unit
) : WebSocketListener() {
    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        postEvent(Closed(code, reason))
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        postEvent(Closing(code, reason))
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        postEvent(Error(t))
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        postEvent(Message(text))
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        postEvent(Message(bytes.toString()))
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        postEvent(Message(response.toString()))
    }

    private fun postEvent(event: SocketEvent<T>) {
        eventListener(event)
    }
}