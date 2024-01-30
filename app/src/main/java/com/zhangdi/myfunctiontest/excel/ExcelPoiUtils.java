package com.zhangdi.myfunctiontest.excel;

import android.content.Context;
import android.util.Log;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelPoiUtils {
    private static final String TAG = "ExcelPoiUtils";
    private static ExcelPoiUtils instance;
    private static final int HAS = -58;//颜色代码

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
     *
     * @param context
     * @param filename
     * @param index
     * @return
     */
    public static List<String> readColumnListFromFile(Context context, String filename, int index) {
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
                Log.d(TAG, "readListFromFile: cell.getStringCellValue() = " + cell.toString());
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
     *
     * @param context
     * @param filename
     * @param index
     * @return
     */
    public static List<String> readRowListFromFile(Context context, String filename, int index) {
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

    public static Map<String, Integer> readMapFromFile(Context context, String filename, String key) {
        Log.d(TAG, "readMapFromFile: 1");
        Map<String, Integer> map = new HashMap<>();
        Thread.currentThread().setContextClassLoader(ExcelPoiUtils.class.getClassLoader());
        try {
            InputStream inputStream = context.getResources().getAssets().open(filename);
            OPCPackage pkg = OPCPackage.open(inputStream);
            XSSFWorkbook wb = new XSSFWorkbook(pkg);
            XSSFSheet sheet0 = wb.getSheetAt(0);//sheet0
            XSSFRow row = sheet0.getRow(1);//第2行
            int rowIndex = -1;
            for (Cell cell : row) {
                cell.toString();
                if (key.equals(cell.toString())) {
                    rowIndex = cell.getColumnIndex();
                    break;
                }
            }
            if (rowIndex == -1) {
                return null;
            }
            //https://blog.csdn.net/weixin_43845227/article/details/123580523  poi颜色
            //https://blog.csdn.net/hadues/article/details/113859228  poi使用文档
            Log.d(TAG, "readMapFromFile: index " + rowIndex);
            for (int i = 0; i < sheet0.getPhysicalNumberOfRows(); i++) {
                XSSFRow rowi = sheet0.getRow(i);
                XSSFCell cellKey = rowi.getCell(0);
                XSSFCell cellValue = rowi.getCell(rowIndex);
                if (cellValue == null) {
                    map.put(cellKey.toString(), -1);
                    continue;
                }
                XSSFCellStyle cellStyle = cellValue.getCellStyle();
                XSSFColor color = cellStyle.getFillForegroundXSSFColor();
                if (color != null) {
                    map.put(cellKey.toString(), (int) color.getRgb()[0]);
                } else {
                    map.put(cellKey.toString(), -1);
                }
            }
            pkg.close();
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
        return map;
    }


    public static Map<String, String> readStringMapFromFile(Context context, String filename, String key) {
        Log.d(TAG, "readStringMapFromFile: threshold = "+Thread.currentThread().getName());
        Map<String, String> map = new HashMap<>();
        Thread.currentThread().setContextClassLoader(ExcelPoiUtils.class.getClassLoader());
        try {
            Log.d(TAG, "readStringMapFromFile: 2");
            InputStream inputStream = context.getResources().getAssets().open(filename);
            Log.d(TAG, "readStringMapFromFile: 3");
//            OPCPackage pkg = OPCPackage.open(inputStream);

            Log.d(TAG, "readStringMapFromFile: 4");
            XSSFWorkbook wb = new XSSFWorkbook(inputStream);


//            long heapSize = Runtime.getRuntime().totalMemory();
//            long heapMaxSize = Runtime.getRuntime().maxMemory();
//            long heapFreeSize = Runtime.getRuntime().freeMemory();
//            long memoryUse = heapSize - heapFreeSize;
//
//            Log.d(TAG, "readStringMapFromFile: heapsize = "+heapSize/1024/1024);
//            Log.d(TAG, "readStringMapFromFile: heapMaxSize = "+heapMaxSize/1024/1024);
//            Log.d(TAG, "readStringMapFromFile: heapFreeSize = "+heapFreeSize/1024/1024);
//            Log.d(TAG, "readStringMapFromFile: memoryUse = "+memoryUse/1024/1024);


            Log.d(TAG, "readStringMapFromFile: 5");
            XSSFSheet sheet0 = wb.getSheetAt(0);//sheet0
            Log.d(TAG, "readStringMapFromFile: 6");

            XSSFRow row = sheet0.getRow(1);//第2行
            Log.d(TAG, "readStringMapFromFile: 7");

            int rowIndex = -1;
            for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                Log.d(TAG, "readStringMapFromFile: i = " + i);
                XSSFCell cell = row.getCell(i);
                if (key.equals(cell.toString())) {
                    rowIndex = cell.getColumnIndex();
                    break;
                }
            }
            if (rowIndex == -1) {
                return null;
            }
            //https://blog.csdn.net/weixin_43845227/article/details/123580523  poi颜色
            //https://blog.csdn.net/hadues/article/details/113859228  poi使用文档
            Log.d(TAG, "readStringMapFromFile: index " + rowIndex);
            for (int i = 0; i < sheet0.getPhysicalNumberOfRows(); i++) {
                XSSFRow rowi = sheet0.getRow(i);
                XSSFCell cellKey = rowi.getCell(0);
                XSSFCell cellValue = rowi.getCell(rowIndex);
                if (cellKey == null || cellValue == null) {
                    Log.d(TAG, "readStringMapFromFile: cellKey == null || cellValue == null");
                    continue;
                }
                XSSFCellStyle cellStyle = cellValue.getCellStyle();
                XSSFColor color = cellStyle.getFillForegroundXSSFColor();
                if (color != null &&  (int) color.getRgb()[0] == HAS) {
                    map.put(cellKey.toString(),cellValue.toString());
                } else {
                    map.put(cellKey.toString(), "");
                }
            }
//            pkg.close();
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
        return map;
    }

    public static List<String> readRowFromFile(String filename,int index){
        List<String> list = new ArrayList<String>();
        Thread.currentThread().setContextClassLoader(ExcelPoiUtils.class.getClassLoader());
        try {
            System.out.println("readStringMapFromFile: 2");
            File inputStream = new File(filename);
            System.out.println("readStringMapFromFile: 3");
//            OPCPackage pkg = OPCPackage.open(inputStream);

            System.out.println("readStringMapFromFile: 4");
            XSSFWorkbook wb = new XSSFWorkbook(inputStream);

            System.out.println("readStringMapFromFile: 5");
            XSSFSheet sheet0 = wb.getSheetAt(0);//sheet0
            System.out.println( "readStringMapFromFile: 6");

            XSSFRow row = sheet0.getRow(index);//第2行
            System.out.println("readStringMapFromFile: 7");

            int rowIndex = -1;
            for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                System.out.println( "readStringMapFromFile: i = " + i);
                XSSFCell cell = row.getCell(i);
                list.add(cell.toString());
            }
        }catch (Exception | Error e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Map<String, String> readStringMapFromFile(String filename, String key) {

        Map<String, String> map = new HashMap<>();
        Thread.currentThread().setContextClassLoader(ExcelPoiUtils.class.getClassLoader());
        try {
            System.out.println("readStringMapFromFile: 2");
            File inputStream = new File(filename);
            System.out.println("readStringMapFromFile: 3");
//            OPCPackage pkg = OPCPackage.open(inputStream);

            System.out.println("readStringMapFromFile: 4");
            XSSFWorkbook wb = new XSSFWorkbook(inputStream);


//            long heapSize = Runtime.getRuntime().totalMemory();
//            long heapMaxSize = Runtime.getRuntime().maxMemory();
//            long heapFreeSize = Runtime.getRuntime().freeMemory();
//            long memoryUse = heapSize - heapFreeSize;
//
//            Log.d(TAG, "readStringMapFromFile: heapsize = "+heapSize/1024/1024);
//            Log.d(TAG, "readStringMapFromFile: heapMaxSize = "+heapMaxSize/1024/1024);
//            Log.d(TAG, "readStringMapFromFile: heapFreeSize = "+heapFreeSize/1024/1024);
//            Log.d(TAG, "readStringMapFromFile: memoryUse = "+memoryUse/1024/1024);


            System.out.println("readStringMapFromFile: 5");
            XSSFSheet sheet0 = wb.getSheetAt(0);//sheet0
            System.out.println( "readStringMapFromFile: 6");

            XSSFRow row = sheet0.getRow(1);//第2行
            System.out.println("readStringMapFromFile: 7");

            int rowIndex = -1;
            for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                System.out.println( "readStringMapFromFile: i = " + i);
                XSSFCell cell = row.getCell(i);
                if (key.equals(cell.toString())) {
                    rowIndex = cell.getColumnIndex();
                    break;
                }
            }
            if (rowIndex == -1) {
                return null;
            }
            //https://blog.csdn.net/weixin_43845227/article/details/123580523  poi颜色
            //https://blog.csdn.net/hadues/article/details/113859228  poi使用文档
            System.out.println("readStringMapFromFile: index " + rowIndex);
            for (int i = 0; i < sheet0.getPhysicalNumberOfRows(); i++) {
                XSSFRow rowi = sheet0.getRow(i);
                XSSFCell cellKey = rowi.getCell(0);
                XSSFCell cellValue = rowi.getCell(rowIndex);
                if (cellKey == null || cellValue == null) {
                    System.out.println( "readStringMapFromFile: cellKey == null || cellValue == null");
                    continue;
                }
                XSSFCellStyle cellStyle = cellValue.getCellStyle();
                XSSFColor color = cellStyle.getFillForegroundXSSFColor();
                if (color != null &&  (int) color.getRgb()[0] == HAS) {
                    map.put(cellKey.toString(),cellValue.toString());
                } else {
                    map.put(cellKey.toString(), "");
                }
            }
//            pkg.close();
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
        return map;
    }

}

