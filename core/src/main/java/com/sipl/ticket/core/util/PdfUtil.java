package com.sipl.ticket.core.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sipl.ticket.core.dto.response.SalesOrderResponseDTO;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class PdfUtil {

    public byte[] generateSalesOrderPdf(SalesOrderResponseDTO salesOrder) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font labelFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

            // --- HEADER (Company + Invoice Info) ---
            PdfPTable header = new PdfPTable(2);
            header.setWidthPercentage(100);

            PdfPCell companyCell = new PdfPCell(new Phrase("Your Company Name\nAddress Line 1\nCity, State, PIN\nGSTIN: XXXXXXXX", normalFont));
            companyCell.setBorder(Rectangle.NO_BORDER);

            PdfPCell invoiceCell = new PdfPCell(new Phrase(
                    "Sales Order #: " + salesOrder.getSalesOrderNumber() + "\n" +
                            "Order Date: " + salesOrder.getOrderDate() + "\n" +
                            "PO Number: " + (salesOrder.getPoNumber() != null ? salesOrder.getPoNumber() : "") + "\n" +
                            "PO Date: " + (salesOrder.getPoDate() != null ? salesOrder.getPoDate() : "")
                    , labelFont));
            invoiceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            invoiceCell.setBorder(Rectangle.NO_BORDER);

            header.addCell(companyCell);
            header.addCell(invoiceCell);
            document.add(header);
            document.add(Chunk.NEWLINE);

            // --- CUSTOMER / DELIVERY DETAILS ---
            PdfPTable custTable = new PdfPTable(2);
            custTable.setWidthPercentage(100);

            PdfPCell billTo = new PdfPCell(new Phrase("Bill To:\n" + salesOrder.getCustomerName() +
                    "\nGST: " + (salesOrder.getGstRegistrationNumber() != null ? salesOrder.getGstRegistrationNumber() : "")
                    , normalFont));
            billTo.setBorder(Rectangle.NO_BORDER);

            PdfPCell shipTo = new PdfPCell(new Phrase("Ship To:\n" + (salesOrder.getDeliveryAddress() != null ? salesOrder.getDeliveryAddress() : "")
                    + "\nDelivery Date: " + (salesOrder.getDeliveryDate() != null ? salesOrder.getDeliveryDate() : "")
                    , normalFont));
            shipTo.setBorder(Rectangle.NO_BORDER);

            custTable.addCell(billTo);
            custTable.addCell(shipTo);
            document.add(custTable);
            document.add(Chunk.NEWLINE);

            // --- ITEMS TABLE ---
            PdfPTable itemsTable = new PdfPTable(8);
            itemsTable.setWidthPercentage(100);
            itemsTable.setWidths(new float[]{5, 25, 10, 10, 10, 10, 10, 10});

            // Headers
            String[] headers = {"S.No", "Item", "Qty", "UOM", "Unit Price", "Discount", "Tax", "Total"};
            for (String h : headers) itemsTable.addCell(createHeaderCell(h, labelFont));

            int srNo = 1;
            if (salesOrder.getItems() != null) {
                for (var item : salesOrder.getItems()) {
                    itemsTable.addCell(new PdfPCell(new Phrase(String.valueOf(srNo++), normalFont)));
                    itemsTable.addCell(new PdfPCell(new Phrase(item.getItemName(), normalFont)));
                    itemsTable.addCell(createRightCell(String.valueOf(item.getQuantity()), normalFont));
                    itemsTable.addCell(new PdfPCell(new Phrase(item.getUom(), normalFont)));
                    itemsTable.addCell(createRightCell(String.valueOf(item.getUnitPrice()), normalFont));
                    itemsTable.addCell(createRightCell(String.valueOf(item.getDiscount()), normalFont));
                    itemsTable.addCell(createRightCell(String.valueOf(item.getTax()), normalFont));
                    itemsTable.addCell(createRightCell(String.valueOf(item.getTotalAmount()), normalFont));
                }
            }
            document.add(itemsTable);
            document.add(Chunk.NEWLINE);

            // --- SUMMARY ---
            PdfPTable summary = new PdfPTable(2);
            summary.setWidthPercentage(40);
            summary.setHorizontalAlignment(Element.ALIGN_RIGHT);

            //addSummaryRow(summary, "Subtotal", String.valueOf(salesOrder.getGrandTotal().subtract(salesOrder.getDeliveryCharge() != null ? salesOrder.getDeliveryCharge() : BigDecimal.ZERO)), normalFont);
            addSummaryRow(summary, "Delivery Charge", String.valueOf(salesOrder.getDeliveryCharge()), normalFont);
            //addSummaryRow(summary, "Grand Total", String.valueOf(salesOrder.getGrandTotal()), labelFont);

            document.add(summary);

            // --- FOOTER ---
            document.add(Chunk.NEWLINE);
            Paragraph footer = new Paragraph("Remarks: " + (salesOrder.getRemarks() != null ? salesOrder.getRemarks() : ""), normalFont);
            document.add(footer);

            Paragraph sysGen = new Paragraph("\nThis is a system generated invoice.", normalFont);
            sysGen.setAlignment(Element.ALIGN_CENTER);
            document.add(sysGen);

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    private void addSummaryRow(PdfPTable table, String label, String value, Font font) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setBorder(Rectangle.NO_BORDER);
        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "", font));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }


    private void addDetailRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        table.addCell(new PdfPCell(new Phrase(label, labelFont)));
        table.addCell(new PdfPCell(new Phrase(value != null ? value : "", valueFont)));
    }

    private PdfPCell createHeaderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    private PdfPCell createRightCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }
}
