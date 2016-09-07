package com.moonsister.tcjy.my.model;

import com.moonsister.tcjy.base.BaseIModel;
import com.moonsister.tcjy.bean.BackTermsBean;
import com.moonsister.tcjy.my.persenter.RenZhengActivityPresenterImpl;
import com.moonsister.tcjy.utils.EnumConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by x on 2016/9/3.
 */
public interface RenZhengActivityModel extends BaseIModel {
    void loadData(onLoadDateSingleListener<BackTermsBean> listener);
//    void sendDynamicPics(EnumConstant.DynamicType dynamicType, String content, List<String> srcdatas, String address, onLoadDateSingleListener defaultDynamicPresenter);
    void submit(String address1, String address2, String text,onLoadDateSingleListener listener);

    void submittt(String address1, String address2, String text,String code,onLoadDateSingleListener listener);

    void submitdata(String str, String order_id, onLoadDateSingleListener listener);
}
