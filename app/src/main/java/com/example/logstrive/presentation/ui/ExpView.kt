package com.example.logstrive.presentation.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ExpView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint1 = Paint().apply {
        color = Color.parseColor("#ffffff")
        textSize = 30f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val desiredWidth = MeasureSpec.getSize(widthMeasureSpec)
        val desiredHeight = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(desiredWidth, desiredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Adjusting the y position to account for the text baseline
        val xPos = 30f
        val yPos = paint1.textSize // Position the text based on its size
        canvas.drawText("aasddsdj", xPos, yPos, paint1)
    }
}
