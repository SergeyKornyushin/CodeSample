package com.codesample.app.hilt.utils

import android.content.Context
import com.codesample.presentation.utils.phoneNumber.PhoneNumberUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    fun getPhoneNumberUtil(@ApplicationContext context: Context) = PhoneNumberUtils(context)
}