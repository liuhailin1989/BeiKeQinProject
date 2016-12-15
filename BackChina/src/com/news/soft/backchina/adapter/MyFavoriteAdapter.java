package com.news.soft.backchina.adapter;

import java.util.Date;

import com.news.soft.backchina.R;
import com.news.soft.backchina.base.adapter.BaseListAdapter;
import com.news.soft.backchina.bean.FavoriteBean;
import com.news.soft.backchina.utils.StringUtils;

public class MyFavoriteAdapter extends BaseListAdapter<FavoriteBean>{

	public MyFavoriteAdapter(Callback callback) {
		super(callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void convert(ViewHolder vh, FavoriteBean item, int position) {
		// TODO Auto-generated method stub
		vh.setText(R.id.tv_title, item.getTitle());
//		vh.setImageForNet(R.id.iv_thumb, item.getPic());
		vh.setGone(R.id.iv_thumb);
		long dateline = 0;
		if(item.getDateline().contains("-")){
			vh.setText(R.id.tv_time,StringUtils.friendlyTime(item.getDateline()));
		} else {
			try {
				dateline = Long.valueOf(item.getDateline());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			vh.setText(R.id.tv_time,StringUtils.friendly_time(dateline));
		}
//		vh.setText(R.id.tv_origin,item.getFrom());
	}

	@Override
	protected int getLayoutId(int position, FavoriteBean item) {
		// TODO Auto-generated method stub
		return R.layout.layout_list_item_favorite;
	}

}
