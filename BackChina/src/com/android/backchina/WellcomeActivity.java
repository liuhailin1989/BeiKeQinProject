package com.android.backchina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WellcomeActivity extends Activity{
    
    private Handler handler = new Handler();
    
    private Runnable runnable;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome_layout);
        setupView();
        init();
    }
    
	private void setupView() {
	    handler.removeCallbacks(runnable);
	}
    
    private void init(){
        runnable = new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                redirectTo();
            }
        };
        handler.postDelayed(runnable, 2000);
    } 
    
    private void redirectTo(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
    
}
