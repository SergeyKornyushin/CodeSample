package com.yusmp.presentation.noInternet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.badoo.mvicore.modelWatcher
import com.yusmp.presentation.common.baseFragment.BaseFragment
import com.yusmp.presentation.common.utils.setSafeOnClickListener
import com.yusmp.presentation.databinding.FragmentNoInternetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoInternetFragment :
    BaseFragment<FragmentNoInternetBinding, NoInternetUiState, NoInternetUiEvent>() {

    companion object {
        const val REQUEST_KEY = "REQUEST_KEY"
    }

    override val viewModel: NoInternetViewModel by viewModels()

    override val stateRenderer = modelWatcher<NoInternetUiState> {}

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentNoInternetBinding.inflate(inflater, container, false)

    override fun NoInternetUiEvent.handleEvent() = Unit

    override fun FragmentNoInternetBinding.setupViews() {
        btnNoInternet.setSafeOnClickListener {
            findNavController().popBackStack()
            setFragmentResult(REQUEST_KEY, bundleOf())
        }
    }
}