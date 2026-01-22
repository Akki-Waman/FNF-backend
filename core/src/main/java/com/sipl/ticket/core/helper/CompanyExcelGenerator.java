package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.CompanyDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class CompanyExcelGenerator {

    public static void generateExcel(List<CompanyDto> companies,
                                     HttpServletResponse response) throws Exception {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=companies.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Companies");

        int rowIndex = 0;

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        setBorders(headerStyle);

        Row headerRow = sheet.createRow(rowIndex++);

        String[] headers = {
                "Company ID",
                "Company Name",
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
        dateStyle.setDataFormat(
                workbook.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));
        setBorders(dateStyle);

        for (CompanyDto c : companies) {

            Row row = sheet.createRow(rowIndex++);

            Cell c0 = row.createCell(0);
            c0.setCellValue(c.getCompanyId() != null ? c.getCompanyId() : 0);
            c0.setCellStyle(dataStyle);

            Cell c1 = row.createCell(1);
            c1.setCellValue(c.getCompanyName() != null ? c.getCompanyName() : "");
            c1.setCellStyle(dataStyle);

            Cell c2 = row.createCell(2);
            c2.setCellValue(c.getCreatedBy() != null ? c.getCreatedBy() : "");
            c2.setCellStyle(dataStyle);

            Cell c3 = row.createCell(3);
            c3.setCellValue(c.getModifiedBy() != null ? c.getModifiedBy() : "");
            c3.setCellStyle(dataStyle);

            Cell c4 = row.createCell(4);
            if (c.getCreatedTime() != null) {
                c4.setCellValue(c.getCreatedTime());
                c4.setCellStyle(dateStyle);
            } else {
                c4.setCellStyle(dataStyle);
            }

            Cell c5 = row.createCell(5);
            if (c.getModifiedTime() != null) {
                c5.setCellValue(c.getModifiedTime());
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
