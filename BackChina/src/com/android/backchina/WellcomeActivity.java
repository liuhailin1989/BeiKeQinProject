package com.android.backchina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class WellcomeActivity extends Activity{
    
    private ImageView ivSkip;
    
    private Handler _handler = new Handler();
    
    private Runnable _runnable;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome_layout);
        setupView();
        init();
    }
    
	private void setupView() {
		_handler.removeCallbacks(_runnable);
		redirectTo();
	}
    
    private void init(){
        _runnable = new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                redirectTo();
            }
        };
        _handler.postDelayed(_runnable, 3000);
    } 
    
    private void redirectTo(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    
}
