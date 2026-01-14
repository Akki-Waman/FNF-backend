package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.ClientProductsResponseDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ClientProductExcelGenerator {

    public static void generateExcel(List<ClientProductsResponseDTO> clientProducts,
                                     HttpServletResponse response) throws Exception {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
                "Content-Disposition", "attachment; filename=client_products.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Client Products");

        int rowIndex = 0;

        // 0 to 17 = 18 columns
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 17));
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
                "Client Product ID",
                "Product Name",
                "Group Name",
                "Region",
                "Zone",
                "Division",
                "Operational Unit",
                "Device Name",
                "Platform Model",
                "Serial Number",
                "IMEI No",
                "MDM Asset No",
                "Part No",
                "Working Status",
                "Device Status",
                "Created By",
                "Modified By",
                "Created Time",
                "Modified Time"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.LEFT);
        setBorders(dataStyle);

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setAlignment(HorizontalAlignment.LEFT);
        dateStyle.setDataFormat(
                workbook.createDataFormat()
                        .getFormat("dd-MM-yyyy HH:mm:ss"));
        setBorders(dateStyle);

        for (ClientProductsResponseDTO dto : clientProducts) {

            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(dto.getClientProductId());
            row.createCell(1).setCellValue(
                    dto.getProduct() != null ? dto.getProduct().getProductName() : "");
            row.createCell(2).setCellValue(dto.getGroupName());
            row.createCell(3).setCellValue(
                    dto.getRegion() != null ? dto.getRegion().getRegionName() : "");
            row.createCell(4).setCellValue(
                    dto.getZone() != null ? dto.getZone().getZoneName() : "");
            row.createCell(5).setCellValue(
                    dto.getDivision() != null ? dto.getDivision().getDivisionName() : "");
            row.createCell(6).setCellValue(
                    dto.getUnit() != null ? dto.getUnit().getOperationalUnitName() : "");
            row.createCell(7).setCellValue(dto.getDeviceName());
            row.createCell(8).setCellValue(dto.getPlatformModel());
            row.createCell(9).setCellValue(dto.getSerialNumber());
            row.createCell(10).setCellValue(dto.getImeiNo());
            row.createCell(11).setCellValue(dto.getMdmAssetNo());
            row.createCell(12).setCellValue(dto.getPartNo());
            row.createCell(13).setCellValue(dto.getWorkingStatus());
            row.createCell(14).setCellValue(dto.getDeviceStatus());

            Cell c15 = row.createCell(15);
            c15.setCellValue(dto.getCreatedBy());
            c15.setCellStyle(dataStyle);

            Cell c16 = row.createCell(16);
            c16.setCellValue(dto.getModifiedBy());
            c16.setCellStyle(dataStyle);

            Cell c17 = row.createCell(17);
            if (dto.getCreatedTime() != null) {
                c17.setCellValue(dto.getCreatedTime());
                c17.setCellStyle(dateStyle);
            } else {
                c17.setCellStyle(dataStyle);
            }

            Cell c18 = row.createCell(18);
            if (dto.getModifiedTime() != null) {
                c18.setCellValue(dto.getModifiedTime());
                c18.setCellStyle(dateStyle);
            } else {
                c18.setCellStyle(dataStyle);
            }

            for (int i = 0; i <= 18; i++) {
                row.getCell(i).setCellStyle(
                        i >= 17 ? row.getCell(i).getCellStyle() : dataStyle);
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
