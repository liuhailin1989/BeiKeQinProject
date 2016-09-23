package com.android.backchina.ui.comment;

import android.view.View;

import com.android.backchina.bean.base.BlogCommentBean;

public interface OnBlogCommentClickListener {
    void onClick(View view, BlogCommentBean comment);
    void seeMoreComments(View view);
}
