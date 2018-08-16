package com.wx.app.wxapp.widget.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.wx.app.wxapp.R;
import com.wx.app.wxapp.utils.DensityUtils;

/**
 * 描述：
 * 动态高斯模糊背景
 * @author wx
 * @date 2018/8/14/014
 */
public class BlurringView extends View {

  public BlurringView(Context context) {
    this(context, null);
  }

  public BlurringView(Context context, AttributeSet attrs) {
    super(context, attrs);

    final Resources res = getResources();
    final int defaultBlurRadius = 11;
    final int defaultDownsampleFactor = 6;
    final int defaultOverlayColor = Color.parseColor("#50FFFFFF");

    initializeRenderScript(context);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PxBlurringView);
    setBlurRadius(a.getInt(R.styleable.PxBlurringView_blurRadius, defaultBlurRadius));
    setDownsampleFactor(a.getInt(R.styleable.PxBlurringView_downsampleFactor,
            defaultDownsampleFactor));
    setOverlayColor(a.getColor(R.styleable.PxBlurringView_overlayColor, defaultOverlayColor));
    a.recycle();
  }

  private int mVagueHeight;

  /**
   * @param blurredView 屏幕跟布局
   * @param vagueHeight 距离底部的高度 dp  -1 全屏
   */
  public void setBlurredView(View blurredView, int vagueHeight) {
    mBlurredView = blurredView;
    mVagueHeight = vagueHeight;
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (mBlurredView != null) {
      if (prepare()) {
        // If the background of the blurred view is a color drawable, we use it to clear
        // the blurring canvas, which ensures that edges of the child views are blurred
        // as well; otherwise we clear the blurring canvas with a transparent color.
        if (mBlurredView.getBackground() != null && mBlurredView.getBackground() instanceof ColorDrawable) {
          mBitmapToBlur.eraseColor(((ColorDrawable) mBlurredView.getBackground()).getColor());
        } else {
          mBitmapToBlur.eraseColor(Color.TRANSPARENT);
        }

        mBlurredView.draw(mBlurringCanvas);
        if (mVagueHeight != -1) {
          blur(DensityUtils.dp2px(this.getContext(), mVagueHeight));
        } else
          blur(mVagueHeight);

        canvas.save();
        canvas.translate(mBlurredView.getX() - getX(), mBlurredView.getY() - getY());
        canvas.scale(mDownsampleFactor, mDownsampleFactor);
        canvas.drawBitmap(mBlurredBitmap, 0, 0, null);
        canvas.restore();
      }
      canvas.drawColor(mOverlayColor);
    }
  }

  public void setBlurRadius(int radius) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      mBlurScript.setRadius(radius);
    }
  }

  public void setDownsampleFactor(int factor) {
    if (factor <= 0) {
      throw new IllegalArgumentException("Downsample factor must be greater than 0.");
    }

    if (mDownsampleFactor != factor) {
      mDownsampleFactor = factor;
      mDownsampleFactorChanged = true;
    }
  }

  public void setOverlayColor(int color) {
    mOverlayColor = color;
  }

  private void initializeRenderScript(Context context) {
    mRenderScript = RenderScript.create(context);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      mBlurScript = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));
    }
  }

  protected boolean prepare() {
    final int width = mBlurredView.getWidth();
    final int height = mBlurredView.getHeight();

    if (mBlurringCanvas == null || mDownsampleFactorChanged
            || mBlurredViewWidth != width || mBlurredViewHeight != height) {
      mDownsampleFactorChanged = false;

      mBlurredViewWidth = width;
      mBlurredViewHeight = height;

      int scaledWidth = width / mDownsampleFactor;
      int scaledHeight = height / mDownsampleFactor;

      // The following manipulation is to avoid some RenderScript artifacts at the edge.
      scaledWidth = scaledWidth - scaledWidth % 4 + 4;
      scaledHeight = scaledHeight - scaledHeight % 4 + 4;

      if (mBlurredBitmap == null
              || mBlurredBitmap.getWidth() != scaledWidth
              || mBlurredBitmap.getHeight() != scaledHeight) {
        mBitmapToBlur = Bitmap.createBitmap(scaledWidth, scaledHeight,
                Bitmap.Config.ARGB_8888);
        if (mBitmapToBlur == null) {
          return false;
        }
        mBlurredBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight,
                Bitmap.Config.ARGB_8888);

        if (mBlurredBitmap == null) {
          return false;
        }
      }

      mBlurringCanvas = new Canvas(mBitmapToBlur);
      mBlurringCanvas.scale(1f / mDownsampleFactor, 1f / mDownsampleFactor);
      mBlurInput = Allocation.createFromBitmap(mRenderScript, mBitmapToBlur,
              Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
      mBlurOutput = Allocation.createTyped(mRenderScript, mBlurInput.getType());
    }
    return true;
  }

  /**
   * 按长方形裁切图片
   *
   * @param bitmap
   * @param imgHeight 剪裁后的高度
   * @return
   */
  private Bitmap imageCropWithRect(Bitmap bitmap, int imgHeight) {
    if (bitmap == null) {
      return null;
    }
    int w = bitmap.getWidth(); // 得到图片的宽，高
    int h = bitmap.getHeight();

        /*int nw, nh, retX, retY;
        if (w > h) {
            nw = h / 2;
            nh = h;
            retX = (w - nw) / 2;
            retY = 0;
        } else {
            nw = w / 2;
            nh = w;
            retX = w / 4;
            retY = (h - w) / 2;
        }*/

    // 下面这句是关键
    Bitmap bmp = Bitmap.createBitmap(bitmap, 0, h - imgHeight, w, imgHeight, null, false);
    if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
      bitmap.recycle();
      bitmap = null;
    }
    return bmp;// Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
    // false);
  }

  /**
   * android.renderscript.RSIllegalArgumentException: Cannot update allocation from bitmap, sizes mismatch
   *
   * @param h
   */
  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
  protected void blur(int h) {
    try {
      mBlurInput.copyFrom(mBitmapToBlur);
      mBlurScript.setInput(mBlurInput);
      mBlurScript.forEach(mBlurOutput);
      mBlurOutput.copyTo(mBlurredBitmap);
      if (h != -1) {
        mBlurredBitmap = imageCropWithRect(mBlurredBitmap, h);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (mRenderScript != null) {
      mRenderScript.destroy();
    }
  }

  private int mDownsampleFactor;
  private int mOverlayColor;

  private View mBlurredView;
  private int mBlurredViewWidth, mBlurredViewHeight;

  private boolean mDownsampleFactorChanged;
  private Bitmap mBitmapToBlur, mBlurredBitmap;
  private Canvas mBlurringCanvas;
  private RenderScript mRenderScript;
  private ScriptIntrinsicBlur mBlurScript;
  private Allocation mBlurInput, mBlurOutput;
}
