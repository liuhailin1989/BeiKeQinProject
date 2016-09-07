package com.android.backchina.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.android.backchina.bean.ChannelItem;
import com.android.backchina.manager.ChannelManager;

public class ChannelBlogActivity extends BaseChannelActivity{

    public static void show(Context context,Fragment fragment) {
        Intent intent = new Intent(context, ChannelBlogActivity.class);
        fragment.startActivityForResult(intent, Activity.RESULT_FIRST_USER);
    }
    
	@Override
	protected List<ChannelItem> getLocalChannelList() {
		// TODO Auto-generated method stub
		return ChannelManager.getInstance().getBlogChannelItemsFromTabLocal(this);
	}

	@Override
	protected List<ChannelItem> getAllChannelList() {
		// TODO Auto-generated method stub
		return ChannelManager.getInstance().getBlogChannelItemsFromTabAll(this);
	}

	@Override
	protected void saveSelectChannelList() {
		// TODO Auto-generated method stub
		ChannelManager.getInstance().saveBlogChannelItemToTabLocal(this, getMyChannelData());
	}

}
