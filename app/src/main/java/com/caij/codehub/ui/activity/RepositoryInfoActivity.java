package com.caij.codehub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.caij.codehub.API;
import com.caij.codehub.CodeHubApplication;
import com.caij.codehub.Constant;
import com.caij.codehub.R;
import com.caij.codehub.bean.Repository;
import com.caij.codehub.presenter.PresenterFactory;
import com.caij.codehub.presenter.RepositoryActionPresent;
import com.caij.codehub.presenter.RepositoryInfoPresenter;
import com.caij.codehub.ui.callback.UiCallBack;
import com.caij.codehub.utils.TimeUtils;
import com.caij.lib.utils.CheckValueUtil;
import com.caij.lib.utils.ToastUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Caij on 2015/9/19.
 */
public class RepositoryInfoActivity extends BaseCodeHubActivity {

    @Bind(R.id.tv_repository_creater)
    TextView mRepositoryCreateTextView;
    @Bind(R.id.tv_repository_icon)
    TextView mRepositoryIcon;
    @Bind(R.id.tv_repository_name)
    TextView mRepositoryNameTextView;
    @Bind(R.id.tv_repository_update_time)
    TextView mRepositoryUpdateTimeTextView;
    @Bind(R.id.tv_repository_desc)
    TextView mRepositoryDescTextView;
    @Bind(R.id.tv_repository_language)
    TextView mRepositoryLanguageTextView;
    @Bind(R.id.tv_repository_create_time)
    TextView mRepositoryCreateTimeTextView;
    @Bind(R.id.tv_repository_star)
    TextView mRepositoryStarTextView;
    @Bind(R.id.tv_repository_fork)
    TextView mRepositoryForkTextView;

    private String mOwner;
    private String mRepo;
    private String mToken;
    private RepositoryInfoPresenter mRepositoryInfoPresenter;
    private RepositoryActionPresent mRepositoryActionPresent;
    private Repository mRepository;
    private Menu mMenu;

