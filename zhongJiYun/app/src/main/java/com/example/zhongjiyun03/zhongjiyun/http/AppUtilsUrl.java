package com.example.zhongjiyun03.zhongjiyun.http;

/**
 * Created by ZHONGJIYUN03 on 2016/3/22.
 */
public class AppUtilsUrl {

    public static final String BaseUrl="http://h148a34804.iok.la/";
    //public  static final String ImageBaseUrl="http://www.zhongjiyun.cn/""http://h148a34804.iok.la/""http://dev.zhongjiyun.cn/";

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
        // appapi/shopapi/receivegifts
        return BaseUrl+"appapi/shopapi/receivegifts" ;

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
    //撤回出租出售
    public static String getRecallRentOrSellData(){
        // appapi/boss/canclerentoutorsell
        return BaseUrl+"appapi/boss/canclerentoutorsell" ;

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
    //项目详情
    public static String getProjecctParticularsData(){
        //   appapi/project/detail
        return BaseUrl+"appapi/project/detail" ;

    }
    //我的竞标
    public static String getCompetitvetListData(){
        //  appapi/Boss/MyProjectReply
        return BaseUrl+"appapi/Boss/MyProjectReply" ;

    }
    //关注项目列表
    public static String getAttentionProjectListData(){
        // appapi/Common/GetProjectCollectList
        return BaseUrl+"appapi/Common/GetProjectCollectList" ;

    }//关注的钻机
    public static String getAttentionExtrunListData(){
        // appapi/Common/GetDeviceSecondHandCollectList
        return BaseUrl+"appapi/Common/GetDeviceSecondHandCollectList" ;

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
    //取消关注
    public static String getCompetitiveDescribeData(){
        //   appapi/boss/SubmitReplyProject
        return BaseUrl+"appapi/boss/SubmitReplyProject" ;

    }
    //我的评论
    public static String getMineCommentData(){
        //  appapi/boss/getmyevaluate
        return BaseUrl+"appapi/boss/getmyevaluate" ;
    }
    //获得系统消息提醒
    public static String getSystemMessageRemindData(){
        //  appapi/system/getnewdatastate
        return BaseUrl+"appapi/system/getnewdatastate" ;

    }
    //评论我的
    public static String getCommentMineData(){
        //  appapi/boss/getownerevaluate
        return BaseUrl+"appapi/boss/getownerevaluate" ;

    }
    //评价业主
    public static String getCommentOwnerData(){
        //  appapi/boss/ownerevaluete
        return BaseUrl+"appapi/boss/ownerevaluete" ;

    }
    //修改钻机出租出售信息
    public static String getModifiExtruderData(){
        //  appapi/boss/ownerevaluete
        return BaseUrl+"appapi/device/getsecondhanddetail" ;

    }

    //获取本版信息
    public static String getVersonData(){
        //  appapi/system/checkappversion
        return BaseUrl+"appapi/system/checkappversion" ;

    }
    //获取本版信息
    public static String getCallOwerData(){
        //  appapi/communicationapi/callowner
        return BaseUrl+"appapi/communicationapi/callowner" ;

    }

    //提交用户信息
    public static String getRegistrationData(){
        //  appapi/system/submitjpush
        return BaseUrl+"appapi/system/submitjpush" ;

    }

    //分享二手机列表获取红包
    public static String getShareRedPacketData(){
        //  appapi/shopapi/sharpsecondhandlist
        return BaseUrl+"appapi/shopapi/sharpsecondhandlist" ;

    }
    //分享二手机获取红包
    public static String getShareRedPacketParticulasrData(){
        //  shopapi/sharpsecondhanddetail
        return BaseUrl+"shopapi/sharpsecondhanddetail" ;

    }

    //向服务器提交错误信息
    public static String getLoggerData(){
        //  appapi/logger/upload
        return BaseUrl+"appapi/logger/upload" ;

    }
    //分享商城获得云币
    public static String getMarketPackedData(){
        //  appapi/logger/upload
        return BaseUrl+"store/mobile/games/app_adpt.php?aspid=ASP_USER_ID" ;

    }
    //删除钻机
    public static String getDeleteExtruderDevice(){
        //   appapi/boss/deletedevice
        return BaseUrl+"appapi/boss/deletedevice" ;

    }
    //防止sesstion丢失登录
    public static String getUserLoginData(){
        //   appapi/user/userlogin
        return BaseUrl+"appapi/user/userlogin" ;

    }

}
