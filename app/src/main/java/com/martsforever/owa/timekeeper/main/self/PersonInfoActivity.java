package com.martsforever.owa.timekeeper.main.self;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.avos.avoscloud.AVUser;
import com.github.zagum.switchicon.SwitchIconView;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_person_info)
public class PersonInfoActivity extends AppCompatActivity {

    @ViewInject(R.id.self_nickname_edit)
    private MaterialEditText nicknameEdit;
    @ViewInject(R.id.self_username_edit)
    private MaterialEditText usernameEdit;
    @ViewInject(R.id.self_email_edit)
    private MaterialEditText emailEdit;
    @ViewInject(R.id.self_mobile_edit)
    private MaterialEditText mobile;
    @ViewInject(R.id.self_switch_schedule_btn)
    private SwitchIconView switchScheduleBtn;
    @ViewInject(R.id.self_switch_invitation_btn)
    private SwitchIconView switchInvitationBtn;

    AVUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
    }

    @Event(R.id.self_edit_save_btn)
    private void editUsername(View view) {
        if (nicknameEdit.isEnabled()) {
            /*保存操作*/
            nicknameEdit.setEnabled(false);
            nicknameEdit.setHideUnderline(true);
            ((ImageView) view).setImageDrawable(getResources().getDrawable(R.drawable.icon_edit));
            user.put(Person.NICK_NAME,nicknameEdit.getText().toString().trim());
            user.saveInBackground();
        } else {
            /*编辑操作*/
            nicknameEdit.setEnabled(true);
            nicknameEdit.setHideUnderline(false);
            ((ImageView) view).setImageDrawable(getResources().getDrawable(R.drawable.icon_save));
        }

    }
    @Event(R.id.self_back_btn)
    private void back(View view){
        this.finish();
    }

    @Event(R.id.self_switch_schedule_btn)
    private void switchSchedule(View view){
        SwitchIconView switchIconView = (SwitchIconView) view;
        switchIconView.switchState();
        user.put(Person.SCHEDULE_AVAILABLE,switchScheduleBtn.isIconEnabled());
        user.saveInBackground();
    }

    @Event(R.id.self_switch_invitation_btn)
    private void switchInvitation(View view){
        SwitchIconView switchIconView = (SwitchIconView) view;
        switchIconView.switchState();
        user.put(Person.RECEIVE_INVITATION,switchScheduleBtn.isIconEnabled());
        user.saveInBackground();
    }

    private void initView() {
        user = AVUser.getCurrentUser();
        usernameEdit.setText(user.getUsername());
        nicknameEdit.setText(user.getString(Person.NICK_NAME));
        emailEdit.setText(user.getEmail());
        mobile.setText(user.getMobilePhoneNumber());
        switchScheduleBtn.setIconEnabled(user.getBoolean(Person.SCHEDULE_AVAILABLE));
        switchInvitationBtn.setIconEnabled(user.getBoolean(Person.RECEIVE_INVITATION));
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PersonInfoActivity.class);
        context.startActivity(intent);
    }
}
