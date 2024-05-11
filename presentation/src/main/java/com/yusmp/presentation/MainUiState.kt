package com.yusmp.basecode.presentation

import com.yusmp.presentation.common.models.UiEvent
import com.yusmp.presentation.common.models.UiState

object MainUiState : UiState

sealed class MainUiEvent : UiEvent() {
    object SetHomeAsStartDestination : MainUiEvent()
    object SetAuthAsStartDestination : MainUiEvent()
    object ObserveDeviceShake : MainUiEvent()
}