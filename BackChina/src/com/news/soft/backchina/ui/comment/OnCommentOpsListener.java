package com.news.soft.backchina.ui.comment;

import android.view.View;

import com.news.soft.backchina.bean.Comment;

public interface OnCommentOpsListener {
    void onClick(View view, Comment comment);
    void seeMoreComments(View view);
    void refreshCommentsCount(int value);
}
