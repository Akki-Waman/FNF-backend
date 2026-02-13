package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.SlaProfileResponseDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class SlaProfileExcelGenerator {

    public static byte[] generateExcel(List<SlaProfileResponseDto> profiles) throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SLA Profiles");

        int rowIndex = 0;

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
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
                "SLA Profile ID",
                "Profile Name",
                "Branch",
                "Effective From",
                "Effective To",
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
                workbook.createDataFormat().getFormat("dd-MM-yyyy"));
        setBorders(dateStyle);

        CellStyle dateTimeStyle = workbook.createCellStyle();
        dateTimeStyle.setAlignment(HorizontalAlignment.LEFT);
        dateTimeStyle.setDataFormat(
                workbook.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));
        setBorders(dateTimeStyle);

        for (SlaProfileResponseDto dto : profiles) {

            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(dto.getSlaProfileId());
            row.createCell(1).setCellValue(dto.getProfileName());
            row.createCell(2).setCellValue(
                    dto.getBranch() != null ? dto.getBranch().getBranchName() : "");

            Cell c3 = row.createCell(3);
            if (dto.getEffectiveFrom() != null) {
                c3.setCellValue(dto.getEffectiveFrom());
                c3.setCellStyle(dateStyle);
            } else {
                c3.setCellStyle(dataStyle);
            }

            Cell c4 = row.createCell(4);
            if (dto.getEffectiveTo() != null) {
                c4.setCellValue(dto.getEffectiveTo());
                c4.setCellStyle(dateStyle);
            } else {
                c4.setCellStyle(dataStyle);
            }

            Cell c5 = row.createCell(5);
            c5.setCellValue(dto.getCreatedBy());
            c5.setCellStyle(dataStyle);

            Cell c6 = row.createCell(6);
            c6.setCellValue(dto.getModifiedBy());
            c6.setCellStyle(dataStyle);

            Cell c7 = row.createCell(7);
            if (dto.getCreatedTime() != null) {
                c7.setCellValue(dto.getCreatedTime());
                c7.setCellStyle(dateTimeStyle);
            } else {
                c7.setCellStyle(dataStyle);
            }

            Cell c8 = row.createCell(8);
            if (dto.getModifiedTime() != null) {
                c8.setCellValue(dto.getModifiedTime());
                c8.setCellStyle(dateTimeStyle);
            } else {
                c8.setCellStyle(dataStyle);
            }

            for (int i = 0; i <= 8; i++) {
                if (row.getCell(i).getCellStyle() == null) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
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

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }

    private static void setBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }
}
