package com.martsforever.owa.timekeeper.register;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.bmob.BmobUtil;

public class RetrivePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    TextView retriveByEmail;
    TextView retriveByMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_passwrod);

        initView();
    }

    private void initView() {
        retriveByEmail = (TextView) findViewById(R.id.retrive_by_email_text);
        retriveByMobile = (TextView) findViewById(R.id.retrive_by_mobile_text);
        retriveByEmail.setOnClickListener(this);
        retriveByMobile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.retrive_by_email_text:
                RetrivePasswordByEmailDialog retrivePasswordByEmailDialog = new RetrivePasswordByEmailDialog(this,this);
                break;
            case R.id.retrive_by_mobile_text:
                RetrivePasswordByMobileDialog retrivePasswordByMobileDialog = new RetrivePasswordByMobileDialog(this,this);
                break;
        }
    }
}
