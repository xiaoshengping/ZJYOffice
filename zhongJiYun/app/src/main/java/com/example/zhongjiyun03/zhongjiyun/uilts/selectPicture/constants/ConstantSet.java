package com.example.zhongjiyun03.zhongjiyun.uilts.selectPicture.constants;

import android.os.Environment;

import java.io.File;

/**
 * Created by yinwei on 2015-11-17.
 */
public class ConstantSet {

    //文件


    public static final String LOCALFILE = Environment.
            getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "CompleteUpdateHeader" + File.separator + "userPic" + File.separator;

    public static final String USERTEMPPIC="userTemp.jpg";

    public static final String USERPIC="user.jpg";

    public static final String IMAGE_CACHE_DIRECTORY="images";

    //图片裁剪

    public static final int TAKEPICTURE = 0X1;    //身份证正面

    public static final int SELECTPICTURE = 0X2;

    public static final int CROPPICTURE = 0X3;

    public static final int TAKEPICTURE0 = 0X4;   //身份证反面

    public static final int SELECTPICTURE0 = 0X5;

    public static final int CROPPICTURE0 = 0X6;

    public static final int TAKEPICTURE1 = 0X7;   //个人照片

    public static final int SELECTPICTURE1 = 0X8;

    public static final int CROPPICTURE1 = 0X9;

    public static final int TAKEPICTURE2 = 0X10;   //身份证正面

    public static final int SELECTPICTURE2 = 0X11;

    public static final int CROPPICTURE2 = 0X12;

    public static final int TAKEPICTURE3 = 0X13;   //身份证反面

    public static final int SELECTPICTURE3 = 0X14;

    public static final int CROPPICTURE3 = 0X15;

    public static final int TAKEPICTURE4 = 0X16;   //个人照片

    public static final int SELECTPICTURE4 = 0X17;

    public static final int CROPPICTURE4 = 0X18;

    public static final int TAKEPICTURE5 = 0X19;   //营业执照

    public static final int SELECTPICTURE5 = 0X20;

    public static final int CROPPICTURE5 = 0X21;

    public static final int TAKEPICTURE6 = 0X22;   //证书

    public static final int SELECTPICTURE6 = 0X23;

    public static final int CROPPICTURE6 = 0X24;

}
