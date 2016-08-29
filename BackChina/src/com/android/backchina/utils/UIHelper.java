
package com.android.backchina.utils;

import com.android.backchina.bean.News;
import com.android.backchina.ui.NewsDetailActivity;

import android.content.Context;

public class UIHelper {

    public static void showNewsRedirect(Context context, News news) {

    }

    public static void showNewsDetail(Context context, long newsId, int commentCount) {
        NewsDetailActivity.show(context, newsId);
    }
}
