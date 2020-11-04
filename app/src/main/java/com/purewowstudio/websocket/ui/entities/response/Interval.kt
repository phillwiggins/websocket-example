package com.purewowstudio.websocket.ui.entities.response

data class Interval(
    val closetime: String,
    val ohlc: Ohlc,
    val periodName: String,
    val volumeBaseStr: String,
    val volumeQuoteStr: String
)