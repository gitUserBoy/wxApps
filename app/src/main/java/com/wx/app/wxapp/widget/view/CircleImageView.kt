package com.wx.app.wxapp.widget.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v7.widget.AppCompatImageView
import android.text.TextPaint
import android.util.AttributeSet
import android.widget.ImageView
import com.wx.app.wxapp.R

/**
 * 描述：
 *
 * @author wx
 * @date 2018/7/20/020
 */
class CircleImageView : AppCompatImageView {

    private val mDrawableRect = RectF()
    private val mBorderRect = RectF()

    private val mShaderMatrix = Matrix()
    private val mBitmapPaint = Paint()
    private val mBorderPaint = Paint()

    private var mBorderColor = DEFAULT_BORDER_COLOR
    private var mBorderWidth = DEFAULT_BORDER_WIDTH

    private var mBitmap: Bitmap? = null
    private var mBitmapShader: BitmapShader? = null
    private var mBitmapWidth: Int = 0
    private var mBitmapHeight: Int = 0

    private var mDrawableRadius: Float = 0.toFloat()
    private var mBorderRadius: Float = 0.toFloat()

    private var mReady: Boolean = false
    private var mSetupPending: Boolean = false
    private val mFlagBackgroundPaint = Paint()
    private val mFlagTextPaint = TextPaint()
    private var mFlagText: String? = null
    private var mShowFlag = false
    private val mFlagTextBounds = Rect()

    internal var mSweepGradient: Shader? = null

    var borderColor: Int
        get() = mBorderColor
        set(borderColor) {
            if (borderColor == mBorderColor) {
                return
            }

            mBorderColor = borderColor
            mBorderPaint.color = mBorderColor
            invalidate()
        }

    /**
     * @param borderWidth
     * 圆形的边框厚度。
     */
    var borderWidth: Int
        get() = mBorderWidth
        set(borderWidth) {
            if (borderWidth == mBorderWidth) {
                return
            }

            mBorderWidth = borderWidth
            setup()
        }

    constructor(context: Context) : super(context) {

        init()
    }

    @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0) : super(context, attrs, defStyle) {

        val a = context.obtainStyledAttributes(attrs,
                R.styleable.CircleImageView, defStyle, 0)

        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_border_width,
                DEFAULT_BORDER_WIDTH)
        mBorderColor = a.getColor(R.styleable.CircleImageView_border_color,
                DEFAULT_BORDER_COLOR)

        a.recycle()

        init()
    }

    private fun init() {
        super.setScaleType(SCALE_TYPE)
        mReady = true

        if (mSetupPending) {
            setup()
            mSetupPending = false
        }
    }

    override fun getScaleType(): ImageView.ScaleType {
        return SCALE_TYPE
    }

    override fun setScaleType(scaleType: ImageView.ScaleType) {
        if (scaleType != SCALE_TYPE) {
            throw IllegalArgumentException(String.format(
                    "ScaleType %s not supported.", scaleType))
        }
    }

    override fun setAdjustViewBounds(adjustViewBounds: Boolean) {
        if (adjustViewBounds) {
            throw IllegalArgumentException(
                    "adjustViewBounds not supported.")
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable == null) {
            return
        }

        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), mDrawableRadius,
                mBitmapPaint)
        if (mBorderWidth != 0) {
            canvas.save()
            canvas.rotate(20f, (width / 2).toFloat(), (height / 2).toFloat())
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), mBorderRadius,
                    mBorderPaint)
            canvas.restore()
        }

        if (mShowFlag && mFlagText != null) {
            canvas.drawArc(mBorderRect, 40f, 100f, false, mFlagBackgroundPaint)
            mFlagTextPaint.getTextBounds(mFlagText, 0, mFlagText!!.length,
                    mFlagTextBounds)
            canvas.drawText(mFlagText!!, (width / 2).toFloat(),
                    ((3 + Math.cos((Math.PI * 5 / 18).toFloat().toDouble())) * height / 4 + mFlagTextBounds.height() / 3).toFloat(),
                    mFlagTextPaint)
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        mBitmap = bm
        setup()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        mBitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        mBitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        mBitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        try {
            val bitmap: Bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
                        COLORDRAWABLE_DIMENSION, BITMAP_CONFIG)
            } else {
                Bitmap.createBitmap(drawable.intrinsicWidth,
                        drawable.intrinsicHeight, BITMAP_CONFIG)
            }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        } catch (e: OutOfMemoryError) {
            return null
        }

    }

    private fun setup() {
        if (!mReady) {
            mSetupPending = true
            return
        }

        if (mBitmap == null) {
            return
        }

        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP)

        mBitmapPaint.isAntiAlias = true
        mBitmapPaint.shader = mBitmapShader

        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = mBorderColor
        mBorderPaint.strokeWidth = mBorderWidth.toFloat()

        mBitmapHeight = mBitmap!!.height
        mBitmapWidth = mBitmap!!.width

        mBorderRect.set(0f, 0f, width.toFloat(), height.toFloat())
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2,
                (mBorderRect.width() - mBorderWidth) / 2)

        mDrawableRect.set(mBorderWidth.toFloat(), mBorderWidth.toFloat(), mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth)
        mDrawableRadius = Math.min(mDrawableRect.height() / 2,
                mDrawableRect.width() / 2)

        mFlagBackgroundPaint.color = Color.BLACK and 0x66FFFFFF
        mFlagBackgroundPaint.flags = TextPaint.ANTI_ALIAS_FLAG

        mFlagTextPaint.flags = TextPaint.ANTI_ALIAS_FLAG
        mFlagTextPaint.textAlign = Paint.Align.CENTER
        mFlagTextPaint.color = Color.WHITE
        mFlagTextPaint.textSize = resources.displayMetrics.density * 18

        //    mSweepGradient = new SweepGradient(getWidth() / 2, getHeight() / 2,
        //            new int[] { Color.rgb(255, 255, 255), Color.rgb(1, 209, 255) },
        //            null);
        //
        //    mBorderPaint.setShader(mSweepGradient);//设置外边框渐变色
        //
        //    updateShaderMatrix();
        invalidate()
    }

    private fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f

        mShaderMatrix.set(null)

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / mBitmapHeight.toFloat()
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f
        } else {
            scale = mDrawableRect.width() / mBitmapWidth.toFloat()
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f
        }

        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate(((dx + 0.5f).toInt() + mBorderWidth).toFloat(),
                ((dy + 0.5f).toInt() + mBorderWidth).toFloat())

        mBitmapShader!!.setLocalMatrix(mShaderMatrix)
    }

    fun setShowFlag(show: Boolean) {
        mShowFlag = show
        invalidate()
    }

    fun setFlagText(text: String) {
        mFlagText = text
        invalidate()
    }

    companion object {

        private val SCALE_TYPE = ImageView.ScaleType.CENTER_CROP

        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private val COLORDRAWABLE_DIMENSION = 2

        // 圆形边框的厚度默认值。
        // 如果是0，则没有天蓝色渐变的边框。
        private val DEFAULT_BORDER_WIDTH = 0

        private val DEFAULT_BORDER_COLOR = Color.BLACK
    }
}