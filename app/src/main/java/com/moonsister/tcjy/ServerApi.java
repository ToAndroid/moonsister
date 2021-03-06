package com.moonsister.tcjy;


import com.moonsister.pay.tencent.PayBean;
import com.moonsister.tcjy.bean.BackInsertBean;
import com.moonsister.tcjy.bean.BackTermsBean;
import com.moonsister.tcjy.bean.BalanceBean;
import com.moonsister.tcjy.bean.BannerBean;
import com.moonsister.tcjy.bean.BaseBean;
import com.moonsister.tcjy.bean.CardInfoBean;
import com.moonsister.tcjy.bean.CertificationStatusBean;
import com.moonsister.tcjy.bean.ChooseKeyBean;
import com.moonsister.tcjy.bean.CommentDataListBean;
import com.moonsister.tcjy.bean.DefaultDataBean;
import com.moonsister.tcjy.bean.DownApkBean;
import com.moonsister.tcjy.bean.DynamicBean;
import com.moonsister.tcjy.bean.DynamicDatailsBean;
import com.moonsister.tcjy.bean.EngagemengOrderBean;
import com.moonsister.tcjy.bean.EngagemengRecommendBean;
import com.moonsister.tcjy.bean.EngagementManagerBean;
import com.moonsister.tcjy.bean.FeelingSquareFilterBean;
import com.moonsister.tcjy.bean.FeelingSquarePMDBean;
import com.moonsister.tcjy.bean.FeelingSquareSearchBean;
import com.moonsister.tcjy.bean.FrientBaen;
import com.moonsister.tcjy.bean.GetMoneyBean;
import com.moonsister.tcjy.bean.GoodSelectBaen;
import com.moonsister.tcjy.bean.HomeThreeFragmentBean;
import com.moonsister.tcjy.bean.HomeTopItemBean;
import com.moonsister.tcjy.bean.InsertBaen;
import com.moonsister.tcjy.bean.KeyMateBean;
import com.moonsister.tcjy.bean.LableBean;
import com.moonsister.tcjy.bean.LoginBean;
import com.moonsister.tcjy.bean.MyThreeFragmentBean;
import com.moonsister.tcjy.bean.NearbyBean;
import com.moonsister.tcjy.bean.PayRedPacketPicsBean;
import com.moonsister.tcjy.bean.PersonalMessageFenBean;
import com.moonsister.tcjy.bean.PersonalReviseMessageBean;
import com.moonsister.tcjy.bean.PingbiBean;
import com.moonsister.tcjy.bean.RankBean;
import com.moonsister.tcjy.bean.RecommendMemberFragmentBean;
import com.moonsister.tcjy.bean.RegFourBean;
import com.moonsister.tcjy.bean.RegOneBean;
import com.moonsister.tcjy.bean.RegThridBean;
import com.moonsister.tcjy.bean.RegiterBean;
import com.moonsister.tcjy.bean.IMDataBean;
import com.moonsister.tcjy.bean.SearchReasonBaen;
import com.moonsister.tcjy.bean.StatusBean;
import com.moonsister.tcjy.bean.TiXinrRecordBean;
import com.moonsister.tcjy.bean.UserDetailBean;
import com.moonsister.tcjy.bean.UserFriendListBean;
import com.moonsister.tcjy.bean.UserInfoBannerBean;
import com.moonsister.tcjy.bean.UserInfoChangeBean;
import com.moonsister.tcjy.bean.UserInfoDetailBean;
import com.moonsister.tcjy.bean.UserInfoListBean;
import com.moonsister.tcjy.bean.UserPermissionBean;
import com.moonsister.tcjy.bean.VersionInfo;
import com.moonsister.tcjy.bean.WithdRawDepositBean;
import com.moonsister.tcjy.utils.LogUtils;
import com.moonsister.tcjy.utils.UnicodeUtils;
import com.moonsister.tcjy.utils.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * * Created by pc on 2016/6/12.
 */

public class ServerApi {
    private static AppAPI mAppApi;
    private static String TAG = "ServerApi";

    public static AppAPI getAppAPI() {
        if (mAppApi == null) {
            if (LogUtils.getDeBugState()) {
                Interceptor mTokenInterceptor = new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        LogUtils.d(TAG, "addNetworkInterceptor : Response  code: " + response.code());
                        BufferedSource source = response.body().source();
                        source.request(Long.MAX_VALUE);
                        Buffer clone = source.buffer().clone();
                        LogUtils.d(TAG, "addNetworkInterceptor : Response  content: " + UnicodeUtils.decodeUnicode(clone.readUtf8()));
                        return response;
                    }

                };
                // init okhttp 3 logger
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        LogUtils.e(TAG, (message.startsWith("{") ? UnicodeUtils.decodeUnicode(message) : message));
                    }

                });
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                //401 Not Authorised
                Authenticator mAuthenticator = new Authenticator() {

                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        Request request = response.request();
                        LogUtils.d(TAG, "Authenticator : The Cookie is " + request.header("Cookie"));
                        LogUtils.e(TAG, "Authenticator : 访问网络地址: " + request.url().toString());
                        LogUtils.d(TAG, "Authenticator : 访问body : " + request.body().toString());
                        return request;
                    }
                };

                Interceptor interceptor = new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        LogUtils.d(TAG, "addInterceptor : 访问request : " + chain.request().toString());
                        Response response = chain.proceed(chain.request());

                        LogUtils.d(TAG, "addInterceptor : Response  code: " + response.code());
                        BufferedSource source = response.body().source();
                        source.request(Long.MAX_VALUE);
                        Buffer clone = source.buffer().clone();
                        LogUtils.d(TAG, "addInterceptor : Response  content: " + UnicodeUtils.decodeUnicode(clone.readUtf8()));
                        return response;
                    }
                };

                //OkHttpClient
                OkHttpClient httpClient = new OkHttpClient.Builder()
                        .addInterceptor(logging)
                        .addInterceptor(interceptor)
                        .retryOnConnectionFailure(true)
