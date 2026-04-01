package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.ClientProductsResponseDTO;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.util.List;

@Slf4j
public class ClientProductsExportHelper {

    private static final String[] HEADERS = {
            "Product",
            "Group Name",
            "Region",
            "Zone",
            "Division",
            "Unit",
            "Device Name",
            "Platform Model",
            "Serial Number",
            "IMEI No",
            "MDM Asset No",
            "Part No",
            "Working Status",
            "Device Status",
            "PO No",
            "PO Date",
            "Warranty",
            "Warranty Start Date",
            "Warranty End Date",
            "Active",
            "Branch"
    };

    public static void export(
            List<ClientProductsResponseDTO> list,
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

    /* ===================== EXCEL ===================== */

    private static void writeExcel(
            List<ClientProductsResponseDTO> list,
            HttpServletResponse response
    ) throws Exception {

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Client Products");

        CellStyle header = excelHeader(wb);
        CellStyle data = excelData(wb);

        int row = 0;
        Row h = sheet.createRow(row++);

        for (int i = 0; i < HEADERS.length; i++) {
            Cell c = h.createCell(i);
            c.setCellValue(HEADERS[i]);
            c.setCellStyle(header);
        }

        for (ClientProductsResponseDTO d : list) {
            Row r = sheet.createRow(row++);

            set(r, 0, d.getProduct() != null ? d.getProduct().getProductName() : null, data);
            set(r, 1, d.getGroupName(), data);
            set(r, 2, d.getRegion() != null ? d.getRegion().getRegionName() : null, data);
            set(r, 3, d.getZone() != null ? d.getZone().getZoneName() : null, data);
            set(r, 4, d.getDivision() != null ? d.getDivision().getDivisionName() : null, data);
            set(r, 5, d.getUnit() != null ? d.getUnit().getOperationalUnitName() : null, data);
            set(r, 6, d.getDeviceName(), data);
            set(r, 7, d.getPlatformModel(), data);
            set(r, 8, d.getSerialNumber(), data);
            set(r, 9, d.getImeiNo(), data);
            set(r, 10, d.getMdmAssetNo(), data);
            set(r, 11, d.getPartNo(), data);
            set(r, 12, d.getWorkingStatus(), data);
            set(r, 13, d.getDeviceStatus(), data);
            set(r, 14, d.getPoNo(), data);
            set(r, 15, d.getPoDate(), data);
            set(r, 16, yesNo(d.getIsWarranty()), data);
            set(r, 17, d.getWarrantyPeriodStartDate(), data);
            set(r, 18, d.getWarrantyPeriodEndDate(), data);
            set(r, 19, yesNo(d.getIsActive()), data);
            set(r, 20, d.getBranchName(), data);
        }

        for (int i = 0; i < HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=client-products.xlsx"
        );

        wb.write(response.getOutputStream());
        wb.close();
    }

    /* ===================== CSV ===================== */

    private static void writeCsv(
            List<ClientProductsResponseDTO> list,
            HttpServletResponse response
    ) throws Exception {

        response.setContentType("text/csv");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=client-products.csv"
        );

        StringBuilder csv = new StringBuilder();
        csv.append(String.join(",", HEADERS)).append("\n");

        for (ClientProductsResponseDTO d : list) {
            csv.append(q(d.getProduct() != null ? d.getProduct().getProductName() : null)).append(",");
            csv.append(q(d.getGroupName())).append(",");
            csv.append(q(d.getRegion() != null ? d.getRegion().getRegionName() : null)).append(",");
            csv.append(q(d.getZone() != null ? d.getZone().getZoneName() : null)).append(",");
            csv.append(q(d.getDivision() != null ? d.getDivision().getDivisionName() : null)).append(",");
            csv.append(q(d.getUnit() != null ? d.getUnit().getOperationalUnitName() : null)).append(",");
            csv.append(q(d.getDeviceName())).append(",");
            csv.append(q(d.getPlatformModel())).append(",");
            csv.append(q(d.getSerialNumber())).append(",");
            csv.append(q(d.getImeiNo())).append(",");
            csv.append(q(d.getMdmAssetNo())).append(",");
            csv.append(q(d.getPartNo())).append(",");
            csv.append(q(d.getWorkingStatus())).append(",");
            csv.append(q(d.getDeviceStatus())).append(",");
            csv.append(q(d.getPoNo())).append(",");
            csv.append(q(d.getPoDate())).append(",");
            csv.append(yesNo(d.getIsWarranty())).append(",");
            csv.append(q(d.getWarrantyPeriodStartDate())).append(",");
            csv.append(q(d.getWarrantyPeriodEndDate())).append(",");
            csv.append(yesNo(d.getIsActive())).append(",");
            csv.append(q(d.getBranchName())).append("\n");
        }

        response.getWriter().write(csv.toString());
    }

    /* ===================== PDF ===================== */

    private static void writePdf(
            List<ClientProductsResponseDTO> list,
            HttpServletResponse response
    ) throws Exception {

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=client-products.pdf"
        );

        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();

        PdfPTable table = new PdfPTable(HEADERS.length);
        table.setWidthPercentage(100);

        Font hFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
        Font dFont = FontFactory.getFont(FontFactory.HELVETICA, 9);

        for (String h : HEADERS) {
            PdfPCell c = new PdfPCell(new Phrase(h, hFont));
            c.setBackgroundColor(Color.LIGHT_GRAY);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c);
        }

        for (ClientProductsResponseDTO d : list) {
            add(table, d.getProduct() != null ? d.getProduct().getProductName() : null, dFont);
            add(table, d.getGroupName(), dFont);
            add(table, d.getRegion() != null ? d.getRegion().getRegionName() : null, dFont);
            add(table, d.getZone() != null ? d.getZone().getZoneName() : null, dFont);
            add(table, d.getDivision() != null ? d.getDivision().getDivisionName() : null, dFont);
            add(table, d.getUnit() != null ? d.getUnit().getOperationalUnitName() : null, dFont);
            add(table, d.getDeviceName(), dFont);
            add(table, d.getPlatformModel(), dFont);
            add(table, d.getSerialNumber(), dFont);
            add(table, d.getImeiNo(), dFont);
            add(table, d.getMdmAssetNo(), dFont);
            add(table, d.getPartNo(), dFont);
            add(table, d.getWorkingStatus(), dFont);
            add(table, d.getDeviceStatus(), dFont);
            add(table, d.getPoNo(), dFont);
            add(table, d.getPoDate(), dFont);
            add(table, yesNo(d.getIsWarranty()), dFont);
            add(table, d.getWarrantyPeriodStartDate(), dFont);
            add(table, d.getWarrantyPeriodEndDate(), dFont);
            add(table, yesNo(d.getIsActive()), dFont);
            add(table, d.getBranchName(), dFont);
        }

        doc.add(table);
        doc.close();
    }

    /* ===================== COMMON ===================== */

    private static String yesNo(Boolean b) {
        return b == null ? "" : (b ? "Yes" : "No");
    }

    private static String q(Object v) {
        if (v == null) return "\"\"";
        return "\"" + v.toString().replace("\"", "\"\"") + "\"";
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
