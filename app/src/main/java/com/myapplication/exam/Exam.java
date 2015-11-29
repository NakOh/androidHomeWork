package com.myapplication.exam;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class Exam extends AppCompatActivity {
    Handler handler = new Handler();
    TimerTask timetask;
    static {
        System.loadLibrary("exam");
    }
    public native int FLEDControl(int led_num, int val1, int val2, int val3);
    public native int DotMatrixControl(String data);
    public native int TextLCDOut(String str, String str2);
    public native int IOCtlClear();
    public native int IOCtlReturnHome();
    public native int IOCtlDisplay(boolean bOn);
    public native int IOCtlCursor(boolean bOn);
    public native int IOCtlBlink(boolean bOn);
    public native int SegmentControl(int value);
    public native int SegmentIOControl(int value);
    public native int LEDControl(int value);

    private TextView time;
    private Button startButton;
    private Button endButton;
    private AlarmManager mManager;
    private GregorianCalendar mCalendar;
    private NotificationManager mNotification;
    private int years, months, days, hours, mins;
    private static final String INTENT_ACTION = "arabiannight.tistory.com.alarmmanager";
    private MediaPlayer mp=new MediaPlayer();

    private TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hours = hourOfDay; mins = minute;
            mCalendar.set(years, months, days, hours, mins);
            setAlarm();
        }
    };

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            years = year; months = monthOfYear; days = dayOfMonth;
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR);
            int minute = c.get(Calendar.MINUTE);
            new TimePickerDialog(Exam.this, AlertDialog.THEME_HOLO_DARK, timeListener, hour, minute, false).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam);
        time = (TextView) findViewById(R.id.timeView);
        startButton = (Button) findViewById(R.id.start);
        endButton = (Button) findViewById(R.id.alarmOff);
        mNotification = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mCalendar = new GregorianCalendar();
        timetask = new TimerTask() {
            @Override
            public void run() {
                update();
            }
        };

        startButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(Exam.this, AlertDialog.THEME_HOLO_DARK, dateListener, year, month, day).show();
            }
        });

        endButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                resetAlarm();
            }
        });
        Timer timer = new Timer();
        timer.schedule(timetask, 0, 1000);
    }

    private void setAlarm() {
        mManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent());
        Toast.makeText(getApplicationContext(), "알람 등록 완료", Toast.LENGTH_SHORT).show();
    }

    private void resetAlarm() {
        mManager.cancel(pendingIntent());
        Toast.makeText(getApplicationContext(), "알람 등록 해제", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exam, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private PendingIntent pendingIntent() {
        Intent i = new Intent(getApplicationContext(), Exam.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        return pi;
    }

    protected void update(){
        Runnable updater = new Runnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String strNow = sdfNow.format(date);
                time.setText(strNow);
            }
        };
        handler.post(updater);
    }
}
