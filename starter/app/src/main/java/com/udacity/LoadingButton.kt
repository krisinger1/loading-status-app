package com.udacity

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
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
    private var textColor = 0
    private var circleColor =0
    private var buttonTextSize=0f

    private var loadingBarWidth =0f
    private var loadingCircleAngle =0
    private var circleMargin= 25f

    private val valueAnimator = ValueAnimator()
    private val circleAnimator = ValueAnimator()
    private val set = AnimatorSet()

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        invalidate()
        when(new){
            ButtonState.Clicked -> Log.i("LoadingButton", "Button clicked")
            ButtonState.Loading -> {
                Log.i("LoadingButton", "Button Loading")
                startLoadingAnimation()
            }
            ButtonState.Completed -> {
                Log.i("LoadingButton", "Button completed")
            }

        }
    }

    val paint = Paint()

    var textWidth=0f
    val downloadString = resources.getString(R.string.button_name)
    val loadingString = resources.getString(R.string.button_loading)


    init {
        isClickable=true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton){
            barColorFront=getColor(R.styleable.LoadingButton_colorLoadingBar,0)
            barColorBack=getColor(R.styleable.LoadingButton_colorLoadingBarBackground,0)
            textColor=getColor(R.styleable.LoadingButton_buttonTextColor,0)
            buttonTextSize=getDimension(R.styleable.LoadingButton_buttonTextSize, 0f)
            circleColor=getColor(R.styleable.LoadingButton_circleColor,0)

        }
        paint.apply {
            style= Paint.Style.FILL
            textSize = buttonTextSize
        }

        textWidth=paint.measureText(downloadString)
    }

    private fun startLoadingAnimation(){
        // animator for loading circle
        circleAnimator.setIntValues(0,360)
        circleAnimator.repeatCount= ValueAnimator.INFINITE
        circleAnimator.addUpdateListener{
            loadingCircleAngle = it.animatedValue as Int
            invalidate()
        }
        //animator for loading bar
        valueAnimator.setFloatValues(0f, widthSize.toFloat())
        valueAnimator.repeatCount= ValueAnimator.INFINITE
        valueAnimator.addUpdateListener {
            loadingBarWidth = it.animatedValue as Float
            invalidate()
        }

        set.playTogether(circleAnimator, valueAnimator)
        set.duration =3000
        set.start()
    }

    override fun performClick(): Boolean {

        buttonState=ButtonState.Loading
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
                paint.color = textColor
                textWidth=paint.measureText(loadingString)
                canvas?.drawText(loadingString,((widthSize-textWidth)/2),((heightSize+buttonTextSize)/2),paint )
                paint.color=circleColor
                canvas?.drawArc(widthSize-heightSize+circleMargin,
                        circleMargin,widthSize-circleMargin,
                        heightSize-circleMargin,
                        0f,loadingCircleAngle.toFloat(),
                        true,paint)
            }
            ButtonState.Completed ->{
                paint.color= barColorBack
                canvas?.drawRect(0f, 0f,widthSize.toFloat(), heightSize.toFloat(),paint)
                paint.color = textColor
                textWidth=paint.measureText(downloadString)
                canvas?.drawText(downloadString,((widthSize-textWidth)/2),((heightSize+buttonTextSize)/2),paint )
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