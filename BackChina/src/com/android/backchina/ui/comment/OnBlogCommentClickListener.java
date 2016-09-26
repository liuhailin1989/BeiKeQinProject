package com.android.backchina.ui.comment;

import android.view.View;

import com.android.backchina.bean.base.BlogCommentBean;

public interface OnBlogCommentClickListener {
    void onClick(View view, int position ,BlogCommentBean comment);
    void seeMoreComments(View view);
}
