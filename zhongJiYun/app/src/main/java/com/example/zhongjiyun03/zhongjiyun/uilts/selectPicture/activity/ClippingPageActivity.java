package com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhongjiyun03.zhongjiyun.R;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.constants.ConstantSet;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.utils.BitmapUtils;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.view.CuttingFrameView;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.view.PerfectControlImageView;
import com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.view.SpinnerProgressDialoag;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by yinwei on 2015-11-12.
 */
public class ClippingPageActivity extends AppCompatActivity {


    private PerfectControlImageView imageView;
    private CuttingFrameView cuttingFrameView;
    private Bitmap bitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipping_page);

        initTitle();

        cuttingFrameView = (CuttingFrameView) findViewById(R.id.cutingFrame);
        imageView = (PerfectControlImageView) findViewById(R.id.targetImage);

        if (getIntent().getStringExtra("type").equals("takePicture")) {
            bitmap = BitmapUtils.DecodLocalFileImage(ConstantSet.LOCALFILE + ConstantSet.USERTEMPPIC, this);

        } else {
            String path=getIntent().getStringExtra("path");
            bitmap = BitmapUtils.DecodLocalFileImage(path, this);


        }

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }

    }


    /**
     * 初始化title
     */
    private void initTitle() {

        ImageView rightImage = (ImageView) findViewById(R.id.id_img_right);
        TextView title = (TextView) findViewById(R.id.id_title);
        TextView righttext=(TextView)findViewById(R.id.id_text_right);
        ImageView back = (ImageView) findViewById(R.id.id_back);
        rightImage.setVisibility(View.GONE);
        righttext.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.picture_cutting));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClippingPageActivity.this.finish();
            }
        });

        righttext.setText(getResources().getString(R.string.sure));
        righttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SpinnerProgressDialoag sp=new SpinnerProgressDialoag(ClippingPageActivity.this);
                sp.show();
                Bitmap bitmap=cuttingFrameView.takeScreenShot(ClippingPageActivity.this);
                //SDCardUtils.saveMyBitmap(ConstantSet.LOCALFILE, ConstantSet.USERPIC, bitmap);
                //Bitmap compressBitmap=compressImage(bitmap);
                Intent it=new Intent();
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte [] bitmapByte =baos.toByteArray();
                it.putExtra("result", bitmapByte);
                setResult(RESULT_OK, it);
                sp.dismiss();
                ClippingPageActivity.this.finish();


            }
        });
    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
