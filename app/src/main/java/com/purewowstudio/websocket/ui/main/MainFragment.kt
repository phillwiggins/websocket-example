package com.purewowstudio.websocket.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.purewowstudio.websocket.R
import com.purewowstudio.websocket.databinding.MainFragmentBinding
import com.purewowstudio.websocket.ui.main.MessageView.Event.Close
import com.purewowstudio.websocket.ui.main.MessageView.Event.Open

class MainFragment : Fragment(R.layout.main_fragment) {

    private var binding: MainFragmentBinding? = null

    private val viewModel by viewModels<MainViewModel>()

    private val adapter = MessageAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragmentBinding.bind(view)

        observeViewState()
        initViews()
    }

    private fun observeViewState() = binding?.apply {
        viewModel.viewState.observe(viewLifecycleOwner) {
            progressBar.visibility = if (it.isLoading) View.VISIBLE else View.INVISIBLE
            adapter.submitList(it.messages)
        }
    }

    private fun initViews() = binding?.apply {
        buttonStart.setOnClickListener { viewModel.onEvent(Open) }
        buttonStop.setOnClickListener { viewModel.onEvent(Close) }
        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}

