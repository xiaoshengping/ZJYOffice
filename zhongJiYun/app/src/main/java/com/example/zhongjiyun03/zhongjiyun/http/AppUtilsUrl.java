package com.example.zhongjiyun03.zhongjiyun.http;

/**
 * Created by ZHONGJIYUN03 on 2016/3/22.
 */
public class AppUtilsUrl {

    public static final String BaseUrl="http://h148a34804.iok.la/";
    //public  static final String ImageBaseUrl="http://www.iclubapps.com/upload/";

    //首页广告
    public static String getAdvertisementData(){
        //Common/GetAdContentList
        return BaseUrl+"appapi/Common/GetAdvertisementList" ;

    }

    //获取验证码
    public static String getCodeData(){
        //appapi/checkcode/sendsms
        return BaseUrl+"appapi/checkcode/sendsms" ;

    }

    //服务商列表
    public static String getServiceListData(){
        //appapi/serviceprovider/list
        return BaseUrl+"appapi/serviceprovider/list" ;

    }
    //服务商详情
    public static String getServiceParticularsData(){
        //appapi/serviceprovider/detail
        return BaseUrl+"appapi/serviceprovider/detail" ;

    }

    //合作伙伴
    public static String getCooperationListData(){
        //cooperativebusiness/list
        return BaseUrl+"appapi/cooperativebusiness/list" ;

    }

    //注册上传照片
    public static String getPhoneData(){
        // appapi/Media/UploadImg
        return BaseUrl+"appapi/Media/UploadImgFile" ;

    }
    //获取红包列表
    public static String getRedPacketListData(){
        // appapi/giftbag/list
        return BaseUrl+"appapi/giftbag/list" ;

    }
    //领取红包
    public static String getRedPacketData(){
        // appapi/user/ReceiveGifts
        return BaseUrl+"appapi/user/ReceiveGifts" ;

    }
    //注册上传字符内容
    public static String getRegisterData(){
        // appapi/giftbag/list
        return BaseUrl+"appapi/boss/register" ;

    }
    //登录
    public static String getLoginData(){
        // appapi/user/login
        return BaseUrl+"appapi/user/login" ;

    }
    //获取用户信息
    public static String getUserInformationData(){
        // appapi/user/getinfo
        return BaseUrl+"appapi/user/getinfo" ;

    }
    //我的钻机列表
    public static String getMyExtruderListData(){
        // appapi/boss/devices
        return BaseUrl+"appapi/boss/devices" ;

    }
    //添加钻机
    public static String getAddMyExtruderData(){
        // appapi/boss/createdevice
        return BaseUrl+"appapi/boss/createdevice" ;

    }

    //二手钻机列表
    public static String getSecondExtruderData(){
        // appapi/Boss/GetDevicesecondhandListByPage
        return BaseUrl+"appapi/Boss/GetDevicesecondhandListByPage" ;

    }
    //二手钻机列表详情
    public static String getSecondExtruderParticualsData(){
        // appapi/Boss/GetDevicesecondhandListById
        return BaseUrl+"appapi/Boss/GetDevicesecondhandListById" ;

    }
    //测试
    public static String getMessageData(){
        // appapi/Common/GetSystemMessageList
        return BaseUrl+"appapi/user/GetInfo" ;

    }
    //系统消息
    public static String getMessageListData(){
        // appapi/Common/GetSystemMessageList
        return BaseUrl+"appapi/Common/GetSystemMessageList" ;

    }
    //出租出售
    public static String getRentOrSellData(){
        // appapi/boss/RentOutOrSellDevice
        return BaseUrl+"appapi/boss/RentOutOrSellDevice" ;

    }
    //新闻列表
    public static String getNewsData(){
        //  appapi/Common/GetNewsListByType
        return BaseUrl+"appapi/Common/GetNewsListByType" ;

    }
    //找机手
    public static String getMachinisData(){
        //  appapi/Boss/GetFindWorkDriver
        return BaseUrl+"appapi/Boss/GetFindWorkDriver" ;

    }
    //找项目
    public static String getProjecctListData(){
        //  appapi/project/ list
        return BaseUrl+"appapi/project/list" ;

    }
    //我的竞标
    public static String getCompetitvetListData(){
        //  appapi/Boss/MyProjectReply
        return BaseUrl+"appapi/Boss/MyProjectReply" ;

    }
    //关注钻机列表
    public static String getAttentionExtrunListData(){
        //  appapi/Common/GetCollectList
        return BaseUrl+"appapi/Common/GetCollectList" ;

    }
    //关注项目列表
    public static String getFacillyData(){
        //   appapi/JsonData/Device
        return BaseUrl+"appapi/JsonData/Device" ;

    }
    //修改手机号码
    public static String getModifyPhoneData(){
        //   appapi/user/modifyphone
        return BaseUrl+"appapi/user/modifyphone" ;

    }
    //意见反馈
    public static String getIdeaFeedBackData(){
        //   appapi/system/SubmitFeedBack
        return BaseUrl+"appapi/system/SubmitFeedBack" ;

    }
    //关注
    public static String getAttentionData(){
        //   appapi/Common/Collecting
        return BaseUrl+"appapi/Common/Collecting" ;

    }

    //取消关注
    public static String getAttentionNoData(){
        //   appapi/Common/CancelCollect
        return BaseUrl+"appapi/Common/CancelCollect" ;

    }




}
