package com.caij.codehub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.caij.codehub.Constant;
import com.caij.codehub.R;
import com.caij.codehub.bean.User;
import com.caij.codehub.dagger.DaggerUtils;
import com.caij.codehub.presenter.UserPresenter;
import com.caij.codehub.ui.fragment.NewsFragment;
import com.caij.codehub.ui.fragment.RepositoryPagesFragment;
import com.caij.codehub.ui.listener.UserUi;
import com.caij.lib.utils.SPUtils;
import com.caij.lib.utils.ToastUtil;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends BaseCodeHubActivity<UserPresenter> implements UserUi {

    @Bind(R.id.img_navigation_avatar)
    ImageView imgNavigationAvatar;
    @Bind(R.id.tv_navigation_username)
    TextView tvNavigationUsername;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.tv_event)
    RadioButton tvEvent;

    private User mUser;

    private Fragment mCurrentShowFragment;
    private RepositoryPagesFragment mRepositoryPagesFragment;
    private NewsFragment mNewsFragment;


    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        toggle.syncState();
        mDrawerLayout.setDrawerListener(toggle);

        mPresenter.getUserInfo(SPUtils.get(Constant.USER_TOKEN, ""), SPUtils.get(Constant.USER_NAME, ""));

        mRepositoryPagesFragment = new RepositoryPagesFragment();
        mNewsFragment = new NewsFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.main_content, mRepositoryPagesFragment).commit();
        mCurrentShowFragment = mRepositoryPagesFragment;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public UserPresenter getPresenter() {
        return DaggerUtils.getPresenterComponent().provideUserPresenter();
    }

    @Override
    public void onGetUserInfoSuccess(User user) {
        mUser = user;
        tvNavigationUsername.setText(user.getLogin());
        Glide.with(this).load(user.getAvatar_url()).placeholder(R.drawable.default_circle_head_image).
                bitmapTransform(new CropCircleTransformation(this)).into(imgNavigationAvatar);
    }

    @OnCheckedChanged(R.id.tv_repository)
    public void onRepositoryCheck(boolean isCheck) {
        if (isCheck) {
            switchContent(mCurrentShowFragment, mRepositoryPagesFragment, R.id.main_content);
            mCurrentShowFragment = mRepositoryPagesFragment;
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    @OnCheckedChanged(R.id.tv_event)
    public void onEventCheck(boolean isCheck) {
//        if (isCheck) {
//            switchContent(mCurrentShowFragment, mNewsFragment, R.id.main_content);
//            mCurrentShowFragment = mNewsFragment;
//            mDrawerLayout.closeDrawer(Gravity.LEFT);
//        }
    }

    @OnClick(R.id.img_navigation_avatar)
    public void onUserOnClick() {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        Intent intent = UserInfoActivity.newIntent(this, mUser.getLogin());
        startActivity(intent);
    }

    @Override
    public void showError(int type, VolleyError error) {
        ToastUtil.show(this, "load user info error");
        processVolleyError(error);
    }

    @Override
    public void showLoading(int type) {

    }
}