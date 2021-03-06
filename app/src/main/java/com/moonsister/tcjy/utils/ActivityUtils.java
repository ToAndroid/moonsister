package com.moonsister.tcjy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.PlayDirectlyActivity;
import com.moonsister.tcjy.bean.PersonInfoDetail;
import com.moonsister.tcjy.bean.RecommendMemberFragmentBean;
import com.moonsister.tcjy.bean.TiXinrRecordBean;
import com.moonsister.tcjy.center.widget.BuyDynamicRedPackketActivity;
import com.moonsister.tcjy.center.widget.DefaultDynamicSendActivity;
import com.moonsister.tcjy.center.widget.DynamicPublishActivity;
import com.moonsister.tcjy.center.widget.DynamicSendActivity;
import com.moonsister.tcjy.center.widget.RedpacketDynaimcActivity;
import com.moonsister.tcjy.engagement.widget.EengegamentPublishActivity;
import com.moonsister.tcjy.engagement.widget.EengegamentRecommendActivity;
import com.moonsister.tcjy.engagement.widget.EngagemengOrderActivity;
import com.moonsister.tcjy.engagement.widget.EngagementManagerActivity;
import com.moonsister.tcjy.engagement.widget.EngagementTypeActivity;
import com.moonsister.tcjy.engagement.widget.EngegamentAppealActivity;
import com.moonsister.tcjy.event.Events;
import com.moonsister.tcjy.event.RxBus;
import com.moonsister.tcjy.find.widget.NearbyActivity;
import com.moonsister.tcjy.find.widget.RankActivity;
import com.moonsister.tcjy.find.widget.VideoDynamicActivity;
import com.moonsister.tcjy.home.widget.HomeSearchActivity;
import com.moonsister.tcjy.home.widget.SearchActivity;
import com.moonsister.tcjy.home.widget.SearchFragmentActivity;
import com.moonsister.tcjy.home.widget.SearchReasonActivity;
import com.moonsister.tcjy.im.widget.AppConversationActivity;
import com.moonsister.tcjy.login.widget.FindPasswordActivity;
import com.moonsister.tcjy.login.widget.FindPasswordNextActivity;
import com.moonsister.tcjy.login.widget.LoginMainActivity;
import com.moonsister.tcjy.login.widget.RegActivity;
import com.moonsister.tcjy.main.widget.BuyVipActivity;
import com.moonsister.tcjy.main.widget.DynamicAtionActivity;
import com.moonsister.tcjy.main.widget.DynamicDatailsActivity;
import com.moonsister.tcjy.main.widget.H5Activity;
import com.moonsister.tcjy.main.widget.HomePageActivity;
import com.moonsister.tcjy.main.widget.ManorGrilActivity;
import com.moonsister.tcjy.main.widget.PayAppointmentActivity;
import com.moonsister.tcjy.main.widget.PayAppointmentOrderActivity;
import com.moonsister.tcjy.main.widget.PersonInfoChangeActivity;
import com.moonsister.tcjy.main.widget.PersonThreeActivity;
import com.moonsister.tcjy.main.widget.PictureSelectorActivity;
import com.moonsister.tcjy.main.widget.RecommendMemberActivity;
import com.moonsister.tcjy.main.widget.RedpacketAcitivity;
import com.moonsister.tcjy.main.widget.RelationActivity;
import com.moonsister.tcjy.main.widget.RenZhengThreeActivity;
import com.moonsister.tcjy.main.widget.SwitchItemActivity;
import com.moonsister.tcjy.main.widget.UserinfoActivity;
import com.moonsister.tcjy.manager.UserInfoManager;
import com.moonsister.tcjy.my.widget.AddActivity;
import com.moonsister.tcjy.my.widget.AddCardActivity;
import com.moonsister.tcjy.my.widget.AppointmentActivity;
import com.moonsister.tcjy.my.widget.BirthdayActivity;
import com.moonsister.tcjy.my.widget.ChangepwdActivity;
import com.moonsister.tcjy.my.widget.DynamicResAddActivity;
import com.moonsister.tcjy.my.widget.EditDynamicActivity;
import com.moonsister.tcjy.my.widget.FollowActivity;
import com.moonsister.tcjy.my.widget.GetMoneyActivity;
import com.moonsister.tcjy.my.widget.HreatFragment;
import com.moonsister.tcjy.my.widget.InsertActivity;
import com.moonsister.tcjy.my.widget.MakeMessageActivity;
import com.moonsister.tcjy.my.widget.MoneyActivity;
import com.moonsister.tcjy.my.widget.MyOrderActivity;
import com.moonsister.tcjy.my.widget.PersonalActivity;
import com.moonsister.tcjy.my.widget.PersonalReviseActivity;
import com.moonsister.tcjy.my.widget.RZFirstActivity;
import com.moonsister.tcjy.my.widget.RZSecondActivity;
import com.moonsister.tcjy.my.widget.RZThidActivity;
import com.moonsister.tcjy.my.widget.RechargeActivity;
import com.moonsister.tcjy.my.widget.RefundActivity;
import com.moonsister.tcjy.my.widget.RuleActivity;
import com.moonsister.tcjy.my.widget.SettingActivity;
import com.moonsister.tcjy.my.widget.SwitchCardActivity;
import com.moonsister.tcjy.my.widget.TiXianRecordActivity;
import com.moonsister.tcjy.my.widget.WithdRawDepositActivity;
import com.moonsister.tcjy.my.widget.WithdrawActivity;
import com.moonsister.tcjy.widget.image.CropImageMainActivity;
import com.moonsister.tcjy.widget.image.PhonePicActivity;
import com.moonsister.tcjy.widget.image.ShowImageActivity;
import com.moonsister.tcjy.widget.image.photoview.ImagePagerActivity;
import com.moonsister.tcjy.widget.takevideo.TakeVideoActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by jb on 2016/6/20.
 */
