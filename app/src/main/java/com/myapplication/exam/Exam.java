package com.myapplication.exam;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Array;
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

    BackThread thread = new BackThread();
    BackThread2 thread2 = new BackThread2();
    BackThread3 thread3 = new BackThread3();

    public native int PiezoControl(int value);
    public native int FLEDControl(int led_num, String str);
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
    private Button button;
    private AlarmManagerBroadcastReceiver alarm;
    private GregorianCalendar mCalendar;
    private NotificationManager mNotification;
    private int years, months, days, hours, mins;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private boolean disp, cursor, blink;
    private boolean timeCheck = false;
    private boolean musicCheck = true;
    private boolean alarmCheck  = false;
    private boolean checkThread3 = false;
    private boolean stop = false;
    private int ret;
    int[] led_val;
    //1 도 2 레 3 미 4파 5 솔 6라 7시 8도
    private String[] color = {"red","green","blue","yellow","greenblue","red","green","blue","yellow","greenblue","red","green","blue","yellow","greenblue","red","green","blue","yellow","greenblue","red","green","blue","yellow","greenblue",
            "red","green","blue","yellow","greenblue","red","green","blue","yellow","greenblue","red","green","blue","yellow","greenblue","red","green","blue","yellow","greenblue","red","green","blue","yellow","greenblue",
            "red","green","blue","yellow","greenblue","red","green","blue","yellow","greenblue","red","green","blue","yellow","greenblue","red","green","blue","yellow","greenblue"};
    private int[] alarm1= {6,6,0,6,6,0,17,0,18,0,6,6,0,6,6,0,5,0,52,0,6,6,0,6,6,0,17,0,18,0,6,6,0,6,6,0,5,0,52,0,33,22,19,19,19,19,19,19,19,19,19,19,19,19,19,19,33,22,66,66,66,66,66,66,66,66,66,66,66,66,66,66,33,22,18,18,18,18,18,18,18,18,18,18,18,18,18,18,17,18,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private int[] alarm2= {0,0,0,0,23,0,23,23,18,0,18,0,18,0,18,18,34,34,34,34,34,0,33,34,33,33,33,33,33,33,33,33,
            0,0,0,0,21,0,22,0,23,0,23,0,18,18,18,0,18,0,18,0,22,22,22,0,21,21,21,21,21,21,21,21,
            0,0,0,0,0,0,23,23,18,0,18,0,18,0,18,18,34,34,34,34,34,0,33,34,33,33,33,33,33,33,33,33,
            0,0,0,0,21,21,22,22,23,0,23,23,18,18,18,0,18,0,18,0,22,22,22,0,21,21,21,0,19,0,19,0,
            19,0,19,0,19,0,19,0,35,0,35,19,0,0,0,35,0,35,19,0,19,0,19,0,19,0,0,3,0,0,0,0,
            19,0,19,0,19,0,19,0,35,0,35,19,0,0,0,67,67,0,67,67,0,21,21,21,21,21,21,21,21,21,21};
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

            Intent i = getIntent();
            checkThread3 = i.getBooleanExtra("checkThread3", false);
            musicCheck = i.getBooleanExtra("song", false);
            setContentView(R.layout.exam);

            led_val = new int[4];
        led_val[0] = 9;
        for (int j = 1; j < 4; j++) {
            led_val[j] = 0;
        }
        disp = true;
        cursor = false;
        blink = false;

        IOCtlClear();
        IOCtlReturnHome();
        IOCtlDisplay(disp);
        IOCtlCursor(cursor);
        IOCtlBlink(blink);

        ret = TextLCDOut(" Wake Up!!! ", "You must go School  ");

        time = (TextView) findViewById(R.id.timeView);

        startButton = (Button) findViewById(R.id.start);
        endButton = (Button) findViewById(R.id.alarmOff);
        button = (Button) findViewById(R.id.button);

        radioButton1 =(RadioButton) findViewById(R.id.radioButton1);
        radioButton2 =(RadioButton) findViewById(R.id.radioButton2);

        mNotification = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        alarm = new AlarmManagerBroadcastReceiver();
        mCalendar = new GregorianCalendar();

        radioButton1.setChecked(true);

        timetask = new TimerTask() {
            @Override
            public void run() {
                update();
            }
        };

        PiezoControl(0);

        if(checkThread3) {
            thread3.setDaemon(true);
            thread3.start();
        }
        else {
            thread.setDaemon(true);
            thread.start();
            thread2.setDaemon(true);
            thread2.start();
        }

        startButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                led_val[0] = 9;
                FLEDControl(led_val[0],"yellow");
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(Exam.this, AlertDialog.THEME_HOLO_DARK, dateListener, year, month, day).show();
            }
        });

        endButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                led_val[0] = 8;
                FLEDControl(led_val[0], "red");
                resetAlarm();
            }
        });

        radioButton1.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                led_val[0] = 7;
                FLEDControl(led_val[0], "blue");
                musicCheck = true;
            }
        });

        radioButton2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                led_val[0] = 6; // 초록
                FLEDControl(led_val[0],"green");
                musicCheck = false;
            }
        });

        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                led_val[0] = 5;
                FLEDControl(led_val[0], "greenblue");
                stop = true;
                IOCtlBlink(true);
                IOCtlBlink(false);
                TextLCDOut(" Wake Up!!! ", "You must go School  ");
                for (int i = 0; i < 20; i++) {
                }
                IOCtlBlink(true);
                IOCtlBlink(false);
                TextLCDOut(" Wake Up!!! ", "You must go School  ");
            }

        });
        Timer timer = new Timer();
        timer.schedule(timetask, 0, 1000);
    }

    private void setAlarm() {
        Context context = this.getApplicationContext();
        if(alarm != null){
            alarmCheck = true;
            alarm.setOnetimeTimer(context, mCalendar.getTimeInMillis(), musicCheck);
        }else{
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetAlarm() {
        Context context = this.getApplicationContext();
        if(alarm != null) {
            alarmCheck = false;
            alarm.CancelAlarm(context);
        }
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
        Intent i = new Intent(Exam.this, Exam.class);
        i.putExtra("checkThread3", true);
        PendingIntent pi = PendingIntent.getActivity(Exam.this, 0, i,0);
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
    class BackThread2 extends Thread {
        public void run(){
            while(true){
                if(timeCheck){
                    if(alarmCheck) {
                        DotMatrixControl("PM YES");
                    }else{
                        DotMatrixControl("PM NO");
                    }
                }else{
                    if(alarmCheck) {
                        DotMatrixControl("AM YES");
                    }else{
                        DotMatrixControl("AM NO");
                    }
                }
            }
        }
    }
    class BackThread3 extends Thread {
        public void run(){
            while(true) {
                if (stop == false) {
                    if (musicCheck) {
                        for (int i = 0; i < alarm1.length; i++) {
                            if (stop == true){
                                PiezoControl(0);
                                break;
                            }
                            PiezoControl(alarm1[i]);
                                try {
                                BackThread3.sleep(150);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (i == alarm1.length - 1) {
                                i = 0;
                            }
                        }
                    } else {
                        for (int i = 0; i < alarm2.length; i++) {
                            if (stop == true){
                                PiezoControl(0);
                                break;
                            }
                            PiezoControl(alarm2[i]);
                            try {
                                BackThread3.sleep(120);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (i == alarm2.length - 1) {
                                i = 0;
                            }
                        }
                    }
                }else{

                }
            }
        }
    }

    class BackThread extends Thread {
        public void run() {
            while (true) {
                SegmentIOControl(1);
                int result = 0;
                Time t = new Time();
                t.set(System.currentTimeMillis());
                result = t.hour * 10000 + t.minute * 100 + t.second;
                SegmentControl(result);
                for(int i =0; i<100; i++) {
                    if (t.hour >= 12) {
                       timeCheck = true;
                    } else {
                        timeCheck = false;
                    }
                }
            }
        }
    }
}
