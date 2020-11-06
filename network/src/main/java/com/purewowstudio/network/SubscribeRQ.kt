package com.purewowstudio.network

import kotlinx.serialization.Serializable

@Serializable
data class SubscribeRQ<T>(
    val subscribe: T
): SubscriptionType