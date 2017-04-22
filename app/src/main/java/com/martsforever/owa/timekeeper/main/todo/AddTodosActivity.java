package com.martsforever.owa.timekeeper.main.todo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@ContentView(R.layout.activity_add_todos)
public class AddTodosActivity extends AppCompatActivity {

    @ViewInject(R.id.todo_add_start_time_edit)
    MaterialEditText startTimeEdit;
    @ViewInject(R.id.todo_add_end_time_edit)
    MaterialEditText endTimeEdit;
    @ViewInject(R.id.todo_add_level_edit)
    MaterialEditText levelEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    public static void actionStart(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, AddTodosActivity.class);
        activity.startActivityForResult(intent, 0);
    }

    @Event(R.id.todo_add_back_btn)
    private void back(View view) {
        finish();
    }

    @Event(R.id.todo_add_cancel_btn)
    private void cancel(View view) {
        finish();
    }

    @Event(R.id.todo_add_select_start_time_btn)
    private void selectStartTime(View view) {
        selectTime(startTimeEdit);
    }

    @Event(R.id.todo_add_select_end_time_btn)
    private void selectEndTime(View view) {
        selectTime(endTimeEdit);
    }

    private void selectTime(final MaterialEditText edit) {
        final Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        edit.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                        TimePickerDialog tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                edit.setText(edit.getText().toString() + " " + hourOfDay + ":" + minute + ":" + second);
                            }
                        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), true);
                        tpd.show(getFragmentManager(), "TimePickerDialog");
                        tpd.setAccentColor(Color.parseColor("#41899A"));
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
        dpd.setAccentColor(Color.parseColor("#41899A"));
    }

    @Event(R.id.todo_add_level_select_btn)
    private void selectPeople(View view) {
        PickDialog pickDialog = new PickDialog(AddTodosActivity.this, getLevelSelectData());
        pickDialog.setTitle("PICK LEVEL");
        pickDialog.setOnItemOkListener(new PickDialog.OnItemOkListener() {
            @Override
            public void OnOk(Object object, int position) {
                levelEdit.setText(object.toString());
                switch (position) {
                    case 0:
                        levelEdit.setTag(Todo.LEVEL_IMPORTANT_NONE);
                        break;
                    case 1:
                        levelEdit.setTag(Todo.LEVEL_IMPORTANT_LOW);
                        break;
                    case 2:
                        levelEdit.setTag(Todo.LEVEL_IMPORTANT_MIDDLE);
                        break;
                    case 3:
                        levelEdit.setTag(Todo.LEVEL_IMPORTANT_HEIGHT);
                        break;
                }
            }
        });
    }

    private List<Object> getLevelSelectData() {
        List<Object> data = new ArrayList<>();
        data.add("Not important");
        data.add("Not very important");
        data.add("important");
        data.add("Very important");
        return data;
    }
}
