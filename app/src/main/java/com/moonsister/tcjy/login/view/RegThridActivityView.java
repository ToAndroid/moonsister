package com.moonsister.tcjy.login.view;

import com.moonsister.tcjy.base.BaseIView;

/**
 * Created by x on 2016/8/31.
 */
public interface RegThridActivityView extends BaseIView {
//    void navigationNext(String code);
    void requestFailed(String reason);
    void LoopMsg();
//    void LoopMsg(String phone,String brithday);
    void getThrid();
}