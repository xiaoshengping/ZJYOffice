package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams
 */
public class HomeGridAdapter extends BaseAdapter {
	private Context mContext;

	String[] gridText={"找活儿","二手机","找机手","商城","部落","悬赏","黑名单","游戏","优惠券","配套服务"};
	int[] gridImage={R.mipmap.rig0,R.mipmap.rig1,R.mipmap.rig2,R.mipmap.rig3,R.mipmap.rig4,R.mipmap.rig5,R.mipmap.rig6,R.mipmap.rig7,R.mipmap.rig8,R.mipmap.rig9};

	public HomeGridAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return gridText.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.home_gridview_adapter_layout, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.home_grid_title);
		ImageView iv = BaseViewHolder.get(convertView, R.id.home_grid_image);
		iv.setBackgroundResource(gridImage[position]);

		tv.setText(gridText[position]);
		return convertView;
	}

}
