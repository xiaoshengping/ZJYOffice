package com.example.zhongjiyun03.zhongjiyun.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.HomeGridAdapter;
import com.example.zhongjiyun03.zhongjiyun.adapter.HomeProjectListAdapter;
import com.example.zhongjiyun03.zhongjiyun.adapter.ImagePagerAdapter;
import com.example.zhongjiyun03.zhongjiyun.adapter.RecommendMachinistAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.SecondHandDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.AdvertisementBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.AppListDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.SecondHandBean;
import com.example.zhongjiyun03.zhongjiyun.bean.seekProject.SeekProjectBean;
import com.example.zhongjiyun03.zhongjiyun.bean.seekProject.SeekProjectDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.example.zhongjiyun03.zhongjiyun.uilts.CooperationActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.ExturderParticularsActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.HomeBlackListActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.HomeMarketActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.HomeMoreProjectActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.HomeTribeActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.LoginActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.MyExtruderActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.NewsCenterActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.SecondHandActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.SeekMachinistActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.SeekProjectParticularsActivity;
import com.example.zhongjiyun03.zhongjiyun.uilts.ServiceProviderActivity;
import com.example.zhongjiyun03.zhongjiyun.view.CustomHomeScrollListView;
import com.example.zhongjiyun03.zhongjiyun.view.MyGridView;
import com.example.zhongjiyun03.zhongjiyun.widget.AutoScrollViewPager;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    @ViewInject(R.id.home_gridView)
    private MyGridView homeGridView;
    //广告轮播
    @ViewInject(R.id.home_banner_viewpager)
    private AutoScrollViewPager autoPager;
    @ViewInject(R.id.home_dot_ll)
    private LinearLayout dotLL;
    private ImagePagerAdapter pagerAdapter;
    private ArrayList<AdvertisementBean> imageUrls = new ArrayList<>();
    //private String url = "http://mobapi.meilishuo.com/2.0/activity/selected?imei=000000000000000&mac=08%3A00%3A27%3A51%3A2e%3Aaa&qudaoid=11601&access_token=d154111f2e870ea8e58198e0f8c59339";



    //项目推荐listView
    @ViewInject(R.id.project_list_view)
    private CustomHomeScrollListView projectListView;
    @ViewInject(R.id.project_more_text)
    private TextView projectMoreText;
    @ViewInject(R.id.home_rg)
    private RadioGroup homeRG;
    @ViewInject(R.id.home_scrollView)
    private PullToRefreshScrollView homePullToScrollView;



    //二手机
    @ViewInject(R.id.more_text_view)
    private TextView moreTextView;
    @ViewInject(R.id.project_gridview)
    private GridView machinistGridview;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_home, container, false);
         ViewUtils.inject(this,view);

        init();
        loadData();

        return view ;
    }

    /**
     * Gson
     */
    private void loadData() {
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getAdvertisementData(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("图片联播",responseInfo.result);
                if(!TextUtils.isEmpty(responseInfo.result)){
                    AppListDataBean<AdvertisementBean> appListDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppListDataBean<AdvertisementBean>>(){});
                    if (appListDataBean.getResult().equals("success")){
                        //MyAppliction.showToast("执行刷新了");
                        List<AdvertisementBean> advertisementBeen=appListDataBean.getData();
                        if (advertisementBeen!=null){
                            imageUrls.addAll(advertisementBeen);
                            pagerAdapter.refreshData(true);
                        }

                    }


                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("dsjfjfj",s);
            }
        });


    }


    private void init() {
        pagerAdapter = new ImagePagerAdapter(getActivity(), imageUrls, dotLL);
        autoPager.setAdapter(pagerAdapter);
        autoPager.setOnPageChangeListener(pagerAdapter);
        initGridView();
        moreTextView.setOnClickListener(this);
        projectMoreText.setOnClickListener(this);
        initListRecommentMachinist();



    }
    public void intiPullToRefresh(){
        homePullToScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        ILoadingLayout startLabels  = homePullToScrollView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        //上拉监听函数
        homePullToScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //执行刷新函数

                new GetDataTask().execute();
            }
        });

    }

    private class GetDataTask extends AsyncTask<Void, Void, LinearLayout> {

        @Override
        protected LinearLayout doInBackground(Void... params) {
            // Simulates a background job.

            return null;
        }

        @Override
        protected void onPostExecute(LinearLayout result) {
            // Do some stuff here

            loadData();
            initListRecommentMachinist();

            // Call onRefreshComplete when the list has been refreshed.
            //在更新UI后，无需其它Refresh操作，系统会自己加载新的listView
            homePullToScrollView.onRefreshComplete();


            super.onPostExecute(result);
        }
    }
       //推荐二手机
    private void initListRecommentMachinist() {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        SQLhelper sqLhelper=new SQLhelper(getActivity());
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);
        }
        if (!TextUtils.isEmpty(uid)){
            requestParams.addBodyParameter("Id",uid);
            //步骤1：创建一个SharedPreferences接口对象
            SharedPreferences read = getActivity().getSharedPreferences("lock", getActivity().MODE_WORLD_READABLE);
            //步骤2：获取文件中的值
            String sesstionId = read.getString("code","");
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        }
        requestParams.addBodyParameter("PageIndex","1");
        requestParams.addBodyParameter("PageSize","6");

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getSecondExtruderData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("推荐二手机",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<SecondHandDataBean> appListDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<SecondHandDataBean>>(){});
                    if ((appListDataBean.getResult()).equals("success")){
                        SecondHandDataBean    secondHandDataBean=appListDataBean.getData();
                        if (secondHandDataBean!=null){
                            List<SecondHandBean>    secondHandBeen=  secondHandDataBean.getPagerData();
                            intiGridView(secondHandBeen);
                        }


                    }

                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("找项目",s);
            }
        });
    }
      //推荐二手机
    private void intiGridView(final List<SecondHandBean> secondHandBeen) {
        RecommendMachinistAdapter recommendMachinistAdapter=new RecommendMachinistAdapter(secondHandBeen,getActivity());
        machinistGridview.setAdapter(recommendMachinistAdapter);
        recommendMachinistAdapter.notifyDataSetChanged();
        machinistGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(getActivity(),ExturderParticularsActivity.class);
                intent.putExtra("secondHandData",secondHandBeen.get(position).getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_open, R.anim.anim_close);

            }
        });




    }

    //推荐项目数据
    private void initListData() {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        SQLhelper sqLhelper=new SQLhelper(getActivity());
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id

        while (cursor.moveToNext()) {
            uid=cursor.getString(0);

        }
        if (!TextUtils.isEmpty(uid)){
            requestParams.addBodyParameter("Id",uid);
            //步骤1：创建一个SharedPreferences接口对象
            SharedPreferences read = getActivity().getSharedPreferences("lock", getActivity().MODE_WORLD_READABLE);
            //步骤2：获取文件中的值
            String sesstionId = read.getString("code","");
            requestParams.setHeader("Cookie", "ASP.NET_SessionId=" + sesstionId);
        }
        requestParams.addBodyParameter("PageIndex","1");
        requestParams.addBodyParameter("PageSize","5");

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getProjecctListData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("推荐的项目",responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)){
                    AppBean<SeekProjectDataBean> appBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppBean<SeekProjectDataBean>>(){});
                    if ((appBean.getResult()).equals("success")){
                        SeekProjectDataBean seekProjectBeanList=appBean.getData();
                        if (seekProjectBeanList!=null){
                            List<SeekProjectBean> seekProjectBean= seekProjectBeanList.getPagerData();
                            if (seekProjectBean!=null){
                                intiListView(seekProjectBean);

                            }
                        }
                    }


                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("找项目",s);
            }
        });
    }
      //项目推荐listView
    private void intiListView(final List<SeekProjectBean> seekProjectBean) {

        HomeProjectListAdapter homeProjectlsitAdapter=new HomeProjectListAdapter(seekProjectBean,getActivity());
        projectListView.setAdapter(homeProjectlsitAdapter);
        homeProjectlsitAdapter.notifyDataSetChanged();
        projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Intent intent=new Intent(getActivity(), SeekProjectParticularsActivity.class);
                 intent.putExtra("seekProjectId",seekProjectBean.get(position).getId());
                 startActivity(intent);
            }
        });



    }

    //首页griview
    private void initGridView() {
        HomeGridAdapter homeGridViewAdapter=new HomeGridAdapter(getActivity());
        //添加Item到网格中
        homeGridView.setAdapter(homeGridViewAdapter);
        //添加点击事件
        homeGridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                    {
                        int index=arg2+1;//id是从0开始的，所以需要+1
                       // Toast.makeText(getActivity(), "你按下了选项："+index, Toast.LENGTH_LONG).show();
                        //Toast用于向用户显示一些帮助/提示
                        SQLhelper sqLhelper=new SQLhelper(getActivity());
                        SQLiteDatabase db= sqLhelper.getWritableDatabase();
                        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
                        String uid=null;  //用户id
                        while (cursor.moveToNext()) {
                            uid=cursor.getString(0);

                        }

                        switch (index){
                            case 1:
                                Intent projectIntent=new Intent(getActivity(),HomeMoreProjectActivity.class);
                                startActivity(projectIntent);
                                break;

                            case 2:
                                Intent SecondHandIntent=new Intent(getActivity(), SecondHandActivity.class)  ;
                                SecondHandIntent.putExtra("tage","secondHand");
                                startActivity(SecondHandIntent);
                                break;
                            case 3:

                                Intent seekMachinistIntent=new Intent(getActivity(), SeekMachinistActivity.class)  ;
                                seekMachinistIntent.putExtra("tage","seekMachinis");
                                startActivity(seekMachinistIntent);
                                break;
                            case 4:
                                Intent serviceIntent=new Intent(getActivity(), ServiceProviderActivity.class)  ;
                                serviceIntent.putExtra("tage","service");
                                startActivity(serviceIntent);
                                break;
                            case 5:
                                Intent marketIntent=new Intent(getActivity(), HomeMarketActivity.class)  ;
                                startActivity(marketIntent);

                                break;
                            case 6:
                                if (!TextUtils.isEmpty(uid)){
                                    Intent MyExtruderIntent=new Intent(getActivity(), MyExtruderActivity.class)  ;
                                    startActivity(MyExtruderIntent);
                                    getActivity().overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                                }else {
                                    Intent intent=new Intent(getActivity(),LoginActivity.class);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                                }
                                break;
                            case 7:
                                Intent tribeIntent=new Intent(getActivity(), HomeTribeActivity.class)  ;
                                startActivity(tribeIntent);
                            break;

                            case 8:
                                Intent blackListIntent=new Intent(getActivity(), HomeBlackListActivity.class)  ;
                                startActivity(blackListIntent);
                                break;
                            case 9:
                                Intent newsIntent=new Intent(getActivity(), NewsCenterActivity.class)  ;
                                startActivity(newsIntent);
                            break;

                            case 10:
                                Intent cooperationIntent=new Intent(getActivity(), CooperationActivity.class)  ;
                                startActivity(cooperationIntent);
                                break;


                        }



                    }
                }
        );

    }

    @Override
    public void onResume() {
        super.onResume();
        autoPager.startAutoScroll();
        initListData();

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.more_text_view:
                Intent intent=new Intent(getActivity(), SecondHandActivity.class);
                intent.putExtra("tage","secondHand");
                startActivity(intent);
                break;
            case R.id.project_more_text:
                Intent projectIntent=new Intent(getActivity(), HomeMoreProjectActivity.class);
                projectIntent.putExtra("tage","MoreProject");
                startActivity(projectIntent);
                break;




        }
    }


}
