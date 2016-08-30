package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.home.SecondHandBean;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/4/11.
 */
public class RecommendMachinistAdapter extends AppBaseAdapter<SecondHandBean> {

     private  HolderView holderView;

    public RecommendMachinistAdapter(List<SecondHandBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.recommend_machinist_adapter,null);
            holderView=new HolderView(convertView);
            convertView.setTag(holderView);
        }else {

            holderView= (HolderView) convertView.getTag();
        }
        init(position);
        return convertView;
    }

    private void init(int position) {
        if (data!=null){

            if (!TextUtils.isEmpty(data.get(position).getManufacture())&&!TextUtils.isEmpty(data.get(position).getNoOfManufacture())){
                holderView.nameText.setText(data.get(position).getManufacture()+data.get(position).getNoOfManufacture());
            }
            if (!TextUtils.isEmpty(data.get(position).getDevicePhoto())){
                MyAppliction.imageLoader.displayImage(data.get(position).getDevicePhoto(),holderView.imageView,MyAppliction.options);
            }

            if (data.get(position).getPrice().equals("面议")){
                holderView.priceTextView.setText("面议");
            }else {
                holderView.priceTextView.setText(data.get(position).getPrice()+"万");
            }
            if (data.get(position).getSecondHandType()==0){
                holderView.dateTextVIew.setVisibility(View.VISIBLE);
                holderView.secondTageImage.setBackgroundResource(R.mipmap.lease_icon);
                holderView.dateTextVIew.setText(data.get(position).getTenancy()+"个月");
            }else if (data.get(position).getSecondHandType()==1){
                holderView.secondTageImage.setBackgroundResource(R.mipmap.sell_icon);
                holderView.dateTextVIew.setVisibility(View.GONE);

            }


        }




    }


    private class HolderView{

        @ViewInject(R.id.image_view)
        private ImageView imageView ;
        @ViewInject(R.id.date_text_view)
        private TextView dateTextVIew;
        @ViewInject(R.id.price_text_view)
        private TextView priceTextView;
        @ViewInject(R.id.name_text)
        private TextView nameText;
        @ViewInject(R.id.second_tage_image)
        private ImageView secondTageImage;

        public HolderView(View view) {
            ViewUtils.inject(this,view);


        }
    }
}
