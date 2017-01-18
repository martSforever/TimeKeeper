package com.martsforever.owa.timekeeper.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.util.ActivityManager;

public class SelectRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView emailText;
    private TextView mobileText;
    private ImageView emailImg;
    private ImageView mobileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_register);
        initView();
    }

    private void initView() {
        emailText = (TextView) findViewById(R.id.select_register_email_text);
        mobileText = (TextView) findViewById(R.id.select_register_mobile_text);
        emailText.setOnClickListener(this);
        mobileText.setOnClickListener(this);

        emailImg = (ImageView) findViewById(R.id.select_register_email_img);
        mobileImg = (ImageView) findViewById(R.id.select_register_mobile_img);
        emailImg.setOnClickListener(this);
        mobileImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_register_email_img:
                System.out.println("email");
            case R.id.select_register_email_text:
                Intent intentEmail = new Intent();
                intentEmail.setClass(this, EmailRegisterActivity.class);
                ActivityManager.addDestoryActivity(this, SelectRegisterActivity.class.getName());
                startActivity(intentEmail);
                break;
            case R.id.select_register_mobile_img:
                System.out.println("mobile");
            case R.id.select_register_mobile_text:
                Intent intentMobile = new Intent();
                intentMobile.setClass(this, MobileRegisterActivity.class);
                ActivityManager.addDestoryActivity(this, MobileRegisterActivity.class.getName());
                startActivity(intentMobile);
        }
    }
}
