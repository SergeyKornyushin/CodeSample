package com.codesample.presentation.auth.phone

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codesample.domain.auth.LoginParams
import com.codesample.domain.auth.LoginUseCase
import com.codesample.presentation.AuthorizationNavGraphArgs
import com.codesample.presentation.R
import com.codesample.presentation.auth.phone.model.CountryPhoneCode.Companion.isCountryCodeSupported
import com.codesample.presentation.common.baseFragment.BaseViewModel
import com.codesample.presentation.utils.phoneNumber.PhoneNumberUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val phoneNumberUtil: PhoneNumberUtils,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<PhoneAuthUiState, PhoneAuthUiEvent>(PhoneAuthUiState()) {

    private val args = AuthorizationNavGraphArgs.fromSavedStateHandle(savedStateHandle)

    fun updatePolicyAgreementCheckboxState(isChecked: Boolean) {
        updateUiState { copy(hasUserAcceptedPolicy = isChecked) }
    }

    fun registerByPhone() {
        loginUseCase(LoginParams(phoneNumberUtil.normalizeNumber(currentState.userPhoneNumber)))
            .onStart { updateUiState { copy(isLoading = true) } }
            .onCompletion { updateUiState { copy(isLoading = false) } }
            .onEach { result ->
                result.onFailure { handleError(it) }
                result.onSuccess {
                    sendUiEvent(
                        if (args.isFirstLaunch) PhoneAuthUiEvent.OpenMainScreen else PhoneAuthUiEvent.NavigateUp
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun updatePhoneNumberValue(phoneNumber: String) {
        with(phoneNumberUtil) {
            val parseNumber = parse(numberToParse = phoneNumber)
            val isNumberValid = isValidNumber(number = parseNumber)
            val countryCode = parseNumber?.countryCode

            val errorMessageId = when (isCountryCodeSupported(countryCode = countryCode)) {
                false -> R.string.authorization_phone_incorrect_number
                else -> null
            }
            updateUiState {
                copy(
                    isNumberValid = isNumberValid,
                    userPhoneNumber = if (isNumberValid) phoneNumber else "",
                    errorMessageId = errorMessageId
                )
            }
        }
    }

    fun handleBackClick() {
        when (args.isFirstLaunch) {
            true -> PhoneAuthUiEvent.OpenMainScreen
            false -> PhoneAuthUiEvent.NavigateUp
        }.apply { sendUiEvent(event = this) }
    }
}