public class ActivityUtils {
    private static String TAG = "ActivityUtils";

    /**
     * activity 上下文
     *
     * @return
     */
    private static Context getActivityContext() {
        return ConfigUtils.getInstance().getActivityContext();
    }

    /**
     * 全局上下文
     *
     * @return
     */
    private static Context getApplicationContext() {

        return ConfigUtils.getInstance().getApplicationContext();
    }

    private static Intent getIntent(Class clz) {
        Intent intent = null;
        Context context = getActivityContext();
        if (context == null) {
            intent = new Intent(getApplicationContext(), clz);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = new Intent(context, clz);
        }
        return intent;
    }

    /**
     * 开启一个activity
     *
     * @param clz
     */
    public static void startActivity(Class clz) {

        Intent intent = getIntent(clz);
        Activity context = ConfigUtils.getInstance().getActivityContext();
        if (context == null) {
            getApplicationContext().startActivity(intent);
        } else {
            context.startActivity(intent);
        }

    }

    /**
     * 开启一个activity
     *
     * @param intent
     */
    public static void startActivity(Intent intent) {
        if (intent == null)
            return;
        Activity context = ConfigUtils.getInstance().getActivityContext();
        if (context == null) {
            Context applicationContext = ConfigUtils.getInstance().getApplicationContext();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            applicationContext.startActivity(intent);
        } else {
            context.startActivity(intent);
        }

    }

    /**
     * @param intent
     */
    public static void startActivityForResult(Intent intent) {
        Activity context = ConfigUtils.getInstance().getActivityContext();
        if (intent == null || context == null)
            return;

        ConfigUtils.getInstance().getActivityContext().startActivityForResult(intent, 1);
    }


//    private static Context getContext() {
//        return ConfigUtils.getInstance().getActivityContext();
//    }


    /**
     * 进入用户信息列表
     */
    public static void startDynamicActivity(String userId) {
//        Intent intent = new Intent(ConfigUtils.getInstance().getApplicationContext(), DynamicActivity.class);
//        LogUtils.e(TAG, "userid : " + userId);
//        intent.putExtra(AppConstant.USER_ID, userId);
//        startActivity(intent);
        startHomePageActivity(userId);
    }

