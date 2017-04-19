package com.martsforever.owa.timekeeper.main.self;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.avos.avoscloud.AVObject;
import com.martsforever.owa.timekeeper.R;
import com.yydcdut.sdlv.Menu;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.util.List;

@ContentView(R.layout.activity_jurisdiction)
public class JurisdictionActivity extends AppCompatActivity {

    List<AVObject> slefFriendship;
    List<AVObject> friendFriendship;

    Menu menu;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    public static void actionStart(Activity activity){
        Intent intent = new Intent();
        intent.setClass(activity,JurisdictionActivity.class);
        activity.startActivity(intent);
    }

}
