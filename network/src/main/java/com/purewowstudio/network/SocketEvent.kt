package com.purewowstudio.network

import com.purewowstudio.network.ConnectionManagerImpl.Companion.CLOSE_NORMAL

sealed class SocketEvent<out T> {
    object Loading : SocketEvent<Nothing>()
    data class Closed(val code: Int? = CLOSE_NORMAL, val reason: String?) : SocketEvent<Nothing>()
    data class Closing(val code: Int? = CLOSE_NORMAL, val reason: String?) : SocketEvent<Nothing>()
    data class Error(val throwable: Throwable) : SocketEvent<Nothing>()
    data class Message<T>(val text: String) : SocketEvent<T>()
}