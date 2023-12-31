package com.zhangdi.utils.excel;


import android.content.Context;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtil {

    private static WritableFont arial14font = null;
    private static WritableCellFormat arial14format = null;
    private static WritableFont arial10font = null;
    private static WritableCellFormat arial10format = null;
    private static WritableFont arial12font = null;
    private static WritableCellFormat arial12format = null;
    private final static String UTF8_ENCODING = "UTF-8";


    /**
     * 单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
     */
    private static void format() {
        try {
            arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            arial14font.setColour(Colour.LIGHT_BLUE);
            arial14format = new WritableCellFormat(arial14font);
            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
            arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial14format.setBackground(Colour.VERY_LIGHT_YELLOW);

            arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial10format.setBackground(Colour.GRAY_25);

            arial12font = new WritableFont(WritableFont.ARIAL, 10);
            arial12format = new WritableCellFormat(arial12font);
            //对齐格式
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            //设置边框
            arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化Excel表格
     *
     * @param filePath  存放excel文件的路径（path/demo.xls）
     * @param sheetName Excel表格的表名
     * @param colName   excel中包含的列名（可以有多个）
     */
    public static void initExcel(String filePath, String sheetName, String[] colName) {
        format();
        WritableWorkbook workbook = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                return;
            }
            workbook = Workbook.createWorkbook(file);
            //设置表格的名字
            WritableSheet sheet = workbook.createSheet(sheetName, 0);
            //创建标题栏
            sheet.addCell((WritableCell) new Label(0, 0, filePath, arial14format));
            for (int col = 0; col < colName.length; col++) {
                sheet.addCell(new Label(col, 0, colName[col], arial10format));
            }
            //设置行高
            sheet.setRowView(0, 340);
            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将制定类型的List写入Excel中
     *
     * @param objList  待写入的list
     * @param fileName
     * @param c
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> void writeObjListToExcel(List<T> objList, String fileName, Context c) {
        if (objList != null && objList.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);

                in = new FileInputStream(new File(fileName));
                Workbook workbook = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(new File(fileName), workbook);
                WritableSheet sheet = writebook.getSheet(0);

                sheet.addCell(new Label(0, 0,"编号",arial12format));
                sheet.addCell(new Label(1, 0,"测试项",arial12format));
                sheet.addCell(new Label(2, 0,"测试结果",arial12format));
                sheet.addCell(new Label(3, 0,"测试时间",arial12format));
                sheet.setRowView(0, 350);

                for (int j = 0; j < objList.size(); j++) {
                    TableData tableDataBean = (TableData) objList.get(j);
                    List<String> list = new ArrayList<>();
                    list.add(tableDataBean.getId()+"");
                    list.add(tableDataBean.getName());
                    list.add(tableDataBean.getResult());
                    list.add(tableDataBean.getTime());
                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(i, j+1, list.get(i), arial12format));
                        if (list.get(i).length() <= 4) {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 8);
                        } else {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 5);
                        }
                    }
                    //设置行高
                    sheet.setRowView(j+1, 350);
                }


                writebook.write();
                workbook.close();
                Toast.makeText(c, "导出Excel成功: "+fileName, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }




    /**
     * 将制定类型的List写入Excel中
     * @param c
     * @param fileName 文件名
     * @param objList 文件内容
     *                  1 1 1 1
     *                  1 1 1 1
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> void writeObjListToExcel(Context c, String fileName,List<List<String>> objList) {
        if (objList == null || objList.size() == 0) {
            return;
        }
        WritableWorkbook writebook = null;
        InputStream in = null;
        try {
            WorkbookSettings setEncode = new WorkbookSettings();
            setEncode.setEncoding(UTF8_ENCODING);

            in = new FileInputStream(new File(fileName));
            Workbook workbook = Workbook.getWorkbook(in);
            writebook = Workbook.createWorkbook(new File(fileName), workbook);
            WritableSheet sheet = writebook.getSheet(0);
            for (int j = 0; j < objList.size(); j++) {
                List<String> tableDataBean = (List<String>) objList.get(j);
                for (int i = 0; i < tableDataBean.size(); i++) {
                    sheet.addCell(new Label(i, j, tableDataBean.get(i), arial12format));
                    if (tableDataBean.get(i).length() <= 4) {
                        //设置列宽
                        sheet.setColumnView(i, tableDataBean.get(i).length() + 8);
                    } else {
                        //设置列宽
                        sheet.setColumnView(i, tableDataBean.get(i).length() + 5);
                    }
                }
                //设置行高
                sheet.setRowView(j+1, 350);
            }


            writebook.write();
            workbook.close();
            Toast.makeText(c, "导出Excel成功: "+fileName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writebook != null) {
                try {
                    writebook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


}