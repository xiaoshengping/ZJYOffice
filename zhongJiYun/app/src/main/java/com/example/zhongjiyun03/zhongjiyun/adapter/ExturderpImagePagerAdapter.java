package com.example.zhongjiyun03.zhongjiyun.adapter;

/**
 * @Package com.qianfeng.zw.demo.adpter
 * @作 用:
 * @创 建 人: zhangwei
 * @日 期: 15/4/7 14:15
 * @修 改 人:
 * @日 期:
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.home.DeviceImagesBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.widget.RecyclingPagerAdapter;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * ImagePagerAdapter
 */
public class ExturderpImagePagerAdapter extends RecyclingPagerAdapter implements ViewPager.OnPageChangeListener {

    private Context context;
    private List<DeviceImagesBean> iamgeUrls;
    private boolean isInfiniteLoop;
    private BitmapUtils bitmapUtils;
    //圆点的父布局
    private LinearLayout dotLL;

    public ExturderpImagePagerAdapter(Context context, List<DeviceImagesBean> iamgeUrls, LinearLayout dotLL) {
        this.context = context;
        this.iamgeUrls = iamgeUrls;
        isInfiniteLoop = false;
        this.dotLL = dotLL;
        bitmapUtils = new BitmapUtils(context);

    }

    @Override
    public int getCount() {
        return isInfiniteLoop ? Integer.MAX_VALUE : iamgeUrls.size();
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % iamgeUrls.size() : position;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = new ImageView(context);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        bitmapUtils.display(holder.imageView, AppUtilsUrl.BaseUrl+iamgeUrls.get(getPosition(position)).getUrl());
        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < iamgeUrls.size(); i++) {
            if (getPosition(position) == i) {
                ((ImageView) dotLL.getChildAt(i)).setImageResource(R.mipmap.dot_press);
            } else {
                ((ImageView) dotLL.getChildAt(i)).setImageResource(R.mipmap.dot_nomal);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private static class ViewHolder {
        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ExturderpImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

    public void refreshData(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        this.notifyDataSetChanged();
        addDotView();
    }

    /**
     * 动态添加小圆点
     */
    private void addDotView() {
        for (int i = 0; i < iamgeUrls.size(); i++) {
            ImageView dotView = new ImageView(context);
            dotView.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            param.rightMargin = 5;
            dotView.setLayoutParams(param);
            if (i == 0) {
                dotView.setImageResource(R.mipmap.dot_press);
            } else {
                dotView.setImageResource(R.mipmap.dot_nomal);
            }
            dotLL.addView(dotView);
        }
    }

}
