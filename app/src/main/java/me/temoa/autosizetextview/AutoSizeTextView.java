package me.temoa.autosizetextview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by lai
 * on 2018/3/26.
 */

public class AutoSizeTextView extends AppCompatTextView {

    private int mWidth;
    private int mHeight;
    private float mMinTextSize;
    private float mMaxTextSize;

    private boolean isFirstTime = true;

    public AutoSizeTextView(Context context) {
        super(context);
        init(null);
    }

    public AutoSizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AutoSizeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setMaxTextSize(float maxTextSize) {
        mMaxTextSize = maxTextSize;
    }

    public void setMinTextSize(float minTextSize) {
        mMinTextSize = minTextSize;
    }

    public void setNewText(String newText) {
        if (newText == null) return;
        float calculateSize = calculateTextSize(newText);
        if (calculateSize >= -1) {
            this.setTextSize(0, calculateSize);
        }
        this.setText(newText);
    }

    private void init(AttributeSet attrs) {
        mMinTextSize = sp2px(12); // default min text size
        mMaxTextSize = sp2px(18); // default max text size
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.AutoSizeTextView);
            mMinTextSize = ta.getDimensionPixelSize(R.styleable.AutoSizeTextView_atv_minTextSize, sp2px(12));
            mMaxTextSize = ta.getDimensionPixelSize(R.styleable.AutoSizeTextView_atv_maxTextSize, sp2px(18));
            Log.d("Test", mMinTextSize + "");
            Log.d("Test", mMaxTextSize + "");
            ta.recycle();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w - getCompoundPaddingLeft() - getCompoundPaddingRight();
        mHeight = h - getCompoundPaddingTop() - getCompoundPaddingBottom();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isFirstTime) {
            float calculateSize = calculateTextSize(this.getText().toString());
            if (calculateSize != -1) {
                this.setTextSize(0, calculateSize);
            }
            isFirstTime = false;
        }
        super.onDraw(canvas);
    }

    /**
     * 根据宽高和字数计算字体的大小
     *
     * @param text new text
     * @return float text size
     */
    private float calculateTextSize(String text) {
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(getTextSize());
        StaticLayout staticLayout = new StaticLayout(text, textPaint, mWidth, Layout.Alignment.ALIGN_CENTER, 1, 0, false);
        int h = staticLayout.getHeight();
        float textSize = staticLayout.getPaint().getTextSize();
        if (h < mHeight) {
            while (h < mHeight && textSize < mMaxTextSize) {
                textPaint.setTextSize((textPaint.getTextSize() + 0.5F));
                staticLayout = new StaticLayout(text, textPaint, mWidth, Layout.Alignment.ALIGN_CENTER, 1, 0, false);
                h = staticLayout.getHeight();
                textSize = staticLayout.getPaint().getTextSize();
            }
        } else {
            while (h > mHeight && textSize > mMinTextSize) {
                textPaint.setTextSize((textPaint.getTextSize() - 0.5F));
                staticLayout = new StaticLayout(text, textPaint, mWidth, Layout.Alignment.ALIGN_CENTER, 1, 0, false);
                h = staticLayout.getHeight();
                textSize = staticLayout.getPaint().getTextSize();
            }
        }
        if (h > mHeight) {
            return -1;
        }
        return textSize;
    }

    public int sp2px(float value) {
        float scale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (value * scale + 0.5F);
    }
}
