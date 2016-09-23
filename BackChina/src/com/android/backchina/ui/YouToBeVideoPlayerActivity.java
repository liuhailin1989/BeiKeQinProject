package com.android.backchina.ui;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.backchina.AppContext;
import com.android.backchina.DeveloperKey;
import com.android.backchina.R;
import com.android.backchina.adapter.YouToBeVideoDetailAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.adapter.BaseListAdapter.Callback;
import com.android.backchina.bean.Video;
import com.android.backchina.bean.VideoDetail;
import com.android.backchina.bean.base.ResultListBean;
import com.android.backchina.utils.TLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class YouToBeVideoPlayerActivity extends YouTubeFailureRecoveryActivity implements Callback ,OnItemClickListener{

	private final static String BUNDLE_KEY_VIDEO = "BUNDLE_KEY_VIDEO";
	
	 protected TextHttpResponseHandler mHandler;
	 
	private Context mContext;
	
	private YouTubePlayerView youtobePlayer;
	
	private TextView mToolBarName;
	
	private TextView mToolBarPlayCount;
	
	private ListView mDetailListView;
	
	private Video mCurrentVideo;
	
	private YouToBeVideoDetailAdapter mYouToBeVideoDetailAdapter;
	
	private RequestManager mImgLoader;
	
	private List<VideoDetail> mListData = new ArrayList<VideoDetail>();
	
	private VideoDetail mCurrentVideoDetail;
	
	private YouTubePlayer youTubePlayer;
	
    public static void show(Context context,Video video) {
        Intent intent = new Intent(context, YouToBeVideoPlayerActivity.class);
        intent.putExtra(BUNDLE_KEY_VIDEO, video);
        context.startActivity(intent);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	initBundle(getIntent().getExtras());
        setContentView(R.layout.activity_video_player_layout);
        mContext = this;
        setupViews();
        initData();
    }
    
	protected void initBundle(Bundle bundle) {
		mCurrentVideo = (Video) bundle.getSerializable(BUNDLE_KEY_VIDEO);
	}
	
	private void setupViews() {
		youtobePlayer = (YouTubePlayerView) findViewById(R.id.youtube_view);
		mToolBarName = (TextView) findViewById(R.id.tv_name);
		mToolBarPlayCount = (TextView) findViewById(R.id.tv_play_count);
		mDetailListView = (ListView) findViewById(R.id.detail_list);
	}
    
    private Type getType(){
        return new TypeToken<ResultListBean<VideoDetail>>() {}.getType();
    }
    
    private void initData(){
		refreshToolBar();
    	//
    	mYouToBeVideoDetailAdapter = new YouToBeVideoDetailAdapter(this);
    	mDetailListView.setAdapter(mYouToBeVideoDetailAdapter);
    	mDetailListView.setItemChecked(0, true);
    	mDetailListView.setOnItemClickListener(this);
		//
    	mHandler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                    Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
				try {
					ResultListBean<VideoDetail> bean = AppContext.createGson()
							.fromJson(responseString, getType());
					mListData = bean.getItems();
					mCurrentVideoDetail = mListData.get(0);
					youtobePlayer.initialize(DeveloperKey.DEVELOPER_KEY, (OnInitializedListener) mContext);
					handleData();
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
    	};
        requestData();
    }
    
    private void handleData(){
    	mYouToBeVideoDetailAdapter.clear();
    	mYouToBeVideoDetailAdapter.addItem(mListData);
    }
    
    private void refreshToolBar(){
    	mToolBarName.setText(mCurrentVideo.getTitle());
    	//
    	String fromat = mContext.getResources().getString(
				R.string.video_item_views);
		String viewsValue = String.format(fromat, mCurrentVideo.getViews());
		mToolBarPlayCount.setText(viewsValue);
		//
		
    }
    
    
    private void requestData(){
    	BackChinaApi.getVideoDetail(mCurrentVideo.getUrlapi(), mHandler);
    }

	@Override
	public void onInitializationSuccess(Provider provider, YouTubePlayer player,
			boolean wasRestored) {
		// TODO Auto-generated method stub
		TLog.d("wasRestored = " +wasRestored);
		youTubePlayer = player;
		if (!wasRestored) {
			player.cueVideo(mCurrentVideoDetail.getUvid());
		}
	}

	@Override
	protected Provider getYouTubePlayerProvider() {
		// TODO Auto-generated method stub
		return (YouTubePlayerView) findViewById(R.id.youtube_view);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mImgLoader != null) {
			mImgLoader.onDestroy();
			mImgLoader = null;
		}
	}

	@Override
	public RequestManager getImgLoader() {
		// TODO Auto-generated method stub
		if (mImgLoader == null) {
			mImgLoader = Glide.with(this);
		}
		return mImgLoader;
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public Date getSystemTime() {
		// TODO Auto-generated method stub
		return new Date();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		mCurrentVideoDetail = (VideoDetail) parent.getAdapter().getItem(position);
		if (youTubePlayer != null) {
			youTubePlayer.cueVideo(mCurrentVideoDetail.getUvid());
		}
	}
    
}
