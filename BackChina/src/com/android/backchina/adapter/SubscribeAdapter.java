package com.android.backchina.adapter;

import com.android.backchina.R;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.Subscribe;

public class SubscribeAdapter extends BaseListAdapter<Subscribe>{

    public SubscribeAdapter(Callback callback) {
        super(callback);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void convert(ViewHolder vh, Subscribe item, int position) {
        // TODO Auto-generated method stub
        vh.setImageForNet(R.id.iv_icon, item.getLogo());
        vh.setText(R.id.tv_name, item.getTitle());
    }

    @Override
    protected int getLayoutId(int position, Subscribe item) {
        // TODO Auto-generated method stub
        return R.layout.layout_list_item_subscribe;
    }
}
