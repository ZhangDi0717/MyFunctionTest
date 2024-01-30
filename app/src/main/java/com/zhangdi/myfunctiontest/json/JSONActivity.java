package com.zhangdi.myfunctiontest.json;

import android.content.Context;
import android.util.Log;
import android.view.View;


import com.zhangdi.myfunctiontest.BaseAppcompat;
import com.zhangdi.myfunctiontest.MessageEvent;
import com.zhangdi.myfunctiontest.excel.ExcelActivity;
import com.zhangdi.myfunctiontest.excel.ExcelManager;
import com.zhangdi.myfunctiontest.excel.FactoryTestMessage;
import com.zhangdi.myfunctiontest.excel.Student;
import com.zhangdi.myfunctiontest.utils.PLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: zhangdi45
 * Date: 20:29 2024/1/8
 */
public class JSONActivity extends BaseAppcompat {
    private static String TAG = ExcelActivity.class.getSimpleName();
    private ExcelManager excelManager;
    private List<Student> students = new ArrayList<Student>();
    private final SimpleDateFormat df = new SimpleDateFormat("yyMMdd");// 设置日期格式
    private String files = System.getProperty("user.dir")+"\\app\\src\\main\\assets\\factory_test_item.json";

    private Student liHua;

    @Override
    protected void initData() {
        files = System.getProperty("user.dir")+"\\app\\src\\main\\assets\\factory_test_item.json";
        Log.d(TAG, "initData: files="+files);
        mList.add("序列化对象");
        mList.add("返序列化对象");
        mList.add("序列化对象数组");
        mList.add("返序列化对象数组");
        mList.add("读取json文件");
        excelManager = new ExcelManager();
        HashMap<String, String> map = new HashMap<>();
        map.put("name","aaa");
        map.put("message","bbb");
        students.add(new Student("zhangsan",12,map));
        students.add(new Student("jack",12,map));
        students.add(new Student("rose",12,map));
        students.add(new Student("mark",12,map));
        students.add(new Student("judy",12,map));
        liHua = new Student("lihua",13,map);
    }

    @Override
    protected void onItemClicked(View view, int position) {
        EventBus.getDefault().post(new MessageEvent(position));
    }




    private String jsonString;
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onBackgroundMessageEvent(MessageEvent event) throws FileNotFoundException {
        Log.d(TAG, "onBackgroundMessageEvent: ");
        switch (event.what) {
            case 0:
                jsonString = JsonUtil.toJSON(liHua);
                Log.d(TAG, "onBackgroundMessageEvent: jsonString: "+jsonString);
                break;
            case 1:
                Student student = JsonUtil.parseObject(jsonString, Student.class);//注意Student 需要有空构造函数
                Log.d(TAG, "onBackgroundMessageEvent: student = " + student);
                break;
            case 2:
                jsonString = JsonUtil.toJSON(students);
                Log.d(TAG, "onBackgroundMessageEvent: jsonString = " + jsonString);
                break;
            case 3:
                List<Student> students = (List<Student>)JsonUtil.parseCollection(jsonString, ArrayList.class, Student.class);//注意Student 需要有空构造函数
                PLog.i(TAG, "onBackgroundMessageEvent: students = " + students);
                for (int i = 0; i < students.size(); i++) {
                    PLog.i(TAG, "onBackgroundMessageEvent: students = " + students.get(i));
                }
                break;
            case 4:
                Log.d(TAG, "onBackgroundMessageEvent:start timestamp "+System.currentTimeMillis());
                jsonString = readTxFile(files);
                List<FactoryTestMessage> factoryTestMessages = (List<FactoryTestMessage>)JsonUtil.parseCollection(jsonString, ArrayList.class, FactoryTestMessage.class);
                for (int i = 0; i < factoryTestMessages.size(); i++) {
                    if (i == factoryTestMessages.size() - 1) {
                        Map<String, String> message = factoryTestMessages.get(i).getMessage();
                        Log.d(TAG, "onBackgroundMessageEvent:stop timestamp "+System.currentTimeMillis());
                    }
                }
                break;
        }
    }

    private String readTxFile(String filename){
        StringBuffer buffer = new StringBuffer();
        try {
            InputStream inputStream = mContext.getAssets().open("factory_test_item.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null){
                buffer.append(line);
            }
            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

}
