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

        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 18);

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        Row titleRow = sheet.createRow(rowIndex++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(titleStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
        rowIndex++;

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        setBorders(headerStyle);

        Row headerRow = sheet.createRow(rowIndex++);

        String[] headers = {
                "Sub Category ID",
                "Category Name",
                "Sub Category Name",
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

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(
                workbook.createDataFormat()
                        .getFormat("dd-MM-yyyy HH:mm:ss"));
        setBorders(dateStyle);

        CellStyle dataStyle = workbook.createCellStyle();
        setBorders(dataStyle);

        for (ProductSubCategoryDto dto : subCategories) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(dto.getProductSubCategoryId());
            row.createCell(1).setCellValue(
                    dto.getProductCategories() != null
                            ? dto.getProductCategories().getProductCategoryName()
                            : "");
            row.createCell(2).setCellValue(dto.getProductSubCategoryName());
            row.createCell(3).setCellValue(dto.getCreatedBy());
            row.createCell(4).setCellValue(dto.getModifiedBy());

            if (dto.getCreatedTime() != null) {
                Cell c = row.createCell(5);
                c.setCellValue(dto.getCreatedTime());
                c.setCellStyle(dateStyle);
            }

            if (dto.getModifiedTime() != null) {
                Cell c = row.createCell(6);
                c.setCellValue(dto.getModifiedTime());
                c.setCellStyle(dateStyle);
            }

            for (int i = 0; i <= 4; i++) {
                row.getCell(i).setCellStyle(dataStyle);
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
