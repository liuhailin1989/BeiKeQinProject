
package com.android.backchina.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.backchina.R;
import com.android.backchina.base.BaseActivity;

public class NewsDetailActivity extends BaseActivity {

    public static void show(Context context, long id) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_layout);
    }
}
