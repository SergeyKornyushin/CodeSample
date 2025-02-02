package com.codesample.presentation.mapPlaceSelection.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoordinatesUi(val x: Int, val y: Int) : Parcelable