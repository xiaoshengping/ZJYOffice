package com.example.zhongjiyun03.zhongjiyun.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.MoreGridAdapter;
import com.example.zhongjiyun03.zhongjiyun.uilts.CooperationActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.MoreConersionActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.MoreDuoBaoActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.MoreEasyBuyActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.MoreHotActivity;
import com.example.zhongjiyun03.zhongjiyun.view.MyGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment {



    @ViewInject(R.id.more_gridView)
    private MyGridView moreGridView;

    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seek_machinist, container, false);
        ViewUtils.inject(this, view);
        init(); //初始化



        return view;
    }

    private void init() {
        initView();

    }
    /*
    * 初始化view
    * */
    private void initView() {
        initGridView();

    }

    /**
     * 初始化gridView
     */
    private void initGridView() {
        MoreGridAdapter homeGridViewAdapter=new MoreGridAdapter(getActivity());
        //添加Item到网格中
        moreGridView.setAdapter(homeGridViewAdapter);
        //添加点击事件
        moreGridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                    {
                        int index=arg2+1;//id是从0开始的，所以需要+1
                        // Toast.makeText(getActivity(), "你按下了选项："+index, Toast.LENGTH_LONG).show();
                        //Toast用于向用户显示一些帮助/提示
                        /*SQLhelper sqLhelper=new SQLhelper(getActivity());
                        SQLiteDatabase db= sqLhelper.getWritableDatabase();
                        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
                        String uid=null;  //用户id
                        while (cursor.moveToNext()) {
                            uid=cursor.getString(0);

                        }*/

                        switch (index){
                            case 1:
                                Intent projectIntent=new Intent(getActivity(),MoreHotActivity.class);
                                startActivity(projectIntent);
                                break;

                            case 2:
                                Intent tribeIntent=new Intent(getActivity(), MoreEasyBuyActivity.class);
                                tribeIntent.putExtra("tage","2");
                                startActivity(tribeIntent);

                                break;
                            case 3:
                                Intent blacklistIntent=new Intent(getActivity(), MoreDuoBaoActivity.class);
                                startActivity(blacklistIntent);
                                break;
                            case 4:
                                Intent newsIntent=new Intent(getActivity(), MoreConersionActivity.class)  ;
                                startActivity(newsIntent);
                                break;
                            case 5:
                                Intent partnerIntent=new Intent(getActivity(), CooperationActivity.class)  ;
                                startActivity(partnerIntent);
                                break;

                        }



                    }
                }
        );

    }


}
