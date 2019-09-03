package com.sharpflux.supergodeliveryapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;



public class SendPlaceOrderActivity extends AppCompatActivity {

    private static final String TAG = "SendPlaceOrderActivity";
    private int mYear, mMonth, mDay, mHour, mMinute;
    private TextView mDisplayDate,mDisplayTime,buttonSubmit;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeListener;
    private String format="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_place_order);

        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mDisplayTime = findViewById(R.id.tvTime);
        buttonSubmit=findViewById(R.id.buttonSubmit);


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendPlaceOrderActivity.this,OrderSuccessfullyPlaced.class);
                startActivity(intent);
            }
        });

        mDisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

               // showTime(mHour,mMinute);

                TimePickerDialog timePickerDialog = new TimePickerDialog(SendPlaceOrderActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mTimeListener,mHour,mMinute,false);

                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                timePickerDialog.show();
            }
        });


        mTimeListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int min) {

               // hourOfDay = hourOfDay + 1;
              //  Log.d(TAG, "onDateSet: hh:mm: " + hourOfDay + ":" + minute);

                //String time = hourOfDay + ":" + minute;

                if (hour == 0) {
                    hour += 12;
                    format = "AM";
                } else if (hour == 12) {
                    format = "PM";
                } else if (hour > 12) {
                    hour -= 12;
                    format = "PM";
                } else {
                    format = "AM";
                }

                mDisplayTime.setText(new StringBuilder().append(hour).append(" : ").append(min)
                        .append(" ").append(format));
               // mDisplayTime.setText(time);

            }
        };

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SendPlaceOrderActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };


        }//0nc



}//act