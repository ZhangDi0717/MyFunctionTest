package com.zhangdi.myfunctiontest.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.haibin.calendarview.CalendarView;
import com.zhangdi.myfunctiontest.R;

import java.util.Calendar;
import java.util.Date;

public class CalendarHaiBinActivity extends AppCompatActivity {
    private static final String TAG = "CalendarHaiBinActivity";
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_hai_bin);

        calendarView = findViewById(R.id.calendar_view);

        Calendar calenderStart = Calendar.getInstance();
        java.sql.Date startData = new java.sql.Date(startTime);
        calenderStart.setTime(startData);

        Calendar calenderEnd = Calendar.getInstance();
        calenderEnd.setTime(new java.sql.Date(endTime));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
//        calendarView.setRange(calenderStart.get(Calendar.YEAR), calenderStart.get(Calendar.MONTH)+1, calenderStart.get(Calendar.DATE),
//                calenderEnd.get(Calendar.YEAR), calenderEnd.get(Calendar.MONTH)+1, calenderEnd.get(Calendar.DATE));
        calendarView.setOnCalendarRangeSelectListener(new CalendarView.OnCalendarRangeSelectListener() {
            @Override
            public void onCalendarSelectOutOfRange(com.haibin.calendarview.Calendar calendar) {
                Log.d(TAG, "onCalendarSelectOutOfRange: calendar  " + calendar.toString());
            }

            @Override
            public void onSelectOutOfRange(com.haibin.calendarview.Calendar calendar, boolean isOutOfMinRange) {
                Log.d(TAG, "onSelectOutOfRange: calendar =  " + calendar.toString() + "  isOutOfMinRange = " + isOutOfMinRange);//越界
            }

            @Override
            public void onCalendarRangeSelect(com.haibin.calendarview.Calendar calendar, boolean isEnd) {
                Log.d(TAG, "onCalendarRangeSelect: calendar =  " + calendar.toString() + "  isEnd = " + isEnd);
            }
        });

    }

    private long startTime = System.currentTimeMillis()-1000*60*60*24*6;
    private long endTime = System.currentTimeMillis();

    public void clickL(View view) {
        calendarView.scrollToNext();
    }

    public void clickR(View view) {
        calendarView.scrollToPre();
    }
}