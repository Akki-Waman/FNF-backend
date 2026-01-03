package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.StateResponseDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class StateExcelGenerator {

    public static void generateExcel(List<StateResponseDto> states,
                                     HttpServletResponse response) throws Exception {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
                "Content-Disposition", "attachment; filename=states.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("States");

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
                "State ID",
                "State Name",
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

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.LEFT);
        setBorders(dataStyle);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        for (StateResponseDto s : states) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(
                    s.getStateId() != null ? s.getStateId() : 0);

            row.createCell(1).setCellValue(
                    s.getStateName() != null ? s.getStateName() : "");

            row.createCell(2).setCellValue(
                    Boolean.TRUE.equals(s.getIsActive()) ? "Active" : "Inactive");

            row.createCell(3).setCellValue(
                    s.getCreatedBy() != null ? s.getCreatedBy() : "");

            row.createCell(4).setCellValue(
                    s.getCreatedTime() != null
                            ? s.getCreatedTime().format(formatter)
                            : "");

            row.createCell(5).setCellValue(
                    s.getModifiedBy() != null ? s.getModifiedBy() : "");

            row.createCell(6).setCellValue(
                    s.getModifiedTime() != null
                            ? s.getModifiedTime().format(formatter)
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

