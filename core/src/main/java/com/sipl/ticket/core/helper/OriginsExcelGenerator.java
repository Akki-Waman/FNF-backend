package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.OriginDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class OriginsExcelGenerator {

    public static void generateExcel(List<OriginDto> origins,
                                     HttpServletResponse response) throws Exception {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
                "Content-Disposition", "attachment; filename=origins.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Origins");

        int rowIndex = 0;

        /* ---------- Header ---------- */
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        setBorders(headerStyle);

        Row headerRow = sheet.createRow(rowIndex++);
        String[] headers = {
                "Origin ID",
                "Origin Name",
                "Status",
                "Created By",
                "Created Time",
                "Modified By",
                "Modified Time"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        /* ---------- Data ---------- */
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.LEFT);
        setBorders(dataStyle);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        for (OriginDto o : origins) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(
                    o.getOriginId() != null ? o.getOriginId() : 0);

            row.createCell(1).setCellValue(
                    o.getOriginName() != null ? o.getOriginName() : "");

            row.createCell(2).setCellValue(
                    Boolean.TRUE.equals(o.getIsActive()) ? "Active" : "Inactive");

            row.createCell(3).setCellValue(
                    o.getCreatedBy() != null ? o.getCreatedBy() : "");

            row.createCell(4).setCellValue(
                    o.getCreatedTime() != null
                            ? o.getCreatedTime().format(formatter)
                            : "");

            row.createCell(5).setCellValue(
                    o.getModifiedBy() != null ? o.getModifiedBy() : "");

            row.createCell(6).setCellValue(
                    o.getModifiedTime() != null
                            ? o.getModifiedTime().format(formatter)
                            : "");

            for (int i = 0; i < headers.length; i++) {
                row.getCell(i).setCellStyle(dataStyle);
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
