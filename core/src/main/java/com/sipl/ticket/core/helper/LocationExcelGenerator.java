package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.LocationResponseDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class LocationExcelGenerator {

    public static void generateExcel(
            List<LocationResponseDTO> locations,
            HttpServletResponse response
    ) throws Exception {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
                "Content-Disposition", "attachment; filename=locations.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Locations");

        int rowIndex = 0;

        // ---------- Title Row (merged like Brand) ----------
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
        rowIndex++;

        // ---------- Header Style ----------
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        setBorders(headerStyle);

        // ---------- Header Row ----------
        Row headerRow = sheet.createRow(rowIndex++);

        String[] headers = {
                "Location ID",
                "Location Name",
                "Status",
                "Created Time",
                "Modified Time"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // ---------- Data Styles ----------
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.LEFT);
        setBorders(dataStyle);

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setAlignment(HorizontalAlignment.LEFT);
        dateStyle.setDataFormat(
                workbook.createDataFormat()
                        .getFormat("dd-MM-yyyy HH:mm:ss"));
        setBorders(dateStyle);

        // ---------- Data Rows ----------
        for (LocationResponseDTO dto : locations) {

            Row row = sheet.createRow(rowIndex++);

            Cell c0 = row.createCell(0);
            c0.setCellValue(dto.getLocationId());
            c0.setCellStyle(dataStyle);

            Cell c1 = row.createCell(1);
            c1.setCellValue(dto.getLocationName());
            c1.setCellStyle(dataStyle);

            Cell c2 = row.createCell(2);
            c2.setCellValue(
                    Boolean.TRUE.equals(dto.getIsActive())
                            ? "Active"
                            : "Inactive"
            );
            c2.setCellStyle(dataStyle);

            Cell c3 = row.createCell(3);
            if (dto.getCreatedTime() != null) {
                c3.setCellValue(dto.getCreatedTime());
                c3.setCellStyle(dateStyle);
            } else {
                c3.setCellStyle(dataStyle);
            }

            Cell c4 = row.createCell(4);
            if (dto.getModifiedTime() != null) {
                c4.setCellValue(dto.getModifiedTime());
                c4.setCellStyle(dateStyle);
            } else {
                c4.setCellStyle(dataStyle);
            }
        }

        // ---------- Auto Filter ----------
        sheet.setAutoFilter(new CellRangeAddress(
                headerRow.getRowNum(),
                headerRow.getRowNum(),
                0,
                headers.length - 1));

        // ---------- Auto Size ----------
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
