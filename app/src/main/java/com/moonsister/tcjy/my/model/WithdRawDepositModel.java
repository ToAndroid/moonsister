package com.moonsister.tcjy.my.model;

import com.moonsister.tcjy.base.BaseIModel;
import com.moonsister.tcjy.bean.DefaultDataBean;
import com.moonsister.tcjy.bean.WithdRawDepositBean;

/**
 * Created by jb on 2016/7/2.
 */
public interface WithdRawDepositModel extends BaseIModel {
    void loadEnableMoney(onLoadDateSingleListener<WithdRawDepositBean> listener);
}
