package com.appinsnap.aishrm.util
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.appinsnap.aishrm.R
class CustomSeekBarProgressDrawable(
    private val ismultipleCheck: Boolean,
    private val segmentcheckValue: Int,
    private val context1 :Context,
    private val maxProgress: Int,
    private val presentProgress: Int,
    private val absentProgress: Int,
    private val leaveProgress: Int,
    private val lateArrivalProgress: Int,
) : Drawable(), SeekBar.OnSeekBarChangeListener {

    private val paint = Paint()

    private val segmentNames = arrayOf("P -", "A -", "L -", "LA -")
    private val segmentCounts = arrayOf(presentProgress, absentProgress - presentProgress, leaveProgress - absentProgress, lateArrivalProgress - leaveProgress)

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        val width = bounds.width()
        val height = bounds.height()

        val segmentWidths = calculateSegmentWidths(width)

        for (i in 0 until 4) {
            if (segmentCounts[i] > 0) {
                val rect = RectF(
                    segmentWidths.take(i).sum(),
                    0f,
                    segmentWidths.take(i + 1).sum(),
                    height.toFloat()
                )

                paint.color = ContextCompat.getColor(context1, getSegmentColor(i))
                canvas.drawRoundRect(rect, 0f, 0f, paint)

                drawSegmentText(canvas, i, rect.centerX(), (height / 2).toFloat())
            }
        }

    }

    private fun calculateSegmentWidths(totalWidth: Int): FloatArray {
        val segmentWidths = FloatArray(4)

        segmentWidths[0] = (totalWidth * presentProgress / maxProgress).toFloat()
        segmentWidths[1] = (totalWidth * (absentProgress - presentProgress) / maxProgress).toFloat()
        segmentWidths[2] = (totalWidth * (leaveProgress - absentProgress) / maxProgress).toFloat()
        segmentWidths[3] = (totalWidth * (lateArrivalProgress - leaveProgress) / maxProgress).toFloat()

        return segmentWidths
    }

    private fun getSegmentColor(segmentIndex: Int): Int {
        return when (segmentIndex) {
            0 -> R.color.present_green
            1 -> R.color.absent_red
            2 -> R.color.leaves_blue
            3 -> R.color.latearrival_yellow
            else -> android.R.color.transparent
        }
    }

    /*private fun drawSegmentText(canvas: Canvas, segmentIndex: Int, centerX: Float, centerY: Float) {

        val count = segmentCounts[segmentIndex]

        val text = "${segmentNames[segmentIndex]} $count"

        val textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.color = ContextCompat.getColor(context1, android.R.color.white)
        textPaint.textSize = 20f
        textPaint.isFakeBoldText = true

        val textBounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, textBounds)

        val textX = centerX - (textBounds.width() / 2)
        val textY = centerY + (textBounds.height() / 2)

        canvas.drawText(text, textX, textY, textPaint)
    }*/


    private fun drawSegmentText(canvas: Canvas, segmentIndex: Int, centerX: Float, centerY: Float) {
        val count = segmentCounts[segmentIndex]

        if (ismultipleCheck && segmentCounts.size!=1 && segmentCounts.any { it <= segmentcheckValue }) {
            if (count <= segmentcheckValue) {
                val text = "${segmentNames[segmentIndex]} $count"

                val textPaint = Paint()
                textPaint.isAntiAlias = true
                textPaint.color = ContextCompat.getColor(context1, android.R.color.white)
                textPaint.textSize = 23f
                textPaint.isFakeBoldText = true

                val textBounds = Rect()
                textPaint.getTextBounds(text, 0, text.length, textBounds)

                val textX = centerX - (textBounds.width() / 4)
                val textY = centerY + (textBounds.height() / 2)

               // canvas.drawText("$count", textX, textY, textPaint)
                return // Do not show text for segments with count less than or equal to 20 when there are multiple segments
            }
        }

            val text = "${segmentNames[segmentIndex]} $count"

            val textPaint = Paint()
            textPaint.isAntiAlias = true
            textPaint.color = ContextCompat.getColor(context1, android.R.color.white)
            textPaint.textSize = 23f
            textPaint.isFakeBoldText = true
            val textBounds = Rect()
            textPaint.getTextBounds(text, 0, text.length, textBounds)
            val textX = centerX - (textBounds.width() / 2)
            val textY = centerY + (textBounds.height() / 2)
            canvas.drawText(text, textX, textY, textPaint)



    }



    override fun setAlpha(alpha: Int) {}

    override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {}

    override fun getOpacity(): Int {
        return android.graphics.PixelFormat.TRANSLUCENT
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        // Handle progress changes if needed
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        // Handle touch tracking if needed
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        // Handle touch tracking if needed
    }
}


