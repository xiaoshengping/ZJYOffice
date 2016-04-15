package com.example.zhongjiyun03.zhongjiyun.uilts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.SelectPictureActivity;
import com.example.zhongjiyun03.zhongjiyun.view.MyGridView;
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

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea);
        ViewUtils.inject(this);
        inti();

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


    }
    private void intiPictureView() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(300).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
        gridview = (MyGridView) findViewById(R.id.gridview);
        adapter = new GridAdapter();
        gridview.setAdapter(adapter);
    }
    public void selectPicture(View view) {
        startActivityForResult(new Intent(this, SelectPictureActivity.class), REQUEST_PICK);


    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            selectedPicture = (ArrayList<String>) data
                    .getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
            if (selectedPicture!=null&&selectedPicture.size()!=0){
                gridview.setVisibility(View.VISIBLE);
            }else {
                gridview.setVisibility(View.GONE);
            }
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.retrun_text_view:
                finish();
                overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
                break;
            case R.id.save_button:

                saveData();

                break;



        }
    }

    private void saveData() {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        if (!TextUtils.isEmpty(textEdit.getText().toString())){
            requestParams.addBodyParameter("Content",textEdit.getText().toString());
            httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getIdeaFeedBackData(),requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {

                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });
        }







    }

    class GridAdapter extends BaseAdapter {
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(250, 250);

        @Override
        public int getCount() {
            return selectedPicture.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new ImageView(IdeaActivity.this);
                ((ImageView) convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);
                convertView.setLayoutParams(params);
            }

            ImageLoader.getInstance().displayImage("file://" + selectedPicture.get(position),
                    (ImageView) convertView);
            return convertView;
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
