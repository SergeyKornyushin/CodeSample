package com.codesample.presentation.utils.phoneNumber

import android.content.Context
import android.telephony.PhoneNumberUtils
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber.PhoneNumber

class PhoneNumberUtils(context: Context) {

    private val phoneNumberUtil = PhoneNumberUtil.createInstance(context)

    fun parse(numberToParse: String, defaultRegion: String = ""): PhoneNumber? {
        return runCatching { phoneNumberUtil.parse(numberToParse, defaultRegion) }
            .getOrElse { phoneNumberUtil.getInvalidExampleNumber("") }
    }

    fun isValidNumber(number: PhoneNumber?): Boolean {
        return runCatching { phoneNumberUtil.isValidNumber(number) }.getOrElse { false }
    }

    fun normalizeNumber(phoneNumber: String): String {
        return PhoneNumberUtils.normalizeNumber(phoneNumber)
    }
}