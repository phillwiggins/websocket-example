package com.purewowstudio.websocket.ui.entities

import kotlinx.serialization.Serializable

@Serializable
data class Subscription(
    val streamSubscription: StreamSubscription
)