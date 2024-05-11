package com.yusmp.presentation.auth.phone.model

enum class CountryPhoneCode(val internationalCode: String, private val internalCode: String = "") {
    RUSSIAN("+7", "8"),
    KAZAKH("+7");

    companion object {
        fun identifyCountry(input: String): CountryPhoneCode? {
            return when (input.getOrNull(1).toString()) {
                RUSSIAN.internalCode -> RUSSIAN
                KAZAKH.internalCode -> KAZAKH
                else -> null
            }
        }

        fun isCountryCodeSupported(countryCode: Int?): Boolean? {
            if (countryCode == null) return null

            return when (PLUS_PREFIX + countryCode.toString()) {
                RUSSIAN.internationalCode -> true
                KAZAKH.internationalCode -> true
                else -> false
            }
        }

        const val PLUS_PREFIX = "+"
    }
}
