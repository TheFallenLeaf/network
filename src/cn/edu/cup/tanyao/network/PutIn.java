package cn.edu.cup.tanyao.network;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 基础参数输入类
 *
 * @author LiYu
 * @version 2017年10月29日
 */
public class PutIn {

    /**
     * 构造函数
     */
    private PutIn(){
    }

    /**
     * 查找excel表中某一单元格中元素
     *
     * @param rowNumber
     *          行序号
     * @param columnNumber
     *          列序号
     * @param sheetNumber
     *          工作表编号
     * @return  单元格中元素
     */
    public static double getElement(int rowNumber, int columnNumber, int sheetNumber){
        double value = 0;
        try {
            //导入工作簿
            InputStream is = new FileInputStream("Network.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheetAt(sheetNumber-1);

            //寻找元素
            sheet.getRow(rowNumber - 1).getCell(columnNumber - 1).setCellType(XSSFCell.CELL_TYPE_NUMERIC);
            value = sheet.getRow(rowNumber - 1).getCell(columnNumber - 1).getNumericCellValue();

            is.close();//关闭工作簿

        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 计算某工作表的行数
     *
     * @param sheetNumber
     *          工作表序号
     * @return 工作表行数
     */
    public static int getRowCount(int sheetNumber){
        int rowCount = 0;
        try {
            InputStream is = new FileInputStream("Network.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheetAt(sheetNumber-1);

            rowCount = sheet.getLastRowNum()+1;

            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rowCount;
    }

    /**
     * 计算某工作表的列数
     *
     * @param sheetNumber
     *          工作表序号
     * @return  工作表列数
     */
    public static int getColumnCount(int sheetNumber){
        int columnCount = 0;
        try {
            InputStream is = new FileInputStream("Network.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheetAt(sheetNumber-1);

            columnCount = sheet.getRow(0).getLastCellNum();

            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return columnCount;
    }

    public static void main(String[] args){
        System.out.println(PutIn.getElement(19, 1, 2));
    }

}

