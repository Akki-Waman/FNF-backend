package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.WorkflowStepsDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class WorkflowStepsExcelGenerator {

    public static void generateExcel(
            List<WorkflowStepsDTO> steps,
            HttpServletResponse response) throws Exception {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=workflow_steps.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Workflow Steps");

        int rowIndex = 0;

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
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
                "Workflow Step ID",
                "Workflow Definition ID",
                "Step Order",
                "Step Name",
                "Role ID",
                "Final Approver"
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

        for (WorkflowStepsDTO dto : steps) {

            Row row = sheet.createRow(rowIndex++);

            Cell c0 = row.createCell(0);
            c0.setCellValue(srNo++);
            c0.setCellStyle(dataStyle);

            Cell c1 = row.createCell(1);
            c1.setCellValue(dto.getWorkFlowStepsId());
            c1.setCellStyle(dataStyle);

            Cell c2 = row.createCell(2);
            if (dto.getWorkflowDefinition() != null) {
                c2.setCellValue(
                        dto.getWorkflowDefinition().getWorkFlowDefinitionId()
                );
            }
            c2.setCellStyle(dataStyle);

            Cell c3 = row.createCell(3);
            c3.setCellValue(dto.getStepOrder());
            c3.setCellStyle(dataStyle);

            Cell c4 = row.createCell(4);
            c4.setCellValue(dto.getStepName());
            c4.setCellStyle(dataStyle);

            Cell c5 = row.createCell(5);

            if (dto.getRoleId() != null) {
                c5.setCellValue(dto.getRoleId());
            } else {
                c5.setCellValue("");
            }

            Cell c6 = row.createCell(6);
            c6.setCellValue(dto.getIsFinalApprover());
            c6.setCellStyle(dataStyle);
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
