package com.codesample.presentation.customViews.mapPlaceSelection

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.PathParser
import com.codesample.presentation.common.extentions.dpToPx

class TicketPlaceSelectionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var path = Path()
    private val rectF = RectF()
    private val matrix = Matrix()

    // Берется из ic_check_mark_big.xml
    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private var isInitialDrawing = true

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isInitialDrawing) setupIconTransaction()

        canvas.drawPath(path, paint)
    }

    fun setupView(initialScale: Float) {
        val scale = initialScale * ICON_WIDTH_FACTOR

        paint.strokeWidth = PAINT_STROKE_WIDTH * scale * ICON_WIDTH_FACTOR

        // Берется из ic_check_mark_big.xml
        path = PathParser.createPathFromPathData(
            "M ${13.5345f.dpToPx * scale}, ${30.0195f.dpToPx * scale}" +
                    "L ${25.419f.dpToPx * scale}, ${42.0345f.dpToPx * scale}" +
                    "L ${52.155f.dpToPx * scale}, ${15f.dpToPx * scale}"
        )
        rectF.apply { path.computeBounds(this, true) }
    }

    private fun setupIconTransaction() {
        isInitialDrawing = false

        matrix.apply {
            reset()
            setTranslate(
                /* dx = */ width / 2 - rectF.width() * ICON_WIDTH_FACTOR,
                /* dy = */ height / 2 - rectF.height() * ICON_HEIGHT_FACTOR
            )
            path.transform(this)
        }
    }

    private companion object {
        // Необходимы т.к. изображение изначально рассчитано на view без границ
        const val ICON_WIDTH_FACTOR = 0.82f
        const val ICON_HEIGHT_FACTOR = 1.072f

        // Width берется из ic_check_mark_big.xml
        val PAINT_STROKE_WIDTH = 10.620645f.dpToPx
    }
}