//                        .authenticator(mAuthenticator)
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .addNetworkInterceptor(mTokenInterceptor)
                        .build();
                //Retrofit.
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(AppAPI.baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .client(httpClient)
                        .build();

                mAppApi = retrofit.create(AppAPI.class);
            } else {
                //OkHttpClient
                OkHttpClient httpClient = new OkHttpClient.Builder()
                        .retryOnConnectionFailure(true)
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .build();
                //Retrofit.
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(AppAPI.baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .client(httpClient)
                        .build();

                mAppApi = retrofit.create(AppAPI.class);
            }
        }
        return mAppApi;
    }


    public interface AppAPI {
//                String baseUrl = "http://3test.yytbzs.cn:88/index.php/index/";
        String baseUrl = "http://3.yytbzs.cn:88/index.php/index/";
//        String baseUrl = "http://2.yytbzs.cn:88/index.php/index/";
//        String baseUrl = "http://mimei.cntttt.com:88/public/index.php/index/";

        /**
         * 登录
         *
         * @param username
         * @param password
         * @return
         */
        @FormUrlEncoded
        @POST("User/login")
        Observable<LoginBean> login(
                @Field("name") String username,
                @Field("pwd") String password,
                @Field("channel") String channel
        );

        /**
         * 获取验证码
         *
         * @param mobile
         * @return
         */
        @FormUrlEncoded
        @POST("User/register_send_mobile_code")
        Observable<BaseBean> sendSecurityCode(@Field("mobile") String mobile,
                                              @Field("channel") String channel);

        /**
         * 验证验证码
         *
         * @param mobile
         * @param code
         * @return
         */
        @FormUrlEncoded
        @POST("User/register_step1")
        Observable<RegiterBean> verifySecurityCode(@Field("mobile") String mobile,
                                                   @Field("code") String code,
                                                   @Field("channel") String channel);

        /**
         * 注册完成
         *
         * @param face
         * @param sex
         * @param pwd
         * @return
         */
        @FormUrlEncoded
        @POST("User/register_step2")
        Observable<BaseBean> regiterLogin(@Field("face") String face,
                                          @Field("sex") String sex,
                                          @Field("pwd") String pwd,
                                          @Field("channel") String channel,
                                          @Field("mobileauth") String mobileauth);

        /**
         * 精选数据
         *
         * @param type
         * @param page
         * @return
         */
        @GET("Userrec/get_list_jingxuan")
        Observable<GoodSelectBaen> getGoodSelect(@Query("type") String type,
                                                 @Query("page") int page,
                                                 @Query("authcode") String authcode,
                                                 @Query("channel") String channel);

        /**
         * 同城
         *
         * @param type
         * @param page
         * @param authcode
         * @return
         */
        @GET("Userrec/get_list_tongcheng")
        Observable<GoodSelectBaen> getSameCity(@Query("type") String type,
                                               @Query("page") int page,
                                               @Query("authcode") String authcode,
                                               @Query("channel") String channel);

        /**
         * 个人动态信息列表
         *
         * @param uid
         * @param authcode
         * @return
         */
        @GET("User/user_detail_addon1")
        Observable<UserInfoDetailBean> getUserInfoDetail(@Query("uid") String uid,
                                                         @Query("authcode") String authcode,
                                                         @Query("channel") String channel);

        /**
         * 个人动态列表
         *
         * @param userId
         * @param authcode
         * @param page
         * @return
         */
        @GET("Latest/get_latest_list")
        Observable<UserInfoListBean> getPersonDynamincList(@Query("uid") String userId,
                                                           @Query("page") int page,
                                                           @Query("ttype") int searchType,
                                                           @Query("authcode") String authcode,
                                                           @Query("channel") String channel);

        /**
         * 动态评论列表
         *
         * @param id
         * @param page
         * @param authcode
         * @return
         */
        @GET("Latestcomment/get_latest_comment")
        Observable<CommentDataListBean> getCommentList(@Query("lid") String id,
                                                       @Query("page") int page,
                                                       @Query("authcode") String authcode,
                                                       @Query("channel") String channel);

        /**
         * 动态发布
         *
         * @param type
         * @param content
         * @param address
         * @param authcode
         * @return
         */
        @FormUrlEncoded
        @POST("Latest/insert_latest")
        Observable<BaseBean> sendAllDefaultDynamic(@Field("type") int type,
                                                   @Field("title") String title,
                                                   @Field("contents") String content,
                                                   @Field("tags") String tags,
                                                   @Field("address") String address,
                                                   @Field("authcode") String authcode,
                                                   @Field("channel") String channel
        );

        /**
         * 打赏
         *
         * @param playType
         * @param uid
         * @param money
         * @param authcode
         * @return
         */
        @FormUrlEncoded
        @POST("Reward/pub_reward")
        Observable<PayBean> getredPacketIndent(@Field("pay_type") String playType,
                                               @Field("to_uid") String uid,
                                               @Field("money") String money,
                                               @Field("authcode") String authcode,
                                               @Field("channel") String channel);

        /**
         * 送花
         *
         * @param type
         * @param uid
         * @param money
         * @param authcode
         * @return
         */
        @FormUrlEncoded
        @POST("Flower/pub_flower")
        Observable<PayBean> getFlowerIndent(@Field("pay_type") String type,
                                            @Field("to_uid") String uid,
                                            @Field("money") String money,
                                            @Field("authcode") String authcode,
                                            @Field("channel") String channel);

        /**
         * 获取token
         *
         * @param authcode
         * @return
         */
        @GET("Rong/get_token")
        Observable<IMDataBean> getRongyunKey(@Query("authcode") String authcode,
                                             @Query("channel") String channel);

        /**
         * 朋友圈
         *
         * @param authcode
         * @param page
         * @return
         */
        @GET("Latest/get_latests_friends")
        Observable<UserInfoListBean> loadPersonDynamic(@Query("authcode") String authcode,
                                                       @Query("page") int page,
                                                       @Query("channel") String channel);

        /**
         * 获取个人的信息
         *
         * @param authcode
         * @return
         */
        @GET("User/user_detail_addon1_center")
        Observable<UserInfoDetailBean> loadPersonInfo(@Query("authcode") String authcode,
                                                      @Query("channel") String channel);

        /**
         * 动态红包支付
         *
         * @param id
         * @param authcode
         * @return
         */
        @FormUrlEncoded
        @POST("Latestbuy/latest_buy")
        Observable<PayBean> redPacketPay(@Field("latest_id") String id,
                                         @Field("pay_type") String type,
                                         @Field("pay_cash_type") String payType,
                                         @Field("version_type") String version_type,
                                         @Field("authcode") String authcode,
                                         @Field("channel") String channel);

        /**
         * 红包图片
         *
         * @param id
         * @param authcode
         * @return
         */
        @GET("Latest/get_latest_pay")
        Observable<PayRedPacketPicsBean> getPayDynamicPic(@Query("latest_id") String id,
                                                          @Query("authcode") String authcode,
                                                          @Query("channel") String channel);

        /**
         * @param address1
         * @param address2
         * @param height
         * @param sexid
         * @param nike
         * @param loadFile
         * @param serialize
         * @param authcode
         * @return
         */
        @FormUrlEncoded
        @POST("Apply/goadd_info")
        Observable<DefaultDataBean> sendAllRzInfo(@Field("province") String address1,
                                                  @Field("city") String address2,
                                                  @Field("height") String height,
                                                  @Field("sex") String sexid,
                                                  @Field("nickname") String nike,
                                                  @Field("face") String loadFile,
                                                  @Field("apply_image") String serialize,
                                                  @Field("order_id") String orderid,
                                                  @Field("authcode") String authcode,
                                                  @Field("channel") String channel);

        /**
         * 获取认证状态
         *
         * @param authcode
         * @return
         */
        @GET("Apply/get_apply_status")
        Observable<CertificationStatusBean> getCertificationStatus(@Query("authcode") String authcode,
                                                                   @Query("channel") String channel);

        /**
         * 提现列表
         *
         * @param authcode
         * @return
         */
        @GET("Withdraw/get_withdraw_list")
        Observable<TiXinrRecordBean> getTixinRecord(@Query("authcode") String authcode,
                                                    @Query("channel") String channel);

        @GET("Withdraw/get_withdraw_money")
        Observable<WithdRawDepositBean> getEnableMoney(@Query("authcode") String authcode,
                                                       @Query("vvv") String apiVersions,
                                                       @Query("channel") String channel);

        /**
         * 提现基本信息
         *
         * @param authcode
         * @return
         */
        @GET("Account/get_latest_info")
        Observable<GetMoneyBean> getGetmoney(@Query("authcode") String authcode,
                                             @Query("channel") String channel);

        /**
         * 添加银行卡
         *
         * @param cardNumber
         * @param username
         * @param cardType
         * @param bankname
         * @param authcode
         * @return
         */
        @FormUrlEncoded
        @POST("Account/add")
        Observable<DefaultDataBean> getsubmitAccount(@Field("bank_no") String cardNumber,
                                                     @Field("name") String username,
                                                     @Field("type") String cardType,
                                                     @Field("bank_name") String bankname,
                                                     @Field("authcode") String authcode,
                                                     @Field("channel") String channel);

        /**
         * 提现
         *
         * @param number
         * @param money
         * @param authcode
         * @return
         */
        @FormUrlEncoded
        @POST("Withdraw/add")
        Observable<DefaultDataBean> getTiXianReason(@Field("account_id") String number,
                                                    @Field("money") int money,
                                                    @Field("authcode") String authcode,
                                                    @Field("channel") String channel);

        /**
         * 银行卡列表
         *
         * @param authcode
         * @return
         */
        @GET("Account/get_list")
        Observable<CardInfoBean> getCardInfo(@Query("authcode") String authcode,
                                             @Query("channel") String channel);

        /**
         * 关注
         *
         * @param uid
         * @param type     1关注操作，2取消关注操作
         * @param authcode
         * @return
         */
        @FormUrlEncoded
        @POST("Follow/follow_act")
        Observable<DefaultDataBean> getWacthAction(@Field("touid") String uid,
                                                   @Field("type") String type,
                                                   @Field("authcode") String authcode,
                                                   @Field("channel") String channel);

        /**
         * 发布评论
         *
         * @param id
         * @param content
         * @param pid
         * @param authcode
         * @return
         */
        @FormUrlEncoded
        @POST("Latestcomment/insert_latest_comment")
        Observable<DefaultDataBean> getSendComment(@Field("lid") String id,
                                                   @Field("title") String content,
                                                   @Field("pid") String pid,
                                                   @Field("authcode") String authcode,
                                                   @Field("channel") String channel);

        /**
         * 关注列表
         *
         * @param page
         * @param authcode
         * @return
         */
        @GET("Follow/get_list_follows")
        Observable<FrientBaen> getWacthList(@Query("page") int page,
                                            @Query("authcode") String authcode,
                                            @Query("channel") String channel);


        /**
         * 关注列表
         *
         * @param page
         * @param authcode
         * @return
         */
        @GET("Follow/get_list_fans")
        Observable<FrientBaen> getFenList(@Query("page") int page,
                                          @Query("authcode") String authcode,
                                          @Query("channel") String channel);

        /**
         * @param key
         * @param page
         * @param authcode @return
         */
        @GET("User/search")
        Observable<SearchReasonBaen> getSearchReason(@Query("name") String key,
                                                     @Query("page") int page,
                                                     @Query("authcode") String authcode,
                                                     @Query("channel") String channel);

        /**
         * 用户的信息
         *
         * @param uid
         * @return
         */
        @GET("User/user_detail")
        Observable<UserInfoChangeBean> getUserInfoBasic(@Query("uid") String uid,
                                                        @Query("channel") String channel);

        /**
         * 修正资料
         *
         * @return
         */
        @FormUrlEncoded
        @POST("User/edit_profile")
        Observable<DefaultDataBean> getChangeInfo(@Field("face") String face,
                                                  @Field("birthday") String birth,
                                                  @Field("nickname") String nickname,
                                                  @Field("height") String height,
                                                  @Field("prifession") String prifession,
                                                  @Field("residence") String addrss,
                                                  @Field("city") String city,
                                                  @Field("signature") String signature,
                                                  @Field("weight") String weight,
                                                  @Field("sex") String sex,
                                                  @Field("degree") String degree,
                                                  @Field("self_intro") String self_intro,
                                                  @Field("authcode") String authcode,
                                                  @Field("channel") String channel);

        /**
         * 修改密码
         *
         * @param oldpwd
         * @param newpwd
         * @return
         */
        @FormUrlEncoded
        @POST("User/edit_pwd")
        Observable<DefaultDataBean> getChangePassword(@Field("pwd1") String oldpwd,
                                                      @Field("pwd2") String newpwd,
                                                      @Field("authcode") String authcode,
                                                      @Field("channel") String channel);

        /**
         * 找回密码 验证码
         *
         * @param phoneNumber
         * @return
         */
        @FormUrlEncoded
        @POST("User/findpwd_send_mobile_code")
        Observable<BaseBean> getSecurityCode(@Field("mobile") String phoneNumber,
                                             @Field("channel") String channel);

        /**
         * 找回密码 验证验证码
         *
         * @param phone
         * @param code
         * @return
         */
        @FormUrlEncoded
        @POST("User/findpwd_step1")
        Observable<RegiterBean> getFindPasswordSecurity(@Field("mobile") String phone,
                                                        @Field("code") String code,
                                                        @Field("channel") String channel);

        /**
         * 找回密码 输入新密码
         *
         * @param newpwd
         * @param code
         * @return
         */
        @FormUrlEncoded
        @POST("User/findpwd_step2")
        Observable<BaseBean> getFindPasswordNext(@Field("pwd") String newpwd,
                                                 @Field("mobileauth") String code,
                                                 @Field("channel") String channel);

        /**
         * 更新背景图
         *
         * @param path
         * @param authcode
         * @return
         */
        @FormUrlEncoded
        @POST("User/edit_like_image")
        Observable<DefaultDataBean> getUploadBackground(@Field("like_image") String path,
                                                        @Field("authcode") String authcode,
                                                        @Field("channel") String channel);

        /**
         * 点赞
         * type 操作类型 1顶 2取消顶，3踩，4取消踩
         *
         * @param authcode
         * @return
         */
        @FormUrlEncoded
        @POST("Latest/updown")
        Observable<DefaultDataBean> getLikeAction(@Field("latest_id") String dynamicId,
                                                  @Field("type") String type,
                                                  @Field("authcode") String authcode,
                                                  @Field("channel") String channel);

        /**
         * 删除动态
         *
         * @param id
         * @param authcode
         * @return
         */
        @FormUrlEncoded
        @POST("Latest/del_latest")
        Observable<DefaultDataBean> getDelectDynamic(@Field("latest_id") String id,
                                                     @Field("authcode") String authcode,
                                                     @Field("channel") String channel);

        /**
         * 置顶
         *
         * @param id
         * @param authcode
         * @param channel
         * @return
         */
        @FormUrlEncoded
        @POST("Latesttop/ins")
        Observable<DefaultDataBean> getupDynamic(@Field("latest_id") String id,
                                                 @Field("authcode") String authcode,
                                                 @Field("channel") String channel);

        /**
         * 取消置顶
         *
         * @param id
         * @param authcode
         * @param channel
         * @return
         */
        @FormUrlEncoded
        @POST("Latesttop/del")
        Observable<DefaultDataBean> getdelUpDynamic(@Field("latest_id") String id,
                                                    @Field("authcode") String authcode,
                                                    @Field("channel") String channel);

        /**
         * 获取更新信息
         *
         * @param
         * @return
         */
        @GET("update/index")
        Observable<VersionInfo> getLoadVersonInfo(@Query("channel") String channel);

        /**
         * 上传手机信息
         *
         * @param serialize
         * @return
         */
        @FormUrlEncoded
        @POST("Location/update_data")
        Observable<RegThridBean> getuploadPhoneInfo(@Field("channel") String channel,
                                                    @Field("authcode") String authcode,
                                                    @Field("address") String serialize);

        /**
         * 获取关注列表
         *
         * @param page
         * @param uid
         * @param authcode @return
         */

        @GET("Follow/get_list_follows_other")
        Observable<FrientBaen> getWacthRelation(@Query("page") int page,
                                                @Query("ouid") String uid,
                                                @Query("authcode") String authcode,
                                                @Query("channel") String channel);


        /**
         * @param page
         * @param authcode
         * @param channel
         * @return
         */
        @GET("Follow/get_list_fans_other")
        Observable<FrientBaen> getFenRelation(@Query("page") int page,
                                              @Query("ouid") String uid,
                                              @Query("authcode") String authcode,
                                              @Query("channel") String channel,
                                              @Query("version_type") String apiVersion);


        /**
         * 更新位置信息
         *
         * @param address
         * @param authcode
         * @param channel
         * @return
         */
        @FormUrlEncoded
        @POST("Location/update_location")
        Observable<DefaultDataBean> uploadLoation(@Field("address") String address,
                                                  @Field("authcode") String authcode,
                                                  @Field("channel") String channel);

        /**
         * 排行
         *
         * @param page
         * @param authcode
         * @return
         */
        @GET("rank/fx")
        Observable<RankBean> getRankData(@Query("type") int type,
                                         @Query("page") int page,
                                         @Query("pagesize") int pagesize,
                                         @Query("authcode") String authcode,
                                         @Query("channel") String channel);

        /**
         * //        1、authcode 用户身份验证码
         * //        2、sex性别（1男，2女），默认为0，为不限
         * //        3、page 分页，默认为1
         * //        4、lat 维度
         * //        5、lng 经度
         *
         * @param longitude
         * @param latitude
         * @param authcode
         * @return
         */
        @GET("user/find_near")
        Observable<NearbyBean> getNearby(@Query("sex") String sex,
                                         @Query("page") int page,
                                         @Query("lng") double longitude,
                                         @Query("lat") double latitude,
                                         @Query("authcode") String authcode,
                                         @Query("channel") String channel);

        /**
         * 动态标签获取
         *
         * @param page
         * @param type
         * @param catid
         * @param pagesize
         * @param authcode
         * @return
         */
        @GET("tags/get")
        Observable<LableBean> getDynamicLable(@Query("page") int page,
                                              @Query("type") int type,
                                              @Query("catid") int catid,
                                              @Query("pagesize") int pagesize,
                                              @Query("authcode") String authcode,
                                              @Query("channel") String channel);

        /**
         * 购买VIP
         *
         * @param payType
         * @param opentype
         * @param phone
         * @param authcode @return
         */
        @FormUrlEncoded
        @POST("mmvip/openvip")
        Observable<PayBean> getBuyVIP(@Field("pay_type") String payType,
                                      @Field("opentype") int opentype,
                                      @Field("fee_mobile") String phone, @Field("authcode") String authcode,
                                      @Field("channel") String channel);

        /**
         * 认证押金
         *
         * @param authcode
         * @return
         */
        @FormUrlEncoded
        @POST("apply/gobuy")
        Observable<PayBean> getCertificationPay(@Field("authcode") String authcode,
                                                @Field("channel") String channel);

        /**
         * 推送用户信息
         *
         * @param pagesize
         * @param authcode
         * @param channel
         * @return
         */
        @GET("push/index")
        Observable<RecommendMemberFragmentBean> getRecommendMemberBean(@Query("pagesize") int pagesize,
                                                                       @Query("authcode") String authcode,
                                                                       @Query("channel") String channel);

        /**
         * 推送用户信息：点击喜欢，不喜欢
         *
         * @param uid
         * @param
         * @return
         */
        @GET("push/ilike")
        Observable<DefaultDataBean> getRecommend(@Query("type") int type,
                                                 @Query("like_uid") String uid,
                                                 @Query("authcode") String authcode,
                                                 @Query("channel") String channel);

        /**
         * 获取好友列表
         *
         * @param page
         * @param authcode
         * @param
         * @return
         */
        @GET("follow/get_list_friends")
        Observable<FrientBaen> getFriendList(@Query("page") int page,
                                             @Query("authcode") String authcode,
//                                             @Query("pagesize") int pagesize,
                                             @Query("channel") String channel);

        /**
         * @param authcode
         * @param channelId
         */
        @GET("user/get_auth")
        Observable<UserPermissionBean> getUserPermission(@Query("authcode") String authcode,
                                                         @Query("channel") String channelId);

        /**
         * 获取全部好友uid：
         *
         * @param authcode
         * @return
         */
        @GET("follow/get_list_friends_all")
        Observable<UserFriendListBean> getUserFriendList(@Query("authcode") String authcode,
                                                         @Query("channel") String channelId);

        /**
         * 首页标签
         *
         * @param page
         * @param tagId
         * @param homeType
         * @param apiVersion
         * @param channelId
         * @param authcode
         */
        @GET("index/tlist")
        Observable<HomeTopItemBean> getHomeTopItem(@Query("page") int page,
                                                   @Query("tagid") String tagId,
                                                   @Query("search_type") int homeType,
                                                   @Query("version_type") String apiVersion,
                                                   @Query("channel") String channelId,
                                                   @Query("authcode") String authcode);


        /**
         * 兴趣选项
         *
         * @param page
         * @param tagname
         * @param pagesize
         */
        @GET("tags/get_xingqu_all")
        Observable<InsertBaen> getInsertRelation(@Query("page") int page,
                                                 @Query("tagname") String tagname,
                                                 @Query("pagesize") int pagesize,
                                                 @Query("channel") String channelId,
                                                 @Query("authcode") String authcode);

        /**
         * 个人资料
         *
         * */

        /**
         * 搜索结果
         *
         * @param key
         * @param searchType
         * @param page
         * @param authcode
         * @param channelId
         * @param apiVersion
         * @return
         */
        @GET("search/index")
        Observable<DynamicBean> getSearchFragmentReason(@Query("key") String key,
                                                        @Query("search_type") int searchType,
                                                        @Query("page") int page,
                                                        @Query("authcode") String authcode,
                                                        @Query("channel") String channelId,
                                                        @Query("version_type") String apiVersion);


        /**
         * 联想关键词
         *
         * @param key
         * @param authcode
         */
        @GET("search/like_keys")
        Observable<KeyMateBean> getKeyMath(@Query("key") String key,
                                           @Query("authcode") String authcode,
                                           @Query("channel") String channelId);

        /**
         * 热门推荐
         */
        @GET("search/search_hot")
        Observable<ChooseKeyBean> getLoadChooesKey(@Query("authcode") String authcode,
                                                   @Query("channel") String channelId);

        /**
         * 动态详情
         *
         * @return
         */
        @GET("Latest/get_latest_detail")
        Observable<DynamicDatailsBean> getDynamicDatail(@Query("lid") String latest_id,
                                                        @Query("authcode") String authcode,
                                                        @Query("channel") String channelId,
                                                        @Query("version_type") String version_type);


        /**
         * 男女选择页
         *
         * @param sex
         * @return
         */
        @FormUrlEncoded
        @POST("usernew/reg_step1")
        Observable<RegOneBean> getRegOneBean(@Field("sex") int sex,
                                             @Field("version_type") String apiVersion,
                                             @Field("channel") String channel);

        /**
         * 兴趣返回
         *
         * @param tlist
         * @return
         */
        @FormUrlEncoded
        @POST("tags/set_xingqu")
        Observable<BackInsertBean> makeInsertBean(@Field("tlist") String tlist,
                                                  @Field("authcode") String authcode,
                                                  @Field("channel") String channel,
                                                  @Field("version_type") String apiVersion);

        /**
         * 填写用户基本信息
         *
         * @param mobile
         * @param pwd
         * @param birthday
         * @param code
         * @return
         */
        @FormUrlEncoded
        @POST("usernew/reg_step3")
        Observable<RegThridBean> getRegThridBean(@Field("mobile") String mobile,
                                                 @Field("pwd") String pwd,
                                                 @Field("birthday") String birthday,
                                                 @Field("code") String code,
                                                 @Field("phoneinfo") String phoneinfo,
                                                 @Field("authcode") String authcode,
                                                 @Field("version_type") String apiVersion,
                                                 @Field("channel") String channel);


        /**
         * 填写资料页面
         *
         * @param face
         * @param nickname
         * @return
         */
        @FormUrlEncoded
        @POST("usernew/reg_step4")
        Observable<RegFourBean> getRegFourBean(@Field("face") String face,
                                               @Field("nickname") String nickname,
                                               @Field("authcode") String authcode,
                                               @Field("channel") String channel);

        /**
         * 查看用户资料   版本3
         *
         * @param uid
         * @param get_source
         * @return
         */
        @GET("user/user_detail_rule")
        Observable<PersonalMessageFenBean> setPersonalFenMessage(@Query("uid") String uid,
                                                                 @Query("get_source") String get_source,
                                                                 @Query("authcode") String authcode,
                                                                 @Query("channel") String channel,
                                                                 @Query("version_type") String apiVersion);

        /**
         * 修改用户资料页展示
         *
         * @param uid
         * @return
         */
        @GET("user/user_detail_rule")
        Observable<PersonalReviseMessageBean> setPersonalReviseMessage(@Query("uid") String uid,
                                                                       @Query("get_source") String get_source,
                                                                       @Query("authcode") String authcode,
                                                                       @Query("channel") String channelId,
                                                                       @Query("version_type") String apiVersion);

        /**
         * 财务中心页，收入和支出明细
         *
         * @param type
         * @param page
         * @return
         */
        @GET("balance/get_list")
        Observable<BalanceBean> balance(@Query("type") int type,
                                        @Query("page") int page,
                                        @Query("pagesize") int pagesize,
                                        @Query("authcode") String authcode,
                                        @Query("channel") String channelId);

        /**
         * 充值页面
         *
         * @param money
         * @param pay_type
         * @return
         */
        @FormUrlEncoded
        @POST("recharge/pub")
        Observable<PayBean> getrechargeBean(@Field("money") String money,
                                            @Field("pay_type") String pay_type,
                                            @Field("authcode") String authcode,
                                            @Field("channel") String channel,
                                            @Field("version_type") String apiVersion);

        /**
         * 获得验证词语
         *
         * @return
         */
        @GET("apply/get_apply_info_before")
        Observable<BackTermsBean> backTermsBean(@Query("authcode") String authcode,
                                                @Query("channel") String channelId);

        /**
         * 资料修改后的数据传递
         *
         * @param contents
         * @param authcode
         * @return
         */
        @FormUrlEncoded
        @POST("user/edit_uinfo_byversion")
        Observable<PersonalReviseMessageBean> getUserJsonBean(@Field("contents") String contents,
                                                              @Field("authcode") String authcode,
                                                              @Field("channel") String channel,
                                                              @Field("version_type") String apiVersion);

        /**
         * 上传视频和语音
         *
         * @param apply_image
         * @param order_id
         * @return
         */
        @FormUrlEncoded
        @POST("apply/goadd_info_v")
        Observable<BaseBean> getupto(@Field("apply_image") String apply_image,
                                     @Field("order_id") String order_id,
                                     @Field("authcode") String auhcode,
                                     @Field("channel") String channel);

        /**
         * 获得用户资料
         *
         * @param uid
         * @return
         */
        @GET("User/user_detail_addon1_Center")
        Observable<UserDetailBean> userdetailbean(@Query("uid") String uid,
                                                  @Query("authcode") String authcode,
                                                  @Query("channel") String channelId);

        /**
         * 屏蔽
         *
         * @param type
         * @param to_uid
         * @return
         */
        @GET("shield/shield_act")
        Observable<DefaultDataBean> pingbibean(@Query("type") String type,
                                               @Query("to_uid") String to_uid,
                                               @Query("authcode") String authcode,
                                               @Query("channel") String channelId,
                                               @Query("version_type") String apiVersion);

        /**
         * 未屏蔽
         *
         * @param type
         * @param to_uid
         * @return
         */
        @GET("shield/shield_act")
        Observable<DefaultDataBean> weipingbibean(@Query("type") String type,
                                                  @Query("to_uid") String to_uid,
                                                  @Query("authcode") String authcode,
                                                  @Query("channel") String channelId,
                                                  @Query("version_type") String apiVersion);

        /**
         * 屏蔽列表
         *
         * @param page
         */
        @GET("shield/tlist")
        Observable<PingbiBean> pingbiliebiao(@Query("page") String page,
                                             @Query("authcode") String authcode,
                                             @Query("channel") String channelId,
                                             @Query("version_type") String apiVersion);

        /**
         * 获取用户信息是否全
         *
         * @return
         */
        @GET("user/get_ustatus")
        Observable<UserInfoBannerBean> getUserInfoStatus(@Query("authcode") String authcode,
                                                         @Query("channel") String channelId);

        /**
         * @param authcode
         * @param channel
         * @return
         */
        @GET("Latest/get_latest_list_faxian")
        Observable<UserInfoListBean> getVideoList(@Query("page") int page,
                                                  @Query("authcode") String authcode,
                                                  @Query("channel") String channel);

        /**
         * 夺宝apk
         *
         * @return
         */
        @GET("download/get")
        Observable<DownApkBean> getDownApk(@Query("authcode") String authcode,
                                           @Query("channel") String channel);


        //搜索人
        @POST("home/search")
        Observable<FeelingSquareSearchBean> getSquareSearchList(@Query("username") String username);

        //跑马灯
        @GET("home/horn")
        Observable<FeelingSquarePMDBean> getPMDContent(
                @Field("laba") String laba,
                @Field("authcode") String authcode,
                @Field("channel") String channel);


        //筛选
        @POST("home/scrccen")
        Observable<FeelingSquareFilterBean> getSquareFilterList(
                @Query("username") String username,
                @Query("sex") String sex,
                @Query("birthday") String birthday,
                @Query("mobile") String smoblie,
                @Query("height") String height,
                @Query("weixin") String weixin,
                @Query("weight") String weight,
                @Query("distance_love") String distance_love,
                @Query("qq") String qq);

        /**
         * 广场：数据列表综合
         *
         * @param type
         * @param serialize
         * @param page
         * @param authcode
         * @param id
         * @return
         */
        @FormUrlEncoded
        @POST("index/v3")
        Observable<HomeThreeFragmentBean> getHomeThree(@Field("type") String type,
                                                       @Field("params") String serialize,
                                                       @Field("page") int page,
                                                       @Field("flag") int flag,
                                                       @Field("authcode") String authcode,
                                                       @Field("channel") String id);

        /**
         * 用户资源信息
         *
         * @param page
         * @param type
         * @param authcode
         * @param id
         * @return
         */
        @GET("source/get_source_list")
        Observable<MyThreeFragmentBean> getMyThreeFragment(@Query("uid") String uid,
                                                           @Query("page") int page,
                                                           @Query("source_type") String type,
                                                           @Query("authcode") String authcode,
                                                           @Query("channel") String id);

        /**
         * 约会推荐用户列表
         *
         * @param page
         * @param type
         * @param authcode
         * @param id
         * @return
         */
        @GET("index/yuehui_list")
        Observable<EngagemengRecommendBean> getEngagemengRecommen(@Query("page") int page,
                                                                  @Query("type") int type,
                                                                  @Query("authcode") String authcode,
                                                                  @Query("channel") String id);

        /**
         * 发布约会
         *
         * @param type
         * @param uid
         * @param money
         * @param date
         * @param address
         * @param message
         * @param authcode
         * @param id
         * @return
         */
        @FormUrlEncoded
        @POST("dating/pub_dating")
        Observable<PayBean> getEngagementOreder(@Field("dating_count") String dating_count,
                                                @Field("type") int type,
                                                @Field("to_uid") String uid,
                                                @Field("money") String money,
                                                @Field("date") String date,
                                                @Field("address") String address,
                                                @Field("msg") String message,
                                                @Field("pay_type") String pay_type,
                                                @Field("version_type") String version_type,
                                                @Field("is_pay_type") String payType,
                                                @Field("authcode") String authcode,
                                                @Field("channel") String id);

        /**
         * 发布的约会
         *
         * @param page
         * @param authcode
         * @param channel
         * @return
         */
        @GET("Dating/get_my_list_pub")
        Observable<EngagementManagerBean> getEngagementList(@Query("page") int page,
                                                            @Query("authcode") String authcode,
                                                            @Query("channel") String channel);

        /**
         * 被要请的约会的约会
         *
         * @param page
         * @param authcode
         * @param channel
         * @return
         */
        @GET("Dating/get_my_list")
        Observable<EngagementManagerBean> getEngagementPassivityList(@Query("page") int page,
                                                                     @Query("authcode") String authcode,
                                                                     @Query("channel") String channel);

        /**
         * 设置约会成功
         *
         * @param id
         * @param authcode
         * @param channel
         * @return
         */
        @FormUrlEncoded
        @POST("Dating/set_datting_success")
        Observable<StatusBean> getSubmitSuccess(@Field("dating_id") String id,
                                                @Field("authcode") String authcode,
                                                @Field("channel") String channel);

        /**
         * 约会状态操作
         *
         * @param id
         * @param authcode
         * @param channel
         * @return
         */
        @FormUrlEncoded
        @POST("Dating/set_dating_status")
        Observable<StatusBean> getsubmitInviteSuccess(@Field("dating_id") String id,
                                                      @Field("action_type") String type,
                                                      @Field("authcode") String authcode,
                                                      @Field("channel") String channel);

        /**
         * 约会申诉
         *
         * @param id
         * @param content
         * @param authcode
         * @param channel
         * @return
         */
        @FormUrlEncoded
        @POST("Dating/dating_appeal")
        Observable<StatusBean> getSubmitEngagementAppeal(@Field("dating_id") String id,
                                                         @Field("msg") String content,
                                                         @Field("authcode") String authcode,
                                                         @Field("channel") String channel);

        /**
         * 用户资源发布
         *
         * @param type
         * @param json
         * @param authcode
         * @param id
         * @return
         */
        @FormUrlEncoded
        @POST("source/add")
        Observable<BaseBean> getDynamicResAdd(@Field("source_type") int type,
                                              @Field("contents") String json,
                                              @Field("authcode") String authcode,
                                              @Field("channel") String id);

        /**
         * @param authcode
         * @param id
         */
        @GET("index/v3_notice")
        Observable<BannerBean> getloadBannerData(@Query("authcode") String authcode,
                                                 @Query("channel") String id);

        /**
         * 认证申请:第二步，提交媒体数据信息【品牌版】
         *
         * @param content
         * @param authcode
         * @param id
         * @return
         */
        @FormUrlEncoded
        @POST("apply/goadd_info_v")
        Observable<BaseBean> getRenzhengThree(@Field("apply_image") String content,
                                              @Field("authcode") String authcode,
                                              @Field("channel") String id);

        /**
         * 约会：发布约会时，获取免费约会信息
         *
         * @param authcode
         * @param id
         * @return
         */
        @GET("Dating/get_dating_ss")
        Observable<EngagemengOrderBean> getEngagemengOrder(@Query("authcode") String authcode,
                                                           @Query("channel") String id);

        /**
         * 用户资源列表
         *
         * @param id
         * @param authcode
         * @param id1
         * @return
         */
        @GET("source/del")
        Observable<StatusBean> getDeleteRes(@Query("source_id") String id,
                                          @Query("authcode") String authcode,
                                          @Query("channel") String id1);
    }
}

