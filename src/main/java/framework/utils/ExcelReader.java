package framework.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReader {

    /** Đọc toàn bộ data từ một sheet cụ thể */
    public static Object[][] getData(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Không tìm thấy sheet: " + sheetName);
            }

            int lastRow = sheet.getLastRowNum();
            int lastCol = sheet.getRow(0).getLastCellNum();
            
            // Khởi tạo mảng 2 chiều chứa data (bỏ dòng 0 là Header)
            Object[][] data = new Object[lastRow][lastCol];

            for (int r = 1; r <= lastRow; r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                for (int c = 0; c < lastCol; c++) {
                    Cell cell = row.getCell(c);
                    data[r - 1][c] = getCellValue(cell);
                }
            }
            return data;

        } catch (IOException e) {
            throw new RuntimeException("Lỗi đọc Excel: " + filePath, e);
        }
    }

    /** Xử lý từng kiểu dữ liệu trong cell an toàn */
    private static String getCellValue(Cell cell) {
        if (cell == null) return ""; // Cell rỗng -> chuỗi rỗng
        
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCachedFormulaResultType() == CellType.NUMERIC
                    ? String.valueOf((long) cell.getNumericCellValue())
                    : cell.getStringCellValue();
            default -> "";
        };
    }
}