package com.zhangdi.utils.excel;

import android.content.Context;
import android.util.Log;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.helpers.ColumnHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelPoiUtils {
    private static final String TAG = "ExcelPoiUtils";
    private static ExcelPoiUtils instance;

    private ExcelPoiUtils() {

    }

    public static ExcelPoiUtils getInstance() {
        if (instance == null) {
            synchronized (ExcelPoiUtils.class) {
                if (instance == null) {
                    instance = new ExcelPoiUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 获取Excel 第index列数据
     * @param context
     * @param filename
     * @param index
     * @return
     */
    public static List<String> readColumnListFromFile(Context context, String filename,int index){
        Log.d(TAG, "readListFromFile: ");
        List<String> stringList = new ArrayList<>();
        try {
            InputStream inputStream = context.getResources().getAssets().open(filename);
            OPCPackage pkg = OPCPackage.open(inputStream);
            XSSFWorkbook wb = new XSSFWorkbook(pkg);
            XSSFSheet sheet0 = wb.getSheetAt(0);
            for (int i = 0; i < sheet0.getPhysicalNumberOfRows(); i++) {
                XSSFRow rowi = sheet0.getRow(i);
                XSSFCell cell = rowi.getCell(0);
                Log.d(TAG, "readListFromFile: cell.getStringCellValue() = "+cell.toString());
                stringList.add(sheet0.getRow(i).getCell(index).toString());

            }
            pkg.close();
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
        return stringList;
    }

    /**
     * 获取excel第index行
     * @param context
     * @param filename
     * @param index
     * @return
     */
    public static List<String> readRowListFromFile(Context context, String filename,int index){
        Log.d(TAG, "readListFromFile: ");
        List<String> stringList = new ArrayList<>();
        try {
            InputStream inputStream = context.getResources().getAssets().open(filename);
            OPCPackage pkg = OPCPackage.open(inputStream);
            XSSFWorkbook wb = new XSSFWorkbook(pkg);
            XSSFSheet sheet0 = wb.getSheetAt(0);
            for (Row row : sheet0) {
                stringList.add(row.toString());
            }
            pkg.close();
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
        return stringList;
    }

    public static Map<String,Integer> readMapFromFile(Context context, String filename, String key){
        Log.d(TAG, "readMapFromFile: 1");
        Map<String,Integer> map = new HashMap<>();
        try {
            InputStream inputStream = context.getResources().getAssets().open(filename);
            OPCPackage pkg = OPCPackage.open(inputStream);
            XSSFWorkbook wb = new XSSFWorkbook(pkg);
            XSSFSheet sheet0 = wb.getSheetAt(0);//sheet0
            XSSFRow row = sheet0.getRow(1);//第2行
            int rowIndex = -1;
            for (Cell cell:row) {
                cell.toString();
                if (key.equals(cell.toString())){
                    rowIndex = cell.getColumnIndex();
                    break;
                }
            }
            if (rowIndex == -1){
                return null;
            }
            //https://blog.csdn.net/weixin_43845227/article/details/123580523  poi颜色
            //https://blog.csdn.net/hadues/article/details/113859228  poi使用文档
            Log.d(TAG, "readMapFromFile: index " + rowIndex);
            for (int i = 0; i < sheet0.getPhysicalNumberOfRows(); i++) {
                XSSFRow rowi = sheet0.getRow(i);
                XSSFCell cellKey = rowi.getCell(0);
                XSSFCell cellValue = rowi.getCell(rowIndex);
                XSSFCellStyle cellStyle = cellValue.getCellStyle();
                XSSFColor color = cellStyle.getFillForegroundXSSFColor();
                if (color != null){
                    map.put(cellKey.toString(),(int)color.getRgb()[0]);
                }else {
                    map.put(cellKey.toString(),-1);
                }
            }
            pkg.close();
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
        return map;
    }

}
