package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.ProductDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ProductExcelGenerator {

    public static void generateExcel(List<ProductDto> products,
                                     HttpServletResponse response) throws Exception {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
                "Content-Disposition", "attachment; filename=products.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");

        int rowIndex = 0;

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 13));
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
                "Product ID",
                "Product Code",
                "Product Name",
                "Product Short Name",
                "Part Number",
                "Brand",
                "Category",
                "Sub Category",
                "Origin",
                "Unit",
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

        for (ProductDto dto : products) {

            Row row = sheet.createRow(rowIndex++);

            Cell c0 = row.createCell(0);
            c0.setCellValue(dto.getProductId());
            c0.setCellStyle(dataStyle);

            Cell c1 = row.createCell(1);
            c1.setCellValue(dto.getProductCode());
            c1.setCellStyle(dataStyle);

            Cell c2 = row.createCell(2);
            c2.setCellValue(dto.getProductName());
            c2.setCellStyle(dataStyle);

            Cell c3 = row.createCell(3);
            c3.setCellValue(dto.getProductShortName());
            c3.setCellStyle(dataStyle);

            Cell c4 = row.createCell(4);
            c4.setCellValue(dto.getPartNumber());
            c4.setCellStyle(dataStyle);

            Cell c5 = row.createCell(5);
            c5.setCellValue(
                    dto.getBrands() != null ? dto.getBrands().getBrandName() : "");
            c5.setCellStyle(dataStyle);

            Cell c6 = row.createCell(6);
            c6.setCellValue(
                    dto.getProductCategory() != null
                            ? dto.getProductCategory().getProductCategoryName()
                            : "");
            c6.setCellStyle(dataStyle);

            Cell c7 = row.createCell(7);
            c7.setCellValue(
                    dto.getProductSubCategory() != null
                            ? dto.getProductSubCategory().getProductSubCategoryName()
                            : "");
            c7.setCellStyle(dataStyle);

            Cell c8 = row.createCell(8);
            c8.setCellValue(
                    dto.getOrigins() != null ? dto.getOrigins().getOriginName() : "");
            c8.setCellStyle(dataStyle);

            Cell c9 = row.createCell(9);
            c9.setCellValue(
                    dto.getUnit() != null ? dto.getUnit().getUnitName() : "");
            c9.setCellStyle(dataStyle);

            Cell c10 = row.createCell(10);
            c10.setCellValue(dto.getCreatedBy());
            c10.setCellStyle(dataStyle);

            Cell c11 = row.createCell(11);
            c11.setCellValue(dto.getModifiedBy());
            c11.setCellStyle(dataStyle);

            Cell c12 = row.createCell(12);
            if (dto.getCreatedTime() != null) {
                c12.setCellValue(dto.getCreatedTime());
                c12.setCellStyle(dateStyle);
            } else {
                c12.setCellStyle(dataStyle);
            }

            Cell c13 = row.createCell(13);
            if (dto.getModifiedTime() != null) {
                c13.setCellValue(dto.getModifiedTime());
                c13.setCellStyle(dateStyle);
            } else {
                c13.setCellStyle(dataStyle);
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
