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
public class MoreGridAdapter extends BaseAdapter {
	private Context mContext;

	String[] gridText={"每周爆款","简单购","云币夺宝","云币兑换","合作伙伴"};
	int[] gridImage={R.mipmap.more_hot_icon,R.mipmap.more_easy_buy,R.mipmap.more_yb_duobao,R.mipmap.more_yb_change,R.mipmap.more_partner_icon};

	public MoreGridAdapter(Context mContext) {
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
					R.layout.more_adapter_layout, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.home_grid_title);
		ImageView iv = BaseViewHolder.get(convertView, R.id.home_grid_image);
		iv.setBackgroundResource(gridImage[position]);

		tv.setText(gridText[position]);
		return convertView;
	}

}
