package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.CountryResponseDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class CountryExcelGenerator {

    public static void generateExcel(List<CountryResponseDto> countries,
                                     HttpServletResponse response) throws Exception {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
                "Content-Disposition", "attachment; filename=countries.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Countries");

        int rowIndex = 0;

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
        rowIndex++;

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        setBorders(headerStyle);

        Row headerRow = sheet.createRow(rowIndex++);

        String[] headers = {
                "Country ID",
                "Country Name",
                "Tax Type",
                "Is Foreign",
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
                workbook.createDataFormat()
                        .getFormat("dd-MM-yyyy HH:mm:ss"));
        setBorders(dateStyle);

        for (CountryResponseDto dto : countries) {

            Row row = sheet.createRow(rowIndex++);

            Cell c0 = row.createCell(0);
            c0.setCellValue(dto.getCountryId());
            c0.setCellStyle(dataStyle);

            Cell c1 = row.createCell(1);
            c1.setCellValue(dto.getCountryName());
            c1.setCellStyle(dataStyle);

            Cell c2 = row.createCell(2);
            c2.setCellValue(dto.getTaxType());
            c2.setCellStyle(dataStyle);

            Cell c3 = row.createCell(3);
            c3.setCellValue(Boolean.TRUE.equals(dto.getIsForeign()) ? "Yes" : "No");
            c3.setCellStyle(dataStyle);

            Cell c4 = row.createCell(4);
            c4.setCellValue(dto.getCreatedBy());
            c4.setCellStyle(dataStyle);

            Cell c5 = row.createCell(5);
            c5.setCellValue(dto.getModifiedBy());
            c5.setCellStyle(dataStyle);

            Cell c6 = row.createCell(6);
            if (dto.getCreatedTime() != null) {
                c6.setCellValue(dto.getCreatedTime());
                c6.setCellStyle(dateStyle);
            } else {
                c6.setCellStyle(dataStyle);
            }

            Cell c7 = row.createCell(7);
            if (dto.getModifiedTime() != null) {
                c7.setCellValue(dto.getModifiedTime());
                c7.setCellStyle(dateStyle);
            } else {
                c7.setCellStyle(dataStyle);
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
