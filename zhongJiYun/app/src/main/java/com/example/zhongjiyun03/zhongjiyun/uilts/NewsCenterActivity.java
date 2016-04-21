package com.example.zhongjiyun03.zhongjiyun.uilts;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
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

public class NewsCenterActivity extends AppCompatActivity  implements View.OnClickListener {

    @ViewInject(R.id.retrun_text_view)
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
     private  List<NewsDataBean> newsDataBeens;
     private  List<NewsDataBean>  andustryNewsDataBeen;
     private  List<NewsDataBean>  enterpriseNewsDataBeen;
     private  List<NewsDataBean>  bidsNewsDataBeen;
     private int pageIndex=1;
     private int pageIndex2=1;
     private int pageIndex3=1;
     private int pageIndex4=1;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_center);
        ViewUtils.inject(this);
        init();



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
        titlNameTv.setText("新闻中心");
        registerTv.setVisibility(View.GONE);
        retrunTv.setOnClickListener(this);
        newsDataBeens=new ArrayList<>();//新闻动态数据
        andustryNewsDataBeen=new ArrayList<>(); //行业新闻数据
        enterpriseNewsDataBeen=new ArrayList<>(); //企业新闻数据
        bidsNewsDataBeen=new ArrayList<>(); //企业新闻数据

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
                Log.e("新闻",responseInfo.result);
                AppListDataBean<NewsDataBean> appListDataBean= JSONObject.parseObject(responseInfo.result,new TypeReference<AppListDataBean<NewsDataBean>>(){});
                if ((appListDataBean.getResult()).equals("success")){

                   List<NewsDataBean> newsDataBeen=appListDataBean.getData();
                    newsDataBeens.addAll(newsDataBeen);
                    dynamicListView.onRefreshComplete();
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("新闻",s);
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
                    andustryNewsDataBeen.addAll(newsDataBeen);

                    andustryListView.onRefreshComplete();
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("新闻",s);
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
                    enterpriseNewsDataBeen.addAll(newsDataBeenss);
                    enterpriseListView.onRefreshComplete();
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("新闻",s);
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
                    bidsNewsDataBeen.addAll(newsDataBeensss);
                     bidsListView.onRefreshComplete();
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("新闻",s);
            }
        });



    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
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
                newsDataBeens.clear();
                intiData1(pageIndex);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex++;
                intiData1(pageIndex);
            }
        });
        ILoadingLayout endLabels  = dynamicListView
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = dynamicListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        dynamicListView.setRefreshing();
        HomeNewsListAdapter homeNewsListAdapter=new HomeNewsListAdapter(newsDataBeens,NewsCenterActivity.this);
        dynamicListView.setAdapter(homeNewsListAdapter);
        homeNewsListAdapter.notifyDataSetChanged();
        dynamicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent dynamicIntetn=new Intent(NewsCenterActivity.this,NewsListParticularsActivity.class);
                dynamicIntetn.putExtra("newDataId",newsDataBeens.get(position).getId());
                startActivity(dynamicIntetn);
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
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
                andustryNewsDataBeen.clear();
                intiData2(pageIndex2);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex2++;
                intiData2(pageIndex2);
            }
        });
        ILoadingLayout endLabels  = andustryListView
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = andustryListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        andustryListView.setRefreshing();
        HomeNewsListAdapter homeNewsListAdapter=new HomeNewsListAdapter(andustryNewsDataBeen,NewsCenterActivity.this);
        andustryListView.setAdapter(homeNewsListAdapter);
        homeNewsListAdapter.notifyDataSetChanged();
        andustryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent dynamicIntetn=new Intent(NewsCenterActivity.this,NewsListParticularsActivity.class);
                dynamicIntetn.putExtra("newDataId",andustryNewsDataBeen.get(position).getId());
                startActivity(dynamicIntetn);
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
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
                enterpriseNewsDataBeen.clear();
                intiData3(pageIndex3);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex3++;
                intiData3(pageIndex3);
            }
        });
        ILoadingLayout endLabels  = enterpriseListView
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = enterpriseListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        enterpriseListView.setRefreshing();
        HomeNewsListAdapter homeNewsListAdapter=new HomeNewsListAdapter(enterpriseNewsDataBeen,NewsCenterActivity.this);
        enterpriseListView.setAdapter(homeNewsListAdapter);
        homeNewsListAdapter.notifyDataSetChanged();
        enterpriseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent dynamicIntetn=new Intent(NewsCenterActivity.this,NewsListParticularsActivity.class);
                dynamicIntetn.putExtra("newDataId",enterpriseNewsDataBeen.get(position).getId());
                startActivity(dynamicIntetn);
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
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
                bidsNewsDataBeen.clear();
                intiData4(pageIndex4);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex4++;
                intiData4(pageIndex4);
            }
        });
        ILoadingLayout endLabels  = bidsListView
                .getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout startLabels  = bidsListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        bidsListView.setRefreshing();
        HomeNewsListAdapter homeNewsListAdapter=new HomeNewsListAdapter(bidsNewsDataBeen,NewsCenterActivity.this);
        bidsListView.setAdapter(homeNewsListAdapter);
        homeNewsListAdapter.notifyDataSetChanged();
        bidsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent dynamicIntetn=new Intent(NewsCenterActivity.this,NewsListParticularsActivity.class);
                dynamicIntetn.putExtra("newDataId",bidsNewsDataBeen.get(position).getId());
                startActivity(dynamicIntetn);
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
            }
        });


    }






private void initViewPager() {
       dynamicView= getLayoutInflater().inflate(R.layout.dynamic_condition_layout, null);
       andustryView= getLayoutInflater().inflate(R.layout.andustry_new_layout, null);
       enterpriseView= getLayoutInflater().inflate(R.layout.enterprise_new_layout, null);
       bidsView= getLayoutInflater().inflate(R.layout.bids_news_layout, null);

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
// KeyEvent.KEYCODE_BACK代表返回操作.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 处理返回操作.
            finish();
            overridePendingTransition(R.anim.anim_open, R.anim.anim_close);

        }
        return true;
    }


}
