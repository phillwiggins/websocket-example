package com.purewowstudio.websocket.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.purewowstudio.network.ConnectionManager
import com.purewowstudio.network.SocketEvent
import com.purewowstudio.websocket.ui.main.MessageView.Event.Close
import com.purewowstudio.websocket.ui.main.MessageView.Event.Open
import com.purewowstudio.websocket.ui.main.MessageView.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _viewState = MutableLiveData<State>()
    val viewState: LiveData<State> get() = _viewState

    private var connectionManager: ConnectionManager<String>? = null

    init {
        _viewState.value = State(isLoading = false, emptyList())
    }

    fun onEvent(event: MessageView.Event) {
        when (event) {
            Open -> openStream()
            Close -> closeStream()
        }
    }

    private fun openStream() = viewModelScope.launch {
        connectionManager = ConnectionManager.Builder<String>().build(URL)
        connectionManager?.start()?.collect {
            when (it) {
                SocketEvent.Loading -> handleLoading()
                is SocketEvent.Closed -> handleClosed()
                is SocketEvent.Closing -> addMessage(text = "Closing")
                is SocketEvent.Error -> addMessage(text = it.throwable.message.toString())
                is SocketEvent.Message -> addMessage(text = it.text)
            }
        }
    }

    private fun handleClosed() {
        addMessage(text = "Closed")
        updateViewState(currentState.copy(isLoading = false))
    }

    private fun closeStream() = viewModelScope.launch {
        connectionManager?.close()
        connectionManager = null
    }

    private fun addMessage(text: String) {
        val currentMessages = currentState.messages.toMutableList()
        currentMessages.add(text)
        updateViewState(
            currentState.copy(
                isLoading = true,
                messages = currentMessages
            )
        )
    }

    private fun handleLoading() {
        updateViewState(currentState.copy(isLoading = true))
    }

    private fun updateViewState(viewState: State) {
        _viewState.value = viewState
    }

    private val currentState get() = _viewState.value!!

    companion object {
        private const val PUBLIC_KEY = "GKY7SHF80BVZCL95K5SC"
        private const val URL = "wss://stream.cryptowat.ch/connect?apikey=$PUBLIC_KEY"
    }
}
