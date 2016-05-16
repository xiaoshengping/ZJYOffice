package com.example.zhongjiyun03.zhongjiyun.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.main.CommentPagerDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by ZHONGJIYUN03 on 2016/3/12.
 *
 * 服务商
 *
 */
public class CommentListAdapter extends AppBaseAdapter<CommentPagerDataBean> {
      private ViewHold viewHold;


    public CommentListAdapter(List<CommentPagerDataBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
          if (convertView==null){
              convertView= LayoutInflater.from(context).inflate(R.layout.comment_list_adapter_layout,null);
              viewHold=new ViewHold(convertView);
              convertView.setTag(viewHold);

          }else {

           viewHold= (ViewHold) convertView.getTag();

          }
        inti(position);
        return convertView;
    }

    private void inti(int position) {
        if (data.size()!=0){
            MyAppliction.imageLoader.displayImage(data.get(position).getHeadthumb(),viewHold.imageView,MyAppliction.RoundedOptions);
            viewHold.nameTextView.setText(data.get(position).getName());
            viewHold.timeText.setText(data.get(position).getDateTime());
            viewHold.commentContentText.setText(data.get(position).getContent());
            //viewHold.ratingBar.setRating(Float.parseFloat(data.get(position).getScore()));
            if (!TextUtils.isEmpty(data.get(position).getScore())){
                String StarRate=data.get(position).getScore();
                //viewHold.ratingBar.setRating(Integer.valueOf(StarRate));
                if (StarRate.equals("1")){
                    viewHold.ratingOne.setBackgroundResource(R.mipmap.eval_star_cur);
                    viewHold.ratingTwo.setBackgroundResource(R.mipmap.eval_star);
                    viewHold.ratingThree.setBackgroundResource(R.mipmap.eval_star);
                    viewHold.ratingFour.setBackgroundResource(R.mipmap.eval_star);
                    viewHold.ratingFive.setBackgroundResource(R.mipmap.eval_star);
                }else if (StarRate.equals("2")){

                    viewHold.ratingOne.setBackgroundResource(R.mipmap.eval_star_cur);
                    viewHold.ratingTwo.setBackgroundResource(R.mipmap.eval_star_cur);
                    viewHold.ratingThree.setBackgroundResource(R.mipmap.eval_star);
                    viewHold.ratingFour.setBackgroundResource(R.mipmap.eval_star);
                    viewHold.ratingFive.setBackgroundResource(R.mipmap.eval_star);

                }else if (StarRate.equals("3")){

                    viewHold.ratingOne.setBackgroundResource(R.mipmap.eval_star_cur);
                    viewHold.ratingTwo.setBackgroundResource(R.mipmap.eval_star_cur);
                    viewHold.ratingThree.setBackgroundResource(R.mipmap.eval_star_cur);
                    viewHold.ratingFour.setBackgroundResource(R.mipmap.eval_star);
                    viewHold.ratingFive.setBackgroundResource(R.mipmap.eval_star);

                }else if (StarRate.equals("4")){

                    viewHold.ratingOne.setBackgroundResource(R.mipmap.eval_star_cur);
                    viewHold.ratingTwo.setBackgroundResource(R.mipmap.eval_star_cur);
                    viewHold.ratingThree.setBackgroundResource(R.mipmap.eval_star_cur);
                    viewHold.ratingFour.setBackgroundResource(R.mipmap.eval_star_cur);
                    viewHold.ratingFive.setBackgroundResource(R.mipmap.eval_star);

                }else if (StarRate.equals("5")){
                    viewHold.ratingOne.setBackgroundResource(R.mipmap.eval_star_cur);
                    viewHold.ratingTwo.setBackgroundResource(R.mipmap.eval_star_cur);
                    viewHold.ratingThree.setBackgroundResource(R.mipmap.eval_star_cur);
                    viewHold.ratingFour.setBackgroundResource(R.mipmap.eval_star_cur);
                    viewHold.ratingFive.setBackgroundResource(R.mipmap.eval_star_cur);

                }
            }
        }
    }


    private class ViewHold {

        @ViewInject(R.id.image_view)
        private ImageView imageView;
        @ViewInject(R.id.name_text)
        private TextView nameTextView;
        @ViewInject(R.id.time_text)
        private TextView timeText;
        @ViewInject(R.id.comment_content_text)
        private TextView commentContentText;
        @ViewInject(R.id.rating_one)
        private TextView ratingOne;
        @ViewInject(R.id.rating_two)
        private TextView ratingTwo;
        @ViewInject(R.id.rating_three)
        private TextView ratingThree;
        @ViewInject(R.id.rating_four)
        private TextView ratingFour;
        @ViewInject(R.id.rating_five)
        private TextView ratingFive;
        /*@ViewInject(R.id.rating_bar)
        private RatingBar ratingBar;*/


        public ViewHold(View view) {
            ViewUtils.inject(this, view);


        }
    }
}
