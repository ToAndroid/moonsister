package com.moonsister.tcjy.viewholder.dynamic;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.hickey.network.ImageServerApi;
import com.hickey.network.bean.DefaultDataBean;
import com.hickey.network.bean.DynamicItemBean;
import com.hickey.tool.base.BaseIModel;
import com.hickey.tool.base.BaseRecyclerViewHolder;
import com.hickey.tool.lang.StringUtis;
import com.hickey.tool.time.TimeUtils;
import com.hickey.tool.widget.UIUtils;
import com.moonsister.tcjy.R;
import com.moonsister.tcjy.adapter.DynamicAdapter;
import com.moonsister.tcjy.main.model.UserActionModelImpl;
import com.moonsister.tcjy.utils.ActivityUtils;
import com.moonsister.tcjy.widget.RoundedImageView;

import butterknife.Bind;

/**
 * Created by jb on 2016/8/11.
 */
public class VideoViewHolder extends BaseRecyclerViewHolder<DynamicItemBean> {
    @Bind(R.id.riv_user_image)
    RoundedImageView rivUserImage;
    @Bind(R.id.tv_user_name)
    TextView tvName;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_wacth_number)
    TextView tv_wacth_number;
    @Bind(R.id.tv_reply_number)
    TextView tv_reply_number;
    @Bind(R.id.tv_play_number)
    TextView tv_play_number;
    @Bind(R.id.tv_add_v)
    ImageView tv_add_v;
    @Bind(R.id.tv_more__number)
    ImageView tv_more__number;
    @Bind(R.id.iv_play)
    ImageView iv_play;
    @Bind(R.id.play)
    VideoView play;
    @Bind(R.id.iv_play_background)
    ImageView iv_play_background;
    @Bind(R.id.tv_show_redpacket)
    TextView tv_show_redpacket;
    private boolean isAction = false;

    public VideoViewHolder(View view) {
        super(view);
    }

    @Override
    public void onBindData(DynamicItemBean bean) {
        if (bean == null)
            return;
        ImageServerApi.showURLBigImage(iv_play_background, bean.getVimg());
        tvContent.setText(bean.getTitle());
        tvTime.setText(TimeUtils.getDynamicTimeString(bean.getCreate_time()));
        tv_play_number.setText(bean.getLkpicn() + "");
        dynamicAction(bean);
        tv_reply_number.setText(bean.getLcomn() + "");
        tv_wacth_number.setText(bean.getLupn() + "");
        tvName.setText(bean.getNickname());
        if (StringUtis.equals(bean.getIstop(), "1")) {
            tvTime.setText(UIUtils.getStringRes(R.string.up_dynamic));
            tvTime.setTextColor(UIUtils.getResources().getColor(R.color.home_navigation_text_red));
        } else {
            tvTime.setText(TimeUtils.getDynamicTimeString(bean.getCreate_time()));
            tvTime.setTextColor(UIUtils.getResources().getColor(R.color.gray_color));
        }
        if (bean.getType() == DynamicAdapter.TYPE_CHARGE_VIDEO) {
            if (tv_show_redpacket != null) {
                String format = String.format(UIUtils.getStringRes(R.string.show_wacth_video), bean.getLredn() == null ? 0 : bean.getLredn(), bean.getTmoney() == null ? 0 : bean.getTmoney());
                tv_show_redpacket.setText(format);
                tv_show_redpacket.setVisibility(View.VISIBLE);
            }
            if (tv_play_number != null) {
                tv_play_number.setText(bean.getLredn());
                tv_play_number.setVisibility(View.VISIBLE);
            }
        } else {
            if (tv_show_redpacket != null) {
                tv_show_redpacket.setVisibility(View.GONE);
            }
            if (tv_play_number != null) {
                tv_play_number.setVisibility(View.GONE);
            }
        }
        if (StringUtis.equals(bean.getIsauth(), "1"))
            tv_add_v.setVisibility(View.VISIBLE);
        else
            tv_add_v.setVisibility(View.GONE);
        ImageServerApi.showURLImage(rivUserImage, bean.getFace());


        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StringUtis.equals(bean.getIspay(), "2")) {
                    ActivityUtils.startPayDynamicRedPackketActivity(bean.getMoney(), bean.getLatest_id());
                } else {
                    ActivityUtils.startShowShortVideoActivity(bean.getVideo());
                }
            }
        });
    }

//    private void playVideo(UserInfoListBean.UserInfoListBeanData.UserInfoListBeanDataList bean) {
//
//        if (play != null && bean != null && !StringUtis.isEmpty(bean.getVideo())) {
//            Uri uri = Uri.parse(bean.getVideo());
//            play.setVideoURI(uri);
//            play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    iv_play_background.setVisibility(View.GONE);
//                    iv_play.setVisibility(View.GONE);
//                    play.showTopBanner();
//                }
//            });
//
//            play.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    mp.stop();
//                    iv_play_background.setVisibility(View.VISIBLE);
//                    iv_play.setVisibility(View.VISIBLE);
//                    play.stopPlayback();
//                }
//            });
//        }
//    }

    @Override
    protected void onItemclick(View view, DynamicItemBean bean, int position) {
        ActivityUtils.startDynamicDatailsActivity(bean.getLatest_id(),bean.getType());
    }

    /**
     * 处理动态行为
     *
     * @param bean
     */
    private void dynamicAction(DynamicItemBean bean) {
        tv_wacth_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAction) {
                    if (baseIView != null)
                        baseIView.transfePageMsg(UIUtils.getStringRes(R.string.already) + UIUtils.getStringRes(R.string.action));
                    return;
                }
                isAction = true;
                if (baseIView != null)
                    baseIView.showLoading();
                UserActionModelImpl userActionModel = new UserActionModelImpl();
                userActionModel.likeAction(bean.getLatest_id(), "1", new BaseIModel.onLoadDateSingleListener<DefaultDataBean>() {
                    @Override
                    public void onSuccess(DefaultDataBean b, BaseIModel.DataType dataType) {
                        if (baseIView != null) {
                            baseIView.hideLoading();
                            baseIView.transfePageMsg(b.getMsg());
                        }
                        String s = tv_wacth_number.getText().toString();
                        int i = StringUtis.string2Int(s) + 1;
                        bean.setLupn(i + "");
                        tv_wacth_number.setText(bean.getLupn());
                    }

                    @Override
                    public void onFailure(String msg) {
                        if (baseIView != null) {
                            baseIView.hideLoading();
                            baseIView.transfePageMsg(msg);
                        }
                    }
                });
            }
        });
        tv_more__number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startDynamicAtionActivity(bean.getUid(), bean.getLatest_id(), bean.getType(), bean.getIstop());
            }
        });
        rivUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startUserinfoActivity(bean.getUid());
            }
        });
    }

}
