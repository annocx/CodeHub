package com.caij.codehub.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.caij.codehub.Constant;
import com.caij.codehub.bean.Page;
import com.caij.codehub.bean.Repository;
import com.caij.codehub.presenter.BasePresent;
import com.caij.lib.utils.CheckValueUtil;
import com.caij.lib.utils.SPUtils;

import java.util.List;

/**
 * Created by Caij on 2015/9/21.
 */
public class UserStarredRepositoriesFragment extends RepositoriesFragment {

    public static RepositoriesFragment newInstance(String username) {
        CheckValueUtil.check(username);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.USER_NAME, username);
        RepositoriesFragment fragment = new UserStarredRepositoriesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    protected String mUsername;
    protected String mToken;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUsername = getArguments().getString(Constant.USER_NAME);
        mToken = SPUtils.get(Constant.USER_TOKEN, "");
    }

    @Override
    protected void onUserFirstVisible() {
        mPage.reset();
        mPresenter.getUserStarredRepositories(BasePresent.LoadType.FIRSTLOAD, mUsername, mToken, mPage);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.getUserStarredRepositories(BasePresent.LoadType.REFRESH, mUsername, mToken, mPage);
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        mPresenter.getUserStarredRepositories(BasePresent.LoadType.LOADMOER, mUsername, mToken, mPage);
    }

    @Override
    public void onReFreshBtnClick(View view) {
        super.onReFreshBtnClick(view);
        mPage.reset();
        mPresenter.getUserStarredRepositories(BasePresent.LoadType.FIRSTLOAD, mUsername, mToken, mPage);
    }
}