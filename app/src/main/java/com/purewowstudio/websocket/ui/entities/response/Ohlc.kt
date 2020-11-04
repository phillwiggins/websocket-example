package com.purewowstudio.websocket.ui.entities.response

data class Ohlc(
    val closeStr: String,
    val highStr: String,
    val lowStr: String,
    val openStr: String
)