package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.BranchDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class BranchExcelGenerator {

    public static void generateExcel(List<BranchDto> branches,
                                     HttpServletResponse response) throws Exception {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=branches.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Branches");

        int rowIndex = 0;

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11));
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
                "Branch ID",
                "Branch Name",
                "Email",
                "Address",
                "Company",
                "Country",
                "State",
                "City",
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

        for (BranchDto dto : branches) {

            Row row = sheet.createRow(rowIndex++);

            Cell c0 = row.createCell(0);
            c0.setCellValue(dto.getBranchId());
            c0.setCellStyle(dataStyle);

            Cell c1 = row.createCell(1);
            c1.setCellValue(dto.getBranchName());
            c1.setCellStyle(dataStyle);

            Cell c2 = row.createCell(2);
            c2.setCellValue(dto.getEmail());
            c2.setCellStyle(dataStyle);

            Cell c3 = row.createCell(3);
            c3.setCellValue(dto.getAddress());
            c3.setCellStyle(dataStyle);

            Cell c4 = row.createCell(4);
            c4.setCellValue(
                    dto.getCompany() != null ? dto.getCompany().getCompanyName() : "");
            c4.setCellStyle(dataStyle);

            Cell c5 = row.createCell(5);
            c5.setCellValue(
                    dto.getCountry() != null ? dto.getCountry().getCountryName() : "");
            c5.setCellStyle(dataStyle);

            Cell c6 = row.createCell(6);
            c6.setCellValue(
                    dto.getState() != null ? dto.getState().getStateName() : "");
            c6.setCellStyle(dataStyle);

            Cell c7 = row.createCell(7);
            c7.setCellValue(
                    dto.getCity() != null ? dto.getCity().getCityName() : "");
            c7.setCellStyle(dataStyle);

            Cell c8 = row.createCell(8);
            c8.setCellValue(dto.getCreatedBy() != null ? dto.getCreatedBy() : "");
            c8.setCellStyle(dataStyle);

            Cell c9 = row.createCell(9);
            c9.setCellValue(dto.getModifiedBy() != null ? dto.getModifiedBy() : "");
            c9.setCellStyle(dataStyle);


            Cell c10 = row.createCell(10);
            if (dto.getCreatedTime() != null) {
                c10.setCellValue(dto.getCreatedTime());
                c10.setCellStyle(dateStyle);
            } else {
                c10.setCellStyle(dataStyle);
            }

            Cell c11 = row.createCell(11);
            if (dto.getModifiedTime() != null) {
                c11.setCellValue(dto.getModifiedTime());
                c11.setCellStyle(dateStyle);
            } else {
                c11.setCellStyle(dataStyle);
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
