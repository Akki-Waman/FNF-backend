package com.sipl.ticket.core.util;

import com.sipl.ticket.core.dto.response.ResponsePenaltyResponseDTO;
import com.sipl.ticket.core.dto.response.TicketResponseDTO;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Font;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.util.List;

@Slf4j
public class ResponsePenaltyExportHelper {

    private static final String[] HEADERS = {
            "Ticket ID",
            "Unit Name",
            "Device Name",
            "Service",
            "Subject",
            "Severity",
            "SLA Hours",
            "Issue Logged",
            "Response On",
            "Response Time",
            "Status",
            "Response Within SLA",
            "Response Within 72 Hours",
            "Penalty Time",
            "Penalty %"
    };


    public static void export(
                List<ResponsePenaltyResponseDTO> list,
                String format,
                HttpServletResponse response
        ) throws Exception {

            if ("excel".equalsIgnoreCase(format)) {
                writeExcel(list, response);
            } else if ("csv".equalsIgnoreCase(format)) {
                writeCsv(list, response);
            } else if ("pdf".equalsIgnoreCase(format)) {
                writePdf(list, response);
            } else {
                throw new IllegalArgumentException("Invalid format");
            }
        }

        /* ================= EXCEL ================= */

        private static void writeExcel(
                List<ResponsePenaltyResponseDTO> list,
                HttpServletResponse response
        ) throws Exception {

            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("Response Penalty");

            CellStyle header = excelHeader(wb);
            CellStyle data = excelData(wb);

            int row = 0;
            Row h = sheet.createRow(row++);

            for (int i = 0; i < HEADERS.length; i++) {
                Cell c = h.createCell(i);
                c.setCellValue(HEADERS[i]);
                c.setCellStyle(header);
            }

            for (ResponsePenaltyResponseDTO d : list) {
                Row r = sheet.createRow(row++);

                set(r, 0, d.getTicketId(), data);
                set(r, 1, d.getUnitName(), data);
                set(r, 2, d.getDeviceName(), data);
                set(r, 3, d.getService(), data);
                set(r, 4, d.getSubject(), data);
                set(r, 5, d.getSeverity(), data);
                set(r, 6, d.getSlaHours(), data);
                set(r, 7, d.getIssueLogged(), data);
                set(r, 8, d.getResponseOn(), data);
                set(r, 9, d.getResponseTime(), data);
                set(r,10, d.getStatus(), data);
                set(r,11, yesNo(d.getResponseWithinSla()), data);
                set(r,12, yesNo(d.getResponseWithin72Hours()), data);
                set(r,13, d.getPenaltyTime(), data);
                set(r,14, d.getPenaltyPercentage(), data);
            }

            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            response.setContentType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            );
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=response-penalty.xlsx"
            );

            wb.write(response.getOutputStream());
            wb.close();
        }

        /* ================= CSV ================= */

        private static void writeCsv(
                List<ResponsePenaltyResponseDTO> list,
                HttpServletResponse response
        ) throws Exception {

            response.setContentType("text/csv");
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=response-penalty.csv"
            );

            StringBuilder csv = new StringBuilder();
            csv.append(String.join(",", HEADERS)).append("\n");

            for (ResponsePenaltyResponseDTO d : list) {
                csv.append(d.getTicketId()).append(",");
                csv.append(q(d.getUnitName())).append(",");
                csv.append(q(d.getDeviceName())).append(",");
                csv.append(q(d.getService())).append(",");
                csv.append(q(d.getSubject())).append(",");
                csv.append(q(d.getSeverity())).append(",");
                csv.append(
                        d.getSlaHours() != null
                                ? String.format("%.2f", d.getSlaHours())
                                : ""
                ).append(",");

                csv.append(d.getIssueLogged()).append(",");
                csv.append(d.getResponseOn()).append(",");
                csv.append(q(d.getResponseTime())).append(",");
                csv.append(q(d.getStatus())).append(",");
                csv.append(yesNo(d.getResponseWithinSla())).append(",");
                csv.append(yesNo(d.getResponseWithin72Hours())).append(",");
                csv.append(q(d.getPenaltyTime())).append(",");
                csv.append(d.getPenaltyPercentage()).append("\n");
            }

            response.getWriter().write(csv.toString());
        }

        /* ================= PDF ================= */

        private static void writePdf(
                List<ResponsePenaltyResponseDTO> list,
                HttpServletResponse response
        ) throws Exception {

            response.setContentType("application/pdf");
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=response-penalty.pdf"
            );

            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, response.getOutputStream());
            doc.open();

            PdfPTable table = new PdfPTable(HEADERS.length);
            table.setWidthPercentage(100);

            Font hFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font dFont = new Font(Font.HELVETICA, 9);

            for (String h : HEADERS) {
                PdfPCell c = new PdfPCell(new Phrase(h, hFont));
                c.setBackgroundColor(Color.LIGHT_GRAY);
                c.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c);
            }

            for (ResponsePenaltyResponseDTO d : list) {
                add(table, d.getTicketId(), dFont);
                add(table, d.getUnitName(), dFont);
                add(table, d.getDeviceName(), dFont);
                add(table, d.getService(), dFont);
                add(table, d.getSubject(), dFont);
                add(table, d.getSeverity(), dFont);
                add(table, d.getSlaHours(), dFont);
                add(table, d.getIssueLogged(), dFont);
                add(table, d.getResponseOn(), dFont);
                add(table, d.getResponseTime(), dFont);
                add(table, d.getStatus(), dFont);
                add(table, yesNo(d.getResponseWithinSla()), dFont);
                add(table, yesNo(d.getResponseWithin72Hours()), dFont);
                add(table, d.getPenaltyTime(), dFont);
                add(table, d.getPenaltyPercentage(), dFont);
            }

            doc.add(table);
            doc.close();
        }

        /* ================= COMMON ================= */

        private static String yesNo(Boolean b) {
            return b == null ? "" : (b ? "Yes" : "No");
        }

        private static String q(String v) {
            if (v == null) return "\"\"";
            return "\"" + v.replace("\"", "\"\"") + "\"";
        }

        private static void set(Row r, int i, Object v, CellStyle s) {
            Cell c = r.createCell(i);
            if (v != null) c.setCellValue(v.toString());
            c.setCellStyle(s);
        }

        private static void add(PdfPTable t, Object v, Font f) {
            t.addCell(new Phrase(v == null ? "" : v.toString(), f));
        }

        private static CellStyle excelHeader(Workbook wb) {
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

        private static CellStyle excelData(Workbook wb) {
            CellStyle s = wb.createCellStyle();
            s.setBorderTop(BorderStyle.THIN);
            s.setBorderBottom(BorderStyle.THIN);
            s.setBorderLeft(BorderStyle.THIN);
            s.setBorderRight(BorderStyle.THIN);
            return s;
        }
    }


