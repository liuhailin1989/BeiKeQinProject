
package com.android.backchina.utils;

import com.android.backchina.bean.News;
import com.android.backchina.ui.NewsDetailActivity;

import android.content.Context;

public class UIHelper {

    
    public static void showUrlRedirect(Context context, String url) {
//        showUrlRedirect(context, 0, url);
    }
    
    public static void enterNewsDetail(Context context, long newsId, int commentCount) {
        NewsDetailActivity.show(context, newsId);
    }
    
    public static void enterNewsDetail(Context context, News news) {
        NewsDetailActivity.show(context, news);
    }
}
