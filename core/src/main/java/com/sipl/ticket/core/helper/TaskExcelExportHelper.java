package com.sipl.ticket.core.helper;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sipl.ticket.core.dto.response.TaskExportDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class TaskExcelExportHelper {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private static final String[] HEADERS = {
            "Task ID",
            "Task Name",
            "Status",
            "Start Date",
            "Due Date",
            "Assigned To",
            "Tags",
            "Priority"
    };

    public static void export(
            List<TaskExportDTO> tasks,
            String format,
            HttpServletResponse response
    ) throws Exception {

        log.info("Task export started | format={} | records={}",
                format, tasks != null ? tasks.size() : 0);

        if ("excel".equalsIgnoreCase(format)) {
            writeExcel(tasks, response);
        } else if ("csv".equalsIgnoreCase(format)) {
            writeCsv(tasks, response);
        } else if ("pdf".equalsIgnoreCase(format)) {
            writePdf(tasks, response);
        } else {
            throw new IllegalArgumentException("Invalid export format");
        }

        log.info("Task export completed | format={}", format);
    }

    private static void writeExcel(
            List<TaskExportDTO> tasks,
            HttpServletResponse response
    ) throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Tasks");

        CellStyle headerStyle = excelHeaderStyle(workbook);
        CellStyle dataStyle = excelDataStyle(workbook);

        int rowIndex = 0;
        Row headerRow = sheet.createRow(rowIndex++);

        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(headerStyle);
        }

        for (TaskExportDTO d : tasks) {
            Row r = sheet.createRow(rowIndex++);

            setCell(r, 0, d.getTaskId(), dataStyle);
            setCell(r, 1, d.getTaskName(), dataStyle);
            setCell(r, 2, d.getStatus(), dataStyle);
            setCell(r, 3, fmt(d.getStartDate()), dataStyle);
            setCell(r, 4, fmt(d.getDueDate()), dataStyle);
            setCell(r, 5, d.getAssignedTo(), dataStyle);
            setCell(r, 6, d.getTags(), dataStyle);
            setCell(r, 7, d.getPriority(), dataStyle);
        }

        for (int i = 0; i < HEADERS.length; i++) {
            sheet.setAutoFilter(
                    new CellRangeAddress(
                            0,
                            rowIndex - 1,
                            0,
                            HEADERS.length - 1
                    )
            );
            sheet.autoSizeColumn(i);
        }

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
                "Content-Disposition", "attachment; filename=tasks.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private static void writeCsv(
            List<TaskExportDTO> tasks,
            HttpServletResponse response
    ) throws Exception {

        response.setContentType("text/csv");
        response.setHeader(
                "Content-Disposition", "attachment; filename=tasks.csv");

        StringBuilder csv = new StringBuilder();
        csv.append(String.join(",", HEADERS)).append("\n");

        for (TaskExportDTO d : tasks) {
            csv.append(d.getTaskId()).append(",");
            csv.append(q(d.getTaskName())).append(",");
            csv.append(q(d.getStatus())).append(",");
            csv.append(fmt(d.getStartDate())).append(",");
            csv.append(fmt(d.getDueDate())).append(",");
            csv.append(q(d.getAssignedTo())).append(",");
            csv.append(q(d.getTags())).append(",");
            csv.append(q(d.getPriority())).append("\n");
        }

        response.getWriter().write(csv.toString());
    }

    private static void writePdf(
            List<TaskExportDTO> tasks,
            HttpServletResponse response
    ) throws Exception {

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition", "attachment; filename=tasks.pdf");

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        PdfPTable table = new PdfPTable(HEADERS.length);
        table.setWidthPercentage(100);

        Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        Font dataFont = new Font(Font.HELVETICA, 9);

        for (String h : HEADERS) {
            PdfPCell c = new PdfPCell(new Phrase(h, headerFont));
            c.setBackgroundColor(Color.LIGHT_GRAY);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c);
        }

        for (TaskExportDTO d : tasks) {
            table.addCell(new Phrase(String.valueOf(d.getTaskId()), dataFont));
            table.addCell(new Phrase(nz(d.getTaskName()), dataFont));
            table.addCell(new Phrase(nz(d.getStatus()), dataFont));
            table.addCell(new Phrase(fmt(d.getStartDate()), dataFont));
            table.addCell(new Phrase(fmt(d.getDueDate()), dataFont));
            table.addCell(new Phrase(nz(d.getAssignedTo()), dataFont));
            table.addCell(new Phrase(nz(d.getTags()), dataFont));
            table.addCell(new Phrase(nz(d.getPriority()), dataFont));
        }

        document.add(table);
        document.close();
    }

    private static String fmt(LocalDate d) {
        return d == null ? "" : d.format(DATE_FMT);
    }

    private static String nz(String v) {
        return v == null ? "" : v;
    }

    private static String q(String v) {
        if (v == null) return "\"\"";
        return "\"" + v.replace("\"", "\"\"") + "\"";
    }

    private static void setCell(
            Row r, int idx, Object val, CellStyle style
    ) {
        Cell c = r.createCell(idx);
        if (val != null) c.setCellValue(val.toString());
        c.setCellStyle(style);
    }

    private static CellStyle excelHeaderStyle(Workbook wb) {
        org.apache.poi.ss.usermodel.Font f = wb.createFont();
        f.setBold(true);

        CellStyle s = wb.createCellStyle();
        s.setFont(f);
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        s.setBorderTop(BorderStyle.THIN);
        s.setBorderBottom(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN);
        s.setBorderRight(BorderStyle.THIN);
        return s;
    }

    private static CellStyle excelDataStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        s.setAlignment(HorizontalAlignment.LEFT);
        s.setBorderTop(BorderStyle.THIN);
        s.setBorderBottom(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN);
        s.setBorderRight(BorderStyle.THIN);
        return s;
    }
}
