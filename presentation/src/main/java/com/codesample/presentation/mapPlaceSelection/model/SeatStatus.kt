package com.codesample.presentation.mapPlaceSelection.model

sealed class SeatStatus {
    data object BOOKED : SeatStatus()
    data object MULTI : SeatStatus()
    data object AVAILABLE : SeatStatus()
    data object EMPTY : SeatStatus()
}