    /**
     * 个人主页
     *
     * @param userId
     */
    public static void startHomePageActivity(String userId) {
        Intent intent = new Intent(ConfigUtils.getInstance().getApplicationContext(), HomePageActivity.class);
        LogUtils.e(TAG, "userid : " + userId);
        intent.putExtra("id", userId);
        startActivity(intent);
    }

    /**
     * 动态详情页面跳转
     *
     * @param
     */
    public static void startDynamicDatailsActivity(String dynamicId, int dynamicType) {
        if (StringUtis.isEmpty(dynamicId) || dynamicType == 0)
            return;
        Intent intent = new Intent(ConfigUtils.getInstance().getApplicationContext(), DynamicDatailsActivity.class);
        intent.putExtra("id", dynamicId);
        intent.putExtra("type", dynamicType);
        startActivity(intent);
    }

    /**
     * 开启动态页面
     */
    public static void startDynamicSendActivity() {
        startActivity(DynamicSendActivity.class);
    }

    /**
     * 普通动态发布
     *
     * @param pics
     */
    public static void startDefaultDynamicSendActivity(ArrayList pics, EnumConstant.DynamicType type) {
        if (UserInfoManager.getInstance().isLogin()) {
            Intent intent = new Intent(ConfigUtils.getInstance().getActivityContext(), DefaultDynamicSendActivity.class);
            intent.putExtra("type", type.getValue());
            intent.putExtra("data", pics);
            startActivity(intent);
        } else
            RxBus.getInstance().send(Events.EventEnum.LOGIN, null);

    }

    public static void startPhonePicActivity() {
        startActivity(PhonePicActivity.class);
    }

    public static void startShowImageActivity(List<String> childList) {
        Intent mIntent = new Intent(ConfigUtils.getInstance().getActivityContext(),
                ShowImageActivity.class);
        mIntent.putStringArrayListExtra("data",
                (ArrayList<String>) childList);
        startActivity(mIntent);
    }

    /**
     * 开启登录页面
     */
    public static void startLoginMainActivity() {
        startActivity(LoginMainActivity.class);
    }

