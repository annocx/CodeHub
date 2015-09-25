package com.caij.codehub.presenter;

import com.caij.codehub.ui.listener.LoginUi;

/**
 * Created by Caij on 2015/8/25.
 */
public interface LoginPresenter extends BasePresent<LoginUi>{
    public void login(String username, String pwd);
}