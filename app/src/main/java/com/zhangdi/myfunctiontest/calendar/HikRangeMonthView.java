package com.zhangdi.myfunctiontest.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.RangeMonthView;
import com.zhangdi.myfunctiontest.R;

/**
 * author: zhangdi45
 * Date: 14:18 2023/12/20
 */
public class HikRangeMonthView extends RangeMonthView {
    private static final String TAG = "HikRangeMonthView";
    private Paint mCurrentDateBGPaint = new Paint();
    private Paint mSelectCenterBGPaint = new Paint();//选中中间日期颜色

    public HikRangeMonthView(Context context) {
        super(context);
        mCurrentDateBGPaint.setColor(getContext().getColor(R.color.black_4));
        mSelectCenterBGPaint.setColor(getContext().getColor(R.color.blue_theme_8));
        Log.d(TAG, "HikRangeMonthView: "+mSelectTextPaint.getTypeface());
        mSelectTextPaint.setTypeface(Typeface.DEFAULT);
        Log.d(TAG, "HikRangeMonthView: "+mSelectTextPaint.getTypeface());

    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelectedPre, boolean isSelectedNext) {
        Log.d(TAG, "onDrawSelected: calendar = " + calendar.toString()+", hasScheme = "+hasScheme+", isSelectedPre = "+isSelectedPre+", isSelectedNext = "+isSelectedNext);
        int cx = x + mItemWidth / 2;
        int top = y;
        if (isSelectedNext && isSelectedPre){//绘制中间
            canvas.drawRoundRect(x, y, x + mItemWidth, y + mItemHeight,(float)  0,0,mSelectCenterBGPaint);
            canvas.drawText(calendar.isCurrentDay() ?"今":String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mCurMonthTextPaint);
        }else {
            canvas.drawRoundRect(x, y, x + mItemWidth, y + mItemHeight,(float)  dipToPx(getContext(), 4),(float)dipToPx(getContext(),4),mSelectedPaint);

            canvas.drawText(calendar.isCurrentDay() ?"今":String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mSelectTextPaint);
        }

        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y, boolean isSelected) {
        Log.d(TAG, "onDrawScheme: calendar = " + calendar.toString()+", isSelected = "+isSelected);

    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        //这里绘制文本，不要再问我怎么隐藏农历了，不要再问我怎么把某个日期换成特殊字符串了，要怎么显示你就在这里怎么画，你不画就不显示，是看你想怎么显示日历的，而不是看框架
//         Log.d(TAG, "onDrawText: ");
        int cx = x + mItemWidth / 2;
        int top = y;
        boolean isInRange = isInRange(calendar);
//        Log.d(TAG, "onDrawText: calendar = "+calendar.toString()+", hasScheme = "+hasScheme+", isSelected = "+isSelected+", isInRange = "+isInRange);
         if (hasScheme ||  isSelected) {
            return;
        }

        if (calendar.isCurrentDay()){
            Log.d(TAG, "onDrawText: ");
            canvas.drawRoundRect(x, y, x + mItemWidth, y + mItemHeight,(float) dipToPx(getContext(), 4),(float)dipToPx(getContext(), 4),mCurrentDateBGPaint);
        }
        canvas.drawText(calendar.isCurrentDay() ?"今":String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
    }

    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
