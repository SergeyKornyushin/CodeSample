package com.codesample.presentation.mapPlaceSelection.model

import android.os.Parcelable
import com.codesample.presentation.common.models.Identity
import kotlinx.parcelize.Parcelize

@Parcelize
data class SeatUi(
    override val id: String,
    val seatId: Int,
    val multiId: Int?,
    val title: String,
    val fillState: Int,
    val maxCapacity: Int,
    val legendId: Int,
    val coordinates: CoordinatesUi?,
    val isTaken: Boolean,
    var legendUi: LegendUi?,
    var count: Int,
    var selected: Boolean,
) : Identity<String>, Parcelable {

    val status: SeatStatus = when {
        multiId != null -> SeatStatus.MULTI
        isTaken -> SeatStatus.BOOKED
        coordinates == null -> SeatStatus.EMPTY
        else -> SeatStatus.AVAILABLE
    }

    fun getTotalPriceForPlace(): Int {
        val price = legendUi?.price ?: return 0

        return if (multiId != null) (count * price) else price
    }

    fun getTotalPriceFormatted(): String {
        return getTotalPriceForPlace().toString() + " ֏"
    }

    fun isMulti(): Boolean = status == SeatStatus.MULTI
}
