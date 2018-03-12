package com.poso2o.lechuan.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017-12-11.
 */

public class ConstraintLayout2 extends ConstraintLayout {
    public ConstraintLayout2(Context context) {
        super(context);
    }

    public ConstraintLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConstraintLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        View childView = getChildAt(0);
        if (childView != null) {
            childView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = childView.getHeight();
            if (h > height) {
                height = h;
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
