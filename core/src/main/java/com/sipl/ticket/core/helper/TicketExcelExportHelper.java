package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.TicketsResponseDTO;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TicketExcelExportHelper {

    private static final String[] HEADERS = {
            "Ticket ID",
            "Subject",
            "Tags",
            "Department",
            "Service",
            "Contact",
            "Status",
            "Priority",
            "Last Reply",
            "Created",
            "Complaint Name",
            "Complaint Mobile No"
    };

    public static void export(
            List<TicketsResponseDTO> tickets,
            String format,
            HttpServletResponse response
    ) throws Exception {

        log.info("Ticket export started | format={} | totalRecords={}",
                format, tickets != null ? tickets.size() : 0);

        Workbook workbook = createExcelWorkbook(tickets);

        switch (format.toLowerCase()) {
            case "excel":
                writeExcel(workbook, response);
                break;
            case "csv":
                writeCsv(tickets, response);
                break;

            case "pdf":
                writePdf(tickets, response);
                break;
            default:
                throw new IllegalArgumentException("Invalid export format");
        }

        log.info("Ticket export completed | format={}", format);
    }

    private static Workbook createExcelWorkbook(List<TicketsResponseDTO> tickets) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Tickets");
        int rowIndex = 0;

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADERS.length - 1));
        Row titleRow = sheet.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("Tickets Export");

        Row headerRow = sheet.createRow(rowIndex++);
        CellStyle headerStyle = createHeaderStyle(workbook);

        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(headerStyle);
        }

        CellStyle dataStyle = createDataStyle(workbook);

        for (TicketsResponseDTO dto : tickets) {
            Row row = sheet.createRow(rowIndex++);

            createCell(row, 0, dto.getTicketId(), dataStyle);
            createCell(row, 1, dto.getSubject(), dataStyle);
            createCell(row, 2,
                    dto.getTagIds() != null
                            ? dto.getTagIds().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(", "))
                            : "",
                    dataStyle);
            createCell(row, 3,
                    dto.getDepartment() != null
                            ? dto.getDepartment().getDepartmentName()
                            : "",
                    dataStyle);
            createCell(row, 4,
                    dto.getService() != null
                            ? dto.getService().getServiceName()
                            : "",
                    dataStyle);
            createCell(row, 5,
                    dto.getContact() != null
                            ? dto.getContact().getContactName()
                            : "",
                    dataStyle);
            createCell(row, 6, dto.getStatus(), dataStyle);
            createCell(row, 7, dto.getPriority(), dataStyle);
            createCell(row, 8, dto.getModifiedTime(), dataStyle);
            createCell(row, 9, dto.getCreatedTime(), dataStyle);
            createCell(row, 10, dto.getComplaintName(), dataStyle);
            createCell(row, 11, dto.getComplaintMobileNo(), dataStyle);
        }

        for (int i = 0; i < HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }

    private static void writeExcel(Workbook workbook, HttpServletResponse response)
            throws Exception {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
                "Content-Disposition", "attachment; filename=tickets.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private static void writeCsv(
            List<TicketsResponseDTO> tickets,
            HttpServletResponse response
    ) throws Exception {

        response.setContentType("text/csv");
        response.setHeader(
                "Content-Disposition", "attachment; filename=tickets.csv");

        StringBuilder csv = new StringBuilder();

        // Header
        csv.append(String.join(",", HEADERS)).append("\n");

        for (TicketsResponseDTO dto : tickets) {
            csv.append(dto.getTicketId()).append(",");
            csv.append(dto.getSubject()).append(",");
            csv.append(dto.getTagIds() != null
                    ? dto.getTagIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining("|"))
                    : "").append(",");
            csv.append(dto.getDepartment() != null
                    ? dto.getDepartment().getDepartmentName() : "").append(",");
            csv.append(dto.getService() != null
                    ? dto.getService().getServiceName() : "").append(",");
            csv.append(dto.getContact() != null
                    ? dto.getContact().getContactName() : "").append(",");
            csv.append(dto.getStatus()).append(",");
            csv.append(dto.getPriority()).append(",");
            csv.append(dto.getModifiedTime()).append(",");
            csv.append(dto.getCreatedTime()).append(",");
            csv.append(dto.getComplaintName()).append(",");
            csv.append(dto.getComplaintMobileNo()).append("\n");
        }

        response.getWriter().write(csv.toString());
    }

    private static void writePdf(
            List<TicketsResponseDTO> tickets,
            HttpServletResponse response
    ) throws Exception {

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition", "attachment; filename=tickets.pdf");

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        PdfPTable table = new PdfPTable(HEADERS.length);

        // Header
        for (String header : HEADERS) {
            table.addCell(new Phrase(header));
        }

        // Data
        for (TicketsResponseDTO dto : tickets) {
            table.addCell(String.valueOf(dto.getTicketId()));
            table.addCell(dto.getSubject());
            table.addCell(dto.getTagIds() != null
                    ? dto.getTagIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "))
                    : "");
            table.addCell(dto.getDepartment() != null
                    ? dto.getDepartment().getDepartmentName() : "");
            table.addCell(dto.getService() != null
                    ? dto.getService().getServiceName() : "");
            table.addCell(dto.getContact() != null
                    ? dto.getContact().getContactName() : "");
            table.addCell(String.valueOf(dto.getStatus()));
            table.addCell(String.valueOf(dto.getPriority()));
            table.addCell(String.valueOf(dto.getModifiedTime()));
            table.addCell(String.valueOf(dto.getCreatedTime()));
            table.addCell(dto.getComplaintName());
            table.addCell(dto.getComplaintMobileNo());
        }

        document.add(table);
        document.close();
    }


    private static CellStyle createHeaderStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        return style;
    }

    private static void createCell(Row row, int index, Object value, CellStyle style) {
        Cell cell = row.createCell(index);
        if (value != null) cell.setCellValue(value.toString());
        cell.setCellStyle(style);
    }
}

