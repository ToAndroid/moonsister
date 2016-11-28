package com.moonsister.tcjy.im.widget;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hickey.tool.base.BaseActivity;
import com.hickey.tool.constant.EnumConstant;
import com.hickey.tool.lang.StringUtis;
import com.hickey.tool.widget.UIUtils;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.CustomConstant;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.db.HxUserDao;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.ChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.moonsister.tcjy.R;
import com.moonsister.tcjy.center.widget.BuyDynamicRedPackketActivity;
import com.moonsister.tcjy.dialogFragment.DialogMannager;
import com.hickey.tool.base.BaseDialogFragment;
import com.moonsister.tcjy.dialogFragment.widget.ImPermissionDialog;
import com.moonsister.tcjy.event.Events;
import com.moonsister.tcjy.event.RxBus;
import com.moonsister.tcjy.im.SendMsgForServiceHelper;
import com.moonsister.tcjy.main.widget.RedpacketAcitivity;
import com.moonsister.tcjy.manager.UserInfoManager;
import com.moonsister.tcjy.permission.UserPermissionManager;
import com.moonsister.tcjy.utils.ActivityUtils;
import com.trello.rxlifecycle.ActivityEvent;


/**
 * Created by jb on 2016/6/18.
 */
public class AppConversationActivity extends BaseActivity {
    public final static String SYSTEM_PATH = "/conversation/system";

    private String mTargetId;
    private String toChatUsername;
    private ChatFragment chatFragment;

    @Override
    protected View setRootContentView() {


        return UIUtils.inflateLayout(R.layout.appconversation);
    }

    @Override
    protected void initView() {
        TextView tv_title_right = (TextView) titleView.findViewById(R.id.tv_title_right);
        Drawable drawable = UIUtils.getResources().getDrawable(R.mipmap.im_userinfo);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_title_right.setCompoundDrawables(drawable, null, null, null);
        tv_title_right.setPadding(10, 10, 10, 10);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startPersonalActivity(mTargetId);
            }
        });
        Intent intent = getIntent();

        mTargetId = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        getIntentDate(intent);
        getPermission();

    }

    private EnumConstant.PermissionReasult mReasult = EnumConstant.PermissionReasult.HAVE_PERSSION;
    private String mSex;
    private int mImCount;

    public void getPermission() {
        if (!StringUtis.equals(mTargetId, "10000"))
            UserPermissionManager.getInstance().checkIMPermission(mTargetId, new UserPermissionManager.PermissionCallback() {
                @Override
                public void onStatus(EnumConstant.PermissionReasult reasult, int count, String sex) {
                    mReasult = reasult;
                    mSex = sex;
                    mImCount = count;
                    if (mReasult != EnumConstant.PermissionReasult.HAVE_PERSSION) {
                        setRxbus();
                    }
                    if (chatFragment != null) {
                        chatFragment.setHavePermission(mReasult);
                    }
                }
            });
        else {
            mReasult = EnumConstant.PermissionReasult.HAVE_PERSSION;
            if (chatFragment != null) {
                chatFragment.setHavePermission(mReasult);
            }
        }

    }

    private void setRxbus() {
        RxBus.with(this)
                .setEndEvent(ActivityEvent.DESTROY)
                .setEvent(Events.EventEnum.BUY_VIP_SUCCESS)
                .onNext(events -> {
                    mReasult = EnumConstant.PermissionReasult.HAVE_PERSSION;
                }).create();
    }


    @Override
    protected String initTitleName() {

        String name = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_NIKE);
        if (StringUtis.isEmpty(name)) {
//            name = RongyunManager.getInstance().getUserName(mTargetId);
        }
        return name;
    }

    @Override
    public boolean isBaseonActivityResult() {
        return false;
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {
        String mTargetId = intent.getExtras().getString(EaseConstant.EXTRA_USER_ID);
        enterFragment(mTargetId);
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param
     * @param mTargetId
     */

    private void enterFragment(String mTargetId) {

        Bundle extras = getIntent().getExtras();
        toChatUsername = extras.getString(EaseConstant.EXTRA_USER_ID);

        HxUserDao dao = new HxUserDao();
        EaseUser user = new EaseUser(toChatUsername);
        user.setAvatar(extras.getString(EaseConstant.EXTRA_USER_AVATER));
        user.setNick(extras.getString(EaseConstant.EXTRA_USER_NIKE));
        dao.saveUser(user);

        chatFragment = new ChatFragment();
        extras.putString(CustomConstant.ESSAGE_ATTRIBUTE_ACTHCODE, UserInfoManager.getInstance().getAuthcode());
        //界面点击
        chatFragment.setOnSendTypeMsgCallBack(new EaseChatFragment.OnSendTypeMsgCallBack() {
            @Override
            public void onSendType(int type, Bundle bundle) {
                switch (type) {
                    case ChatFragment.REQUEST_CODE_SEND_RED_PACKET:
                        Intent intent = new Intent(AppConversationActivity.this, RedpacketAcitivity.class);
                        intent.putExtra("id", bundle.getString("id"));
                        intent.putExtra("type", BuyDynamicRedPackketActivity.RedpacketType.TYPE_REDPACKET.getValue());
                        intent.putExtra("avater", bundle.getString("avater"));
                        startActivityForResult(intent, ChatFragment.REQUEST_CODE_SEND_RED_PACKET);
                        break;
                }
            }
        });
        //消息发送
        chatFragment.setOnSendMsgListenter(new EaseChatFragment.OnSendMsgListenter() {
            @Override
            public boolean isSendMsg(EMMessage message) {
                boolean isSend = false;
                switch (mReasult) {
                    case HAVE_PERSSION:
                        isSend = true;
                        break;
                    case NOT_PERSSION:
                        if (mImCount > 0) {
                            isSend = true;
                            if (message != null)
                                mImCount--;
                        } else {
                            isSend = false;
                            showPermissionDialog();
                        }
                        break;
                    case NOT_NET:
                        isSend = false;
                        showToast(getString(R.string.request_failed));
                        break;
                }
                if (mHelper == null)
                    mHelper = new SendMsgForServiceHelper();
                if (mReasult != EnumConstant.PermissionReasult.HAVE_PERSSION && message != null) {
                    mHelper.send(message);
                }

                return isSend;
            }
        });
        //set arguments
        chatFragment.setArguments(extras);
        getSupportFragmentManager().beginTransaction().add(R.id.conversation, chatFragment).commit();

//
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (chatFragment != null) {
            chatFragment.onActivityResult(requestCode, resultCode, data);
        }
    }


    private SendMsgForServiceHelper mHelper;

    private void showPermissionDialog() {
        DialogMannager.getInstance().showImPermission(mSex, getSupportFragmentManager(), new ImPermissionDialog.OnCallBack() {
            @Override
            public void onStatus(BaseDialogFragment dialogFragment, EnumConstant.DialogCallBack statusCode) {
                if (statusCode == EnumConstant.DialogCallBack.CONFIRM) {
                    if (StringUtis.equals("1", mSex))
                        ActivityUtils.startBuyVipActivity();
                    else
                        ActivityUtils.startRenZhengThreeActivity();
                    dialogFragment.dismissDialogFragment();
                }

            }
        });
    }
}
