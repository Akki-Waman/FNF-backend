package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.ProductSubCategoryDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ProductSubCategoryExcelGenerator {

    public static void generateExcel(List<ProductSubCategoryDto> subCategories,
                                     HttpServletResponse response) throws Exception {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=product_sub_categories.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Product Sub Categories");

        int rowIndex = 0;

        /* ================= TITLE ================= */
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 18);

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        Row titleRow = sheet.createRow(rowIndex++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Product Sub Categories");
        titleCell.setCellStyle(titleStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
        rowIndex++;

        /* ================= HEADER ================= */
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        setBorders(headerStyle);

        Row headerRow = sheet.createRow(rowIndex++);
        String[] headers = {
                "Sub Category ID",
                "Category Name",
                "Sub Category Name"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        /* ================= DATA ================= */
        CellStyle dataStyle = workbook.createCellStyle();
        setBorders(dataStyle);

        for (ProductSubCategoryDto s : subCategories) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(s.getProductSubCategoryId());
            row.createCell(1).setCellValue(
                    s.getProductCategories().getProductCategoryName());
            row.createCell(2).setCellValue(
                    s.getProductSubCategoryName());

            row.getCell(0).setCellStyle(dataStyle);
            row.getCell(1).setCellStyle(dataStyle);
            row.getCell(2).setCellStyle(dataStyle);
        }

        /* ================= FILTER ================= */
        sheet.setAutoFilter(
                new CellRangeAddress(
                        headerRow.getRowNum(),
                        headerRow.getRowNum(),
                        0,
                        headers.length - 1));

        /* ================= AUTO SIZE ================= */
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
