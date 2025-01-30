package com.codesample.codesample.presentation

import com.codesample.presentation.common.models.UiEvent
import com.codesample.presentation.common.models.UiState

object MainUiState : UiState

sealed class MainUiEvent : UiEvent() {
    object SetHomeAsStartDestination : MainUiEvent()
    object SetAuthAsStartDestination : MainUiEvent()
    object ObserveDeviceShake : MainUiEvent()
}