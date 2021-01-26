package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var barColorFront = 0
    private var barColorBack = 0

    private var loadingBarWidth =0f

    private val valueAnimator = ValueAnimator()

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        invalidate()
        when(new){
            ButtonState.Clicked -> Log.i("LoadingButton", "Button clicked")
            ButtonState.Loading -> {
                Log.i("LoadingButton", "Button Loading")
                startLoadingAnimation()
            }
            ButtonState.Completed -> Log.i("LoadingButton", "Button completed")

        }
    }

    val paint = Paint().apply {
        style= Paint.Style.FILL }


    init {
        isClickable=true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton){
            barColorFront=getColor(R.styleable.LoadingButton_colorLoadingBar,0)
            barColorBack=getColor(R.styleable.LoadingButton_colorLoadingBarBackground,0)
        }

    }

    private fun startLoadingAnimation(){
        valueAnimator.duration=3000
        valueAnimator.repeatCount= 1
        valueAnimator.setFloatValues(0f, widthSize.toFloat())
        valueAnimator.addUpdateListener {
            loadingBarWidth = it.animatedValue as Float
            invalidate()
        }
        valueAnimator.start()

    }

    override fun performClick(): Boolean {

        buttonState=ButtonState.Loading
        //invalidate()
        return super.performClick()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        when(buttonState){
            ButtonState.Loading->{
                paint.color= barColorBack
                canvas?.drawRect(0f, 0f,widthSize.toFloat(), heightSize.toFloat(),paint)
                paint.color=barColorFront
                canvas?.drawRect(0f, 0f,(loadingBarWidth).toFloat(), (heightSize).toFloat(),paint)
            }
            ButtonState.Completed ->{
                paint.color= barColorBack
                canvas?.drawRect(0f, 0f,widthSize.toFloat(), heightSize.toFloat(),paint)
            }
        }


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