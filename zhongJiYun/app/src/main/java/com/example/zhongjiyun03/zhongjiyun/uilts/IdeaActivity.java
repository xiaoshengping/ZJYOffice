package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.adapter.AppBaseAdapter;
import com.example.zhongjiyun03.zhongjiyun.bean.AppBean;
import com.example.zhongjiyun03.zhongjiyun.bean.AppDataBean;
import com.example.zhongjiyun03.zhongjiyun.bean.FeedBackDataBean;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.http.MyAppliction;
import com.example.zhongjiyun03.zhongjiyun.http.SQLhelper;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.SelectPictureActivity;
import com.example.zhongjiyun03.zhongjiyun.view.MyGridView;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IdeaActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.register_tv)
    private TextView addExtruderTv;   //头部右边
    @ViewInject(R.id.title_name_tv)
    private TextView titleNemeTv;     //头部中间
    @ViewInject(R.id.retrun_text_view)
    private TextView retrunText;     //头部左边
    @ViewInject(R.id.text_edit)
    private EditText textEdit;
    @ViewInject(R.id.save_button)
    private Button saveButton;
    private static final int REQUEST_PICK = 0;
    private ArrayList<String> selectedPicture = new ArrayList<String>();
    private GridAdapter adapter;
    private GridView gridview;

    @ViewInject(R.id.image_layout)
    private LinearLayout imageLayout;
    private SVProgressHUD mSVProgressHUD;//loding
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea);
        ViewUtils.inject(this);
        inti();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void inti() {
        intiView();
        intiPictureView();

    }

    private void intiView() {
        addExtruderTv.setVisibility(View.GONE);
        titleNemeTv.setText("设置");
        retrunText.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        mSVProgressHUD = new SVProgressHUD(this);


    }

    private void intiPictureView() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(300).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
        gridview = (MyGridView) findViewById(R.id.gridview);

    }

    public void selectPicture(View view) {
        if (selectedPicture != null && selectedPicture.size() != 0) {
            selectedPicture.clear();
        }
        startActivityForResult(new Intent(this, SelectPictureActivity.class), REQUEST_PICK);


    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            selectedPicture = (ArrayList<String>) data
                    .getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
            if (selectedPicture != null && selectedPicture.size() != 0) {
                gridview.setVisibility(View.VISIBLE);
            } else {
                gridview.setVisibility(View.GONE);
            }
            adapter = new GridAdapter(selectedPicture,IdeaActivity.this);
            gridview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        SQLhelper sqLhelper = new SQLhelper(IdeaActivity.this);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        Cursor cursor = db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid = null;  //用户id

        while (cursor.moveToNext()) {
            uid = cursor.getString(0);

        }
        switch (v.getId()) {

            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;
            case R.id.save_button:
                if (!TextUtils.isEmpty(uid)) {
                    saveData(uid);
                } else {
                    Intent intent = new Intent(IdeaActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                }


                break;


        }
    }

    private void saveData(final String uid) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        if (!TextUtils.isEmpty(textEdit.getText().toString())) {
            requestParams.addBodyParameter("Id", uid);
            requestParams.addBodyParameter("Content", textEdit.getText().toString());
            if (selectedPicture.size() == 3) {
                mSVProgressHUD.showWithStatus("上传照片中(3)...");
            } else if (selectedPicture.size() == 2) {
                mSVProgressHUD.showWithStatus("上传照片中(2)...");
            } else if (selectedPicture.size() == 1) {
                mSVProgressHUD.showWithStatus("上传照片中(1)...");
            }
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getIdeaFeedBackData(), requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Log.e("意见反馈", responseInfo.result);
                    if (!TextUtils.isEmpty(responseInfo.result)) {
                        AppBean<FeedBackDataBean> appBean = JSONObject.parseObject(responseInfo.result, new TypeReference<AppBean<FeedBackDataBean>>() {
                        });
                        if (appBean.getResult().equals("success")) {
                            FeedBackDataBean feedBackDataBean = appBean.getData();
                            if (feedBackDataBean != null) {
                                intiPhontData0(uid, "17", selectedPicture.get(0), feedBackDataBean.getId());
                            }

                        } else {
                            MyAppliction.showToast("提交失败");
                            mSVProgressHUD.dismiss();
                        }


                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.e("意见反馈", s);
                    mSVProgressHUD.dismiss();
                }
            });
        } else {
            MyAppliction.showToast("请输入要反馈的内容");
        }


    }

    private void intiPhontData0(final String id, String imageType, String imagePath, final String OwnId) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requwstParams = new RequestParams();
        requwstParams.addBodyParameter("Id", id);
        requwstParams.addBodyParameter("ImageType", imageType);
        requwstParams.addBodyParameter("UserType", "boss");
        requwstParams.addBodyParameter("SourceType", "6");
        requwstParams.addBodyParameter("File", new File(imagePath));
        requwstParams.addBodyParameter("OwnId", OwnId);

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getPhoneData(), requwstParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //Log.e("照片请求", responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)) {
                    AppBean<AppDataBean> appBean = JSONObject.parseObject(responseInfo.result, new TypeReference<AppBean<AppDataBean>>() {
                    });

                    if (appBean.getResult().equals("success")) {
                        if (selectedPicture.size() == 3) {
                            mSVProgressHUD.showWithStatus("上传照片中(2)...");
                            intiPhontData1(id, "17", selectedPicture.get(1), OwnId);
                        } else if (selectedPicture.size() == 2) {
                            mSVProgressHUD.showWithStatus("上传照片中(1)...");
                            intiPhontData1(id, "17", selectedPicture.get(1), OwnId);
                        } else if (selectedPicture.size() == 1) {
                            mSVProgressHUD.showSuccessWithStatus("感谢您提供宝贵的意见");
                            finish();
                            overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                            mSVProgressHUD.dismiss();
                        }


                    } else {
                        MyAppliction.showToast("上传照片失败");
                        mSVProgressHUD.dismiss();
                    }


                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("照片请求", s);
                mSVProgressHUD.dismiss();
            }
        });


    }

    private void intiPhontData1(final String id, String userType, String imagePath, final String OwnId) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requwstParams = new RequestParams();
        requwstParams.addBodyParameter("Id", id);
        requwstParams.addBodyParameter("ImageType", userType);
        requwstParams.addBodyParameter("UserType", "boss");
        requwstParams.addBodyParameter("SourceType", "6");
        requwstParams.addBodyParameter("File", new File(imagePath));
        requwstParams.addBodyParameter("OwnId", OwnId);

        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getPhoneData(), requwstParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("照片请求", responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)) {
                    AppBean<AppDataBean> appBean = JSONObject.parseObject(responseInfo.result, new TypeReference<AppBean<AppDataBean>>() {
                    });

                    if (appBean.getResult().equals("success")) {
                        if (selectedPicture.size() == 3) {
                            mSVProgressHUD.showWithStatus("上传照片中(1)...");
                            intiPhontData2(id, "17", selectedPicture.get(2), OwnId);
                        } else if (selectedPicture.size() == 2) {

                            mSVProgressHUD.showSuccessWithStatus("感谢您提供宝贵的意见");
                            finish();
                            overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                            mSVProgressHUD.dismiss();
                        }


                    } else {
                        mSVProgressHUD.dismiss();
                        MyAppliction.showToast("上传照片失败");

                    }


                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("照片请求", s);
                mSVProgressHUD.dismiss();
            }
        });


    }

    private void intiPhontData2(final String id, String userType, String imagePath, final String OwnId) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requwstParams = new RequestParams();
        requwstParams.addBodyParameter("Id", id);
        requwstParams.addBodyParameter("ImageType", userType);
        requwstParams.addBodyParameter("UserType", "boss");
        requwstParams.addBodyParameter("SourceType", "6");
        requwstParams.addBodyParameter("File", new File(imagePath));
        requwstParams.addBodyParameter("OwnId", OwnId);
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getPhoneData(), requwstParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("照片请求", responseInfo.result);
                if (!TextUtils.isEmpty(responseInfo.result)) {
                    AppBean<AppDataBean> appBean = JSONObject.parseObject(responseInfo.result, new TypeReference<AppBean<AppDataBean>>() {
                    });

                    if (appBean.getResult().equals("success")) {

                        mSVProgressHUD.showSuccessWithStatus("感谢您提供宝贵的意见");
                        finish();
                        overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                        mSVProgressHUD.dismiss();
                    } else {
                        MyAppliction.showToast(appBean.getMsg());
                        mSVProgressHUD.dismiss();
                    }


                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("照片请求", s);
                mSVProgressHUD.dismiss();
            }
        });


    }



    class GridAdapter extends AppBaseAdapter<String> {
        public ViewHolde viewHolde;

        public GridAdapter(List<String> data, Context context) {
            super(data, context);
        }
        @Override
        public View createView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(IdeaActivity.this).inflate(R.layout.idea_picture_layout, parent, false);
                viewHolde = new ViewHolde(convertView);
                convertView.setTag(viewHolde);
            } else {
                viewHolde = (ViewHolde) convertView.getTag();
            }

            ImageLoader.getInstance().displayImage("file://" + data.get(position),
                    viewHolde.imageView);
            return convertView;
        }


        private class ViewHolde {
            ImageView imageView;

            public ViewHolde(View view) {
                imageView = (ImageView) view.findViewById(R.id.picture_image_view);

            }
        }


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
