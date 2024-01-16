package com.zhangdi.myfunctiontest.excel;

import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * author: zhangdi45
 * Date: 14:07 2023/11/2
 */
public class ExcelManager {
    private static final String TAG = "ExcelManager";
    public ExcelManager() {

    }

    private POIFSFileSystem fs  = null;
    private HSSFWorkbook wb = null;
    public void newExcel(String path){
        try {
            fs = new POIFSFileSystem(new FileInputStream(path));
            wb = new HSSFWorkbook();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addDate(List<Student> list){
        Log.d(TAG, "addDate: ");
        HSSFSheet sheet = wb.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();

        //添加数据
        for (int i = 0; i <list.size(); i++) {
            HSSFRow row = sheet.createRow(lastRowNum + i);
            row.createCell(1).setCellValue(list.get(i).getName());
            row.createCell(2).setCellValue(list.get(i).getAge());

        }
    }

    public void close(){
        try {
            wb.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public void saveListBeanToExcel(String path, List<Student> list){
        newExcel(path);
        addDate(list);
        close();
    }


}
