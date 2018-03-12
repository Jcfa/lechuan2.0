package com.poso2o.lechuan.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.poso2o.lechuan.util.ArithmeticUtils;
import com.poso2o.lechuan.util.NumberFormatUtils;

/**
 * Created by mr zhang on 2017/8/23.
 * 进度标尺
 */

public class MyProgressBar extends View {

    //屏幕宽高
    private int sWidth;
    private int sHeight;

    //左边距
    private float marginLeft = 50;
    //右边距
    private float marginRight = 50;
    //标尺的高
    private float mRulerHeitght = 100;
    //标尺的宽
    private float mRulerWidth = 500;
    //标度的距离
    private float mValueWith = 280;
    //上下边距，默认居中显示
    private float marginTop;
    //字体大小
    private int mTextSize = 24;
    //保本颜色
    private int costColor = 0xFFFFA54F;
    //保本长度
    private float costWidth = 100;
    //达标颜色
    private int smileColor = 0xFF3CB371;
    //标度颜色
    private int valueColor = 0x55000000;
    //保本
    private double mCost = 0;
    //目标
    private double mTarget = 1;
    //完成
    private double mAchieve = 1;
    //完成率
    private double mRate = 100.00;

    //保本矩形画笔
    private Paint mCostPaint;
    //达标范围矩形画笔
    private Paint mAchievePaint;
    //完成标度画笔
    private Paint mFinishPaint;
    //最外层边框画笔
    private Paint mOutsitPaint;
    //文字画笔
    private Paint mTextPaint;

    public MyProgressBar(Context context) {
        super(context);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        computerInit();
        drawOutside(canvas);
        drawCostRect(canvas);
        drawAchieveRect(canvas);
        drawFinishRect(canvas);
    }

    //画保本矩形
    private void drawCostRect(Canvas canvas){
        mCostPaint = new Paint();
        mCostPaint.setColor(costColor);
        canvas.drawRect(computeWidth(marginLeft),computeHeigh(marginTop),computeWidth(marginLeft + costWidth),computeHeigh(marginTop + mRulerHeitght),mCostPaint);
        float x = computeWidth(costWidth - 16);
        float y = computeHeigh(marginTop - 20);
        if (x < computeWidth(marginLeft))x = computeWidth(40);
        canvas.drawText("保本：" + NumberFormatUtils.format(mCost),x,y,mTextPaint);
    }

    //画达标范围矩形
    private void drawAchieveRect(Canvas canvas){
        mAchievePaint = new Paint();
        mAchievePaint.setColor(smileColor);
        canvas.drawRect(computeWidth(marginLeft + costWidth),computeHeigh(marginTop),computeWidth(marginLeft + mRulerWidth),computeHeigh(marginTop + mRulerHeitght),mAchievePaint);
    }

    //画标尺刻度矩形
    private void drawFinishRect(Canvas canvas){
        mFinishPaint = new Paint();
        mFinishPaint.setColor(valueColor);
        canvas.drawRect(computeWidth(marginLeft + mValueWith - 20),computeHeigh(marginTop - 10),computeWidth(marginLeft + mValueWith),computeHeigh(marginTop + mRulerHeitght + 10),mFinishPaint);
        float x = computeWidth(mValueWith - 10);
        if (x < computeWidth(marginLeft))x = computeWidth(40);
        canvas.drawText(NumberFormatUtils.format(mRate) + "%",x,computeHeigh(marginTop + mRulerHeitght + 40),mTextPaint);
    }

    //画最外面的包围框
    private void drawOutside(Canvas canvas){
        mOutsitPaint = new Paint();
        mOutsitPaint.setColor(valueColor);
        canvas.drawRect(computeWidth(marginLeft - 3),computeHeigh(marginTop - 3),computeWidth(marginLeft + mRulerWidth +3),computeHeigh(marginTop + mRulerHeitght + 3),mOutsitPaint);
    }

    //设置各项数据
    public void setValue(double cost,double target,double achieve,double rate){
        mCost = cost;
        mTarget = target;
        mAchieve = achieve;
        mRate = rate;
        if (mTarget == 0){
            mTarget = 1;
        }
    }

    //设置标尺宽高
    public void setSpec(int height){
        mRulerHeitght = height;
    }

    //设置颜色
    public void setRulerColor(int costColor ,int smileColor,int valueColor){
        this.costColor = costColor;
        this.smileColor = smileColor;
        this.valueColor = valueColor;
    }

    //计算保本长度和完成长度
    private void computerInit(){
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        sWidth = wm.getDefaultDisplay().getWidth();
        sHeight = wm.getDefaultDisplay().getHeight();

        mTextPaint = new Paint();
        mTextPaint.setColor(0xFF323232);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setStrokeWidth(3);
        mTextPaint.setAntiAlias(true);

        mRulerWidth = this.getWidth() - computeWidth(marginLeft) - computeWidth(marginRight);
        marginTop = (this.getHeight() - computeHeigh(mRulerHeitght))/2;

        double tempWith = ArithmeticUtils.mul(ArithmeticUtils.div(mCost,mTarget),mRulerWidth);
        String strWith = NumberFormatUtils.format(tempWith);
        costWidth = Float.parseFloat(strWith);

        double achieveWith = ArithmeticUtils.mul(ArithmeticUtils.div(mAchieve,mTarget),mRulerWidth);
        if (achieveWith > mRulerWidth)achieveWith = mRulerWidth;
        String achieveStr = NumberFormatUtils.format(achieveWith);
        mValueWith = Float.parseFloat(achieveStr);

    }

    //根据当前屏幕计算传进来的参数在该屏幕大小下的对应数值
    private float computeWidth(float value){
        return sWidth*value/720;
    }
    private float computeHeigh(float value){
        return sHeight*value/1280;
    }
}
