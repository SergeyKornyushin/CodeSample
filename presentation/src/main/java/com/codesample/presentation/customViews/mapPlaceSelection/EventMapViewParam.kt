package com.codesample.presentation.customViews.mapPlaceSelection


import com.codesample.presentation.common.extentions.dpToPx
import com.codesample.presentation.mapPlaceSelection.model.CoordinatesUi
import kotlin.math.roundToInt

internal data class EventMapViewParam(
    val maxColumn: Int = -1,
    val maxRow: Int = -1,
    val containerHeight: Float = -1f,
    val containerWidth: Float = -1f,
    val maxZoomTicketPlaceCount: Int = DEFAULT_MAX_ZOOM_TICKET_PLACE_COUNT
) {

    private val contentWidth = maxColumn * TICKET_PLACE_FULL_SIZE
    private val contentHeight = maxRow * TICKET_PLACE_FULL_SIZE

    /**
     * В зависимости от соотношения сторон выставляются размеры view по размеру контейнера
     */
    private val initialScale = when (contentHeight > contentWidth) {
        true -> containerHeight / contentHeight
        false -> containerWidth / contentWidth
    }

    fun getInitialScale() = initialScale

    fun getScaledContainerHeight() = when (contentHeight > contentWidth) {
        true -> containerHeight
        false -> contentHeight * initialScale
    }

    fun getScaledContainerWidth() = when (contentHeight > contentWidth) {
        true -> contentWidth * initialScale
        false -> containerWidth
    }

    /**
     * Высчитать размер ticketPlace без отступов между view
     */
    fun getScaledTicketPlaceViewSize() =
        ((TICKET_PLACE_VIEW_SIZE + VIEW_PADDING * 2) * initialScale).roundToInt()

    /**
     * Высчитать координаты для отрисовки view относительно позиции
     */
    fun getTicketPlaceViewCoordinate(position: Int) =
        (position * TICKET_PLACE_FULL_SIZE + VIEW_MARGIN) * initialScale

    fun getScaledTicketPlaceCornerRadius() = TICKET_PLACE_CORNER_RADIUS * initialScale

    fun getScaledSelectionCornerRadius() = SELECTION_CORNER_RADIUS * initialScale

    /**
     * Высчитать размер view со всеми отступами
     */
    fun getScaledSingleTicketViewSize() = (TICKET_PLACE_FULL_SIZE * initialScale).roundToInt()

    /**
     * Высчитать CornerRadius для ticketPlaceView и selectedView
     */
    fun getUpdatedCornerRadius(
        targetCoordinates: CoordinatesUi,
        coordinates: CoordinatesUi
    ): Pair<Float, Float> {
        val viewCornerRadius = calculateCornerRadius(targetCoordinates, coordinates) {
            getScaledTicketPlaceCornerRadius()
        }

        val selectionCornerRadius = calculateCornerRadius(targetCoordinates, coordinates) {
            getScaledSelectionCornerRadius()
        }

        return Pair(first = viewCornerRadius, second = selectionCornerRadius)
    }

    /**
     * Высчитать ticketPlaceView margin
     */
    fun getScaledTicketPlaceMargins() = (VIEW_MARGIN * initialScale).roundToInt()

    /**
     * Высчитать максимальный коэффициент увеличения.
     * Ширина экрана должна вмещать [количество элементов][maxZoomTicketPlaceCount]
     */
    fun calculateMaxZoomFactor(): Float {
        val maxZoomWidth =
            TICKET_PLACE_FULL_SIZE * maxZoomTicketPlaceCount - VIEW_MARGIN * 2
        return when (containerWidth > maxZoomWidth * initialScale) {
            true -> containerWidth / (maxZoomWidth * initialScale)
            false -> maxZoomWidth / containerWidth
        }
    }

    /**
     * Высчитать CornerRadius в зависимости от размера (устанавливается по минимальной стороне)
     */
    private fun calculateCornerRadius(
        targetCoordinates: CoordinatesUi,
        coordinates: CoordinatesUi,
        getRadiusAction: () -> Float
    ): Float {
        return when {
            targetCoordinates.x < coordinates.x &&
                    targetCoordinates.y < coordinates.y -> {
                val minSide = minOf(
                    coordinates.x - targetCoordinates.x,
                    coordinates.y - targetCoordinates.y
                )
                (minSide + 1) * getRadiusAction.invoke()
            }

            else -> getRadiusAction.invoke()
        }
    }

    private companion object {
        // Все параметры из дизайна
        val TICKET_PLACE_VIEW_SIZE = 60.dpToPx

        // Padding одной стороны
        val VIEW_PADDING = 10.dpToPx

        // Margin одной стороны
        val VIEW_MARGIN = 10.dpToPx

        // Размер места со всеми отступами
        val TICKET_PLACE_FULL_SIZE = TICKET_PLACE_VIEW_SIZE + (VIEW_PADDING + VIEW_MARGIN) * 2

        // Количество мест по горизонтали при максимальном зуме
        const val DEFAULT_MAX_ZOOM_TICKET_PLACE_COUNT = 6

        const val CORNER_RADIUS_MODIFIER = 6
        val TICKET_PLACE_CORNER_RADIUS =
            (TICKET_PLACE_VIEW_SIZE + VIEW_PADDING * 2) / CORNER_RADIUS_MODIFIER
        val SELECTION_CORNER_RADIUS = TICKET_PLACE_CORNER_RADIUS
    }
}