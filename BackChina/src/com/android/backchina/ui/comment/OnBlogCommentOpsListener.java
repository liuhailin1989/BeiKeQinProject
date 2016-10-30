package com.android.backchina.ui.comment;

import android.view.View;
import android.widget.ImageView;

import com.android.backchina.bean.base.BlogCommentBean;

public interface OnBlogCommentOpsListener {
    void onClick(View view, int position ,BlogCommentBean comment);
    void seeMoreComments(View view);
    void setUserAvatarImage(ImageView imageView, String imageUrl, int placeholder);
}