    public static Intent newInstance(Activity activity, String owner, String repo) {
        CheckValueUtil.check(owner);
        CheckValueUtil.check(repo);
        Intent intent = new Intent(activity, RepositoryInfoActivity.class);
        intent.putExtra(Constant.USER_NAME, owner);
        intent.putExtra(Constant.REPO_NAME, repo);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentContainer.setVisibility(View.GONE);

        Intent intent = getIntent();
        mOwner = intent.getStringExtra(Constant.USER_NAME);
        mRepo = intent.getStringExtra(Constant.REPO_NAME);
        mToken = getToken();

        setToolbarTitle(mRepo);

        mRepositoryInfoPresenter = PresenterFactory.newPresentInstance(RepositoryInfoPresenter.class);
        mRepositoryInfoPresenter.getRepositoryInfo(mRepo, mOwner, mToken, this, mFirstLoadUiCallBack);

        mRepositoryActionPresent = PresenterFactory.newPresentInstance(RepositoryActionPresent.class);
        mRepositoryActionPresent.hasStarRepo(mOwner, mRepo, mToken, this, new UiCallBack<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                getMenuInflater().inflate(R.menu.menu_repository_info, mMenu);
                mMenu.findItem(R.id.star).setTitle(aBoolean ? getString(R.string.un_star) : getString(R.string.star));
            }

            @Override
            public void onLoading() {
            }

            @Override
            public void onError(VolleyError error) {
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_repository_info;
    }

    @OnClick(R.id.ll_create)
    public void onCreaterClick() {
        Intent intent = UserInfoActivity.newIntent(this, mRepository.getOwner().getLogin());
        startActivity(intent);
    }

    @OnClick(R.id.ll_readme)
    public void onReadmeClick() {
        Intent intent = WebActivity.newIntent(this, String.format(API.GITHUB_README, mOwner, mRepo));
        startActivity(intent);
    }

    @OnClick(R.id.ll_source)
    public void onSourceClick() {
        Intent intent = FileTreeActivity.newIntent(this, mOwner, mRepo, mRepository.getDefault_branch());
        startActivity(intent);
    }

    @OnClick(R.id.ll_website)
    public void onWebsiteClick() {
        String homepage = mRepository.getHomepage();
        if (!TextUtils.isEmpty(homepage)) {
            Intent intent = WebActivity.newIntent(this, mRepository.getHomepage());
            startActivity(intent);
        }else {
            ToastUtil.show(this, R.string.not_have_website);
        }
    }

    @OnClick(R.id.ll_issue)
    public void onIssueClick() {
        Intent intent = IssueListActivity.newIntent(this, mOwner, mRepo);
        startActivity(intent);
    }


    @Override
    public void onReFreshBtnClick(View view) {
        super.onReFreshBtnClick(view);
        mRepositoryInfoPresenter.getRepositoryInfo(mRepo, mOwner, mToken, this, mFirstLoadUiCallBack);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.star) {
            if (item.getTitle().equals(getString(R.string.star))) {
                mRepositoryActionPresent.starRepo(mOwner, mRepo, mToken, this, mStarUiCallback);
            }else {
                mRepositoryActionPresent.unstarRepo(mOwner, mRepo, mToken, this, mUnStarUiCallback);
            }
            return true;
        }else if (id == R.id.fork) {
            mRepositoryActionPresent.forkRepo(mOwner, mRepo, mToken, this, mForkUiCallBack);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private UiCallBack<Repository> mFirstLoadUiCallBack = new UiCallBack<Repository>() {
        @Override
        public void onSuccess(Repository repository) {
            hideLoading();
            mRepository = repository;
            showContentContainer();
            mRepositoryNameTextView.setText(repository.getName());
            mRepositoryDescTextView.setText(repository.getDescription());
            mRepositoryStarTextView.setText(String.valueOf(repository.getStargazers_count()));
            mRepositoryForkTextView.setText(String.valueOf(repository.getForks_count()));
            if (repository.isFork()) {
                mRepositoryIcon.setText(getString(R.string.icon_fork));
            } else {
                mRepositoryIcon.setText(getString(R.string.icon_repo));
            }
            mRepositoryLanguageTextView.setText(repository.getLanguage());
            mRepositoryCreateTimeTextView.setText(TimeUtils.getStringTime(repository.getCreated_at()));
            mRepositoryUpdateTimeTextView.setText(getString(R.string.update) + " " + TimeUtils.getRelativeTime(repository.getUpdated_at()));
            mRepositoryCreateTextView.setText(repository.getOwner().getLogin());
        }

        @Override
        public void onLoading() {
            showLoading();
        }

        @Override
        public void onError(VolleyError error) {
            hideLoading();
            showError();
        }
    };

    private UiCallBack<NetworkResponse> mStarUiCallback = new UiCallBack<NetworkResponse>() {
        @Override
        public void onSuccess(NetworkResponse networkResponse) {
            ToastUtil.show(RepositoryInfoActivity.this, R.string.star_repo_success);
            mMenu.findItem(R.id.star).setTitle(getString(R.string.un_star));
        }

        @Override
        public void onLoading() {
            showLoading();
        }

        @Override
        public void onError(VolleyError error) {
            ToastUtil.show(RepositoryInfoActivity.this, R.string.star_repo_error);
        }
    };

    private UiCallBack<NetworkResponse> mUnStarUiCallback = new UiCallBack<NetworkResponse>() {
        @Override
        public void onSuccess(NetworkResponse networkResponse) {
            ToastUtil.show(RepositoryInfoActivity.this, R.string.unstar_repo_success);
            mMenu.findItem(R.id.star).setTitle(getString(R.string.star));
        }

        @Override
        public void onLoading() {
            showLoading();
        }

        @Override
        public void onError(VolleyError error) {
            ToastUtil.show(RepositoryInfoActivity.this, R.string.unstar_repo_error);
        }
    };

    private UiCallBack<NetworkResponse> mForkUiCallBack = new UiCallBack<NetworkResponse>() {
        @Override
        public void onSuccess(NetworkResponse networkResponse) {
            ToastUtil.show(RepositoryInfoActivity.this, R.string.fork_success);
        }

        @Override
        public void onLoading() {
            showLoading();
        }

        @Override
        public void onError(VolleyError error) {
            ToastUtil.show(RepositoryInfoActivity.this, R.string.fork_error);
        }
    };
}
