package com.example.zhongjiyun03.zhongjiyun;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.zhongjiyun03.zhongjiyun.fragment.FragmentTabAdapter;
import com.example.zhongjiyun03.zhongjiyun.fragment.HomeFragment;
import com.example.zhongjiyun03.zhongjiyun.fragment.MineFragment;
import com.example.zhongjiyun03.zhongjiyun.fragment.SeekMachinistFragment;
import com.example.zhongjiyun03.zhongjiyun.fragment.SeekProjectFragment;
import com.example.zhongjiyun03.zhongjiyun.http.AppUtilsUrl;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private ArrayList<Fragment> fragments=new ArrayList<Fragment>();
    @ViewInject(R.id.home_rg)
    private RadioGroup homeRG;
    private long mExitTime;
    public Boolean isFirstIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        ViewUtils.inject(this);
        init();
        SharedPreferences pref = this.getSharedPreferences("myActivityName", 0);
        //取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        isFirstIn = pref.getBoolean("isFirstIn", true);
        if(isFirstIn) {
            /*Intent intent = new Intent().setClass(HomeActivity.this,MainActivity.class);
            startActivityForResult(intent,0);*/
            testAddContacts();  //添加联系人
            getVersontData();   //获取本版

        }
        //HomeFragment.setStart(0);
        //startPage();

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        String device_token = UmengRegistrar.getRegistrationId(this);
        //Log.e("shdhdhdh",device_token);
        //开启推送并设置注册的回调处理
        mPushAgent.enable(new IUmengRegisterCallback() {

            @Override
            public void onRegistered(String registrationId) {
                //onRegistered方法的参数registrationId即是device_token
                Log.d("device_token", registrationId);
            }
        });

        PushAgent.getInstance(this).onAppStart();
    }

    private void getVersontData() {
        HttpUtils httpUtils=new HttpUtils();
        RequestParams requestParams=new RequestParams();
        try {
            requestParams.addBodyParameter("versionNo",getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestParams.addBodyParameter("versionType","0");
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUtilsUrl.getVersonData(),requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
             Log.e("获取本版信息",responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("获取本版信息",s);
            }
        });



    }

    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }

    /**
     * 添加联系人
     * 在同一个事务中完成联系人各项数据的添加
     * 使用ArrayList<ContentProviderOperation>，把每步操作放在它的对象中执行
     * */
    public void testAddContacts(){
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = getContentResolver();
        // 第一个参数：内容提供者的主机名
        // 第二个参数：要执行的操作
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();

        // 操作1.添加Google账号，这里值为null，表示不添加
        ContentProviderOperation operation = ContentProviderOperation.newInsert(uri)
                .withValue("account_name", null)// account_name:Google账号
                .build();

        // 操作2.添加data表中name字段
        uri = Uri.parse("content://com.android.contacts/data");
        ContentProviderOperation operation2 = ContentProviderOperation.newInsert(uri)
                // 第二个参数int previousResult:表示上一个操作的位于operations的第0个索引，
                // 所以能够将上一个操作返回的raw_contact_id作为该方法的参数
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/name")
                .withValue("data2", "中基云免费电话")
                .build();

        // 操作3.添加data表中phone字段
        uri = Uri.parse("content://com.android.contacts/data");
        ContentProviderOperation operation3 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                .withValue("data2", "2")
                .withValue("data1", "02038361432")
                .build();
        uri = Uri.parse("content://com.android.contacts/data");
        ContentProviderOperation operation4 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                .withValue("data2", "2")
                .withValue("data1", "02038258459")
                .build();
        uri = Uri.parse("content://com.android.contacts/data");
        ContentProviderOperation operation5 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                .withValue("data2", "2")
                .withValue("data1", "4002029797")
                .build();
        uri = Uri.parse("content://com.android.contacts/data");
        ContentProviderOperation operation6 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                .withValue("data2", "2")
                .withValue("data1", "18515333333")
                .build();

        // 向data表插入头像数据
        ContentValues values = new ContentValues();
        Uri rawContactUri = getContentResolver().insert(
                ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        Bitmap sourceBitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.logo_icon);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 将Bitmap压缩成PNG编码，质量为100%存储
        sourceBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        byte[] avatar = os.toByteArray();
        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, avatar);
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                values);

        operations.add(operation);
        operations.add(operation2);
        operations.add(operation3);
        operations.add(operation4);
        operations.add(operation5);
        operations.add(operation6);

        try {
            resolver.applyBatch("com.android.contacts", operations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init() {
        addFragment();
        FragmentTabAdapter fragmentTabAdapter=new FragmentTabAdapter(HomeActivity.this,fragments,R.id.home_layout,homeRG);


    }



    private void addFragment() {
        fragments.add(new HomeFragment());
        fragments.add(new SeekProjectFragment());
        fragments.add(new SeekMachinistFragment());
        fragments.add(new MineFragment());
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                HomeActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
