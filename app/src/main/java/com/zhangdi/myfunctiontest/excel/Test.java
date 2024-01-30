package com.zhangdi.myfunctiontest.excel;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * author: zhangdi45
 * Date: 16:30 2024/1/25
 */
public class Test {

    public static void main(String[] args) throws JSONException {


        String fileName = System.getProperty("user.dir")+"\\app\\src\\main\\assets\\factory_test_item.xlsx";
        String files = System.getProperty("user.dir")+"\\app\\src\\main\\assets\\factory_test_item.json";
        System.out.println(fileName);
        StringBuffer buffer = new StringBuffer("[");
        List<FactoryTestMessage> factoryTestList = new ArrayList<FactoryTestMessage>();
        List<String> list = ExcelPoiUtils.readRowFromFile(fileName, 1);
        for (int i = 1; i < list.size(); i++) {
            System.out.println("loading " + list.get(i));
            Map<String,String> map = ExcelPoiUtils.readStringMapFromFile(fileName, list.get(i));
            if (map == null) {
                continue;
            }
            StringBuffer stringBuffer = new StringBuffer("{");
            Set<String> keySet = map.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                stringBuffer.append("\""+key+"\"").append(":").append("\""+map.get(key)+"\"").append(",");
            }


            if (i ==list.size() -1) {
                buffer.append("{\"version\":\""+list.get(i)+"\",\"message\":"+stringBuffer.substring(0,stringBuffer.length()-2)+"\"}}");
            }else {
                buffer.append("{\"version\":\""+list.get(i)+"\",\"message\":"+stringBuffer.substring(0,stringBuffer.length()-2)+"\"}},\n");
            }
        }

        buffer.append("]");


//        JSONArray jsonArray = new JSONArray(factoryTestList);
        System.out.println(buffer);
        writeTxt(files,buffer.toString());
    }

    public static void writeTxt(String fileName,String data) {
        try
        {   //要指定编码方式，否则会出现乱码
            FileOutputStream fileOutputStream = null;
            File file = new File(fileName);
            if(!file.exists()){
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data.getBytes("utf-8"));
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
