package com.example.zhongjiyun03.zhongjiyun.widget;

/**
 * @Package com.qianfeng.zw.demo.widget
 * @作 用:
 * @创 建 人: zhangwei
 * @日 期: 15/4/7 14:04
 * @修 改 人:
 * @日 期:
 */

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * CustomDurationScroller
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-3-2
 */
public class CustomDurationScroller extends Scroller {

    private double scrollFactor = 1;

    public CustomDurationScroller(Context context) {
        super(context);
    }

    public CustomDurationScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    /**
     * not exist in android 2.3
     *
     * @param context
     * @param interpolator
     * @param flywheel
     */
    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        this.scrollFactor = scrollFactor;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, (int) (duration * scrollFactor));
    }
}