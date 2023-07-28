package com.yusmp.data.debugging

import android.content.Context
import com.chuckerteam.chucker.api.Chucker
import com.yusmp.domain.debugging.LaunchChuckerUseCase
import javax.inject.Inject

class LaunchChuckerUseCaseImpl @Inject constructor() : LaunchChuckerUseCase {
    override fun invoke(context: Context) {
        context.startActivity(Chucker.getLaunchIntent(context))
    }
}