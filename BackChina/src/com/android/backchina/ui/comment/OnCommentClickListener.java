package com.android.backchina.ui.comment;

import com.android.backchina.bean.Comment;
import com.android.backchina.bean.base.BlogCommentBean;

import android.view.View;

public interface OnCommentClickListener {
    void onClick(View view, Comment comment);
}
