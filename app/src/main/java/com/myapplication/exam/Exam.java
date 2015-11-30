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
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class Exam extends AppCompatActivity implements View.OnTouchListener{
    /** Called when the activity is first created. */
    static { System.loadLibrary("exam"); }  // JNI Library load
    public native int PiezoControl(int value);	// JNI Interface
    int PiezoData;
    static ImageButton[] white;
    static ImageButton[] black;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam);
        PiezoData = 0;
        PiezoControl(PiezoData);
        white = new ImageButton[21];
        black = new ImageButton[15];

        white[0] = (ImageButton)findViewById(R.id.white1);
        white[0].setTag(new int[]{1, R.drawable.whiteback1, R.drawable.white1});
        white[1] = (ImageButton)findViewById(R.id.white2);
        white[1].setTag(new int[]{2, R.drawable.whiteback2, R.drawable.white2});
        white[2] = (ImageButton)findViewById(R.id.white3);
        white[2].setTag(new int[]{3, R.drawable.whiteback3, R.drawable.white3});
        white[3] = (ImageButton)findViewById(R.id.white4);
        white[3].setTag(new int[]{4, R.drawable.whiteback4, R.drawable.white4});
        white[4] = (ImageButton)findViewById(R.id.white5);
        white[4].setTag(new int[]{5, R.drawable.whiteback5, R.drawable.white5});
        white[5] = (ImageButton)findViewById(R.id.white6);
        white[5].setTag(new int[]{6, R.drawable.whiteback6, R.drawable.white6});
        white[6] = (ImageButton)findViewById(R.id.white7);
        white[6].setTag(new int[]{7, R.drawable.whiteback7, R.drawable.white7});
        white[7] = (ImageButton)findViewById(R.id.white8);
        white[7].setTag(new int[]{17, R.drawable.whiteback1, R.drawable.white1});
        white[8] = (ImageButton)findViewById(R.id.white9);
        white[8].setTag(new int[]{18, R.drawable.whiteback2, R.drawable.white2});
        white[9] = (ImageButton)findViewById(R.id.white10);
        white[9].setTag(new int[]{19, R.drawable.whiteback3, R.drawable.white3});
        white[10] = (ImageButton)findViewById(R.id.white11);
        white[10].setTag(new int[]{20, R.drawable.whiteback4, R.drawable.white4});
        white[11] = (ImageButton)findViewById(R.id.white12);
        white[11].setTag(new int[]{21, R.drawable.whiteback5, R.drawable.white5});
        white[12] = (ImageButton)findViewById(R.id.white13);
        white[12].setTag(new int[]{22, R.drawable.whiteback6, R.drawable.white6});
        white[13] = (ImageButton)findViewById(R.id.white14);
        white[13].setTag(new int[]{23, R.drawable.whiteback7, R.drawable.white7});
        white[14] = (ImageButton)findViewById(R.id.white15);
        white[14].setTag(new int[]{33, R.drawable.whiteback1, R.drawable.white1});
        white[15] = (ImageButton)findViewById(R.id.white16);
        white[15].setTag(new int[]{34, R.drawable.whiteback2, R.drawable.white2});
        white[16] = (ImageButton)findViewById(R.id.white17);
        white[16].setTag(new int[]{35, R.drawable.whiteback3, R.drawable.white3});
        white[17] = (ImageButton)findViewById(R.id.white18);
        white[17].setTag(new int[]{36, R.drawable.whiteback4, R.drawable.white4});
        white[18] = (ImageButton)findViewById(R.id.white19);
        white[18].setTag(new int[]{37, R.drawable.whiteback5, R.drawable.white5});
        white[19] = (ImageButton)findViewById(R.id.white20);
        white[19].setTag(new int[]{38, R.drawable.whiteback6, R.drawable.white6});
        white[20] = (ImageButton)findViewById(R.id.white21);
        white[20].setTag(new int[]{39, R.drawable.whiteback7, R.drawable.white7});

        black[0] = (ImageButton)findViewById(R.id.black1);
        black[0].setTag(new int[]{49, R.drawable.blackback1,R.drawable.black1});
        black[1] = (ImageButton)findViewById(R.id.black2);
        black[1].setTag(new int[]{50, R.drawable.blackback2,R.drawable.black2});
        black[2] = (ImageButton)findViewById(R.id.black3);
        black[2].setTag(new int[]{51, R.drawable.blackback3,R.drawable.black3});
        black[3] = (ImageButton)findViewById(R.id.black4);
        black[3].setTag(new int[]{52, R.drawable.blackback4,R.drawable.black4});
        black[4] = (ImageButton)findViewById(R.id.black5);
        black[4].setTag(new int[]{53, R.drawable.blackback5,R.drawable.black5});
        black[5] = (ImageButton)findViewById(R.id.black6);
        black[5].setTag(new int[]{65, R.drawable.blackback1,R.drawable.black1});
        black[6] = (ImageButton)findViewById(R.id.black7);
        black[6].setTag(new int[]{66, R.drawable.blackback2,R.drawable.black2});
        black[7] = (ImageButton)findViewById(R.id.black8);
        black[7].setTag(new int[]{67, R.drawable.blackback3,R.drawable.black3});
        black[8] = (ImageButton)findViewById(R.id.black9);
        black[8].setTag(new int[]{68, R.drawable.blackback4,R.drawable.black4});
        black[9] = (ImageButton)findViewById(R.id.black10);
        black[9].setTag(new int[]{69, R.drawable.blackback5,R.drawable.black5});
        black[10] = (ImageButton)findViewById(R.id.black11);
        black[10].setTag(new int[]{81, R.drawable.blackback1,R.drawable.black1});
        black[11] = (ImageButton)findViewById(R.id.black12);
        black[11].setTag(new int[]{82, R.drawable.blackback2,R.drawable.black2});
        black[12] = (ImageButton)findViewById(R.id.black13);
        black[12].setTag(new int[]{83, R.drawable.blackback3,R.drawable.black3});
        black[13] = (ImageButton)findViewById(R.id.black14);
        black[13].setTag(new int[]{84, R.drawable.blackback4,R.drawable.black4});
        black[14] = (ImageButton)findViewById(R.id.black15);
        black[14].setTag(new int[]{85, R.drawable.blackback5,R.drawable.black5});

        white[0].setOnTouchListener(this);
        white[1].setOnTouchListener(this);
        white[2].setOnTouchListener(this);
        white[3].setOnTouchListener(this);
        white[4].setOnTouchListener(this);
        white[5].setOnTouchListener(this);
        white[6].setOnTouchListener(this);
        white[7].setOnTouchListener(this);
        white[8].setOnTouchListener(this);
        white[9].setOnTouchListener(this);
        white[10].setOnTouchListener(this);
        white[11].setOnTouchListener(this);
        white[12].setOnTouchListener(this);
        white[13].setOnTouchListener(this);
        white[14].setOnTouchListener(this);
        white[15].setOnTouchListener(this);
        white[16].setOnTouchListener(this);
        white[17].setOnTouchListener(this);
        white[18].setOnTouchListener(this);
        white[19].setOnTouchListener(this);
        white[20].setOnTouchListener(this);

        black[0].setOnTouchListener(this);
        black[1].setOnTouchListener(this);
        black[2].setOnTouchListener(this);
        black[3].setOnTouchListener(this);
        black[4].setOnTouchListener(this);
        black[5].setOnTouchListener(this);
        black[6].setOnTouchListener(this);
        black[7].setOnTouchListener(this);
        black[8].setOnTouchListener(this);
        black[9].setOnTouchListener(this);
        black[10].setOnTouchListener(this);
        black[11].setOnTouchListener(this);
        black[12].setOnTouchListener(this);
        black[13].setOnTouchListener(this);
        black[14].setOnTouchListener(this);
    }
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        boolean bret = false;
        if (view instanceof ImageButton) {
            ImageButton imgBtn = (ImageButton) view;
            bret = pianoKeyHandle(imgBtn, action);
        }
        return bret;
    }

    private boolean pianoKeyHandle(ImageButton imgBtn, int action) {
        boolean bret = false;
        Object obj = imgBtn.getTag();
        if (obj != null) {
            if (obj instanceof int[]) {
                int[] tag = (int[]) obj;
                if (tag.length == 3) {
                    if (action == MotionEvent.ACTION_DOWN) {
                        System.out.println(tag[0]);
                        PiezoControl(tag[0]);
                        imgBtn.setImageResource(tag[1]);
                    } else if (action == MotionEvent.ACTION_UP) {
                        imgBtn.setImageResource(tag[2]);
                        /*try {
                            Thread.sleep(9 * 10);
                          } catch (InterruptedException e) { }*/
                        PiezoControl(0);
                    } else if (action == MotionEvent.ACTION_MOVE){
                        imgBtn.setImageResource(tag[2]);
                        PiezoControl(0);
                    }
                }
            }
        }
        return bret;
    }
}

