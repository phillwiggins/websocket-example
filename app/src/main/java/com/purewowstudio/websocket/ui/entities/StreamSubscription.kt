package com.purewowstudio.websocket.ui.entities

import kotlinx.serialization.Serializable

@Serializable
data class StreamSubscription(
    val resource: String
)