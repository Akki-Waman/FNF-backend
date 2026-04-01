package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.ShiftResponseDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShiftExcelGenerator {

    public static void generateExcel(List<ShiftResponseDTO> shifts,
                                     HttpServletResponse response) throws Exception {

        // Set response headers
        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
                "Content-Disposition", "attachment; filename=shifts.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Shifts");

        int rowIndex = 0;


        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        rowIndex++;


        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        setBorders(headerStyle);

        // Header row
        Row headerRow = sheet.createRow(rowIndex++);

        String[] headers = {
                "Shift ID",
                "Shift Name",
                "Created By",
                "Modified By",
                "Created Time",
                "Modified Time"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }


        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.LEFT);
        setBorders(dataStyle);

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setAlignment(HorizontalAlignment.LEFT);
        dateStyle.setDataFormat(workbook.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));
        setBorders(dateStyle);


        for (ShiftResponseDTO dto : shifts) {
            Row row = sheet.createRow(rowIndex++);

            Cell c0 = row.createCell(0);
            c0.setCellValue(dto.getShiftId() != null ? dto.getShiftId() : 0);
            c0.setCellStyle(dataStyle);

            Cell c1 = row.createCell(1);
            c1.setCellValue(dto.getShiftName() != null ? dto.getShiftName() : "");
            c1.setCellStyle(dataStyle);

            Cell c2 = row.createCell(2);
            c2.setCellValue(dto.getCreatedBy() != null ? dto.getCreatedBy() : "");
            c2.setCellStyle(dataStyle);

            Cell c3 = row.createCell(3);
            c3.setCellValue(dto.getModifiedBy() != null ? dto.getModifiedBy() : "");
            c3.setCellStyle(dataStyle);

            Cell c4 = row.createCell(4);
            if (dto.getCreatedTime() != null) {
                c4.setCellValue(dto.getCreatedTime());
                c4.setCellStyle(dateStyle);
            } else {
                c4.setCellStyle(dataStyle);
            }

            Cell c5 = row.createCell(5);
            if (dto.getModifiedTime() != null) {
                c5.setCellValue(dto.getModifiedTime());
                c5.setCellStyle(dateStyle);
            } else {
                c5.setCellStyle(dataStyle);
            }
        }


        sheet.setAutoFilter(new CellRangeAddress(
                headerRow.getRowNum(),
                headerRow.getRowNum(),
                0,
                headers.length - 1));


        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }


        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private static void setBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }
}
