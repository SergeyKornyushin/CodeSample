package com.codesample.presentation.customViews.mapPlaceSelection

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.updateLayoutParams
import coil.load
import com.codesample.domain.common.extentions.orDefault
import com.codesample.domain.common.extentions.orZero
import com.codesample.presentation.common.extentions.symmetricDifference
import com.codesample.presentation.databinding.ViewEventMapBinding
import com.codesample.presentation.mapPlaceSelection.model.EventMapUi
import com.codesample.presentation.mapPlaceSelection.model.SeatUi

class EventMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewEventMapBinding.inflate(LayoutInflater.from(context), this)

    private var onTicketPlaceClick: (SeatUi) -> Unit = {}
    private var isPlaceSelectable: Boolean = false

    private lateinit var eventMap: EventMapUi
    private val selectedSeatIds = mutableListOf<SeatUi>()

    private var viewParam = EventMapViewParam()
    private var multiViewIds = mutableMapOf<Int, SeatUi>()

    private var canZoomInAction: (Boolean) -> Unit = {}
    private var canZoomOutAction: (Boolean) -> Unit = {}

    init {
        with(binding) {
            root.doOnPreDraw {
                viewParam = viewParam.copy(
                    containerHeight = it.height.toFloat(),
                    containerWidth = it.width.toFloat()
                )
            }
            zoomContainer.setOnZoomChangedListener { zoom ->
                canZoomInAction.invoke(canZoomIn(newZoom = zoom))
                canZoomOutAction.invoke(canZoomOut(newZoom = zoom))
                isPlaceSelectable = zoom.isMaxZoom()
            }
            with(viewContainer) {
                setOnClickListener { event ->
                    if (isPlaceSelectable) return@setOnClickListener

                    binding.zoomContainer.maxZoomToPoint(event = event, needToAnimate = true)
                }
                setIsChildClickable(listener = { isPlaceSelectable })
            }
        }
    }

    fun updateSelectedSeats(updatedSeats: List<SeatUi>) {
        with(selectedSeatIds) {
            symmetricDifference(updatedSeats).onEach { seat ->
                val seatId = seat.seatId
                val isSelectedCurrently = this.any { it.seatId == seatId }

                val selectedSeat = when (isSelectedCurrently) {
                    true -> seat.copy(selected = false).also { removeAll { it.seatId == seatId } }
                    false -> seat.also { add(seat) }
                }

                getTicketPlaceView(seatId)?.onBind(selectedSeat)
            }
        }
    }

    fun setOnTicketPlaceClickListener(listener: (SeatUi) -> Unit) {
        onTicketPlaceClick = listener
    }

    fun setEventMap(eventMap: EventMapUi) {
        this.eventMap = eventMap

        setupContainerSize()
        setupMaxZoom()
        addTicketPlaceViews()
        addBackgroundImage()
    }

    fun increaseMap() {
        binding.zoomContainer.zoomIn()
    }

    fun reduceMap() {
        binding.zoomContainer.zoomOut()
    }

    fun setOnZoomChangedListener(
        canZoomInAction: (Boolean) -> Unit,
        canZoomOutAction: (Boolean) -> Unit
    ) {
        this.canZoomInAction = canZoomInAction
        this.canZoomOutAction = canZoomOutAction
    }

    private fun setupContainerSize() {
        viewParam = viewParam.copy(
            maxColumn = eventMap.columns,
            maxRow = eventMap.rows
        )

        binding.viewContainer.apply {
            updateLayoutParams<ViewGroup.LayoutParams> {
                height = viewParam.getScaledContainerHeight().toInt()
                width = viewParam.getScaledContainerWidth().toInt()
            }
        }
    }

    private fun setupMaxZoom() {
        binding.zoomContainer.setMaxZoom(maxZoom = viewParam.calculateMaxZoomFactor())
    }

    private fun addTicketPlaceViews() {
        with(binding.viewContainer) {
            eventMap.seats.forEach { seat ->
                when (multiViewIds.contains(seat.multiId)) {
                    true -> updateTicketPlaceViewSize(seat = seat)
                    false -> addTicketPlaceView(parent = this, seat = seat)
                }
            }
        }
    }

    private fun addTicketPlaceView(parent: ViewGroup, seat: SeatUi) {
        with(seat) { if (seat.isMulti()) multiViewIds[multiId.orDefault()] = this }

        TicketPlaceView(
            context = context,
            parent = parent,
            onPlaceClickAction = { onTicketPlaceClick.invoke(it) },
            isPlaceSelectableAction = { isPlaceSelectable }
        ).apply {
            id = seat.seatId

            onBind(seat)

            with(viewParam) {
                setupSizeAndPosition(
                    viewSize = getScaledTicketPlaceViewSize(),
                    positionX = getTicketPlaceViewCoordinate(position = seat.getX()),
                    positionY = getTicketPlaceViewCoordinate(position = seat.getY())
                )

                setupPlaceViewBackground(
                    legend = eventMap.legends,
                    cornerRadius = getScaledTicketPlaceCornerRadius()
                )

                updatePlaceViewMargins(margin = getScaledTicketPlaceMargins())

                setupSelectionView(
                    cornerRadius = getScaledSelectionCornerRadius(),
                    initialScale = getInitialScale()
                )
            }
        }.also { parent.addView(it) }
    }

    private fun updateTicketPlaceViewSize(seat: SeatUi) {
        val targetSeatView = multiViewIds[seat.multiId] ?: return

        getTicketPlaceView(targetSeatView.seatId)?.apply {
            val cornerRadius = viewParam.getUpdatedCornerRadius(
                targetCoordinates = targetSeatView.coordinates ?: return,
                coordinates = seat.coordinates ?: return
            )
            updateCornerRadius(
                viewCornerRadius = cornerRadius.first,
                selectionCornerRadius = cornerRadius.second
            )
            updateSize(
                targetCoordinates = targetSeatView.coordinates,
                newCoordinates = seat.coordinates,
                viewSize = viewParam.getScaledSingleTicketViewSize()
            )
        }
    }

    private fun getTicketPlaceView(seatId: Int): TicketPlaceView? =
        binding.viewContainer.findViewById(seatId)

    private fun addBackgroundImage() {
        binding.ivEventPlace.load(eventMap.backgroundImageUrl)
    }

    private fun SeatUi.getX() = this.coordinates?.x.orZero()

    private fun SeatUi.getY() = this.coordinates?.y.orZero()

    private fun canZoomIn(newZoom: Float) = newZoom < binding.zoomContainer.getMaxZoom()

    private fun canZoomOut(newZoom: Float): Boolean {
        with(binding.zoomContainer) {
            val zoom = newZoom.coerceIn(minimumValue = getMinZoom(), maximumValue = getMaxZoom())
            return zoom > getMinZoom()
        }
    }

    private fun Float.isMaxZoom() = this >= binding.zoomContainer.getMaxZoom()
}