package com.purewowstudio.network

interface MessageDecoder {
    fun decode(message: String): String
}
