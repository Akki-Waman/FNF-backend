package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.BrandDto;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

public class BrandExcelGenerator {

    public static void generateCsv(List<BrandDto> brands, HttpServletResponse response) throws Exception {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=brands.csv");

        PrintWriter writer = response.getWriter();

        // Header
        writer.println("Brand ID,Brand Name,Status");

        for (BrandDto b : brands) {
            writer.printf("%s,%s,%s%n",
                    b.getBrandId(),
                    escapeCsv(b.getBrandName()),
                    b.getIsActive() != null && b.getIsActive() ? "Active" : "Inactive"
            );
        }

        writer.flush();
        writer.close();
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}
