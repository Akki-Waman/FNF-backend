package com.sipl.ticket.product.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.sipl.ticket.core.util.CbmCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.sipl.ticket.core.dao.entity.*;
import com.sipl.ticket.core.dao.repository.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExcelReaderService {

    private final BrandRepository brandRepository;
    private final OriginsRepository originsRepository;
    private final ProductSubCategoryRepository productSubCategoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final UnitRepository unitRepository;
    private final ProductRepository productRepository;
    private final ProductUnitRepository productUnitRepository;
    private final CbmCalculator cbmCalculator;

    // ================= MAIN ENTRY =================
    public void readAndSaveExcelData(String filePath) {
        List<Products> products = readProductsFromExcel(filePath);
        productRepository.saveAll(products);

        List<ProductUnit> productUnits = readProductUnitsFromExcel(filePath);
        productUnitRepository.saveAll(productUnits);
    }

    // ================= PRODUCTS SHEET =================
    private List<Products> readProductsFromExcel(String filePath) {
        List<Products> products = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Products");

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                Products product = new Products();

                product.setProductCode(row.getCell(0).getStringCellValue());
                product.setProductDesc(row.getCell(1).getStringCellValue());

                String productName = row.getCell(2).getStringCellValue();

                productRepository.findByProductName(productName)
                        .ifPresent(p -> {
                            throw new RuntimeException(
                                    "Product already exists: " + productName);
                        });

                product.setProductName(productName);

                // -------- Sub Category --------
                String subCategoryName = row.getCell(3).getStringCellValue();
                ProductSubCategories subCategory =
                        productSubCategoryRepository.findAll().stream()
                                .filter(sc -> sc.getProductSubCategoryName()
                                        .equalsIgnoreCase(subCategoryName))
                                .findFirst()
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "SubCategory not found: " + subCategoryName));
                product.setProductSubCategory(subCategory);

                // -------- Category --------
                String categoryName = row.getCell(4).getStringCellValue();
                ProductCategories category =
                        productCategoryRepository
                                .findByProductCategoryNameIgnoreCaseAndIsActive(
                                        categoryName, true)
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Category not found: " + categoryName));
                product.setProductCategory(category);

                // -------- Brand --------
                String brandName = row.getCell(5).getStringCellValue();
                Brands brand =
                        brandRepository.findAll().stream()
                                .filter(b -> b.getBrandName()
                                        .equalsIgnoreCase(brandName))
                                .findFirst()
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Brand not found: " + brandName));
                product.setBrands(brand);

                // -------- Origin --------
                String originName = row.getCell(6).getStringCellValue();
                Origins origin =
                        originsRepository.findActiveByOriginName(originName)
                                .stream()
                                .findFirst()
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Origin not found: " + originName));
                product.setOrigins(origin);

                product.setReorderLevel((long) row.getCell(7).getNumericCellValue());
                product.setReorderQty((long) row.getCell(8).getNumericCellValue());
                product.setMinLevel(row.getCell(9).getNumericCellValue());
                product.setMaxLevel(row.getCell(10).getNumericCellValue());
                product.setNetWeight(row.getCell(12).getNumericCellValue());
                product.setGrossWeight(row.getCell(13).getNumericCellValue());

                // -------- Unit --------
                String unitName = row.getCell(14).getStringCellValue();
                Unit unit =
                        unitRepository.findAll().stream()
                                .filter(u -> u.getUnitName()
                                        .equalsIgnoreCase(unitName))
                                .findFirst()
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Unit not found: " + unitName));
                product.setUnit(unit);

                product.setWithBatchTracking(row.getCell(15).getBooleanCellValue());
                product.setWithExpiryDate(row.getCell(16).getBooleanCellValue());
                product.setIsService(row.getCell(17).getBooleanCellValue());
                product.setIsActive(row.getCell(18).getBooleanCellValue());
                product.setIsDelete(false);
                product.setIsSync(false);

                products.add(product);
            }

        } catch (Exception e) {
            log.error("Error reading Products sheet", e);
            throw new RuntimeException(e.getMessage(), e);
        }

        return products;
    }

    // ================= PRODUCT UNITS SHEET =================
    private List<ProductUnit> readProductUnitsFromExcel(String filePath) {
        List<ProductUnit> productUnits = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("ProductUnits");

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                ProductUnit productUnit = new ProductUnit();

                String productName = row.getCell(0).getStringCellValue();
                Products product =
                        productRepository.findByProductName(productName)
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Product not found: " + productName));
                productUnit.setProduct(product);

                String unitName = row.getCell(1).getStringCellValue();
                Unit unit =
                        unitRepository.findAll().stream()
                                .filter(u -> u.getUnitName()
                                        .equalsIgnoreCase(unitName))
                                .findFirst()
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Unit not found: " + unitName));
                productUnit.setUnit(unit);

                productUnit.setCFactor(row.getCell(2).getNumericCellValue());
                productUnit.setIsSellingUnit(row.getCell(3).getBooleanCellValue());
                productUnit.setIsPurchaseUnit(row.getCell(4).getBooleanCellValue());
                productUnit.setPurchasePrice(row.getCell(5).getNumericCellValue());
                productUnit.setSalesPrice(row.getCell(6).getNumericCellValue());
                productUnit.setIsActive(row.getCell(7).getBooleanCellValue());

                double length = getNumeric(row, 8);
                double width  = getNumeric(row, 9);
                double height = getNumeric(row, 10);

                productUnit.setLength(length);
                productUnit.setWidth(width);
                productUnit.setHeight(height);
                productUnit.setCbmValue(
                        cbmCalculator.calculateCbm(length, width, height));

                productUnits.add(productUnit);
            }

        } catch (Exception e) {
            log.error("Error reading ProductUnits sheet", e);
            throw new RuntimeException(e.getMessage(), e);
        }

        return productUnits;
    }

    private double getNumeric(Row row, int index) {
        if (row.getCell(index) == null) {
            throw new RuntimeException(
                    "Cell is empty at column index: " + index +
                            ", row: " + (row.getRowNum() + 1)
            );
        }

        switch (row.getCell(index).getCellType()) {
            case NUMERIC:
                return row.getCell(index).getNumericCellValue();

            case STRING:
                try {
                    return Double.parseDouble(
                            row.getCell(index).getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    throw new RuntimeException(
                            "Invalid numeric value at column index: " + index +
                                    ", row: " + (row.getRowNum() + 1));
                }

            default:
                throw new RuntimeException(
                        "Unsupported cell type at column index: " + index +
                                ", row: " + (row.getRowNum() + 1)
                );
        }
    }

}
