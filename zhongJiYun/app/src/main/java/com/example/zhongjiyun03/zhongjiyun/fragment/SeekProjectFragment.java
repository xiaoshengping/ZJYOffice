package com.example.zhongjiyun03.zhongjiyun.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.HomeNewsListAdapter;
import com.example.zhongjiyun03.zhongjiyun.adapter.MyAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.home.AppListDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.home.NewsDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.popwin.PopupWindowHelper;
import com.example.zhongjiyun03.zhongjiyun.uilts.NewsListParticularsActivity;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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
public class SeekProjectFragment extends Fragment implements View.OnClickListener {



    @ViewInject(R.id.retrun_tv)
    private TextView retrunTv;
    @ViewInject(R.id.title_name_tv)
    private TextView titlNameTv;
    @ViewInject(R.id.register_tv)
    private TextView registerTv;


    @ViewInject(R.id.viewPager)
    private ViewPager viewPager;
    @ViewInject(R.id.cursor)
    private ImageView imageView;
    private List<View> lists = new ArrayList<View>();
    private MyAdapter myAdapter;
    private Bitmap cursor;
    private int offSet;
    private int currentItem;
    private Matrix matrix = new Matrix();
    private int bmWidth;
    private Animation animation;
    private PullToRefreshListView dynamicListView;
    @ViewInject(R.id.textView1)
    private TextView textView1;  //新闻动态textview
    private  PullToRefreshListView andustryListView;
    @ViewInject(R.id.textView2)
    private TextView textView2;  //行业新闻textview
    private PullToRefreshListView enterpriseListView;
    @ViewInject(R.id.textView3)
    private TextView textView3;  //企业新闻textview
    private PullToRefreshListView bidsListView;
    @ViewInject(R.id.textView4)
    private TextView textView4;  //招标新闻textview
    private View dynamicView;  //新闻动态view
    private View andustryView; //行业新闻view
    private View enterpriseView; //企业新闻view
    private View bidsView;  //招标新闻view
    private  List<NewsDataBean> newsDataBeens;//新闻动态数据
    private  List<NewsDataBean>  andustryNewsDataBeen; //行业新闻数据
    private  List<NewsDataBean>  enterpriseNewsDataBeen; //企业新闻数据
    private  List<NewsDataBean>  bidsNewsDataBeen;  //招标新闻数据
    private int pageIndex=1;    //新闻动态页数
    private int pageIndex2=1;    //行业新闻页数
    private int pageIndex3=1;    //企业新闻页数
    private int pageIndex4=1;    //招标新闻页数
    private HomeNewsListAdapter dynamicNewsListAdapter;//新闻动态
    private boolean isPullDownRefresh1=true; //判断新闻动态是下拉，还是上拉的标记
    private HomeNewsListAdapter andustryNewsListAdapter; //行业新闻
    private boolean isPullDownRefresh2=true; //判断行业新闻是下拉，还是上拉的标记
    private HomeNewsListAdapter enterPriseNewsListAdapter;//企业新闻
    private boolean isPullDownRefresh3=true; //判断企业新闻是下拉，还是上拉的标记
    private HomeNewsListAdapter bidsNewsListAdapter;//招标新闻
    private boolean isPullDownRefresh4=true; //判断招标新闻是下拉，还是上拉的标记
    @ViewInject(R.id.network_remind_layout)
    private LinearLayout networkRemindLayout;//网络提示


