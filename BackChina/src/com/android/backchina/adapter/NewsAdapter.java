package com.android.backchina.adapter;

import com.android.backchina.R;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.News;
import com.android.backchina.utils.StringUtils;

public class NewsAdapter extends BaseListAdapter<News>{

	 private String systemTime;
	 
	public NewsAdapter(Callback callback) {
		super(callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void convert(ViewHolder vh, News item, int position) {
		// TODO Auto-generated method stub
		vh.setText(R.id.tv_title, item.getTitle());
		vh.setImageForNet(R.id.iv_thumb, item.getPic());
		vh.setText(R.id.tv_time,StringUtils.friendly_time(item.getDateline()));
		vh.setText(R.id.tv_origin,item.getFrom());
	}

	@Override
	protected int getLayoutId(int position, News item) {
		// TODO Auto-generated method stub
		return R.layout.layout_list_item_news;
	}
	
    public void setSystemTime(String systemTime) {
        this.systemTime = systemTime;
    }

}
