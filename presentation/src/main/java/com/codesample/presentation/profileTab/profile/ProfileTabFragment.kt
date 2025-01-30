package com.codesample.presentation.profileTab.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.badoo.mvicore.modelWatcher
import com.codesample.presentation.R
import com.codesample.presentation.common.baseFragment.BaseFragment
import com.codesample.presentation.common.extentions.isLoading
import com.codesample.presentation.databinding.FragmentProfileTabBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileTabFragment : BaseFragment<FragmentProfileTabBinding, ProfileUiState, ProfileEvent>() {

    override val viewModel: ProfileTabViewModel by viewModels()

    override val stateRenderer = modelWatcher {
        ProfileUiState::isLoading { isLoading ->
            binding.progressBar.isLoading = isLoading
        }
        ProfileUiState::phoneNumber { phoneNumber ->
            binding.tvPhoneNumber.text = phoneNumber
        }
        ProfileUiState::isUserAuthorized { isUserAuthorized ->
            binding.btnLogin.text = if (isUserAuthorized) {
                getString(R.string.authorization_phone_logout)
            } else {
                getString(R.string.authorization_phone_login)
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentProfileTabBinding {
        return FragmentProfileTabBinding.inflate(inflater, container, false)
    }

    override fun ProfileEvent.handleEvent() {
        when (this) {
            is ProfileEvent.NavigateToAuthorization -> showAuthorization()
        }
    }

    override fun FragmentProfileTabBinding.setupViews() {
        btnLogin.setOnClickListener {
            viewModel.changeAuthorizationState()
        }
    }

    private fun showAuthorization() {
        navigate(ProfileTabFragmentDirections.actionProfileToAuthorization())
    }
}