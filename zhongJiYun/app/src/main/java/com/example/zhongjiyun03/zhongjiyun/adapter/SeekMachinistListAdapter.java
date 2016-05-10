package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.SekkMachinisDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 */
public class SeekMachinistListAdapter extends AppBaseAdapter<SekkMachinisDataBean> {
      private ViewHold viewHold;


    public SeekMachinistListAdapter(List<SekkMachinisDataBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
          if (convertView==null){
              convertView= LayoutInflater.from(context).inflate(R.layout.seel_machinist_list_adapter,null);
              viewHold=new ViewHold(convertView);
              convertView.setTag(viewHold);

          }else {

           viewHold= (ViewHold) convertView.getTag();

          }
        inti(position);
        return convertView;
    }

    private void inti(int position) {
        if (data!=null){
            if (!TextUtils.isEmpty(data.get(position).getDriverName())){
                viewHold.tailtTextView.setText(data.get(position).getDriverName());
            }
            if (!TextUtils.isEmpty(data.get(position).getDistanceStr())){
                viewHold.addressText.setText(data.get(position).getDistanceStr()+"Km");
                viewHold.addressText.setVisibility(View.VISIBLE);
            }else {
                viewHold.addressText.setVisibility(View.GONE);

            }
            if (!TextUtils.isEmpty(data.get(position).getLastUpdateTimeSubStr())){
                viewHold.timeText.setText(data.get(position).getLastUpdateTimeSubStr()+"更新");
            }
            if (!TextUtils.isEmpty(data.get(position).getCity())){
                viewHold.companyAddressText.setText("期望工作地:"+data.get(position).getCity());
            }
            if (!TextUtils.isEmpty(data.get(position).getWage()+"")){
                viewHold.neirongText.setText(data.get(position).getWage()+"元");
            }
            if (!TextUtils.isEmpty(data.get(position).getWorkingAge())){
                viewHold.dataText.setText(data.get(position).getWorkingAge()+"年");
            }
            if (!TextUtils.isEmpty(data.get(position).getDriverHeader())){
                MyAppliction.imageLoader.displayImage(data.get(position).getDriverHeader(),viewHold.imageView,MyAppliction.RoundedOptionsOne);
            }
            if (data.get(position).getWorkInfoItemDtos()!=null&&data.get(position).getWorkInfoItemDtos().size()!=0){
                if (data.get(position).getWorkInfoItemDtos().size()==1){
                    if (!TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(0).getManufacture())&&
                            !TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(0).getNoOfManufacture())){
                        viewHold.imageLiginEnd.setVisibility(View.VISIBLE);
                        viewHold.imageLiginr.setVisibility(View.VISIBLE);
                        viewHold.textView.setVisibility(View.VISIBLE);
                        viewHold.textView.setText(data.get(position).getWorkInfoItemDtos().get(0).getManufacture()+
                                data.get(position).getWorkInfoItemDtos().get(0).getNoOfManufacture());
                    }else {
                        viewHold.imageLiginEnd.setVisibility(View.GONE);
                        viewHold.imageLiginr.setVisibility(View.GONE);
                        viewHold.textView.setVisibility(View.GONE);
                    }


                }else if (data.get(position).getWorkInfoItemDtos().size()==2){
                    viewHold.imageLiginEnd.setVisibility(View.VISIBLE);
                    viewHold.imageLiginr.setVisibility(View.VISIBLE);
                    viewHold.textView.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(0).getManufacture())&&
                            !TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(0).getNoOfManufacture())) {
                        viewHold.textView.setText(data.get(position).getWorkInfoItemDtos().get(0).getManufacture() +
                                data.get(position).getWorkInfoItemDtos().get(0).getNoOfManufacture());
                    }
                    viewHold.textViewOne.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(1).getManufacture())&&!TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(1).getNoOfManufacture())){
                        viewHold.textViewOne.setText(data.get(position).getWorkInfoItemDtos().get(1).getManufacture() +
                                data.get(position).getWorkInfoItemDtos().get(1).getNoOfManufacture());
                    }
                }else if (data.get(position).getWorkInfoItemDtos().size()==3){
                    viewHold.imageLiginEnd.setVisibility(View.VISIBLE);
                    viewHold.imageLiginr.setVisibility(View.VISIBLE);
                    viewHold.textView.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(0).getManufacture())&&
                            !TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(0).getNoOfManufacture())) {
                        viewHold.textView.setText(data.get(position).getWorkInfoItemDtos().get(0).getManufacture() +
                                data.get(position).getWorkInfoItemDtos().get(0).getNoOfManufacture());
                    }
                    viewHold.textViewOne.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(1).getManufacture())&&!TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(1).getNoOfManufacture())){
                        viewHold.textViewOne.setText(data.get(position).getWorkInfoItemDtos().get(1).getManufacture() +
                                data.get(position).getWorkInfoItemDtos().get(1).getNoOfManufacture());
                    }


                }else if (data.get(position).getWorkInfoItemDtos().size()==4){
                    viewHold.imageLiginEnd.setVisibility(View.VISIBLE);
                    viewHold.imageLiginr.setVisibility(View.VISIBLE);
                    viewHold.textView.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(0).getManufacture())&&
                            !TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(0).getNoOfManufacture())) {
                        viewHold.textView.setText(data.get(position).getWorkInfoItemDtos().get(0).getManufacture() +
                                data.get(position).getWorkInfoItemDtos().get(0).getNoOfManufacture());
                    }
                    viewHold.textViewOne.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(1).getManufacture())&&!TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(1).getNoOfManufacture())){
                        viewHold.textViewOne.setText(data.get(position).getWorkInfoItemDtos().get(1).getManufacture() +
                                data.get(position).getWorkInfoItemDtos().get(1).getNoOfManufacture());
                    }


                }


            }else {
                viewHold.textView.setVisibility(View.GONE);
                viewHold.imageLiginEnd.setVisibility(View.GONE);
                viewHold.textViewOne.setVisibility(View.GONE);
                viewHold.imageLiginr.setVisibility(View.GONE);
            }





            /*if (data.get(position).getWorkInfoItemDtos()!=null&&data.get(position).getWorkInfoItemDtos().size()!=0){
                viewHold.imageLiginEnd.setVisibility(View.VISIBLE);
                viewHold.imageLiginr.setVisibility(View.VISIBLE);
                if (data.get(position).getWorkInfoItemDtos().size()==1){

                    if (!TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(0).getManufacture())&&
                            !TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(0).getNoOfManufacture())){

                    }else {

                    }


                }else if (data.get(position).getWorkInfoItemDtos().size()==2){



                    else {
                        viewHold.textView.setVisibility(View.GONE);
                        viewHold.textViewOne.setVisibility(View.GONE);
                    }

                   }else if (data.get(position).getWorkInfoItemDtos().size()==3){
                    viewHold.imageLiginEnd.setVisibility(View.VISIBLE);
                    viewHold.imageLiginr.setVisibility(View.VISIBLE);
                    viewHold.textView.setVisibility(View.VISIBLE);
                    viewHold.textView.setText(data.get(position).getWorkInfoItemDtos().get(0).getManufacture()+
                            data.get(position).getWorkInfoItemDtos().get(0).getNoOfManufacture());
                    viewHold.textViewOne.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(1).getManufacture())&&!TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(1).getNoOfManufacture())){
                        viewHold.textViewOne.setText(data.get(position).getWorkInfoItemDtos().get(1).getManufacture() +
                                data.get(position).getWorkInfoItemDtos().get(1).getNoOfManufacture());
                    }else {
                        viewHold.textView.setVisibility(View.GONE);
                        viewHold.textViewOne.setVisibility(View.GONE);
                    }
                    }else if (data.get(position).getWorkInfoItemDtos().size()==4){
                    viewHold.imageLiginEnd.setVisibility(View.VISIBLE);
                    viewHold.imageLiginr.setVisibility(View.VISIBLE);
                    viewHold.textView.setVisibility(View.VISIBLE);
                    viewHold.textView.setText(data.get(position).getWorkInfoItemDtos().get(0).getManufacture()+
                            data.get(position).getWorkInfoItemDtos().get(0).getNoOfManufacture());
                    viewHold.textViewOne.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(1).getManufacture())&&!TextUtils.isEmpty(data.get(position).getWorkInfoItemDtos().get(1).getNoOfManufacture())){
                        viewHold.textViewOne.setText(data.get(position).getWorkInfoItemDtos().get(1).getManufacture() +
                                data.get(position).getWorkInfoItemDtos().get(1).getNoOfManufacture());
                    }else {
                        viewHold.textView.setVisibility(View.GONE);
                        viewHold.textViewOne.setVisibility(View.GONE);
                    }
                }

            }else {

            }*/

        }



    }


    private class ViewHold {

        @ViewInject(R.id.tailt_text_view)
        private TextView tailtTextView;
        @ViewInject(R.id.address_text_view)
        private  TextView addressText;
        @ViewInject(R.id.time_text_view)
        private TextView timeText;
        @ViewInject(R.id.company_text_view)
        private TextView companyAddressText;
        @ViewInject(R.id.neirong_text_view)
        private TextView neirongText;
        @ViewInject(R.id.data_text)
        private TextView dataText;
        @ViewInject(R.id.image_view)
        private ImageView imageView;
        @ViewInject(R.id.text_view)
        private TextView textView;
        @ViewInject(R.id.text_view_1)
        private TextView textViewOne;
        @ViewInject(R.id.image_ligin_end)
        private ImageView imageLiginEnd;
        @ViewInject(R.id.image_liginr)
        private ImageView imageLiginr;
        /*@ViewInject(R.id.text_view_2)
        private TextView textViewTwo;*/

        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
