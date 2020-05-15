package com.weiyu.sp.lsjy.net;

import com.weiyu.sp.lsjy.base.BaseObjectBean;
import com.weiyu.sp.lsjy.bean.AccountRecordBean;
import com.weiyu.sp.lsjy.bean.BalanceBean;
import com.weiyu.sp.lsjy.bean.BannerBean;
import com.weiyu.sp.lsjy.bean.CourseBean;
import com.weiyu.sp.lsjy.bean.CourseDetailBean;
import com.weiyu.sp.lsjy.bean.CourseVipBean;
import com.weiyu.sp.lsjy.bean.HomeBean;
import com.weiyu.sp.lsjy.bean.IntegralBean;
import com.weiyu.sp.lsjy.bean.IntegralExchangeBean;
import com.weiyu.sp.lsjy.bean.IntegralListBean;
import com.weiyu.sp.lsjy.bean.IntegralRecordBean;
import com.weiyu.sp.lsjy.bean.LoginBean;
import com.weiyu.sp.lsjy.bean.MeBean;
import com.weiyu.sp.lsjy.bean.OrderBean;
import com.weiyu.sp.lsjy.bean.PayChannel;
import com.weiyu.sp.lsjy.bean.PayResultBean;
import com.weiyu.sp.lsjy.bean.PersonalBean;
import com.weiyu.sp.lsjy.bean.ShareBean;
import com.weiyu.sp.lsjy.bean.StudyBean;
import com.weiyu.sp.lsjy.bean.StudyListBean;
import com.weiyu.sp.lsjy.bean.UpdateBean;
import com.weiyu.sp.lsjy.bean.VipBean;
import com.weiyu.sp.lsjy.bean.VipCardBean;


