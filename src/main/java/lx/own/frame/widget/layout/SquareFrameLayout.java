package lx.own.frame.widget.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import lx.own.frame.R;


/**
 * <b></b>
 * Created on 2017/6/22.
 *
 * @author LeiXun
 */

public class SquareFrameLayout extends FrameLayout {
    private static final int KEEP_WIDTH = 1;
    private static final int KEEP_HEIGHT = 2;

    private boolean keepWidth = true;

    public SquareFrameLayout(Context context) {
        this(context, null);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

//    public SquareFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (keepWidth) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        }
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SquareLayout);
            if (typedArray != null) {
                int keepEdge = typedArray.getInt(R.styleable.SquareLayout_keepEdge, KEEP_WIDTH);
                keepWidth = keepEdge == KEEP_WIDTH;
                typedArray.recycle();
            }
        }
    }
}
