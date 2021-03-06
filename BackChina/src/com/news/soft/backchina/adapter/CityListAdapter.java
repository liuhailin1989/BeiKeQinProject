package com.news.soft.backchina.adapter;

import java.util.ArrayList;
import java.util.List;

import com.news.soft.backchina.R;
import com.news.soft.backchina.bean.City;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CityListAdapter extends BaseAdapter {

	final int VIEW_TYPE = 4;

	public static final int TYPE_NORMAL = 0;
	
	public static final int TYPE_HOT = 1;

	public static final int TYPE_CITY_GROUP = 2;
	
	public static final int TYPE_CITY_CAT = 3;

	private Context mContext;

	private LayoutInflater inflater;

	private List<City> mListData = new ArrayList<City>();
	
	public CityListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<City> history,List<City> list) {
		mListData.clear();
		if (history != null ) {
			mListData.addAll(0, history);
		}
		mListData.addAll(list);
	}

	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (mListData != null && position < mListData.size()) {
			return mListData.get(position).getListItemType();
		}
		return super.getItemViewType(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mListData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		int viewType = getItemViewType(position);
		
		switch (viewType) {
		case TYPE_NORMAL:
		case TYPE_HOT:
		{
			ViewHolder holder = null;
			if (convertView == null) {
				 holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.layout_list_item_type_city, null);
				holder.itemName = (TextView) convertView.findViewById(R.id.city_name);
				convertView.setTag(holder);
			}else {
                holder = (ViewHolder) convertView.getTag();
            }
			holder.itemName.setText(mListData.get(position).getTitle());
			break;
		}
		case TYPE_CITY_GROUP:
		case TYPE_CITY_CAT:
		{
			GroupViewHolder holder = null;
			if (convertView == null) {
				holder = new GroupViewHolder();
				convertView = inflater.inflate(R.layout.layout_list_item_type_city_group, null);
				holder.groupName = (TextView) convertView.findViewById(R.id.city_group);
				convertView.setTag(holder);
			}else {
                holder = (GroupViewHolder) convertView.getTag();
            }
			holder.groupName.setText(mListData.get(position).getTitle());
			break;
		}
		default:
			break;
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView itemName;
	}
	
	private static class GroupViewHolder {
		TextView groupName;
	}
}
