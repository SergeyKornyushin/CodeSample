package com.yusmp.domain.debugging

import android.content.Context

interface LaunchChuckerUseCase {
    operator fun invoke(context: Context)
}