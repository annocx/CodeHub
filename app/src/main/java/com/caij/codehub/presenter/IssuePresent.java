package com.caij.codehub.presenter;

import com.caij.codehub.ui.listener.IssueUi;

/**
 * Created by Caij on 2015/10/31.
 */
public interface IssuePresent extends BasePresent<IssueUi>{

    public void getIssue(String repo, String issueNumber );
}