package com.example.dogoodsoft_app.timelog.defineview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.dogoodsoft_app.timelog.R;


/**
 * Created by AZ002 on 2017/8/8 11:52.
 */

public class PercentRelativeLayoyut extends RelativeLayout {
    public PercentRelativeLayoyut(Context context) {
        super(context);
    }

    public PercentRelativeLayoyut(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentRelativeLayoyut(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 重写测量方法
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //先拿到父控件的宽高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int count = this.getChildCount();
        for (int i = 0;i < count;i++){
            //循环迭代子控件
            View child = this.getChildAt(i);//取出每一个子控件
            ViewGroup.LayoutParams lp = child.getLayoutParams();
            float widthPercent = 0;
            float heightPercent = 0;
            if (lp instanceof  PercentRelativeLayoyut.LayoutParams){
                widthPercent = ((PercentRelativeLayoyut.LayoutParams) lp).widthPercent;
                heightPercent = ((PercentRelativeLayoyut.LayoutParams) lp).heightPercent;
            }

            if (widthPercent != 0){
                lp.width = (int) (width*widthPercent);
            }

            if (heightPercent != 0){
                lp.height = (int) (height * heightPercent);
            }

        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //重写对子控件布局的方法


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    //重写对子控件布局属性进行获取解析


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return super.generateLayoutParams(lp);
        return new LayoutParams(getContext(), attrs);
    }


    public static class LayoutParams extends RelativeLayout.LayoutParams{

        private float widthPercent;
        private float heightPercent;


        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.PercentRelativeLayoyut);
            widthPercent = a.getFloat(R.styleable.PercentRelativeLayoyut_layout_widthPercent,
                    widthPercent);
            heightPercent = a.getFloat(R.styleable.PercentRelativeLayoyut_layout_heightPercent,
                    heightPercent);
            a.recycle();


        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public LayoutParams(RelativeLayout.LayoutParams source) {
            super(source);
        }
    }
}
