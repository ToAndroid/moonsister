package com.moonsister.tcjy.engagement.model;

import com.moonsister.tcjy.AppConstant;
import com.moonsister.tcjy.AppointmentServerApi;
import com.moonsister.tcjy.bean.EngagementTextBane;
import com.moonsister.tcjy.manager.UserInfoManager;
import com.moonsister.tcjy.utils.EnumConstant;
import com.moonsister.tcjy.utils.ObservableUtils;

import rx.Observable;

/**
 * Created by jb on 2016/11/11.
 */
public class EngegamentTextModelImpl implements EngegamentTextModel {
    @Override
    public void loadText(String datingID, EnumConstant.EngegamentTextType type, onLoadDateSingleListener<EngagementTextBane> listener) {
        Observable observable = AppointmentServerApi.getAppAPI().getEngagemengText(datingID, type.getTextType(), UserInfoManager.getInstance().getAuthcode(), AppConstant.CHANNEL_ID);
        ObservableUtils.parser(observable, new ObservableUtils.Callback<EngagementTextBane>() {
            @Override
            public void onSuccess(EngagementTextBane bean) {
                listener.onSuccess(bean, DataType.DATA_ZERO);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }
}
