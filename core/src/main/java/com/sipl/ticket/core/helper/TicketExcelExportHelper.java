package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.TicketsResponseDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class TicketExcelExportHelper {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private static final String[] HEADERS = {
            "Ticket ID",
            "Subject",
            "Tags",
            "Department",
            "Service",
            "Contact",
            "Status",
            "Priority",
            "Created By",
            "First Note",
            "Location",
            "Last Reply",
            "Created Date",
            "Complaint Name",
            "Complaint Mobile No",
            "Start Date Time",
            "End Date Time"
    };

    public static void export(
            List<TicketsResponseDTO> tickets,
            String format,
            HttpServletResponse response
    ) throws Exception {

        if ("excel".equalsIgnoreCase(format)) {
            writeExcel(tickets, response);
        } else if ("csv".equalsIgnoreCase(format)) {
            writeCsv(tickets, response);
        } else if ("pdf".equalsIgnoreCase(format)) {
            writePdf(tickets, response);
        } else {
            throw new IllegalArgumentException("Invalid export format");
        }
    }

    // ================= EXCEL =================
    private static void writeExcel(
            List<TicketsResponseDTO> tickets,
            HttpServletResponse response
    ) throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Tickets");

        CellStyle headerStyle = excelHeaderStyle(workbook);
        CellStyle dataStyle = excelDataStyle(workbook);

        int rowIndex = 0;
        Row headerRow = sheet.createRow(rowIndex++);

        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(headerStyle);
        }

        for (TicketsResponseDTO d : tickets) {
            Row r = sheet.createRow(rowIndex++);

            setCell(r, 0, d.getTicketId(), dataStyle);
            setCell(r, 1, d.getSubject(), dataStyle);
            setCell(r, 2, d.getTags(), dataStyle);
            setCell(r, 3, dept(d), dataStyle);
            setCell(r, 4, service(d), dataStyle);
            setCell(r, 5, contact(d), dataStyle);
            setCell(r, 6, d.getStatusLabel(), dataStyle);
            setCell(r, 7, d.getPriorityLabel(), dataStyle);

            setCell(r, 8, d.getCreatedByUsername(), dataStyle);
            setCell(r, 9, d.getFirstNote(), dataStyle);
            setCell(r, 10, d.getLocationName(), dataStyle);

            setCell(r, 11, format(d.getModifiedTime()), dataStyle);
            setCell(r, 12, format(d.getCreatedTime()), dataStyle);
            setCell(r, 13, d.getComplaintName(), dataStyle);
            setCell(r, 14, d.getComplaintMobileNo(), dataStyle);
            setCell(r, 15, format(d.getStartDateTime()), dataStyle);
            setCell(r, 16, format(d.getEndDateTime()), dataStyle);
        }

        sheet.setAutoFilter(new CellRangeAddress(0, rowIndex - 1, 0, HEADERS.length - 1));

        for (int i = 0; i < HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=tickets.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    // ================= CSV =================
    private static void writeCsv(
            List<TicketsResponseDTO> tickets,
            HttpServletResponse response
    ) throws Exception {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=tickets.csv");

        StringBuilder csv = new StringBuilder();
        csv.append(String.join(",", HEADERS)).append("\n");

        for (TicketsResponseDTO d : tickets) {
            csv.append(d.getTicketId()).append(",");
            csv.append(q(d.getSubject())).append(",");
            csv.append(q(d.getTags())).append(",");
            csv.append(q(dept(d))).append(",");
            csv.append(q(service(d))).append(",");
            csv.append(q(contact(d))).append(",");
            csv.append(q(d.getStatusLabel())).append(",");
            csv.append(q(d.getPriorityLabel())).append(",");

            csv.append(q(d.getCreatedByUsername())).append(",");
            csv.append(q(d.getFirstNote())).append(",");
            csv.append(q(d.getLocationName())).append(",");

            csv.append(csvDate(d.getModifiedTime())).append(",");
            csv.append(csvDate(d.getCreatedTime())).append(",");
            csv.append(q(d.getComplaintName())).append(",");
            csv.append("'").append(safe(d.getComplaintMobileNo())).append("'").append(",");
            csv.append(csvDate(d.getStartDateTime())).append(",");
            csv.append(csvDate(d.getEndDateTime())).append("\n");
        }

        response.getWriter().write(csv.toString());
    }

    // ================= PDF =================
    private static void writePdf(
            List<TicketsResponseDTO> tickets,
            HttpServletResponse response
    ) throws Exception {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=tickets.pdf");

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        PdfPTable table = new PdfPTable(HEADERS.length);
        table.setWidthPercentage(100);

        com.lowagie.text.Font headerFont =
                new com.lowagie.text.Font(
                        com.lowagie.text.Font.HELVETICA,
                        10,
                        com.lowagie.text.Font.BOLD
                );

        com.lowagie.text.Font dataFont =
                new com.lowagie.text.Font(
                        com.lowagie.text.Font.HELVETICA,
                        9
                );

        for (String h : HEADERS) {
            PdfPCell c = new PdfPCell(new Phrase(h, headerFont));
            c.setBackgroundColor(Color.LIGHT_GRAY);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c);
        }

        for (TicketsResponseDTO d : tickets) {
            table.addCell(new Phrase(String.valueOf(d.getTicketId()), dataFont));
            table.addCell(new Phrase(safe(d.getSubject()), dataFont));
            table.addCell(new Phrase(safe(d.getTags()), dataFont));
            table.addCell(new Phrase(dept(d), dataFont));
            table.addCell(new Phrase(service(d), dataFont));
            table.addCell(new Phrase(contact(d), dataFont));
            table.addCell(new Phrase(safe(d.getStatusLabel()), dataFont));
            table.addCell(new Phrase(safe(d.getPriorityLabel()), dataFont));

            table.addCell(new Phrase(safe(d.getCreatedByUsername()), dataFont));
            table.addCell(new Phrase(safe(d.getFirstNote()), dataFont));
            table.addCell(new Phrase(safe(d.getLocationName()), dataFont));

            table.addCell(new Phrase(format(d.getModifiedTime()), dataFont));
            table.addCell(new Phrase(format(d.getCreatedTime()), dataFont));
            table.addCell(new Phrase(safe(d.getComplaintName()), dataFont));
            table.addCell(new Phrase(safe(d.getComplaintMobileNo()), dataFont));
            table.addCell(new Phrase(format(d.getStartDateTime()), dataFont));
            table.addCell(new Phrase(format(d.getEndDateTime()), dataFont));
        }

        document.add(table);
        document.close();
    }

    // ================= HELPERS =================
    private static String dept(TicketsResponseDTO d) {
        return d.getDepartment() != null ? d.getDepartment().getDepartmentName() : "";
    }

    private static String service(TicketsResponseDTO d) {
        return d.getService() != null ? d.getService().getServiceName() : "";
    }

    private static String contact(TicketsResponseDTO d) {
        return d.getContact() != null ? d.getContact().getContactName() : "";
    }

    private static String format(LocalDateTime dt) {
        return dt == null ? "" : dt.format(DATE_FMT);
    }

    private static String csvDate(LocalDateTime dt) {
        return dt == null ? "" : "'" + dt.format(DATE_FMT);
    }

    private static String safe(Object v) {
        return v != null ? v.toString() : "";
    }

    private static String q(String v) {
        if (v == null) return "\"\"";
        return "\"" + v.replace("\"", "\"\"") + "\"";
    }

    private static void setCell(Row r, int idx, Object val, CellStyle style) {
        Cell c = r.createCell(idx);
        if (val != null) c.setCellValue(val.toString());
        c.setCellStyle(style);
    }

    private static CellStyle excelHeaderStyle(Workbook wb) {
        Font f = wb.createFont();
        f.setBold(true);

        CellStyle s = wb.createCellStyle();
        s.setFont(f);
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return s;
    }

    private static CellStyle excelDataStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        s.setAlignment(HorizontalAlignment.LEFT);
        return s;
    }
}