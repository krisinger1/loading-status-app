package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new){
            ButtonState.Clicked -> Log.i("LoadingButton", "Button clicked")
            ButtonState.Loading -> Log.i("LoadingButton", "Button Loading")
            ButtonState.Completed -> Log.i("LoadingButton", "Button completed")

        }
    }

    val paint = Paint().apply {
        style= Paint.Style.FILL }


    init {
        isClickable=true

    }

    override fun performClick(): Boolean {
        if(super.performClick()) return true
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color= Color.GRAY
        canvas?.drawRect(0f, 0f,widthSize.toFloat(), heightSize.toFloat(),paint)
        paint.color=Color.BLUE
        canvas?.drawRect(0f, 0f,(widthSize/2).toFloat(), (heightSize).toFloat(),paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}