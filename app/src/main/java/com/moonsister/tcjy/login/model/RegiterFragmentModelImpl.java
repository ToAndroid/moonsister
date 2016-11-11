package com.moonsister.tcjy.login.model;

import com.moonsister.tcjy.AppConstant;
import com.moonsister.tcjy.ServerApi;
import com.moonsister.tcjy.bean.BaseBean;
import com.moonsister.tcjy.bean.RegThridBean;
import com.moonsister.tcjy.bean.RegiterBean;
import com.moonsister.tcjy.manager.UserInfoManager;
import com.moonsister.tcjy.utils.ConfigUtils;
import com.moonsister.tool.parse.JsonUtils;
import com.moonsister.tcjy.utils.LogUtils;
import com.moonsister.tcjy.utils.ObservableUtils;
import com.moonsister.tool.phoneinfo.PhoneInfoUtils;
import com.moonsister.tool.lang.StringUtis;

import rx.Observable;

/**
 * Created by pc on 2016/6/14.
 */
public class RegiterFragmentModelImpl implements RegiterFragmentModel {
    private String TAG = getClass().getSimpleName();


    @Override
    public void loadSubmit(String phoneNumber, String code, onLoadSubmitListenter listenter) {

        Observable<RegiterBean> observable = ServerApi.getAppAPI().verifySecurityCode(phoneNumber, code, AppConstant.CHANNEL_ID);
        ObservableUtils.parser(observable, new ObservableUtils.Callback<RegiterBean>() {
            @Override
            public void onSuccess(RegiterBean baseBean) {
                if (StringUtis.equals(baseBean.getCode(), "1")) {
                    uploadPhoneInfo(phoneNumber);
                }
                listenter.onSubmitSuccess(baseBean);
            }

            @Override
            public void onFailure(String msg) {
                listenter.onSubmitFailure(msg, new Exception());
            }
        });
//        .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//
//                .subscribe(new Subscriber<RegiterBean>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtils.e(TAG, "Throwable : " + e.getMessage().toString());
//                        listenter.onSubmitFailure(e.getLocalizedMessage(), (Exception) e);
//                    }
//
//                    @Override
//                    public void onNext(RegiterBean baseBean) {
//                        LogUtils.e(TAG, "onNext : " + baseBean.toString());
//                        listenter.onSubmitSuccess(baseBean);
//                    }
//                });
    }

    @Override
    public void loadSecurity(String phoneMunber, final onLoadDateSingleListener listener) {
        Observable<BaseBean> observable = ServerApi.getAppAPI().sendSecurityCode(phoneMunber, AppConstant.CHANNEL_ID);
        ObservableUtils.parser(observable, new ObservableUtils.Callback<RegiterBean>() {
            @Override
            public void onSuccess(RegiterBean baseBean) {
                listener.onSuccess(baseBean, DataType.DATA_ZERO);
            }

            @Override
            public void onFailure(String msg) {

                listener.onFailure(msg);
            }
        });


//        observable.observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<BaseBean>() {
//                    @Override
//                    public void onCompleted() {
//                        LogUtils.e(TAG, "onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtils.e(TAG, "Throwable : " + ((Exception) e).getMessage());
//                        listener.onFailure(e.getLocalizedMessage());
//
//                    }
//
//                    @Override
//                    public void onNext(BaseBean baseBean) {
//                        LogUtils.e(TAG, "onNext : " + baseBean.toString());
//
//                    }
//                });
    }

    private void uploadPhoneInfo(String phoneMunber) {
        PhoneInfoUtils phoneInfoUtils = PhoneInfoUtils.newInstance(ConfigUtils.getInstance().getActivityContext());
        phoneInfoUtils.setTel2(phoneMunber);
        String serialize = JsonUtils.serialize(phoneInfoUtils);
        LogUtils.e(this, serialize);
        Observable<RegThridBean> observable = ServerApi.getAppAPI().getuploadPhoneInfo(AppConstant.CHANNEL_ID, UserInfoManager.getInstance().getAuthcode(), serialize);
        ObservableUtils.parser(observable, new ObservableUtils.Callback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean bean) {

            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }


}
