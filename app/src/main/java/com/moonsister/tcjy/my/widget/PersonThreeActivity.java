package com.moonsister.tcjy.my.widget;

import android.support.v4.app.Fragment;

import com.hickey.tool.base.BaseFragmentActivity;


/**
 * Created by jb on 2016/9/29.
 */

public class PersonThreeActivity extends BaseFragmentActivity {
    @Override
    protected Fragment initFragment() {
        return new PersonThreeFragment();
    }

    @Override
    protected String initTitleName() {
        return getIntent().getStringExtra("name");
    }
}
