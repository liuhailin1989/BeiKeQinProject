package com.android.backchina.adapter;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.backchina.R;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.Subscribe;
import com.android.backchina.interf.ISubscribeListener;
import com.android.backchina.utils.StringUtils;

public class SubscribeAdapter extends BaseListAdapter<Subscribe>{

    public static final int TYPE_INFOLIST = 0;//infolist
	
	public static final int TYPE_SPACE = 1;//space
	
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
		if (item.getType() == TYPE_INFOLIST) {
			if (StringUtils.isEmpty(item.getFavid())) {
				vh.setText(R.id.btn_add_subscribe, "+ 订阅");
			} else {
				vh.setText(R.id.btn_add_subscribe, "取消订阅");
			}
		}else if(item.getType() == TYPE_SPACE){
			vh.setText(R.id.btn_add_subscribe, "取消订阅");
		}
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
