package com.caij.codehub.presenter.imp;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.caij.codehub.API;
import com.caij.codehub.bean.Comment;
import com.caij.codehub.presenter.CommentActionPresent;
import com.caij.codehub.ui.callback.UiCallBack;
import com.caij.lib.utils.VolleyUtil;
import com.caij.lib.volley.request.GsonRequest;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Caij on 2015/11/3.
 */
public class CommentActionPresentImp implements CommentActionPresent{

//    POST /repos/:owner/:repo/issues/:number/comments
    @Override
    public void createCommentForIssue(String comment, String owner, String repo, final String num, String token, Object requestTag, final UiCallBack<Comment> uiCallBack) {
        try {
            uiCallBack.onLoading();
            StringBuilder builder = new StringBuilder(API.API_HOST);
            builder.append("/repos/").append(owner).append("/").append(repo).append("/issues").append("/").append(num).append("/comments");
            String url = builder.toString();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("body", comment);
            Map<String, String> head = new HashMap<>();
            API.configAuthorizationHead(head, token);
            GsonRequest<Comment> request = new GsonRequest<Comment>(Request.Method.POST, url, jsonObject.toString(), head, new TypeToken<Comment>() {}.getType(),
                    new Response.Listener<Comment>() {
                        @Override
                        public void onResponse(Comment response) {
                            if (response != null) {
                                uiCallBack.onSuccess(response);
                            }else {
                                uiCallBack.onError(new VolleyError());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    uiCallBack.onError(error);
                }
            });
            VolleyUtil.addRequest(request, requestTag);
        } catch (JSONException e) {
            uiCallBack.onError(new VolleyError(e));
        }
    }

}
