package com.zhangdi.myfunctiontest.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.zhangdi.myfunctiontest.R;

/**
 * author: zhangdi45
 * Date: 13:57 2023/11/10
 */
public class HikMonthView extends MonthView {
    private static final String TAG = "HikariCalendarView";

    /**
     * 自定义魅族标记的文本画笔
     */
    private Paint mTextPaint = new Paint();

    private Paint mCurrentDateBGPaint = new Paint();

    /**
     * 自定义魅族标记的圆形背景
     */
    private Paint mSchemeBasicPaint = new Paint();
    private float mRadio;
    private int mPadding;
    private float mSchemeBaseLine;
    public HikMonthView(Context context) {
        super(context);
        mTextPaint.setTextSize(dipToPx(context, 8));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);
        mRadio = dipToPx(getContext(), 7);
        mPadding = dipToPx(getContext(), 4);
        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mRadio - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);

        Log.d(TAG, "HikMonthView: mCurDayTextPaint = "+mCurDayTextPaint.getColor());

        mCurrentDateBGPaint.setColor(getContext().getColor(R.color.black_4));
        mCurDayTextPaint.setColor(getContext().getColor(R.color.black));
        Log.d(TAG, "HikMonthView: mCurDayTextPaint = "+mCurDayTextPaint.getColor());

        mSelectTextPaint.setColor(Color.WHITE);
        mSelectedPaint.setColor(getContext().getColor(R.color.blue_theme));
        Log.d(TAG, "HikMonthView: 设置背景色 = "+getContext().getColor(R.color.blue_theme));
    }

    /**
     * 绘制选中的日子
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return 返回true 则绘制onDrawScheme，因为这里背景色不是是互斥的，所以返回true
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        ////这里绘制选中的日子样式，看需求需不需要继续调用onDrawScheme
        Log.d(TAG, "onDrawSelected: "+calendar.toString());
        mSelectedPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x + mPadding, y + mPadding, x + mItemWidth - mPadding, y + mItemHeight - mPadding, mSelectedPaint);
        return true;
    }



    /**
     * 绘制标记的事件日子
     *
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        //这里绘制标记的日期样式，想怎么操作就怎么操作
        Log.d(TAG, "onDrawScheme: calendar = "+calendar.toString());
    }

    /**
     * 绘制文本
     *
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        //这里绘制文本，不要再问我怎么隐藏农历了，不要再问我怎么把某个日期换成特殊字符串了，要怎么显示你就在这里怎么画，你不画就不显示，是看你想怎么显示日历的，而不是看框架
       // Log.d(TAG, "onDrawText: ");
        int cx = x + mItemWidth / 2;
        int top = y;
        boolean isInRange = isInRange(calendar);
        if (isSelected) {
//            mSelectedPaint.setColor(getContext().getColor(R.color.blue_theme));
//            mSelectTextPaint.setColor(getContext().getColor(R.color.white));
            Log.d(TAG, "isSelected onDrawText: "+calendar.toString());
            Log.d(TAG, "onDrawText: 背景色 = "+mSelectedPaint.getColor());
            canvas.drawRoundRect(x + mPadding, y + mPadding, x + mItemWidth - mPadding, y + mItemHeight - mPadding,(float) 4.0,(float)4.0,mSelectedPaint);
            canvas.drawText(calendar.isCurrentDay() ?"今":String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mSelectTextPaint);
        } else if (hasScheme) {
            Log.d(TAG, "hasScheme onDrawText: "+calendar.toString());
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentMonth() && isInRange ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            if (calendar.isCurrentDay()){
                canvas.drawRoundRect(x + mPadding, y + mPadding, x + mItemWidth - mPadding, y + mItemHeight - mPadding,(float) 4.0,(float)4.0,mCurrentDateBGPaint);
                Log.d(TAG, "onDrawText: mCurDayTextPaint = "+mCurDayTextPaint.getColor());

                mCurDayTextPaint.setColor(getContext().getColor(R.color.black));
                Log.d(TAG, "onDrawText: mCurDayTextPaint = "+mCurDayTextPaint.getColor());
            }
//            mCurMonthTextPaint.setColor(getContext().getColor(R.color.black_text));
            Log.d(TAG, "onDrawText: 其它日期字体颜色 = "+mCurMonthTextPaint.getColor());
            Log.d(TAG, "onDrawText: isInRange = "+isInRange);
            canvas.drawText(calendar.isCurrentDay() ?"今":String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint : calendar.isCurrentMonth() && isInRange ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
