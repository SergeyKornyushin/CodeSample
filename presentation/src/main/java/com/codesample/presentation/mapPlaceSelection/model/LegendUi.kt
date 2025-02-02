package com.codesample.presentation.mapPlaceSelection.model

import android.os.Parcelable
import com.codesample.presentation.common.models.Identity
import kotlinx.parcelize.Parcelize

@Parcelize
data class LegendUi(
    override val id: String,
    val legendId: Int,
    val title: String,
    val price: Int,
    val hexColor: String,
) : Identity<String>, Parcelable