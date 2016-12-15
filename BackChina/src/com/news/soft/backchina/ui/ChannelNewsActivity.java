
package com.news.soft.backchina.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.news.soft.backchina.bean.ChannelItem;
import com.news.soft.backchina.manager.ChannelManager;

/**
 * 频道管理
 */
public class ChannelNewsActivity extends BaseChannelActivity {
    public static String TAG = "ChannelActivity";  

    public static void show(Context context,Fragment fragment) {
        Intent intent = new Intent(context, ChannelNewsActivity.class);
        fragment.startActivityForResult(intent, Activity.RESULT_FIRST_USER);
    }

	@Override
	protected List<ChannelItem> getLocalChannelList() {
		// TODO Auto-generated method stub
		return ChannelManager.getInstance().getNewsChannelItemsFromTabLocal(this);
	}

	@Override
	protected List<ChannelItem> getAllChannelList() {
		// TODO Auto-generated method stub
		return  ChannelManager.getInstance().getNewsChannelItemsFromTabAll(this);
	}

	@Override
	protected void saveSelectChannelList() {
		// TODO Auto-generated method stub
		ChannelManager.getInstance().saveNewsChannelItemToTabLocal(this, getMyChannelData());
	}
}
