package com.caij.codehub.interactor;

import com.caij.codehub.bean.Page;
import com.caij.codehub.bean.Repository;

import java.util.List;

/**
 * Created by Caij on 2015/9/18.
 */
public interface RepositoryListInteractor extends Interactor {

    public void getUserStarredRepositories(String username, String token, Page page, Object requestTag, InteractorCallBack<List<Repository>> interactorCallBack);

    public void getUserRepositories(String username, String token, Page page, Object requestTag, InteractorCallBack<List<Repository>> interactorCallBack);

    public void getSearchRepository(String q, String sort, String order, Page page, Object requestTag, InteractorCallBack<List<Repository>> interactorCallBack);

    public void getTrendingRepository(String since, String language,Object requestTag, InteractorCallBack<List<Repository>> interactorCallBack);

}
