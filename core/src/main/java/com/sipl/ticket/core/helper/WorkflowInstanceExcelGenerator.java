package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.WorkflowInstanceDTO;
import com.sipl.ticket.core.enums.WorkFlowStatusEnum;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class WorkflowInstanceExcelGenerator {

    public static void generateExcel(
            List<WorkflowInstanceDTO> list,
            HttpServletResponse response) throws Exception {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=workflow_instance.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Workflow Instance");

        int rowIndex = 0;

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
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
                "Instance ID",
                "Workflow ID",
                "Entity ID",
                "Entity Type",
                "Current Step",
                "Status",
                "Started At",
                "Completed At",
                "Assigned User"
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

        for (WorkflowInstanceDTO dto : list) {

            Row row = sheet.createRow(rowIndex++);

            // Always create all cells first (IMPORTANT)
            for (int i = 0; i <= 9; i++) {
                row.createCell(i);
            }

            row.getCell(0).setCellValue(srNo++);

            if (dto.getWorkflowInstanceId() != null)
                row.getCell(1).setCellValue(dto.getWorkflowInstanceId());

            if (dto.getWorkflow() != null)
                row.getCell(2).setCellValue(dto.getWorkflow().getWorkFlowDefinitionId());
            else
                row.getCell(2).setCellValue("");

            if (dto.getEntityId() != null)
                row.getCell(3).setCellValue(dto.getEntityId());
            else
                row.getCell(3).setCellValue(0);

            row.getCell(4).setCellValue(dto.getEntityType() != null ? dto.getEntityType() : "");

            if (dto.getCurrentStep() != null && dto.getCurrentStep().getStepName() != null)
                row.getCell(5).setCellValue(dto.getCurrentStep().getStepName());
            else
                row.getCell(5).setCellValue("");

            if (dto.getWorkFlowStatus() != null) {
                String statusName = WorkFlowStatusEnum
                        .fromCode(dto.getWorkFlowStatus())
                        .name()
                        .replace("_", " ");

                row.getCell(6).setCellValue(statusName);
            } else {
                row.getCell(6).setCellValue("");
            }



            if (dto.getStartedAt() != null)
                row.getCell(7).setCellValue(dto.getStartedAt().toString());
            else
                row.getCell(7).setCellValue("");

            if (dto.getCompletedAt() != null)
                row.getCell(8).setCellValue(dto.getCompletedAt().toString());
            else
                row.getCell(8).setCellValue("");

            if (dto.getAssignedUser() != null && dto.getAssignedUser().getUserName() != null)
                row.getCell(9).setCellValue(dto.getAssignedUser().getUserName());
            else
                row.getCell(9).setCellValue("");

            // Apply style safely
            for (int i = 0; i <= 9; i++) {
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
