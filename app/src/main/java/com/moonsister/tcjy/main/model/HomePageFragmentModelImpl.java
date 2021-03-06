package com.moonsister.tcjy.main.model;

import com.moonsister.tcjy.AppConstant;
import com.moonsister.tcjy.R;
import com.moonsister.tcjy.ServerApi;
import com.moonsister.tcjy.bean.DynamicItemBean;
import com.moonsister.tcjy.bean.UserInfoDetailBean;
import com.moonsister.tcjy.bean.UserInfoListBean;
import com.moonsister.tcjy.manager.UserInfoManager;
import com.moonsister.tcjy.utils.EnumConstant;
import com.moonsister.tcjy.utils.ObservableUtils;
import com.moonsister.tcjy.utils.StringUtis;
import com.moonsister.tcjy.utils.UIUtils;

import java.util.List;

import rx.Observable;

/**
 * Created by jb on 2016/9/1.
 */
public class HomePageFragmentModelImpl implements HomePageFragmentModel {
    @Override
    public void loadDynamicData(String userId, int page, EnumConstant.SearchType type, onLoadDateSingleListener<List<DynamicItemBean>> listener) {
        Observable<UserInfoListBean> observable = ServerApi.getAppAPI().getPersonDynamincList(userId, page, type.getType(), UserInfoManager.getInstance().getAuthcode(), AppConstant.CHANNEL_ID);
        ObservableUtils.parser(observable, new ObservableUtils.Callback<UserInfoListBean>() {
            @Override
            public void onSuccess(UserInfoListBean bean) {
                if (bean == null) {
                    listener.onFailure(UIUtils.getStringRes(R.string.request_failed));
                } else {
                    if (StringUtis.equals(bean.getCode(), AppConstant.code_request_success)) {
                        UserInfoListBean.UserInfoListBeanData data = bean.getData();
                        List<DynamicItemBean> list = data.getList();
                        listener.onSuccess(list, DataType.DATA_ZERO);
                    } else {
                        listener.onFailure(bean.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    @Override
    public void loadheaderData(String id, onLoadDateSingleListener<UserInfoDetailBean> listener) {
        Observable<UserInfoDetailBean> observable = ServerApi.getAppAPI().getUserInfoDetail(id, UserInfoManager.getInstance().getAuthcode(), AppConstant.CHANNEL_ID);
        ObservableUtils.parser(observable, new ObservableUtils.Callback<UserInfoDetailBean>() {
            @Override
            public void onSuccess(UserInfoDetailBean bean) {
                listener.onSuccess(bean, DataType.DATA_ONE);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }
}
