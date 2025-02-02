package com.codesample.presentation.customViews.mapPlaceSelection

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.OnGestureListener
import android.view.MotionEvent
import android.widget.FrameLayout

class ViewContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var onClickListener: (event: MotionEvent) -> Unit = {}
    private var isChildClickable: () -> Boolean = { false }
    private val gestureDetector = createGestureDetector()

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return isChildClickable.invoke().not()
    }

    fun setOnClickListener(listener: (event: MotionEvent) -> Unit) {
        this.onClickListener = listener
    }

    fun setIsChildClickable(listener: () -> Boolean) {
        isChildClickable = listener
    }

    private fun createGestureDetector() = GestureDetector(context, object : OnGestureListener {
        override fun onDown(event: MotionEvent): Boolean = true

        override fun onShowPress(event: MotionEvent) = Unit

        override fun onSingleTapUp(event: MotionEvent): Boolean {
            onClickListener.invoke(event)
            return true
        }

        override fun onScroll(
            event1: MotionEvent?,
            event2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean = true

        override fun onLongPress(event: MotionEvent) = Unit

        override fun onFling(
            event1: MotionEvent?,
            event2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean = true
    })
}