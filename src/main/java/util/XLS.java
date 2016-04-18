package util;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileOutputStream;
/**
 * Created by miroslav.kudlac on 4/18/2016.
 */
public class XLS {
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private String file;

    public XLS(String name, int rows, int cells) {
        this.file = name;
        try {
            workbook = new HSSFWorkbook();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        sheet = workbook.createSheet("FirstSheet");
        for (int i = 0; i < rows; i++) {
            Row row = sheet.createRow(i);
            for (int a = 0; a < cells; a++) {
                row.createCell(a).setCellValue(0);
            }
        }
    }

    public void setCell(int x, int fitness, Object value) {
        HSSFRow rowhead = sheet.createRow((short)x);
        rowhead.createCell(0).setCellValue(x);
        rowhead.createCell(1).setCellValue(fitness);
        rowhead.createCell(2).setCellValue(value.toString());
    }

    public void write() {
        try {
            FileOutputStream outFile = new FileOutputStream(file);
            workbook.write(outFile);
            outFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

