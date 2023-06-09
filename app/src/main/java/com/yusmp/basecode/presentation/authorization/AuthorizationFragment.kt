package com.yusmp.basecode.presentation.authorization

import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.badoo.mvicore.modelWatcher
import com.yusmp.basecode.R
import com.yusmp.basecode.databinding.FragmentAuthorizationBinding
import com.yusmp.basecode.presentation.common.BaseFragment
import com.yusmp.basecode.presentation.common.extentions.ClickablePart
import com.yusmp.basecode.presentation.common.extentions.changeErrorState
import com.yusmp.basecode.presentation.common.extentions.isLoading
import com.yusmp.basecode.presentation.common.extentions.openLinkInBrowser
import com.yusmp.basecode.presentation.common.extentions.setClickableParts
import com.yusmp.basecode.presentation.common.utils.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorizationFragment :
    BaseFragment<FragmentAuthorizationBinding, AuthorizationPhoneNumberState, AuthorizationPhoneNumberEvent>() {

    override val viewModel: AuthorizationViewModel by viewModels()

    private val russianCountryCodeIso = "RU"

    override val stateRenderer = modelWatcher {
        AuthorizationPhoneNumberState::hasUserAcceptedPolicy { isChecked ->
            binding.chkPolicy.isChecked = isChecked
        }
        AuthorizationPhoneNumberState::errorMessageId { errorMessageId ->
            binding.ilPhoneNumber.changeErrorState(errorMessageId?.let(::getString))
        }
        AuthorizationPhoneNumberState::isLoginButtonEnabled { isEnabled ->
            binding.btnLogin.isEnabled = isEnabled
        }
        AuthorizationPhoneNumberState::isLoading { isLoading ->
            binding.progressBar.isLoading = isLoading
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentAuthorizationBinding {
        return FragmentAuthorizationBinding.inflate(inflater, container, false)
    }

    override fun AuthorizationPhoneNumberEvent.handleEvent() {
        when (this) {
            is AuthorizationPhoneNumberEvent.NavigateUp -> {
                findNavController().popBackStack(R.id.authorization_nav_graph, true)
            }
        }
    }

    override fun FragmentAuthorizationBinding.setupViews() {
        with(binding) {
            tvPolicy.setClickableParts(
                completeTextId = R.string.authorization_policy,
                clickableParts = listOf(
                    ClickablePart(
                        textId = R.string.authorization_company_rules,
                        clickListener = { openLinkInBrowser(getString(R.string.link_policy)) }
                    )
                )
            )
            etPhoneNumber.doAfterTextChanged { text ->
                viewModel.updatePhoneNumberValue(text.toString())
            }
            btnLogin.setSafeOnClickListener {
                viewModel.registerByPhone()
            }
            btnNavigateBack.setSafeOnClickListener {
                findNavController().popBackStack(R.id.authorization_nav_graph, true)
            }
            chkPolicy.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updatePolicyAgreementCheckboxState(isChecked)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.etPhoneNumber.addTextChangedListener(
            PhoneNumberFormattingTextWatcher(
                russianCountryCodeIso
            )
        )
    }
}