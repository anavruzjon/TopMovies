package com.example.topmovies.ui.custom_views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import com.example.topmovies.R
import com.example.topmovies.utils.dpToPx
import com.example.topmovies.utils.spToPx

class VoteRating @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attributes, defStyleAttr) {

    private val PERCENT_SIZE_LESS_TEXT_SIZE_ON = 2.5f

    private val backgroundRect = RectF()
    private var percentage: Int
    private var sweepAngle: Float
    private val strokeWidthCircle: Float
    private val textSizee: Float
    private val backgroundColor: Int
    private val textColor: Int

    private var viewRadius: Int = 0

    private val progressPaint = Paint()
    private val incompleteProgressPaint = Paint()
    private val textPaint = Paint()
    private val percentagePaint = Paint()
    private val backgroundPaint = Paint()


    init {
        val typedArray = context.obtainStyledAttributes(attributes, R.styleable.VoteRatingView)
        percentage = typedArray.getInteger(R.styleable.VoteRatingView_percentage, 0)
        strokeWidthCircle = typedArray.getDimensionPixelSize(R.styleable.VoteRatingView_strokeWidth, context.dpToPx(8).toInt()).toFloat()
        textSizee = typedArray.getDimensionPixelSize(R.styleable.VoteRatingView_android_textSize, context.spToPx(24).toInt()).toFloat()
        backgroundColor = typedArray.getColor(R.styleable.VoteRatingView_backgroundColor, context.getColor(R.color.voteRatingBackground))
        textColor = typedArray.getColor(R.styleable.VoteRatingView_android_textColor, context.getColor(R.color.white))
        typedArray.recycle()

        sweepAngle = percentage * 360 / 100f

        setup()

    }

    fun setPercentageValue(percent: Int) {
        this.percentage = percent
        sweepAngle = percentage * 360 / 100f

        setup()

        invalidate()
        requestLayout()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        backgroundRect.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawCircle(width / 2.0f, height / 2.0f, viewRadius.toFloat(), backgroundPaint)

        backgroundRect.set(
            strokeWidthCircle * 1.5f, // Space between the strokeCircle and border is strokeWidthCircle * 1.5
            strokeWidthCircle * 1.5f, // Space between the strokeCircle and border is strokeWidthCircle * 1.5
            width.toFloat() - strokeWidthCircle * 1.5f,
            height.toFloat() - strokeWidthCircle * 1.5f
        )
        canvas.drawArc(backgroundRect, (sweepAngle + 270) % 360, 360 - sweepAngle, false, incompleteProgressPaint)

        backgroundRect.set(
            strokeWidthCircle * 1.5f, // Space between the strokeCircle and border is strokeWidthCircle * 1.5
            strokeWidthCircle * 1.5f, // Space between the strokeCircle and border is strokeWidthCircle * 1.5
            width.toFloat() - strokeWidthCircle * 1.5f,
            height.toFloat() - strokeWidthCircle * 1.5f
        )
        canvas.drawArc(backgroundRect, 270f, sweepAngle, false, progressPaint)

        drawText(canvas, percentage.toString(), textPaint, percentagePaint)

    }


    private fun drawText(canvas: Canvas, text: String, textPaint: Paint, percentagePaint: Paint) {
        val textBounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        val percentageBounds = Rect()
        percentagePaint.getTextBounds(" %", 0, 2, percentageBounds)

        val x = (canvas.width / 2.0f) - (textBounds.width() / 2.0f) - (percentageBounds.width() / 2.0f)
        val y = (canvas.height / 2.0f) + (textBounds.height() / 2.0f)
        canvas.drawText(text, x, y, textPaint)
        canvas.drawText(" %", x + textBounds.width(), y - textBounds.height() + percentageBounds.height(), percentagePaint)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val textWidth = textPaint.measureText(percentage.toString()) + percentagePaint.measureText(" %")
        val contentSize = textWidth + (strokeWidthCircle * 2) * 2
        val viewSize = reconcileSize(contentSize.toInt(), widthMeasureSpec)

        viewRadius = viewSize / 2

        setMeasuredDimension(viewSize, viewSize)
    }

    private fun reconcileSize(contentSize: Int, spec: Int): Int {

        val mode = MeasureSpec.getMode(spec)
        val size = MeasureSpec.getSize(spec)

        return when (mode) {
            MeasureSpec.EXACTLY -> if (size < contentSize) contentSize else size
            MeasureSpec.AT_MOST -> if (size < contentSize) contentSize else size
            MeasureSpec.UNSPECIFIED -> contentSize
            else -> size
        }
    }

    private fun setup() {

        with(progressPaint) {
            strokeWidth = strokeWidthCircle
            style = Paint.Style.STROKE
            flags = Paint.ANTI_ALIAS_FLAG
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            pathEffect = CornerPathEffect(10f)
            color = when (percentage) {
                in 70..100 -> context.getColor(R.color.voteGreenFront)
                in 40..69 -> context.getColor(R.color.voteYellowFront)
                in 0..39 -> context.getColor(R.color.voteRedFront)
                else -> context.getColor(R.color.voteRedFront)
            }
        }


        with(incompleteProgressPaint) {
            strokeWidth = strokeWidthCircle
            style = Paint.Style.STROKE
            flags = Paint.ANTI_ALIAS_FLAG
            color = when (percentage) {
                in 70..100 -> context.getColor(R.color.voteGreenBack)
                in 40..69 -> context.getColor(R.color.voteYellowBack)
                in 0..39 -> context.getColor(R.color.voteRedBack)
                else -> context.getColor(R.color.voteRedBack)
            }
        }

        val tf = Typeface.create("sans-serif-condensed-medium", Typeface.NORMAL)
        with(textPaint) {
            flags = Paint.ANTI_ALIAS_FLAG
            color = textColor
            textSize = textSizee
            typeface = tf
        }

        with(percentagePaint) {
            flags = Paint.ANTI_ALIAS_FLAG
            color = textColor
            textSize = textSizee / PERCENT_SIZE_LESS_TEXT_SIZE_ON
        }

        with(backgroundPaint) {
            flags = Paint.ANTI_ALIAS_FLAG
            color = backgroundColor
        }
    }
}