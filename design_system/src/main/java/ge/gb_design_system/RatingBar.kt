package ge.gb_design_system

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import ge.designs.design_system.R
import kotlin.math.cos
import kotlin.math.sin

class RatingBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var maxRating = 5
    private var currentRating = 0f
    private var starSize = 40f
    private var spacing = 8f
    private var filledColor = Color.WHITE
    private var unfilledColor = Color.GRAY
    private var isIndicator = false
    private var cornerRadius = 6f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var starPath = Path()



    init {
        attrs?.let {
            context.withStyledAttributes(attrs, R.styleable.RatingBar) {
                maxRating = getInt(R.styleable.RatingBar_maxRating, 5)
                currentRating = getFloat(R.styleable.RatingBar_rating, 0f)
                starSize = getDimension(R.styleable.RatingBar_starSize, 40f)
                spacing = getDimension(R.styleable.RatingBar_starSpacing, 8f)
                filledColor = getColor(R.styleable.RatingBar_filledColor, Color.WHITE)
                unfilledColor = getColor(R.styleable.RatingBar_unfilledColor, Color.GRAY)
                isIndicator = getBoolean(R.styleable.RatingBar_isIndicator, false)
                cornerRadius = getDimension(R.styleable.RatingBar_cornerRadius, 6f)
            }
            println(cornerRadius)
        }
        createRoundedStarPath()
    }

    private fun createRoundedStarPath() {
        starPath.reset()
        val centerX = starSize / 2
        val centerY = starSize / 2
        val outerRadius = starSize / 2
        val innerRadius = outerRadius * 0.45f

        val outerPoints = mutableListOf<PointF>()
        val innerPoints = mutableListOf<PointF>()

        for (i in 0 until 5) {
            val outerAngle = Math.PI * (0.5 + i * 0.4)
            val innerAngle = Math.PI * (0.5 + 0.2 + i * 0.4)

            outerPoints.add(PointF(
                (centerX + outerRadius * cos(outerAngle)).toFloat(),
                (centerY - outerRadius * sin(outerAngle)).toFloat()
            ))
            innerPoints.add(PointF(
                (centerX + innerRadius * cos(innerAngle)).toFloat(),
                (centerY - innerRadius * sin(innerAngle)).toFloat()
            ))
        }

        starPath.moveTo(outerPoints[0].x, outerPoints[0].y)
        for (i in 0 until 5) {
            val nextOuter = outerPoints[(i + 1) % 5]
            val inner = innerPoints[i]

            starPath.lineTo(inner.x, inner.y)
            starPath.lineTo(nextOuter.x, nextOuter.y)
        }
        starPath.close()

        val cornerPathEffect = CornerPathEffect(cornerRadius)
        paint.pathEffect = cornerPathEffect
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = (maxRating * (starSize + spacing) - spacing).toInt()
        val desiredHeight = starSize.toInt()

        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until maxRating) {
            val startX = i * (starSize + spacing)
            canvas.save()
            canvas.translate(startX, 0f)

            paint.color = unfilledColor
            canvas.drawPath(starPath, paint)

            if (i < currentRating.toInt() || (i == currentRating.toInt() && currentRating % 1 > 0)) {
                paint.color = filledColor
                paint.style = Paint.Style.FILL

                if (i == currentRating.toInt()) {
                    canvas.save()
                    canvas.clipRect(0f, 0f, starSize * (currentRating % 1), starSize)
                    canvas.drawPath(starPath, paint)
                    canvas.restore()
                } else {
                    canvas.drawPath(starPath, paint)
                }
            }
            canvas.restore()
        }
    }

    fun setRating(rating: Float) {
        currentRating = rating.coerceIn(0f, maxRating.toFloat())
        invalidate()
    }

    fun getRating(): Float = currentRating
}
