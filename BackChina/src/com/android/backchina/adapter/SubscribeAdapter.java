package com.android.backchina.adapter;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.backchina.R;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.Subscribe;
import com.android.backchina.interf.ISubscribeListener;

public class SubscribeAdapter extends BaseListAdapter<Subscribe>{

	private ISubscribeListener listener;
	
    public SubscribeAdapter(Callback callback) {
        super(callback);
        // TODO Auto-generated constructor stub
    }
    
    public void setSubscribeListener(ISubscribeListener l){
    	listener = l;
    }

    @Override
    public void addItem(List<Subscribe> items) {
    	// TODO Auto-generated method stub
    	super.addItem(items);
    }
    
    @Override
    protected void convert(ViewHolder vh, final Subscribe item, int position) {
        // TODO Auto-generated method stub
        vh.setImageForNet(R.id.iv_icon, item.getLogo());
        vh.setText(R.id.tv_name, item.getTitle());
        vh.getView(R.id.btn_add_subscribe).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listener != null){
					listener.onSubscribe(item);
				}
			}
		});;
    }

    @Override
    protected int getLayoutId(int position, Subscribe item) {
        // TODO Auto-generated method stub
        return R.layout.layout_list_item_subscribe;
    }
}