    /**
     * 开启聊天页面
     *
     * @param userId
     * @param name
     * @param avatar
     */
    public static void startAppConversationActivity(String userId, String name, String avatar) {
//        if (!UserInfoManager.getInstance().isLogin()) {
//            RxBus.getInstance().send(Events.EventEnum.LOGIN, null);
//            return;
//        }
//        Uri uri = Uri.parse("rong://" + ConfigUtils.getInstance().getApplicationContext().getApplicationInfo().processName)
//                .buildUpon().appendPath("conversation")
//                .appendPath(Conversation.ConversationType.PRIVATE.getName().toLowerCase())
//                .appendQueryParameter("targetId", userId)
//                .appendQueryParameter("title", name).build();
//        RongyunManager.getInstance().setUserInfoCache(userId, name, avatar);
//        startActivity(new Intent("android.intent.action.VIEW", uri));
        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, userId);
        args.putString(EaseConstant.EXTRA_USER_NIKE, name);
        args.putString(EaseConstant.EXTRA_USER_AVATER, avatar);
        startActivity(getIntent(AppConversationActivity.class).putExtras(args));


    }

    /**
     * 打赏
     *
     * @param userId
     * @param typ
     * @param avater
     */
    public static void startRedpacketActivity(String userId, RedpacketAcitivity.RedpacketType typ, String avater) {
        if (!UserInfoManager.getInstance().isLogin()) {
            RxBus.getInstance().send(Events.EventEnum.LOGIN, null);
            return;
        }
        Intent intent = new Intent(ConfigUtils.getInstance().getActivityContext(), RedpacketAcitivity.class);
        intent.putExtra("id", userId);
        intent.putExtra("type", typ.getValue());
        intent.putExtra("avater", avater);
        startActivity(intent);
    }

    /**
     * 订单
     */
    public static void startMyOrderActivity() {
        startActivity(MyOrderActivity.class);
    }

    /**
     * 认证
     */
    public static void startCertificationActivity() {
//        startActivity(CertificationActivity.class);
        startRenZhengActivity();
    }

    /**
     * 认证第一步
     */
    public static void startRZFirstActivity() {
        startActivity(RZFirstActivity.class);
    }

    /**
     * 认证第二步
     *
     * @param address
     * @param height
     * @param sex
     * @param nike
     */
    public static void startRZSecondActivity(String address, String height, String sex, String nike, String path) {
        Intent intent = getIntent(RZSecondActivity.class);
        intent.putExtra("address", address);
        intent.putExtra("height", height);
        intent.putExtra("sex", sex);
        intent.putExtra("nike", nike);
        intent.putExtra("path", path);
        startActivity(intent);
    }

    /**
     * 预约
     */
    public static void startAppointmentActivity() {
        startActivity(AppointmentActivity.class);
    }

    /**
     * 退款
     */
    public static void startRefundActivity() {
        startActivity(RefundActivity.class);
    }

    /**
     * 设置
     */
    public static void startSettingActivity() {
        startActivity(SettingActivity.class);
    }

    /**
     * 密码修改
     */
    public static void startChangepwdActivity() {
        startActivity(ChangepwdActivity.class);
    }

    /**
     * 提现
     */
    public static void startWithdRawDepositActivity() {
        startActivity(WithdRawDepositActivity.class);
    }

    /**
     * 提现
     */
    public static void startGetMoneyActivity() {
        startActivity(GetMoneyActivity.class);
    }

    /**
     * 红包动态
     */
    public static void startRedpacketDynaimcActivity() {
        startActivity(RedpacketDynaimcActivity.class);
    }

    /**
     * 发红包看照片
     *
     * @param money
     */
    public static void startPayDynamicRedPackketActivity(String money, String id) {
        if (!UserInfoManager.getInstance().isLogin()) {
            RxBus.getInstance().send(Events.EventEnum.LOGIN, null);
            return;
        }
        Intent intent = new Intent(ConfigUtils.getInstance().getActivityContext(), BuyDynamicRedPackketActivity.class);
        intent.putExtra("money", money);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    /**
     * 开启图片查看器
     *
     * @param urls2
     * @param position
     */
    public static void startImagePagerActivity(ArrayList<String> urls2, int position) {
        Intent intent = new Intent(ConfigUtils.getInstance().getActivityContext(), ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }

    /**
     * 开启相机
     */
    public static void startTakeVideoActivity() {
        startActivity(TakeVideoActivity.class);
    }

    /**
     * 修改信息
     *
     * @param type
     */
    public static void startPersonInfoChangeActivity(PersonInfoChangeActivity.ChangeType type) {
        Intent intent = getIntent(PersonInfoChangeActivity.class);
        intent.putExtra("type", type.getValue());
        startActivity(intent);
    }

    /**
     * 认证三步
     */
    public static void startRZThidActivity() {
        startActivity(RZThidActivity.class);
    }

    /**
     * 约会
     */
    public static void startPayAppointmentActivity() {
        startActivity(PayAppointmentActivity.class);
    }

    /**
     * 约会订单页
     */
    public static void startPayAppointmentOrderActivity() {
        startActivity(PayAppointmentOrderActivity.class);
    }

    /**
     * 提现记录
     *
     * @param dataBean
     */
    public static void startTiXianRecordActivity(TiXinrRecordBean.DataBean dataBean) {
        Intent intent = getIntent(TiXianRecordActivity.class);
        intent.putExtra("data", dataBean);
        startActivity(intent);
    }

    /**
     * 添加银行账号
     */
    public static void startAddCardActivity(String bankname, String type) {
        Intent intent = getIntent(AddCardActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("bankname", bankname);
        startActivity(intent);
    }

    /**
     * 选择支付银行卡
     *
     * @param type
     * @param number
     */
    public static void startSwitchCardActivity(String type, String number) {
        Intent intent = getIntent(SwitchCardActivity.class);
        intent.putExtra("number", number);
        intent.putExtra("type", type);
        startActivity(intent);

    }

    /**
     * 开启搜索页面
     */
    public static void startSearchActivity() {
        startActivity(SearchActivity.class);
    }

    /**
     * 搜索结果
     *
     * @param search
     */
    public static void startSearchReasonActivity(String search) {
        Intent intent = getIntent(SearchReasonActivity.class);
        intent.putExtra("key", search);
        startActivity(intent);
    }

    /**
     * 修改资料
     */
    public static void startUserInfoChangeActivity() {
//        startActivity(UserInfoChangeActivity.class);
        startPersonalReviseActivity();
    }

    /**
     * 展示用户信息
     *
     * @param uid
     */
    public static void startUserinfoActivity(String uid) {
        Intent intent = getIntent(UserinfoActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    /**
     * 找回密码第二步
     *
     * @param authcode
     */
    public static void startFindPasswordNextActivity(String authcode) {
        Intent intent = getIntent(FindPasswordNextActivity.class);
        intent.putExtra("code", authcode);
        startActivity(intent);
    }

    /**
     * 找回密码
     */
    public static void startFindPasswordActivity() {
        startActivity(FindPasswordActivity.class);
    }

    /**
     * 图片裁剪
     */
    public static void startCropImageMainActivity() {
        startActivity(CropImageMainActivity.class);
    }

    /**
     * 开启动态删除
     *
     * @param uid
     * @param id
     * @param type
     * @param
     */
    public static void startDynamicAtionActivity(String uid, String id, int type, String istop) {
        Intent intent = getIntent(DynamicAtionActivity.class);
        // 1 自己动态  2 他人的动态
        intent.putExtra("uid", uid);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        intent.putExtra("top", istop);
        startActivity(intent);
    }

    /**
     * 选择生日
     *
     * @param s
     */
    public static void startBirthdayActivity(String s) {
        Intent intent = getIntent(BirthdayActivity.class);
        intent.putExtra("editdata", s);
        startActivity(intent);
    }

    /**
     * 关注点击列表
     *
     * @param uid
     */
    public static void startWacthRelationActivity(String uid) {
        Intent intent = getIntent(RelationActivity.class);
        intent.putExtra("type", RelationActivity.WACTH_PAGE);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    /**
     * 粉丝点击列表
     *
     * @param uid
     */
    public static void startFenRelationActivity(String uid) {
        Intent intent = getIntent(RelationActivity.class);
        ;
        intent.putExtra("type", RelationActivity.FANS_PAGE);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    /**
     * 排行榜
     */
    public static void startRankActivity() {
        startActivity(RankActivity.class);
    }

    public static void startNearbyActivity() {
        startActivity(NearbyActivity.class);
    }

    /**
     * 选择条目
     *
     * @param map
     * @param tag
     */
    public static void startSwitchItemActivity(LinkedHashMap<String, String> map, String tag) {
        Intent intent = getIntent(SwitchItemActivity.class);
        intent.putExtra("map", map);
        intent.putExtra("tag", tag);
        startActivity(intent);
    }


    public static void startPictureSelectorActivity(Activity activity) {
        Intent intent = new Intent(activity, PictureSelectorActivity.class);
        startActivityForResult(intent);
    }

    /**
     * 开启购买vip
     */
    public static void startBuyVipActivity() {
        Intent intent = getIntent(BuyVipActivity.class);
        startActivity(intent);
    }

    /**
     * 开启推荐
     *
     * @param datas
     */
    public static void startRecommendMemberActivity(ArrayList<RecommendMemberFragmentBean.DataBean> datas) {
        Intent intent = getIntent(RecommendMemberActivity.class);
        intent.putExtra("datas", datas);
        startActivity(intent);

    }

    public static void startDynamicPublishActivity() {
        startActivity(DynamicPublishActivity.class);
    }


    //规则说明    定义跳转的activity
    public static void startRuleActivity() {
        startActivity(RuleActivity.class);
    }

    /**
     * 播放页面
     *
     * @param path
     */
    public static void startShowShortVideoActivity(String path) {
        if (!StringUtis.isEmpty(path)) {
            Intent intent = getIntent(PlayDirectlyActivity.class);
            intent.putExtra("path", path);
            startActivity(intent);
        }

    }

    //    //我的页面gridview中跳转关注页面   定义跳转的activity
    public static void startFollowActivity(String uid, int type) {
        Intent intent = getIntent(FollowActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    //关注页面中返回键的监听
    public static void startHreatFragment() {
        startActivity(HreatFragment.class);
    }

    //关注页面中返回键的监听
    //跳转动态管理页面
    public static void startMakeMessageActivity() {
        startActivity(MakeMessageActivity.class);
    }

    //跳转动态管理页面
    public static void startInsertActivity(String my) {
        Intent intent = getIntent(InsertActivity.class);
        intent.putExtra("my", "我爱你");
        startActivity(intent);
    }

    //跳转VIP认证页面
    public static void startRenZhengActivity() {
//        startActivity(RenZhengActivity.class);
        startBuyVipActivity();
    }

    /**
     * 跳转资料展示页面
     *
     * @param uid
     */
    public static void startPersonalActivity(String uid) {
        Intent intent = getIntent(PersonalActivity.class);
        intent.putExtra("id", uid);
        startActivity(intent);
    }

    //跳转修改资料页面
    public static void startPersonalReviseActivity() {
//        Intent intent=new Intent(getContext(),PersonalActivity.class);
//        intent.putExtra("my","我爱你");
//        startActivity(intent);
        startActivity(PersonalReviseActivity.class);
    }

    //跳转财务中心页面
    public static void startMoneyActivity(String uid) {
        Intent intent = getIntent(MoneyActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    //跳转余额充值界面
    public static void startRechargeActivity() {
        startActivity(RechargeActivity.class);
    }

    //跳转提现界面
    public static void startWithdrawActivity() {
        startActivity(WithdrawActivity.class);
    }

    //跳转余额充值界面
    public static void startAddActivity() {
        startActivity(AddActivity.class);
    }

    public static void startSearchFragmentActivity() {
        startActivity(SearchFragmentActivity.class);
    }

    public static void startRechaargeMoneyActivity() {
        startActivity(MoneyActivity.class);
    }

    /**
     * 选择男女
     */
    public static void startManorGrilActivity() {
        startActivity(ManorGrilActivity.class);
    }

    /**
     * 绑定手机
     */
    public static void startRegActivity() {
        Intent intent = getIntent(RegActivity.class);
        startActivity(intent);
    }

    public static void starVideoDynamicActivity() {
        startActivity(VideoDynamicActivity.class);
    }

    /**
     * 个人约会
     */
    public static void startPersonEngagementTypeActivity(String uid, String nickname, String face) {
        Intent intent = null;
        PersonInfoDetail detail = UserInfoManager.getInstance().getMemoryPersonInfoDetail();
        int attestation = detail.getAttestation();
        String sex = detail.getSex();
        int status = detail.getVipStatus();
        if (StringUtis.equals(sex, "1")) {
            if (status == 1) {
                intent = getIntent(EngagementTypeActivity.class);

            } else {
                UIUtils.showToast(getActivityContext(), "您还未是VIP,请先购买VIP");
                intent = getIntent(BuyVipActivity.class);
            }
        } else {
            if (attestation == 1) {
                intent = getIntent(EngagementTypeActivity.class);
            } else if (attestation == 2) {
                UIUtils.showToast(getApplicationContext(), "您的认证正在审核中，请稍后再试");
            } else {
                intent = getIntent(RenZhengThreeActivity.class);
                UIUtils.showToast(getApplicationContext(), "您还未认证，请先认证");
            }
        }
        if (intent != null) {
            intent.putExtra("id", uid);
            intent.putExtra("name", nickname);
            intent.putExtra("avater", face);
            startActivity(intent);
        }
    }

    /**
     * 发布约会
     */
    public static void startEengegamentPublishActivity() {
        Intent intent = getIntent(EengegamentPublishActivity.class);
        startActivity(intent);
    }

    /**
     * 约会下单
     */
    public static void startEngagemengOrderActivity(EnumConstant.EngegamentType type, String uid, String nickname, String face) {
        Intent intent = null;
        PersonInfoDetail detail = UserInfoManager.getInstance().getMemoryPersonInfoDetail();
        int attestation = detail.getAttestation();
        String sex = detail.getSex();
        int status = detail.getVipStatus();
        if (StringUtis.equals(sex, "1")) {
            if (status == 1) {
                intent = getIntent(EngagemengOrderActivity.class);

            } else {
                UIUtils.showToast(getApplicationContext(), "您还未是VIP,请先购买VIP");
                intent = getIntent(BuyVipActivity.class);
            }
        } else {
            if (attestation == 1) {
                intent = getIntent(EngagemengOrderActivity.class);
            } else if (attestation == 2) {
                UIUtils.showToast(getApplicationContext(), "您的认证正在审核中，请稍后再试");
            } else {
                intent = getIntent(RenZhengThreeActivity.class);
                UIUtils.showToast(getApplicationContext(), "您还未认证，请先认证");
            }
        }
        if (intent != null) {
            intent.putExtra("id", uid);
            intent.putExtra("nike", nickname);
            intent.putExtra("face", face);
            intent.putExtra("type", type);
            startActivity(intent);
        }

    }

    /**
     * 认证三版
     */
    public static void startRenzhengThreeActivity() {
        int status = UserInfoManager.getInstance().getCertificationStatus();

        if (status == 1) {
            UIUtils.showToast(getApplicationContext(), "您已经认证");
        } else if (status == 2) {
            UIUtils.showToast(getApplicationContext(), "您的认证正在审核中，请稍后再试");
        } else {
            startActivity(RenZhengThreeActivity.class);
        }

    }

    /**
     * 推荐约会
     *
     * @param type
     */
    public static void startEengegamentRecommendActivity(EnumConstant.EngegamentType type) {
        Intent intent = getIntent(EengegamentRecommendActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    /**
     * 约会管理
     */
    public static void startEngagementManagerActivity() {
        startActivity(EngagementManagerActivity.class);
    }

    /**
     * 约会申诉
     *
     * @param engagementID
     */
    public static void startEengegamentAppealActivity(String engagementID) {
        if (StringUtis.isEmpty(engagementID))
            return;
        Intent intent = getIntent(EngegamentAppealActivity.class);
        intent.putExtra("id", engagementID);
        startActivity(intent);

    }

    /**
     * 三版个人展示
     *
     * @param id
     */
    public static void startPersonThreeActivity(String id, String nikename, String avater) {
        Intent intent = getIntent(PersonThreeActivity.class);
        intent.putExtra("name", nikename);
        intent.putExtra("avater", avater);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    /**
     * 编辑页面
     *
     * @param uid
     * @param nickname
     * @param face
     */
    public static void startEditDynamicActivity(String type, String uid, String nickname, String face) {
        Intent intent = getIntent(EditDynamicActivity.class);
        intent.putExtra("name", nickname);
        intent.putExtra("avater", face);
        intent.putExtra("id", uid);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    /**
     * 添加动态资源
     */
    public static void startDynamicResAddActivity(String type) {
        Intent intent = getIntent(DynamicResAddActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    public static void startH5Activity(String url) {
        Intent intent = getIntent(H5Activity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    /**
     * 首页搜索
     *
     * @param pageType
     */
    public static void startHomeSearchActivity(String pageType) {
        Intent intent = getIntent(HomeSearchActivity.class);
        intent.putExtra("type", pageType);
        startActivity(intent);
    }
}
