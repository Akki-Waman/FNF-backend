package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.SlaRuleDetailsDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class SlaRuleDetailsExcelGenerator {

    public static byte[] generateExcel(List<SlaRuleDetailsDto> list) throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SLA Rule Details");

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
                "Sr No",
                "SLA Rule Detail ID",
                "SLA Profile ID",
                "Service ID",
                "Severity ID",
                "SLA Type ID",
                "SLA Hours",
                "Grace Hours",
                "Penalty %",
                "Max Penalty %",
                "Action On Exceed"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.LEFT);
        setBorders(dataStyle);

        int srNo = 1;

        for (SlaRuleDetailsDto dto : list) {

            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(srNo++);
            row.createCell(1).setCellValue(dto.getSlaRuleDetailId());

            if (dto.getSlaProfile() != null) {
                row.createCell(2).setCellValue(
                        dto.getSlaProfile().getSlaProfileId());
            }

            if (dto.getService() != null) {
                row.createCell(3).setCellValue(
                        dto.getService().getServiceId());
            }

            if (dto.getSeverityId() != null)
                row.createCell(4).setCellValue(dto.getSeverityId());

            if (dto.getSlaTypeId() != null)
                row.createCell(5).setCellValue(dto.getSlaTypeId());

            if (dto.getSlaHours() != null)
                row.createCell(6).setCellValue(dto.getSlaHours());

            if (dto.getGraceHours() != null)
                row.createCell(7).setCellValue(dto.getGraceHours());

            if (dto.getPenaltyPercent() != null)
                row.createCell(8).setCellValue(dto.getPenaltyPercent());

            if (dto.getMaxPenaltyPercent() != null)
                row.createCell(9).setCellValue(dto.getMaxPenaltyPercent());

            if (dto.getActionOnExceed() != null)
                row.createCell(10).setCellValue(dto.getActionOnExceed());


            for (int i = 0; i <= 10; i++) {
                if (row.getCell(i) != null) {
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
