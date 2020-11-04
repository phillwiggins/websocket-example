package com.purewowstudio.websocket.ui.entities

import com.purewowstudio.network.SubscriptionType

data class SubscribeRQ(
    val subscribe: Subscribe
): SubscriptionType