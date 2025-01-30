package com.codesample.presentation.common.extentions

import android.view.Window
import androidx.core.view.WindowCompat
import com.codesample.presentation.common.models.StatusBarContentAppearanceMode

fun Window.setStatusBarContentColor(mode: StatusBarContentAppearanceMode) {
    WindowCompat.getInsetsController(this, decorView).isAppearanceLightStatusBars = mode()
}