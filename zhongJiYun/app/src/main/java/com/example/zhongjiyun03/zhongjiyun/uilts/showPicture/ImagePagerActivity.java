package com.example.zhongjiyun03.zhongjiyun.uilts.showPicture;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.bean.home.MyExtruderBean;
import com.example.zhongjiyun03.zhongjiyun.bean.main.PersonageInformationBean;
import com.example.zhongjiyun03.zhongjiyun.fragment.ImageDetailFragment;
import com.example.zhongjiyun03.zhongjiyun.view.HackyViewPager;

import java.util.ArrayList;
import java.util.List;


public class ImagePagerActivity extends ActionBarActivity {

    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager);

        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        List<String> urls= new ArrayList<>();
        if (getIntent().getStringExtra("tage").equals("personageInformation")){
            PersonageInformationBean resumeValueBean = (PersonageInformationBean) getIntent().getSerializableExtra(EXTRA_IMAGE_URLS);

            urls.add(resumeValueBean.getIdCardImage1());
            urls.add(resumeValueBean.getIdCardImage2());
            urls.add(resumeValueBean.getPhoto());
        }else if (getIntent().getStringExtra("tage").equals("MyExtruderBean")){
            MyExtruderBean MyExtruderBean= (MyExtruderBean) getIntent().getSerializableExtra(EXTRA_IMAGE_URLS);
            urls.add(MyExtruderBean.getDeviceNoPhoto());
            urls.add(MyExtruderBean.getDevicePhoto());
            urls.add(MyExtruderBean.getDeviceInvoicePhoto());
            urls.add(MyExtruderBean.getDeviceContractPhoto());
            if (!TextUtils.isEmpty(MyExtruderBean.getDeviceCertificatePhoto())){
                urls.add(MyExtruderBean.getDeviceCertificatePhoto());
            }

        }


        mPager = (HackyViewPager) findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        indicator = (TextView) findViewById(R.id.indicator);

        CharSequence text = getString(R.string.hello_world, 1, mPager
                .getAdapter().getCount());
        indicator.setText(text);
        // 更新下标
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.hello_world,
                        arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public List<String> fileList;

        public ImagePagerAdapter(FragmentManager fm, List<String> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position);
            return ImageDetailFragment.newInstance(url);
        }

    }
}
