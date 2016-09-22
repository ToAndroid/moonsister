package com.moonsister.tcjy.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moonsister.tcjy.widget.XListView;

import java.util.List;

/**
 * Created by jb on 2016/9/22.
 */
public abstract class BaseListFragment<T extends BaseRecyclerViewAdapter> extends BaseFragment {
    protected XListView mXListView;
    protected T t;
    private boolean isRefresh;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mXListView = new XListView(container.getContext());
        mXListView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (isVerticalLinearLayoutManager())
            mXListView.setVerticalLinearLayoutManager();
        else
            mXListView.setHorizontalLinearLayoutManager();
        View rootView = null;
        View view = addListViewHead();
        if (view == null) {
            rootView = mXListView;
        } else {
            LinearLayout linearLayout = new LinearLayout(container.getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(view);
            linearLayout.addView(mXListView);
            rootView = linearLayout;

        }
        return rootView;
    }

    /**
     * 加在listview 上面布局
     *
     * @return
     */
    protected View addListViewHead() {
        return null;
    }

    @Override
    protected void initData() {
        if (isRefreshStatus()) {
            mXListView.setLoadingListener(new XRecyclerView.LoadingListener() {
                @Override
                public void onRefresh() {
                    isRefresh = true;
                    BaseListFragment.this.onRefresh();
                }

                @Override
                public void onLoadMore() {
                    isRefresh = false;
                    BaseListFragment.this.onLoadMore();
                }
            });
        } else {
            mXListView.setPullRefreshEnabled(false);
            mXListView.setLoadingMoreEnabled(false);
        }

        t = setAdapter();
        if (t == null) {
            throw new RuntimeException("Adapter not is null ");
        }
        View headerView = addHeaderView();
        if (headerView != null) {
            mXListView.addHeaderView(headerView);
        }
        mXListView.setAdapter(t);
        mXListView.setRefreshing(true);
    }

    /**
     * 添加头布局
     *
     * @return
     */
    protected View addHeaderView() {
        return null;
    }

    public abstract T setAdapter();

    /**
     * 刷新数据
     */
    protected abstract void onRefresh();

    /**
     * 加载更多
     */
    protected abstract void onLoadMore();


    protected void addData(List datas) {
        if (isRefresh) {
            t.clean();
        }
        t.addListData(datas);
        t.onRefresh();
        mXListView.loadMoreComplete();
        mXListView.refreshComplete();
    }

    /**
     * 横向
     *
     * @return
     */
    public boolean isVerticalLinearLayoutManager() {
        return true;
    }

    /**
     * 是否刷新
     *
     * @return
     */
    public boolean isRefreshStatus() {
        return true;
    }
}
