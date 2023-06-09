package com.yusmp.basecode.presentation.authorization

import com.yusmp.basecode.presentation.common.models.UiEvent
import com.yusmp.basecode.presentation.common.models.UiState

data class AuthorizationPhoneNumberState(
    val hasUserAcceptedPolicy: Boolean = false,
    val userPhoneNumber: String = "",
    val errorMessageId: Int? = null,
    val isLoading: Boolean = false,
) : UiState {
    val isLoginButtonEnabled: Boolean
        get() = hasUserAcceptedPolicy && userPhoneNumber.isNotEmpty() && errorMessageId == null
}

sealed class AuthorizationPhoneNumberEvent : UiEvent() {

    object NavigateUp : AuthorizationPhoneNumberEvent()
}