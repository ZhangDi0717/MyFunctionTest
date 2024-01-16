package com.zhangdi.myfunctiontest.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hdev.calendar.base.BaseCalendarView;
import com.hdev.calendar.bean.DateInfo;
import com.hdev.calendar.view.RangeCalendarView;
import com.hdev.calendar.view.WeekCalendarView;
import com.zhangdi.myfunctiontest.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function4;

public class CalendarHDevActivity extends AppCompatActivity {

    private RangeCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_hdev);
        calendarView = findViewById(R.id.calendar_view);
        long time = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();

        List<DateInfo> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            calendar.setTime(new Date(time-1000*60*60*24*i));
            list.add(new DateInfo(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE)));
        }
        calendarView.setClickableDateList(list);
        calendarView.setOnDateRangeSelectedListener(new Function4<BaseCalendarView, DateInfo, DateInfo, DateInfo, Unit>() {
            @Override
            public Unit invoke(BaseCalendarView baseCalendarView, DateInfo dateInfo, DateInfo dateInfo2, DateInfo dateInfo3) {
                Toast.makeText(baseCalendarView.getContext(), dateInfo2.format()+"---"+dateInfo3.format(), Toast.LENGTH_SHORT).show();
                return null;
            }
        });

    }
}