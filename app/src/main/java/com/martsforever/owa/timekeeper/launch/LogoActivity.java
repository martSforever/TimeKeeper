package com.martsforever.owa.timekeeper.launch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.bmob.BmobUtil;

import java.util.Timer;
import java.util.TimerTask;

public class LogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        BmobUtil.bmobInitialize(this);
        BmobUtil.bmobSmsInitialize(this);
        entryLaunchActivity(3000);
    }

    /**
     * 暂停几秒之后进入引导界面
     *
     * @param second
     */
    private void entryLaunchActivity(int second) {
        final Intent jumpIntent = new Intent(this, LaunchActivity.class);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startActivity(jumpIntent);
                finish();
            }
        };
        (new Timer()).schedule(timerTask, second);
    }
}
