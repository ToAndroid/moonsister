package com.moonsister.tcjy.main.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moonsister.tcjy.R;
import com.moonsister.tcjy.adapter.HomePageFragmentAdapter;
import com.moonsister.tcjy.base.BaseFragment;
import com.moonsister.tcjy.bean.DynamicItemBean;
import com.moonsister.tcjy.bean.UserInfoDetailBean;
import com.moonsister.tcjy.main.presenter.HomePageFragmentPresenter;
import com.moonsister.tcjy.main.presenter.HomePageFragmentPresenterImpl;
import com.moonsister.tcjy.main.view.HomePageFragmentView;
import com.moonsister.tcjy.utils.EnumConstant;
import com.moonsister.tcjy.viewholder.HomePageHeadHolder;
import com.moonsister.tcjy.widget.XListView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by jb on 2016/9/1.
 */
public class HomePageFragment extends BaseFragment implements HomePageFragmentView {
    @Bind(R.id.xlv)
    XListView xlv;
    private HomePageFragmentPresenter presenter;
    private HomePageFragmentAdapter adapter;
    private String userId;
    private boolean isRefresh;
    private HomePageHeadHolder headHolder;
    private EnumConstant.SearchType type = EnumConstant.SearchType.all;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userId = getActivity().getIntent().getStringExtra("id");
        presenter = new HomePageFragmentPresenterImpl();
        presenter.attachView(this);
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    public static Fragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    protected void initData() {
        headHolder = new HomePageHeadHolder();
        headHolder.setOnClickListener(new HomePageHeadHolder.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presenter == null)
                    return;
                switch (view.getId()) {
                    case R.id.rl_all:
                        type = EnumConstant.SearchType.all;

                        break;
                    case R.id.rl_user:
                        type = EnumConstant.SearchType.user;
                        break;
                    case R.id.rl_dynamic:
                        type = EnumConstant.SearchType.dynamic;
                        break;
                }
                isRefresh = true;
                presenter.loadRefresh(userId, type);
            }
        });
        xlv.setVerticalLinearLayoutManager();
        xlv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                presenter.loadHeader(userId);
                presenter.loadRefresh(userId, type);
            }

            @Override
            public void onLoadMore() {
                isRefresh = false;
                presenter.loadMore(userId, type);
            }
        });
        xlv.addHeaderView(headHolder.getContentView());
        xlv.setRefreshing(true);
    }

    @Override
    public void setDynamicData(List<DynamicItemBean> list) {
        if (adapter == null) {
            adapter = new HomePageFragmentAdapter(list);
            xlv.setAdapter(adapter);
        } else {
            if (isRefresh)
                adapter.clean();
            adapter.addListData(list);
            adapter.onRefresh();
        }
        if (xlv != null) {
            xlv.refreshComplete();
            xlv.loadMoreComplete();
        }
    }

    @Override
    public void setHeaderData(UserInfoDetailBean bean) {
        if (headHolder != null) {
            headHolder.refreshView(bean);
        }
    }


    @Override
    public void showLoading() {
        showProgressDialog();
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    @Override
    public void transfePageMsg(String msg) {
        showToast(msg);
    }
}
