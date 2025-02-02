package com.codesample.presentation.customViews.mapPlaceSelection

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.codesample.presentation.R
import com.codesample.presentation.common.extentions.updateMargins
import com.codesample.presentation.databinding.ViewTicketPlaceBinding
import com.codesample.presentation.mapPlaceSelection.model.CoordinatesUi
import com.codesample.presentation.mapPlaceSelection.model.LegendUi
import com.codesample.presentation.mapPlaceSelection.model.SeatUi

class TicketPlaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    parent: ViewGroup? = null,
    private val onPlaceClickAction: (SeatUi) -> Unit = {},
    private val isPlaceSelectableAction: () -> Boolean = { false }
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewTicketPlaceBinding.inflate(LayoutInflater.from(context), parent, true)

    private var seat: SeatUi = INITIAL_SEAT

    private val root get() = binding.root
    private val viewPlace get() = binding.viewPlace
    private val viewSelection get() = binding.viewSelection

    init {
        root.setOnClickListener {
            if (isPlaceSelectableAction.invoke().not() || seat.isTaken) return@setOnClickListener

            onPlaceClickAction.invoke(seat)
        }
    }

    fun onBind(seat: SeatUi) {
        this.seat = seat
        setSelection()
    }

    fun setupSizeAndPosition(viewSize: Int, positionX: Float, positionY: Float) {
        with(root) {
            x = positionX; y = positionY
            updateLayoutParams<ViewGroup.LayoutParams> { height = viewSize; width = viewSize }
        }
    }

    fun updatePlaceViewMargins(margin: Int) {
        viewPlace.updateMargins(left = margin, top = margin, right = margin, bottom = margin)
    }

    fun setupPlaceViewBackground(legend: List<LegendUi>, cornerRadius: Float) {
        viewPlace.background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(seat.getBackgroundColor(legend))
            this.cornerRadius = cornerRadius
        }
    }

    fun setupSelectionView(cornerRadius: Float, initialScale: Float) {
        viewSelection.apply {
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(context.getColor(R.color.black_transparent_50))
                this.cornerRadius = cornerRadius
            }
            this.setupView(initialScale = initialScale)
        }
    }

    fun updateCornerRadius(viewCornerRadius: Float, selectionCornerRadius: Float) {
        viewPlace.background.apply {
            (this as? GradientDrawable)?.let { it.cornerRadius = viewCornerRadius }
        }

        viewSelection.background.apply {
            (this as? GradientDrawable)?.let { it.cornerRadius = selectionCornerRadius }
        }
    }

    fun updateSize(targetCoordinates: CoordinatesUi, newCoordinates: CoordinatesUi, viewSize: Int) {
        root.updateLayoutParams<ViewGroup.LayoutParams> {
            when {
                targetCoordinates.x == newCoordinates.x -> height += viewSize
                targetCoordinates.y == newCoordinates.y -> width += viewSize
            }
        }
    }

    private fun setSelection() {
        viewSelection.isVisible = seat.selected
    }

    private fun SeatUi.getBackgroundColor(legend: List<LegendUi>): Int {
        return when (isTaken) {
            true -> Color.parseColor(BOOKED_TICKET_VIEW_HEX_COLOR)
            false -> legend.firstOrNull { it.legendId == legendId }?.hexColor?.toColorInt()
                ?: Color.parseColor(DEFAULT_TICKET_VIEW_HEX_COLOR)
        }
    }

    private companion object {
        const val BOOKED_TICKET_VIEW_HEX_COLOR = "#EDEDED"
        const val DEFAULT_TICKET_VIEW_HEX_COLOR = "#F18E1C"

        val INITIAL_SEAT = SeatUi(
            id = "",
            coordinates = null,
            fillState = -1,
            maxCapacity = -1,
            isTaken = true,
            legendId = -1,
            multiId = null,
            title = "",
            count = 0,
            seatId = -1,
            legendUi = LegendUi(
                id = "",
                legendId = -1,
                title = "",
                price = -1,
                hexColor = BOOKED_TICKET_VIEW_HEX_COLOR
            ),
            selected = false
        )
    }
}