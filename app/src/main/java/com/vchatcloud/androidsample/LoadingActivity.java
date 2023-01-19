package com.vchatcloud.androidsample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.vchatcloud.android.VChatCloud;

public class LoadingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        VChatCloud.getInstance();
    }
    @Override
    protected void onStart() {
        super.onStart();
        startLoading();
    }
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            int maxRetry = 5;
            @Override
            public void run() {
                --maxRetry;
                if (MainActivity.getLoading()) { // 소켓이 붙는 경우 바로 진입
                    finish();
                } else {
                    if (maxRetry > 0) { // 5회 시도
                        handler.postDelayed(this, 1000);
                    } else {
                        // 5회 실패 시 실패시 강제 종료
                        moveTaskToBack(true);
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }
            }
        }, 1000);
    }
}
