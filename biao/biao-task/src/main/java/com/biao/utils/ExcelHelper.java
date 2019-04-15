package com.biao.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class ExcelHelper {

    private static final Logger logger = LoggerFactory.getLogger(ExcelHelper.class);

    private static List<String> NULL_VALUE = Arrays.asList(new String[]{"null", "NULL", "\\N"});

    public static List<Map<String, Object>> strartParseFile(String filePath, String fileName) {
        int index = fileName.indexOf(".");
        if (index != -1) {
            String suffix = fileName.substring(index + 1);
            if (suffix.equalsIgnoreCase("txt")) {
                return parseTxtFile(filePath, fileName);
            } else if (suffix.equalsIgnoreCase("xlsx")) {
                return parseExcelFile(filePath, fileName);
            } else {
                logger.error("不支持解析的文件后缀,suffix = {}", suffix);
            }
        } else {
            logger.error("请命名正确的文件名,xxx.xx");
        }
        return null;
    }

    private static List<Map<String, Object>> parseTxtFile(String filePath, String fileName) {
        List<Map<String, Object>> datas = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = readBufferedReader(filePath, fileName);
            String str = null;
            int first = 0;
            while ((str = reader.readLine()) != null) {
                // 第一行 标头
                if (first == 0) {
                    first++;
                    continue;
                }
                Map<String, Object> map = parseData(str);
                if (map != null) {
                    datas.add(map);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return datas;
    }

    private static Map<String, Object> parseData(String str) {
        logger.debug("read data :【{}】", str.trim());
        Map<String, Object> data = new HashMap<>();
        String[] datas = str.split("\\s+");
        if (datas.length < 4 || datas.length > 7) {
            logger.error("read data,格式不正确....");
            return null;
        }
        data.put("id", filterNull(datas[0]));
        data.put("parent_id", filterNull(datas[1]));
        data.put("mobile", filterNull(datas[2]));
        if (datas.length == 4) {
            // 时间格式 yyyy-MM-dd
            data.put("create_date", filterNull(datas[3]));
        }
        if (datas.length == 5) {
            // 时间格式 yyyy-MM-dd
            data.put("create_date", filterNull(datas[3]));
            data.put("source", filterNull(datas[4]));
        }
        if (datas.length == 6) {
            // 时间格式 yyyy-MM-dd HH:mm:ss
            data.put("create_date", filterNull(datas[3]) + " " + filterNull(datas[4]));
            data.put("source", filterNull(datas[5]));
        }
        return data;
    }

    private static String filterNull(String data) {
        if (StringUtils.isBlank(data.toString()) || NULL_VALUE.contains(data)) {
            return null;
        }
        return data;
    }

    private static Map<String, Object> parseData(Row row) {
        Map<String, Object> data = new HashMap<>();
        short minColNum = row.getFirstCellNum();
        short maxColNum = row.getLastCellNum();
        for (short colIndex = minColNum; colIndex < maxColNum; colIndex++) {
            Cell cell = row.getCell(colIndex);
            if (cell == null) {
                continue;
            }
            cell.setCellType(Cell.CELL_TYPE_STRING);
            Object value = cell.getStringCellValue();
            if (value == null || StringUtils.isBlank(value.toString()) || NULL_VALUE.contains(value.toString())) {
                value = null;
            }
            if (colIndex == 0) {
                data.put("id", value);
            } else if (colIndex == 1) {
                data.put("parent_id", value);
            } else if (colIndex == 2) {
                data.put("mobile", value);
            } else if (colIndex == 3) {
                data.put("create_date", value);
            } else if (colIndex == 4) {
                data.put("source", value);
            }
        }
        return data;
    }

    private static List<Map<String, Object>> parseExcelFile(String filePath, String fileName) {
        InputStream reader = null;
        List<Map<String, Object>> datas = new ArrayList<>();
        try {
            reader = readInputStream(filePath, fileName);
            Workbook workbook = WorkbookFactory.create(reader);
            if (workbook != null && workbook instanceof SXSSFWorkbook) {
                SXSSFWorkbook sxssWorkbook = (SXSSFWorkbook) workbook;
                Sheet sheet = sxssWorkbook.getSheetAt(0);
                int last = sheet.getLastRowNum();
                int first = sheet.getFirstRowNum();
                // 第一行 标头
                for (int i = first + 1; i <= last; i++) {
                    Row row = sheet.getRow(i);
                    Map<String, Object> map = parseData(row);
                    if (map != null) {
                        datas.add(map);
                    }
                }
            } else if (workbook != null && workbook instanceof XSSFWorkbook) {
                XSSFWorkbook xssWorkbook = (XSSFWorkbook) workbook;
                Sheet sheet = xssWorkbook.getSheetAt(0);
                int last = sheet.getLastRowNum();
                int first = sheet.getFirstRowNum();
                // 第一行 标头
                for (int i = first + 1; i <= last; i++) {
                    Row row = sheet.getRow(i);
                    Map<String, Object> map = parseData(row);
                    if (map != null) {
                        datas.add(map);
                    }
                }
            } else {
                logger.error("只支持2007的excel");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return datas;
    }

    private static InputStream readInputStream(String filePath, String fileName) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File(filePath, fileName));
        return inputStream;
    }

    private static BufferedReader readBufferedReader(String filePath, String fileName) throws FileNotFoundException {
        BufferedReader inputStream = new BufferedReader(new FileReader(new File(filePath, fileName)));
        return inputStream;
    }

    public static void main(String[] args) {
        String filePath = "E:\\syn";
        //String fileName = "20180806.txt" ;
        String fileName = "20180806.xlsx";
        List<Map<String, Object>> maps = strartParseFile(filePath, fileName);
        System.err.println(maps);
    }
}