import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {
    @FormUrlEncoded
    @POST("/api/LoginUserController/userLoginCode.html")
    Flowable<BaseObjectBean<String>>userLoginCode(@FieldMap Map<String,String> map);

    //首页banner
    @GET("/api/LsjyShiPHomeController/selectSpBanner.html")
    Flowable<BaseObjectBean<List<BannerBean>>>getBanner();
    //首页banner
    @GET("/api/LsjyShiPHomeController/selectHomeMaps.html")
    Flowable<BaseObjectBean<HomeBean>>getHomeList();
    //发送短信验证码
    @FormUrlEncoded
    @POST("/api/LoginUserController/userLoginSendSms.html")
    Flowable<BaseObjectBean<String>>sendMsg(@Field("loginName")String loginName );
    //登录接口
    @FormUrlEncoded
    @POST("/api/LoginUserController/userLoginCode.html")
    Flowable<BaseObjectBean<LoginBean>>doLogin(@FieldMap Map<String,String> map);

    //刷新token
    @FormUrlEncoded
    @POST("/api/LoginUserController/refreshLogin.html")
    Flowable<BaseObjectBean<LoginBean>> refreshLogin(@Field("refreshToken")String refreshToken);
    //刷新token
    @FormUrlEncoded
    @POST("/api/LoginUserController/loginOut.html")
    Flowable<BaseObjectBean<String>> loginOut(@Field("refreshToken")String refreshToken);

    //课程查询
    @FormUrlEncoded
    @POST("/api/LsjyHomeMoreController/selectCourseList.html")
    Flowable<BaseObjectBean<List<CourseBean>>>selectCourseList(@FieldMap Map<String,String> map);

    //学习列表
    @FormUrlEncoded
    @POST("/api/LsjyHomeMoreController/selectCourseList.html")
    Flowable<BaseObjectBean<StudyListBean>>selectCourseList3(@FieldMap Map<String,String> map);

    //我的收藏
    @FormUrlEncoded
    @POST("/api/LsjyMyContoller/selectCollectionList.html")
    Flowable<BaseObjectBean<List<CourseBean>>>selectCourseList2(@FieldMap Map<String,String> map);

    //大家都在搜
    @GET("/api/LsjyHomeMoreController/hotKeyword.html")
    Flowable<BaseObjectBean<List<String>>>hotSearch();

    //收藏
    @FormUrlEncoded
    @POST("/api/LsjyHomeMoreController/courseCollection.html")
    Flowable<BaseObjectBean<String>>courseCollection(@FieldMap Map<String,String> map);

    //课程详情
    @FormUrlEncoded
    @POST("/api/LsjyCourseContoller/coureseDetail.html")
    Flowable<BaseObjectBean<CourseDetailBean>>courseDetail(@Field("courseId")String courseId);

    //学习
    @FormUrlEncoded
    @POST("/api/LsjyStudyContoller/startStudy.html")
    Flowable<BaseObjectBean<String>>startStudy(@Field("courseId")String courseId);

    //激活vip
    @FormUrlEncoded
    @POST("/api/LsjyMyContoller/activeVip.html")
    Flowable<BaseObjectBean<String>>activeVip(@Field("activeCode")String activeCode);

    //支付通道
    @GET("/api/LsjyMyContoller/selectPayChannelList.html")
    Flowable<BaseObjectBean<VipBean>>selectPayChannelList();

    //购买vip
    @FormUrlEncoded
    @POST("/api/LsjyCourseContoller/buyVip.html")
    Flowable<BaseObjectBean<PayResultBean>>buyVip(@FieldMap Map<String,String> map);
    //购买课程
    @FormUrlEncoded
    @POST("/api/LsjyCourseContoller/buyCourse.html")
    Flowable<BaseObjectBean<PayResultBean>>buyCourse(@FieldMap Map<String,String> map);
    //购买课程
    @FormUrlEncoded
    @POST("/api/LsjyCourseContoller/buyCourseDetail.html")
    Flowable<BaseObjectBean<CourseVipBean>>buyCourseDetail(@FieldMap Map<String,String> map);


    //我的
    @GET("/api/LsjyMyContoller/selectUserInfo.html")
    Flowable<BaseObjectBean<MeBean>>meDetail();

    //邀请码
    @FormUrlEncoded
    @POST("/api/LsjyCourseContoller/registerShareInfo.html")
    Flowable<BaseObjectBean<String>>registerShareInfo(@Field("inviteCode")String inviteCode);


    //个人中心
    @FormUrlEncoded
    @POST("/api/LsjyMyContoller/selectUserDetailInfo.html")
    Flowable<BaseObjectBean<PersonalBean>>selectUserDetail(@FieldMap Map<String,String> map);
    //个人中心
    @FormUrlEncoded
    @POST("/api/LsjyMyContoller/updateUserDetailInfo.html")
    Flowable<BaseObjectBean<PersonalBean>>updateUserDetail(@FieldMap Map<String,String> map);

    //账户明细
    @FormUrlEncoded
    @POST("/api/LsjyMyContoller/selectAccoutDetailInfo.html")
    Flowable<BaseObjectBean<List<AccountRecordBean>>>getAccountRecordDetail(@FieldMap Map<String,String> map);


    //账户管理
    @GET("/api/LsjyMyContoller/selectAccoutInfo.html")
    Flowable<BaseObjectBean<BalanceBean>>selectAccountInfo();
    //积分管理
    @GET("/api/LsjyMyContoller/selectIntegralInfo.html")
    Flowable<BaseObjectBean<IntegralBean>>selectIntegralInfo();

    //积分兑换列表
    @FormUrlEncoded
    @POST("/api/LsjyMyContoller/selectGoods.html")
    Flowable<BaseObjectBean<List<IntegralListBean>>>selectGoods(@FieldMap Map<String,String> map);
    //积分兑换
    @FormUrlEncoded
    @POST("/api/LsjyMyContoller/exchangeGoods.html")
    Flowable<BaseObjectBean<IntegralExchangeBean>>exchangeGoods(@FieldMap Map<String,String> map);

    //积分账单
    @FormUrlEncoded
    @POST("/api/LsjyMyContoller/selectIntegralDetailInfo.html")
    Flowable<BaseObjectBean<List<IntegralRecordBean>>>integralRecordList(@FieldMap Map<String,String> map);

    //积分账单
    @FormUrlEncoded
    @POST("/api/LsjyMyContoller/selectOrderList.html")
    Flowable<BaseObjectBean<List<OrderBean>>> getOrderList(@FieldMap Map<String,String> map);

    //vip中心
    @GET("/api/LsjyMyContoller/memberCenter.html")
    Flowable<BaseObjectBean<VipCardBean>> getMemberCenter();

    //意见反馈
    @FormUrlEncoded
    @POST("/api/LsjyMyContoller/feedback.html")
    Flowable<BaseObjectBean<String>> postOpinion(@FieldMap Map<String,String> map);

    //学习时长
    @GET("/api/LsjyStudyContoller/studyLength.html")
    Flowable<BaseObjectBean<StudyBean>> studyLength();

    //分享
    @GET("/api/LsjyCourseContoller/appShare.html")
    Flowable<BaseObjectBean<ShareBean>> appShare();

    //升级
    @FormUrlEncoded
    @POST("api/LsjyShiPHomeController/editionUpdate.html")
    Flowable<BaseObjectBean<UpdateBean>> update(@FieldMap Map<String,String> map);


}
