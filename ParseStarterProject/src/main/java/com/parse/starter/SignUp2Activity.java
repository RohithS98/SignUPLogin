package com.parse.starter;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.List;

public class SignUp2Activity extends AppCompatActivity {

    CheckBox c1,c2,c3,c4;
    EditText t1, openTime, closeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);
        c1 = (CheckBox)findViewById(R.id.checkBox1);
        c2 = (CheckBox)findViewById(R.id.checkBox2);
        c3 = (CheckBox)findViewById(R.id.checkBox3);
        c4 = (CheckBox)findViewById(R.id.checkBox4);
        t1 = (EditText)findViewById(R.id.hospitalCode);
        openTime = (EditText)findViewById(R.id.openTime);
        openTime.setInputType(InputType.TYPE_NULL);
        closeTime = (EditText)findViewById(R.id.closeTime);
        closeTime.setInputType(InputType.TYPE_NULL);

        openTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(SignUp2Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        openTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });

        closeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(SignUp2Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        closeTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });
    }

    public void submitDetails(View view) {
        if(t1.getText().toString().matches("")){
            Toast.makeText(this, "Please Enter Hospital ID", Toast.LENGTH_SHORT).show();
        }
        else{
            Boolean b1, b2, b3, b4;
            b1 = c1.isChecked();
            b2 = c2.isChecked();
            b3 = c3.isChecked();
            b4 = c4.isChecked();
            String checked = b1.toString() + " " + b2.toString() + " " + b3.toString() + b4.toString();
            Toast.makeText(SignUp2Activity.this, checked, Toast.LENGTH_SHORT).show();
        }
    }
}