    public SeekProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.activity_news_center, container, false);
        ViewUtils.inject(this,view);
        PopupWindowHelper.init(getActivity());
        
        init();

        return view;
    }
    private void init() {
        intiView();
        initViewPager();
        initDConditionListView();
        initAndustryListView();
        initentErpriseListView();
        initBidsListView();
    }

    private void intiView() {
        titlNameTv.setText("资讯");
        registerTv.setVisibility(View.GONE);
        retrunTv.setVisibility(View.GONE);
        newsDataBeens=new ArrayList<>();//新闻动态数据
        andustryNewsDataBeen=new ArrayList<>(); //行业新闻数据
        enterpriseNewsDataBeen=new ArrayList<>(); //企业新闻数据
        bidsNewsDataBeen=new ArrayList<>(); //企业新闻数据
        networkRemindLayout.setOnClickListener(this);
    }

    private void intiData1(int pageIndex) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("pageIndex",pageIndex+"");
        requestParams.addBodyParameter("pageSize","10");
        requestParams.addBodyParameter("type","0");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getNewsData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("新闻",responseInfo.result);
                AppListDataBean<NewsDataBean> appListDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppListDataBean<NewsDataBean>>(){});
                if ((appListDataBean.getResult()).equals("success")){

                    List<NewsDataBean> newsDataBeen=appListDataBean.getData();
                    if (newsDataBeen!=null){
                        if (isPullDownRefresh1){
                            newsDataBeens.clear();
                        }
                        newsDataBeens.addAll(newsDataBeen);
                    }
                    dynamicNewsListAdapter.notifyDataSetChanged();
                    dynamicListView.onRefreshComplete();

                }
                networkRemindLayout.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("新闻",s);
                networkRemindLayout.setVisibility(View.VISIBLE);
                dynamicListView.onRefreshComplete();
            }
        });



    }
    private void intiData2(int pageIndex2) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("pageIndex",pageIndex2+"");
        requestParams.addBodyParameter("pageSize","10");
        requestParams.addBodyParameter("type","20");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getNewsData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // Log.e("新闻",responseInfo.result);\
                AppListDataBean<NewsDataBean> appListDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppListDataBean<NewsDataBean>>(){});
                if ((appListDataBean.getResult()).equals("success")){

                    List<NewsDataBean>  newsDataBeen=appListDataBean.getData();
                    if (newsDataBeen!=null){
                        if (isPullDownRefresh2){
                            andustryNewsDataBeen.clear();
                        }
                        andustryNewsDataBeen.addAll(newsDataBeen);
                    }
                    andustryNewsListAdapter.notifyDataSetChanged();
                    andustryListView.onRefreshComplete();
                }

                networkRemindLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("新闻",s);
                networkRemindLayout.setVisibility(View.VISIBLE);
                andustryListView.onRefreshComplete();
            }
        });



    }
    private void intiData3(int pageIndex3) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("pageIndex",pageIndex3+"");
        requestParams.addBodyParameter("pageSize","10");
        requestParams.addBodyParameter("type","25");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getNewsData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // Log.e("新闻",responseInfo.result);\
                AppListDataBean<NewsDataBean> appListDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppListDataBean<NewsDataBean>>(){});
                if ((appListDataBean.getResult()).equals("success")){
                    List<NewsDataBean>  newsDataBeenss=appListDataBean.getData();
                    if (newsDataBeenss!=null){
                        if (isPullDownRefresh3){
                            enterpriseNewsDataBeen.clear();
                        }
                        enterpriseNewsDataBeen.addAll(newsDataBeenss);
                    }
                    enterPriseNewsListAdapter.notifyDataSetChanged();

                }

                enterpriseListView.onRefreshComplete();
                networkRemindLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("新闻",s);
                networkRemindLayout.setVisibility(View.VISIBLE);
                enterpriseListView.onRefreshComplete();
            }
        });



    }
    private void intiData4(int pageIndex) {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        requestParams.addBodyParameter("pageIndex",pageIndex+"");
        requestParams.addBodyParameter("pageSize","10");
        requestParams.addBodyParameter("type","15");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getNewsData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // Log.e("新闻",responseInfo.result);\
                AppListDataBean<NewsDataBean> appListDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppListDataBean<NewsDataBean>>(){});
                if ((appListDataBean.getResult()).equals("success")){

                    List<NewsDataBean>  newsDataBeensss =appListDataBean.getData();
                    if (newsDataBeensss!=null){
                        if (isPullDownRefresh4){
                            bidsNewsDataBeen.clear();
                        }
                        bidsNewsDataBeen.addAll(newsDataBeensss);
                    }
                    bidsNewsListAdapter.notifyDataSetChanged();
                    bidsListView.onRefreshComplete();
                }
                networkRemindLayout.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("新闻",s);
                networkRemindLayout.setVisibility(View.VISIBLE);
                bidsListView.onRefreshComplete();
            }
        });



    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.network_remind_layout:
                //跳转到设置界面
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                break;


        }


    }
    private void initDConditionListView() {
        dynamicListView= (PullToRefreshListView) dynamicView.findViewById(R.id.dynamic_condition_listview);

        dynamicListView.setMode(PullToRefreshBase.Mode.BOTH);
        dynamicListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex=1;
                isPullDownRefresh1=true;
                intiData1(pageIndex);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex++;
                isPullDownRefresh1=false;
                intiData1(pageIndex);
            }
        });
        ILoadingLayout endLabels  = dynamicListView
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚上拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 上来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = dynamicListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        dynamicListView.setRefreshing();
        dynamicNewsListAdapter=new HomeNewsListAdapter(newsDataBeens,getActivity());
        dynamicListView.setAdapter(dynamicNewsListAdapter);
        dynamicNewsListAdapter.notifyDataSetChanged();
        dynamicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent dynamicIntetn=new Intent(getActivity(),NewsListParticularsActivity.class);
                dynamicIntetn.putExtra("newDataId",newsDataBeens.get(position-1).getId());
                startActivity(dynamicIntetn);
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });


    }

    private void initAndustryListView() {
        andustryListView= (PullToRefreshListView) andustryView.findViewById(R.id.andustry_new_listview);
        andustryListView.setMode(PullToRefreshBase.Mode.BOTH);
        andustryListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex2=1;
                isPullDownRefresh2=true;
                intiData2(pageIndex2);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex2++;
                isPullDownRefresh2=false;
                intiData2(pageIndex2);
            }
        });
        ILoadingLayout endLabels  = andustryListView
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚上拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 上来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = andustryListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        andustryListView.setRefreshing();
        andustryNewsListAdapter=new HomeNewsListAdapter(andustryNewsDataBeen,getActivity());
        andustryListView.setAdapter(andustryNewsListAdapter);
        andustryNewsListAdapter.notifyDataSetChanged();
        andustryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent dynamicIntetn=new Intent(getActivity(),NewsListParticularsActivity.class);
                dynamicIntetn.putExtra("newDataId",andustryNewsDataBeen.get(position-1).getId());
                startActivity(dynamicIntetn);
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });


    }
    private void initentErpriseListView() {
        enterpriseListView= (PullToRefreshListView) enterpriseView.findViewById(R.id.enterprise_new_listview);
        enterpriseListView.setMode(PullToRefreshBase.Mode.BOTH);
        enterpriseListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex3=1;
                isPullDownRefresh3=true;
                intiData3(pageIndex3);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex3++;
                isPullDownRefresh3=false;
                intiData3(pageIndex3);
            }
        });
        ILoadingLayout endLabels  = enterpriseListView
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚上拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 上来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = enterpriseListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        enterpriseListView.setRefreshing();
        enterPriseNewsListAdapter=new HomeNewsListAdapter(enterpriseNewsDataBeen,getActivity());
        enterpriseListView.setAdapter(enterPriseNewsListAdapter);
        enterPriseNewsListAdapter.notifyDataSetChanged();
        enterpriseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent dynamicIntetn=new Intent(getActivity(),NewsListParticularsActivity.class);
                dynamicIntetn.putExtra("newDataId",enterpriseNewsDataBeen.get(position-1).getId());
                startActivity(dynamicIntetn);
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });


    }
    private void initBidsListView() {
        bidsListView= (PullToRefreshListView) bidsView.findViewById(R.id.bids_news_listview);
        bidsListView.setMode(PullToRefreshBase.Mode.BOTH);
        bidsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex4=1;
                isPullDownRefresh4=true;
                intiData4(pageIndex4);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex4++;
                isPullDownRefresh4=false;
                intiData4(pageIndex4);
            }
        });
        ILoadingLayout endLabels  = bidsListView
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚上拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 上来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = bidsListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        bidsListView.setRefreshing();
        bidsNewsListAdapter=new HomeNewsListAdapter(bidsNewsDataBeen,getActivity());
        bidsListView.setAdapter(bidsNewsListAdapter);
        bidsNewsListAdapter.notifyDataSetChanged();
        bidsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent dynamicIntetn=new Intent(getActivity(),NewsListParticularsActivity.class);
                dynamicIntetn.putExtra("newDataId",bidsNewsDataBeen.get(position-1).getId());
                startActivity(dynamicIntetn);
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });


    }






    private void initViewPager() {
        dynamicView= getActivity().getLayoutInflater().inflate(R.layout.dynamic_condition_layout, null);
        andustryView= getActivity().getLayoutInflater().inflate(R.layout.andustry_new_layout, null);
        enterpriseView= getActivity().getLayoutInflater().inflate(R.layout.enterprise_new_layout, null);
        bidsView= getActivity().getLayoutInflater().inflate(R.layout.bids_news_layout, null);

        lists.add(dynamicView);
        lists.add(andustryView);
        lists.add(enterpriseView);
        lists.add(bidsView);
        initeCursor();
        myAdapter = new MyAdapter(lists);
        viewPager.setAdapter(myAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int one = offSet * 2 + bmWidth;// 页卡1 -> 页卡2 偏移量
            int two = one * 2;// 页卡1 -> 页卡3 偏移量
            int three = one * 3;// 页卡1 -> 页卡4 偏移量
            @Override
            public void onPageSelected(int arg0) { // 当滑动式，顶部的imageView是通过animation缓慢的滑动
                // TODO Auto-generated method stub
                switch (arg0) {
                    case 0:
                        if (currentItem == 1) {
                            animation = new TranslateAnimation(one, 0, 0, 0);
                        } else if (currentItem== 2) {
                            animation = new TranslateAnimation(two, 0, 0, 0);
                        }else if (currentItem== 3){

                            animation = new TranslateAnimation(three, 0, 0, 0);
                        }
                        //intiData("0");

                        break;
                    case 1:
                        if (currentItem == 0) {
                            animation = new TranslateAnimation(offSet, one, 0, 0);
                        } else if (currentItem== 2) {
                            animation = new TranslateAnimation(two, one, 0, 0);
                        }else if (currentItem== 3) {
                            animation = new TranslateAnimation(three, one, 0, 0);
                        }
                        //intiData("20");

                        break;
                    case 2:
                        if (currentItem == 0) {
                            animation = new TranslateAnimation(offSet, two, 0, 0);
                        } else if (currentItem== 1) {
                            animation = new TranslateAnimation(one, two, 0, 0);
                        }else if (currentItem== 3) {
                            animation = new TranslateAnimation(three, two, 0, 0);
                        }
                        //intiData("25");

                        break;
                    case 3:

                        if (currentItem == 0) {
                            animation = new TranslateAnimation(offSet, three, 0, 0);
                        } else if (currentItem== 2) {
                            animation = new TranslateAnimation(two, three, 0, 0);
                        } else if (currentItem== 1) {
                            animation = new TranslateAnimation(one, three, 0, 0);
                        }
                        //intiData("15");
                        break;


                }
                currentItem = arg0;
                animation.setDuration(500);
                animation.setFillAfter(true);
                imageView.startAnimation(animation);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        textView1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                viewPager.setCurrentItem(0);
                //intiData("0");
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                viewPager.setCurrentItem(1);
                //intiData("20");
            }
        });

        textView3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                viewPager.setCurrentItem(2);
                //intiData("25");
            }
        });

        textView4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                viewPager.setCurrentItem(3);
                //intiData("15");
            }
        });


    }

    private void initeCursor() {
        cursor = BitmapFactory
                .decodeResource(getResources(), R.drawable.cursor);
        bmWidth = cursor.getWidth();

        DisplayMetrics dm;
        dm = getResources().getDisplayMetrics();

        offSet = (dm.widthPixels - 4 * bmWidth) / 8;


        offSet = (dm.widthPixels / 4 - bmWidth) / 2;// 计算偏移量
        matrix.setTranslate(offSet, 0);
        imageView.setImageMatrix(matrix); // 需要iamgeView的scaleType为matrix
        currentItem = 0;
    }

    



}
