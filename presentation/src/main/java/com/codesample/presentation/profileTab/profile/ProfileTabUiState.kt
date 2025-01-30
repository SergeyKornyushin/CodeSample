package com.codesample.presentation.profileTab.profile

import com.codesample.presentation.common.models.UiEvent
import com.codesample.presentation.common.models.UiState

data class ProfileUiState(
    val phoneNumber: String? = null,
    val isLoading: Boolean = true,
    val isUserAuthorized: Boolean = false,
) : UiState

sealed class ProfileEvent : UiEvent() {
    object NavigateToAuthorization : ProfileEvent()
}