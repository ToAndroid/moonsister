package com.moonsister.tcjy.home.model;

import com.moonsister.tcjy.base.BaseIModel;
import com.moonsister.tcjy.bean.ChooseKeyBean;

/**
 * Created by jb on 2016/8/28.
 */
public interface SearchContentFragmentModel extends BaseIModel {
    void loadChooseKey(onLoadDateSingleListener<ChooseKeyBean> listener);
}
