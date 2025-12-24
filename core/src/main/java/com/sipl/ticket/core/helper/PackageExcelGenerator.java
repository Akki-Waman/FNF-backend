package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.PackageDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class PackageExcelGenerator {

    public static void generateCsv(List<PackageDto> dtos, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=packages.csv");

        try (PrintWriter writer = response.getWriter();
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "Package Id", "Package Name", "Package Type", "Package Weighment", "Package Capacity", "Status"))) {

            for (PackageDto dto : dtos) {
                csvPrinter.printRecord(
                        dto.getPackageId(),
                        dto.getPackageName(),
                        dto.getPackageType(),
                        dto.getPackageWeighment(),
                        dto.getPackageCapacity(),
                        dto.getIsActive() != null && dto.getIsActive() ? "Active" : "Inactive"
                );
            }
            csvPrinter.flush();
        }
    }
}
