package com.caij.codehub.interactor.imp;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.caij.codehub.API;
import com.caij.codehub.bean.Issue;
import com.caij.codehub.bean.Page;
import com.caij.codehub.interactor.IssueListInteractor;
import com.caij.codehub.interactor.InteractorCallBack;
import com.caij.lib.utils.VolleyManager;
import com.caij.lib.volley.request.GsonRequest;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Caij on 2015/11/3.
 */
public class IssueListInteractorImp implements IssueListInteractor {

    @Override
    public void getIssueList(String owner, String repoName, Page page, Object requestTag, final InteractorCallBack<List<Issue>> interactorCallBack) {
        interactorCallBack.onLoading();
        StringBuilder builder = new StringBuilder(API.API_HOST);
        builder.append("/repos/").append(owner).append("/").append(repoName).append("/").append("issues");
        String url = builder.toString();

        Map<String, String> params = new HashMap<>();
        params.put(API.PAGE, String.valueOf(page.getPageIndex()));
        params.put(API.PER_PAGE, String.valueOf(page.getPageDataCount()));

        GsonRequest<List<Issue>> request = new GsonRequest<List<Issue>>(Request.Method.GET, url, params, new TypeToken<List<Issue>>() {
        }.getType(),
                new Response.Listener<List<Issue>>() {
                    @Override
                    public void onResponse(List<Issue> response) {
                        interactorCallBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               interactorCallBack.onError(error);
            }
        });
        VolleyManager.addRequest(request, requestTag);
    }
}
