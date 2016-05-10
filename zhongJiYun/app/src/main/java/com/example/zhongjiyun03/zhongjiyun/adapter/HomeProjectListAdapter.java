package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.seekProject.SeekProjectBean;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 */
public class HomeProjectListAdapter extends AppBaseAdapter<SeekProjectBean> {
      private ViewHold viewHold;


    public HomeProjectListAdapter(List<SeekProjectBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
          if (convertView==null){
              convertView= LayoutInflater.from(context).inflate(R.layout.project_list_layout,null);
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
            if (!TextUtils.isEmpty(data.get(position).getTitle())){
                viewHold.tailtText.setText(data.get(position).getTitle());
            }

            if (!TextUtils.isEmpty(data.get(position).getProjectCompany())){
                viewHold.companyName.setText(data.get(position).getProjectCompany());
            }

            if (!TextUtils.isEmpty(data.get(position).getProvince())&&!TextUtils.isEmpty(data.get(position).getCity())){
                viewHold.addressText.setText(data.get(position).getProvince()+data.get(position).getCity());
            }
            if (!TextUtils.isEmpty(data.get(position).getCreateDateStr())){
                viewHold.timeText.setText(data.get(position).getCreateDateStr());
            }
            if (!TextUtils.isEmpty(data.get(position).getStatusStr())){
                viewHold.bidsTageText.setText(data.get(position).getStatusStr());
            }
            SQLhelper sqLhelper=new SQLhelper(context);
            SQLiteDatabase db= sqLhelper.getWritableDatabase();
            Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
            String uid=null;  //用户id
            while (cursor.moveToNext()) {
                uid=cursor.getString(0);
            }
            if (!TextUtils.isEmpty(uid)){
                if (data.get(position).getReplyStatus()>0){
                    viewHold.successImage.setVisibility(View.VISIBLE);
                    //MyAppliction.imageLoader.displayImage(data.get(position).getFlag(),viewHold.successImage,MyAppliction.options);
                }else {
                    viewHold.successImage.setVisibility(View.GONE);
                }
            }else {
                viewHold.successImage.setVisibility(View.GONE);
            }




        }




    }


    private class ViewHold {

        @ViewInject(R.id.company_name)
        private TextView companyName;
        @ViewInject(R.id.tailt_text)
        private TextView tailtText;
        @ViewInject(R.id.address_text_view)
        private TextView addressText;
        @ViewInject(R.id.time_text)
        private TextView timeText;
        @ViewInject(R.id.bids_tage_text)
        private TextView bidsTageText;
        @ViewInject(R.id.success_image)
        private ImageView successImage;

        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
