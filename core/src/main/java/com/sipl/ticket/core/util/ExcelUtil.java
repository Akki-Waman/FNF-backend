package com.sipl.ticket.core.util;

import com.sipl.ticket.core.dto.response.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtil {

    public static ByteArrayInputStream generateYardExcel(List<YardReportResponseDto> yardData) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Yard Report");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Header
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "Movement Name", "Transaction Id", "Plant Code", "Tag", "Lep Number",
                    "Yard In Time", "Yard In By", "Yard In Name",
                    "Load In Time", "Load In By", "Load In Name",
                    "Load Out Time", "Load Out By", "Load Out Name",
                    "Yard Out Time", "Yard Out By", "Yard Out Name"
            };

            for (int col = 0; col < headers.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers[col]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            int rowIdx = 1;
            for (YardReportResponseDto dto : yardData) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(dto.getMovementName());
                row.createCell(1).setCellValue(dto.getTransactionId() != null ? dto.getTransactionId() : 0);
                row.createCell(2).setCellValue(dto.getPlantCode());
                row.createCell(3).setCellValue(dto.getTag());
                row.createCell(4).setCellValue(dto.getLepNumber());

                row.createCell(5).setCellValue(dto.getYardInTime() != null ? dto.getYardInTime().toString() : "");
                row.createCell(6).setCellValue(dto.getYardInBy());
                row.createCell(7).setCellValue(dto.getYardInName());

                row.createCell(8).setCellValue(dto.getLoadInTime() != null ? dto.getLoadInTime().toString() : "");
                row.createCell(9).setCellValue(dto.getLoadInBy());
                row.createCell(10).setCellValue(dto.getLoadInName());

                row.createCell(11).setCellValue(dto.getLoadOutTime() != null ? dto.getLoadOutTime().toString() : "");
                row.createCell(12).setCellValue(dto.getLoadOutBy());
                row.createCell(13).setCellValue(dto.getLoadOutName());

                row.createCell(14).setCellValue(dto.getYardOutTime() != null ? dto.getYardOutTime().toString() : "");
                row.createCell(15).setCellValue(dto.getYardOutBy());
                row.createCell(16).setCellValue(dto.getYardOutName());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file: " + e.getMessage(), e);
        }
    }

    public static ByteArrayInputStream generateExcel(String sheetName, List<String> headers, List<WeighmentReportResponseDTO> dataList) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName);

            // Header styling
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Header
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }

            // Data
            int rowIdx = 1;
            for (WeighmentReportResponseDTO dto : dataList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(dto.getTransactionId() != null ? dto.getTransactionId() : 0);
                row.createCell(1).setCellValue(dto.getWeighmentType());
                row.createCell(2).setCellValue(dto.getMovementDescription());
                row.createCell(3).setCellValue(dto.getPlantCode());
                row.createCell(4).setCellValue(dto.getTagNumber());
                row.createCell(5).setCellValue(dto.getGateInTime() != null ? dto.getGateInTime().toString() : "");
                row.createCell(6).setCellValue(dto.getTruckNumber());

                row.createCell(7).setCellValue(dto.getTareWeight() != null ? dto.getTareWeight().doubleValue() : 0);
                row.createCell(8).setCellValue(dto.getTareLocation());
                row.createCell(9).setCellValue(dto.getGrossWeight() != null ? dto.getGrossWeight().doubleValue() : 0);
                row.createCell(10).setCellValue(dto.getGrossLocation());
                row.createCell(11).setCellValue(dto.getNetWeight() != null ? dto.getNetWeight().doubleValue() : 0);

                row.createCell(12).setCellValue(dto.getTareTime() != null ? dto.getTareTime().toString() : "");
                row.createCell(13).setCellValue(dto.getGrossTime() != null ? dto.getGrossTime().toString() : "");
                row.createCell(14).setCellValue(dto.getNetTime() != null ? dto.getNetTime().toString() : "");
                row.createCell(15).setCellValue(dto.getTareBy());
                row.createCell(16).setCellValue(dto.getGrossBy());
            }

            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public static ByteArrayInputStream generateTransactionExcel(List<TransactionReportResponseDto> dataList) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Transaction Report");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            String[] headers = {
                    "Status", "Movement", "Transaction Id", "Plant Code", "RFID Number", "LEP Number",
                    "Sales Orders", "Child Orders", "Material", "Order Quantity", "Truck Number",
                    "Driver Name", "Customer Name",
                    "Yard In Name", "Yard In Time",
                    "Gate In Name", "Gate In Time",
                    "Tare Weight", "Tare Weight Time",
                    "Gross Weight", "Gross Weight Time",
                    "Net Weight", "Net Weight Time",
                    "Load In Location", "Load In Time",
                    "Load Out Location", "Load Out Time",
                    "Gate Out Name", "Gate Out Time",
                    "Yard Out Name", "Yard Out Time"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (TransactionReportResponseDto dto : dataList) {
                Row row = sheet.createRow(rowIdx++);
                int col = 0;

                row.createCell(col++).setCellValue(dto.getStatus() != null ? dto.getStatus() : "");
                row.createCell(col++).setCellValue(dto.getMovement() != null ? dto.getMovement() : "");
                row.createCell(col++).setCellValue(dto.getTransactionId() != null ? dto.getTransactionId() : 0);
                row.createCell(col++).setCellValue(dto.getPlantCode() != null ? dto.getPlantCode() : "");
                row.createCell(col++).setCellValue(dto.getRfidNumber() != null ? dto.getRfidNumber() : "");
                row.createCell(col++).setCellValue(dto.getLepNumber() != null ? dto.getLepNumber() : "");
                row.createCell(col++).setCellValue(dto.getSalesOrders() != null ? dto.getSalesOrders() : "");
                row.createCell(col++).setCellValue(dto.getChildOrders() != null ? dto.getChildOrders() : "");
                row.createCell(col++).setCellValue(dto.getItems() != null ? dto.getItems() : "");
                row.createCell(col++).setCellValue(dto.getOrderQuantity() != null ? dto.getOrderQuantity() : 0);
                row.createCell(col++).setCellValue(dto.getVehicleRegistrationNumber() != null ? dto.getVehicleRegistrationNumber() : "");
                row.createCell(col++).setCellValue(dto.getDriverName() != null ? dto.getDriverName() : "");
                row.createCell(col++).setCellValue(dto.getCustomerName() != null ? dto.getCustomerName() : "");

                row.createCell(col++).setCellValue(dto.getYardInName() != null ? dto.getYardInName() : "");
                row.createCell(col++).setCellValue(dto.getYardInTime() != null ? dto.getYardInTime().toString() : "");
                row.createCell(col++).setCellValue(dto.getGateInName() != null ? dto.getGateInName() : "");
                row.createCell(col++).setCellValue(dto.getGateInTime() != null ? dto.getGateInTime().toString() : "");
                row.createCell(col++).setCellValue(dto.getTareWeight() != null ? dto.getTareWeight() : 0);
                row.createCell(col++).setCellValue(dto.getTareWeightTime() != null ? dto.getTareWeightTime().toString() : "");
                row.createCell(col++).setCellValue(dto.getGrossWeight() != null ? dto.getGrossWeight() : 0);
                row.createCell(col++).setCellValue(dto.getGrossWeightTime() != null ? dto.getGrossWeightTime().toString() : "");
                row.createCell(col++).setCellValue(dto.getNetWeight() != null ? dto.getNetWeight() : 0);
                row.createCell(col++).setCellValue(dto.getNetWeightTime() != null ? dto.getNetWeightTime().toString() : "");
                row.createCell(col++).setCellValue(dto.getLoadInLocation() != null ? dto.getLoadInLocation() : "");
                row.createCell(col++).setCellValue(dto.getLoadInTime() != null ? dto.getLoadInTime().toString() : "");
                row.createCell(col++).setCellValue(dto.getLoadOutLocation() != null ? dto.getLoadOutLocation() : "");
                row.createCell(col++).setCellValue(dto.getLoadOutTime() != null ? dto.getLoadOutTime().toString() : "");
                row.createCell(col++).setCellValue(dto.getGateOutName() != null ? dto.getGateOutName() : "");
                row.createCell(col++).setCellValue(dto.getGateOutTime() != null ? dto.getGateOutTime().toString() : "");
                row.createCell(col++).setCellValue(dto.getYardOutName() != null ? dto.getYardOutName() : "");
                row.createCell(col++).setCellValue(dto.getYardOutTime() != null ? dto.getYardOutTime().toString() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Transaction Report Excel file: " + e.getMessage(), e);
        }
    }

    public static ByteArrayInputStream generateGateReportExcel(String sheetName, List<String> headers, List<GateReportResponseDTO> dataList) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (GateReportResponseDTO dto : dataList) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(dto.getStatus() != null ? dto.getStatus() : "");
                row.createCell(1).setCellValue(dto.getMovement() != null ? dto.getMovement() : "");
                row.createCell(2).setCellValue(dto.getTransactionId() != null ? dto.getTransactionId() : 0);
                row.createCell(3).setCellValue(dto.getPlantCode() != null ? dto.getPlantCode() : "");
                row.createCell(4).setCellValue(dto.getTagNumber() != null ? dto.getTagNumber() : "");
                row.createCell(5).setCellValue(dto.getLepNumber() != null ? dto.getLepNumber() : "");
                row.createCell(6).setCellValue(dto.getTruckNumber() != null ? dto.getTruckNumber() : "");
                row.createCell(7).setCellValue(dto.getGateInTime() != null ? dto.getGateInTime().toString() : "");
                row.createCell(8).setCellValue(dto.getGateInBy() != null ? dto.getGateInBy() : "");
                row.createCell(9).setCellValue(dto.getGateInName() != null ? dto.getGateInName() : "");
                row.createCell(10).setCellValue(dto.getGateOutTime() != null ? dto.getGateOutTime().toString() : "");
                row.createCell(11).setCellValue(dto.getGateOutBy() != null ? dto.getGateOutBy() : "");
                row.createCell(12).setCellValue(dto.getGateOutName() != null ? dto.getGateOutName() : "");
            }

            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public static ByteArrayInputStream generateTransporterCustomerExcel(List<TransporterCustomerReportResponseDTO> data) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Transporter-Customer Report");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Headers
            String[] headers = {
                    "Order No", "Child Order No", "Transporter Name", "Customer Name",
                    "Material Type", "Material Name", "Total Weight", "UOM", "Status"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (TransporterCustomerReportResponseDTO dto : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(dto.getOrderNo());
                row.createCell(1).setCellValue(dto.getChildOrderNo());
                row.createCell(2).setCellValue(dto.getTransporterName());
                row.createCell(3).setCellValue(dto.getCustomerName());
                row.createCell(4).setCellValue(dto.getMaterialType());
                row.createCell(5).setCellValue(dto.getMaterialName());
                row.createCell(6).setCellValue(dto.getTotalWeight() != null ? dto.getTotalWeight().toString() : "");
                row.createCell(7).setCellValue(dto.getUom());
                row.createCell(8).setCellValue(dto.getStatus());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Transporter-Customer Excel: " + e.getMessage(), e);
        }
    }

    public static ByteArrayInputStream generateDailyMovementExcel(List<DailyMovementReportResponseDTO> data) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Daily Movement Report");

            // Header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Headers
            String[] headers = {
                    "LEP Number",
                    "Order No",
                    "Child Order No",
                    "Transporter Name",
                    "Customer Name",
                    "Material Name",
                    "Total Weight"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            int rowIdx = 1;
            for (DailyMovementReportResponseDTO dto : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(dto.getLepNumber());
                row.createCell(1).setCellValue(dto.getOrderNo());
                row.createCell(2).setCellValue(dto.getChildOrderNo());
                row.createCell(3).setCellValue(dto.getTransporterName());
                row.createCell(4).setCellValue(dto.getCustomerName());
                row.createCell(5).setCellValue(dto.getMaterialName());
                row.createCell(6).setCellValue(dto.getTotalWeight() != null ? dto.getTotalWeight().toString() : "");
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Daily Movement Excel: " + e.getMessage(), e);
        }
    }

    public static ByteArrayInputStream generateStoReportExcel(List<StoReportResponseDTO> data) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("STO Report");

            // ====== Header Style ======
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // ====== Headers ======
            String[] headers = {
                    "Order No",
                    "Order Date",
                    "Customer Name",
                    "Material Type",
                    "Material Code",
                    "Material Name",
                    "Order Qty",
                    "UOM",
                    "Status"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ====== Data Rows ======
            int rowIdx = 1;
            for (StoReportResponseDTO dto : data) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(dto.getOrderNo() != null ? dto.getOrderNo() : "");
                row.createCell(1).setCellValue(dto.getOrderDate() != null ? dto.getOrderDate().toString() : "");
                row.createCell(2).setCellValue(dto.getCustomerName() != null ? dto.getCustomerName() : "");
                row.createCell(3).setCellValue(dto.getMaterialType() != null ? dto.getMaterialType() : "");
                row.createCell(4).setCellValue(dto.getMaterialCode() != null ? dto.getMaterialCode() : "");
                row.createCell(5).setCellValue(dto.getMaterialName() != null ? dto.getMaterialName() : "");
                row.createCell(6).setCellValue(dto.getOrderQty() != null ? dto.getOrderQty().toString() : "");
                row.createCell(7).setCellValue(dto.getUom() != null ? dto.getUom() : "");
                row.createCell(8).setCellValue(dto.getStatus() != null ? dto.getStatus() : "");
            }

            // ====== Auto-size Columns ======
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate STO Report Excel: " + e.getMessage(), e);
        }
    }






}
