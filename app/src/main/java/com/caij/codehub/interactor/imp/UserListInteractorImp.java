package com.caij.codehub.interactor.imp;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.caij.codehub.API;
import com.caij.codehub.bean.Page;
import com.caij.codehub.bean.User;
import com.caij.codehub.interactor.UserListInteractor;
import com.caij.codehub.interactor.InteractorCallBack;
import com.caij.lib.utils.VolleyManager;
import com.caij.lib.volley.request.GsonRequest;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Caij on 2015/9/24.
 */
public class UserListInteractorImp implements UserListInteractor {

    @Override
    public void getFollowers(String token, String username, Page page, Object requestTag, final InteractorCallBack<List<User>> interactorCallBack) {
        interactorCallBack.onLoading();
        String url = API.API_HOST + "/users/" + username + "/followers";
        Map<String, String> head = new HashMap<>();
        API.configAuthorizationHead(head, token);

        Map<String, String> params = new HashMap<>();
        params.put(API.PAGE, String.valueOf(page.getPageIndex()));
        params.put(API.PER_PAGE, String.valueOf(page.getPageDataCount()));

        GsonRequest<List<User>> request = new GsonRequest<List<User>>(Request.Method.GET, url, params, head, new TypeToken<List<User>>() {
        }.getType(),
                new Response.Listener<List<User>>() {
                    @Override
                    public void onResponse(List<User> response) {
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

    @Override
    public void getFollowing(String token, String username, Page page, Object requestTag, final InteractorCallBack<List<User>> interactorCallBack) {
        interactorCallBack.onLoading();
        String url = API.API_HOST + "/users/" + username + "/following";
        Map<String, String> head = new HashMap<>();
        API.configAuthorizationHead(head, token);

        Map<String, String> params = new HashMap<>();
        params.put(API.PAGE, String.valueOf(page.getPageIndex()));
        params.put(API.PER_PAGE, String.valueOf(page.getPageDataCount()));

        GsonRequest<List<User>> request = new GsonRequest<List<User>>(Request.Method.GET, url, params, head, new TypeToken<List<User>>() {
        }.getType(),
                new Response.Listener<List<User>>() {
                    @Override
                    public void onResponse(List<User> response) {
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
