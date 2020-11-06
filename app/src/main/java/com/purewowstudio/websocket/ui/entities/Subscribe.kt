package com.purewowstudio.websocket.ui.entities

import kotlinx.serialization.Serializable

@Serializable
data class Subscribe(
    val subscriptions: List<Subscription>
)