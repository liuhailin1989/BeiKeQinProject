package com.android.backchina.ui;

import android.content.Context;
import android.content.Intent;

import com.android.backchina.base.BaseActivity;

public class CommentBlogActivity extends BaseActivity{

	public static void show(Context context) {
        Intent intent = new Intent(context, CommentBlogActivity.class);
        context.startActivity(intent);
    }
}
