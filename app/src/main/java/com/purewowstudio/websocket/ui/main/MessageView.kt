package com.purewowstudio.websocket.ui.main

interface MessageView {
    data class State(val isLoading: Boolean, val messages: List<String>)

    sealed class Event {
        object Open : MessageView.Event()
        object Close : MessageView.Event()
    }
}