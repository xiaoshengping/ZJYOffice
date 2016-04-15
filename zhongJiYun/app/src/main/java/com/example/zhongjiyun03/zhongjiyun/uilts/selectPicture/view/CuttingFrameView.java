package com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.utils.BitmapUtils;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.utils.ScreenUtils;


/**
 * Created by yinwei on 2015-11-12.
 */
public class CuttingFrameView extends View {


    private int width,height,titleHeight,eage,top;


    public CuttingFrameView(Context context) {
        this(context, null);


    }

    public CuttingFrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray=context.obtainStyledAttributes
                (attrs, R.styleable.CuttingFrameView,defStyleAttr,0);

        //获取到标题的高度
        titleHeight=typedArray.getDimensionPixelSize(R.styleable.CuttingFrameView_headerHeight,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 59, getResources().getDisplayMetrics()));


    }

    public CuttingFrameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    public void draw(Canvas canvas) {

        drawShape(canvas);

        super.draw(canvas);

    }

    private void drawShape(Canvas canvas) {


        width=getWidth();
        height=getHeight();

        //矩形的画笔
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.Semi_transparent));
        paint.setAntiAlias(true);

        //裁剪框白线的画笔
        Paint lPaint = new Paint();
        lPaint.setStyle(Paint.Style.FILL);
        lPaint.setColor(Color.WHITE);
        lPaint.setStrokeWidth(1);
        lPaint.setAntiAlias(true);


        eage = width * 4 / 5;//设定裁剪框的变成是屏幕宽度的五分之四

        top = (height - eage) / 2;//绘制的正方形的顶部距离view顶端的距离

        int bottom = eage + top;//绘制的正方形的底部距离view顶端的距离


        canvas.drawRect(0, 0, width, top, paint);//上矩形
        canvas.drawRect(0, top, width / 10, bottom, paint);//左矩形
        canvas.drawRect(width * 9 / 10, top, width, bottom, paint);//右矩形
        canvas.drawRect(0, bottom, width, height, paint);//下矩形


        canvas.drawLine(width / 10, top, width * 9 / 10, top, lPaint);//上白线
        canvas.drawLine(width / 10, top, width / 10, bottom, lPaint);//左白线
        canvas.drawLine(width * 9 / 10, top, width * 9 / 10, bottom, lPaint);//右白线
        canvas.drawLine(width / 10, bottom, width * 9 / 10, bottom, lPaint);//下白线




    }


    /**
     * 获取裁剪图片
     *
     * @param activity
     * @return
     */
    public  Bitmap takeScreenShot(Activity activity) {

        //去掉标题栏,actionbar 的高度
        Bitmap b = Bitmap.createBitmap(ScreenUtils.snapShotWithStatusBar(activity), 0,
                ScreenUtils.getScreenHeight(activity) - height, width,
                height);


        //偏移一个像素，避免截取白线
        b = Bitmap.createBitmap(b, width / 10 + 1, top + 1, eage - 2, eage - 2);



        return BitmapUtils.comp(b);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
