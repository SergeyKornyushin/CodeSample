package com.codesample.presentation.mapPlaceSelection.model

data class EventMapUi(
    val columns: Int,
    val rows: Int,
    val backgroundImageUrl: String? = null,
    val legends: List<LegendUi>,
    val seats: List<SeatUi>,
) {

    private fun getPlace(seaId: Int) = seats.first { it.seatId == seaId }

    fun getNeighborsMultiSeats(seatId: Int): List<SeatUi> {
        val place = getPlace(seatId)

        return if (place.status == SeatStatus.MULTI) {
            val allMultiIds = seats.filter { it.multiId == place.multiId }
            allMultiIds
        } else {
            emptyList()
        }
    }
}