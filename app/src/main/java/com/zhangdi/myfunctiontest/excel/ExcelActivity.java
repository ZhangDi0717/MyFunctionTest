package com.zhangdi.myfunctiontest.excel;

import android.util.Log;
import android.view.View;

import com.zhangdi.myfunctiontest.BaseAppcompat;
import com.zhangdi.myfunctiontest.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * author: zhangdi45
 * Date: 13:55 2023/11/2
 */
public class ExcelActivity extends BaseAppcompat {
    private static String TAG = ExcelActivity.class.getSimpleName();
    private ExcelManager excelManager;
    private List<Student> students = new ArrayList<Student>();
    private final SimpleDateFormat df = new SimpleDateFormat("yyMMdd");// 设置日期格式

    @Override
    protected void initData() {
        mList.add("新建EXCEL");
        mList.add("添加数据");
        excelManager = new ExcelManager();
        students.add(new Student("zhangsan",12));
        students.add(new Student("jack",12));
        students.add(new Student("rose",12));
        students.add(new Student("mark",12));
        students.add(new Student("judy",12));
    }

    @Override
    protected void onItemClicked(View view, int position) {
        EventBus.getDefault().post(new MessageEvent(position));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onBackgroundMessageEvent(MessageEvent event) {
        Log.d(TAG, "onBackgroundMessageEvent: ");
        switch (event.what) {
            case 0:
                String filePath = "/sdcard/uhf";
                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                Date date = new Date();
                String format = df.format(date);
                Log.d(TAG, "onClick: format = "+format);
                String excelFileName = "/result_"+format+".xls";
                String sheetName = "result";
                filePath = filePath + excelFileName;
                excelManager.saveListBeanToExcel(filePath,students);
                break;
            case 1:
                String vision = "DS-MDT201/4&64/5G/M";;
                Map<String,String> map = ExcelPoiUtils.readStringMapFromFile(mContext, "factory_test_item.xlsx", vision);
                break;

        }
    }
}
