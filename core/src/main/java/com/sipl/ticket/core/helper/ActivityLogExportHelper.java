package com.sipl.ticket.core.helper;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.sipl.ticket.core.dto.response.ActivityLogReportResponseDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ActivityLogExportHelper {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static void export(List<ActivityLogReportResponseDto> logs,
                              String format,
                              HttpServletResponse response) throws Exception {
        switch (format.toLowerCase()) {
            case "excel":
                generateExcel(logs, response);
                break;
            case "csv":
                generateCsv(logs, response);
                break;
            case "pdf":
                generatePdf(logs, response);
                break;
            default:
                throw new IllegalArgumentException("Invalid export format");
        }
    }

    private static void generateExcel(List<ActivityLogReportResponseDto> logs,
                                      HttpServletResponse response) throws Exception {
        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
                "Content-Disposition", "attachment; filename=activity_logs.xlsx");
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Activity Logs");
        int rowIndex = 0;
        String[] headers = {
                "Log ID", "Description", "Staff Name",
                "Performed By", "IP Address", "Created Time"
        };
        Row headerRow = sheet.createRow(rowIndex++);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
        for (ActivityLogReportResponseDto log : logs) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(log.getActivityLogId());
            row.createCell(1).setCellValue(safe(log.getDescription()));
            row.createCell(2).setCellValue(safe(log.getStaffName()));
            row.createCell(3).setCellValue(safe(log.getPerformedBy()));
            row.createCell(4).setCellValue(safe(log.getIpAddress()));
            row.createCell(5).setCellValue(formatDate(log));
        }
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private static void generateCsv(List<ActivityLogReportResponseDto> logs,
                                    HttpServletResponse response) throws Exception {
        response.setContentType("text/csv");
        response.setHeader(
                "Content-Disposition", "attachment; filename=activity_logs.csv");
        PrintWriter writer = response.getWriter();
        writer.println("Log ID,Description,Staff Name,Performed By,IP Address,Created Time");
        for (ActivityLogReportResponseDto log : logs) {
            writer.println(
                    log.getActivityLogId() + "," +
                            safe(log.getDescription()) + "," +
                            safe(log.getStaffName()) + "," +
                            safe(log.getPerformedBy()) + "," +
                            safe(log.getIpAddress()) + "," +
                            formatDate(log)
            );
        }
        writer.flush();
    }

    private static void generatePdf(List<ActivityLogReportResponseDto> logs,
                                    HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition", "attachment; filename=activity_logs.pdf");
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        addHeader(table, "Log ID");
        addHeader(table, "Description");
        addHeader(table, "Staff Name");
        addHeader(table, "Performed By");
        addHeader(table, "IP Address");
        addHeader(table, "Created Time");
        for (ActivityLogReportResponseDto log : logs) {
            table.addCell(String.valueOf(log.getActivityLogId()));
            table.addCell(safe(log.getDescription()));
            table.addCell(safe(log.getStaffName()));
            table.addCell(safe(log.getPerformedBy()));
            table.addCell(safe(log.getIpAddress()));
            table.addCell(formatDate(log));
        }

        document.add(table);
        document.close();
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }

    private static String formatDate(ActivityLogReportResponseDto log) {
        return log.getCreatedTime() != null
                ? formatter.format(log.getCreatedTime())
                : "";
    }

    private static void addHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
    }
}
