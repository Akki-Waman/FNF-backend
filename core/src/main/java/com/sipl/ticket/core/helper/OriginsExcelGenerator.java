package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.OriginDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class OriginsExcelGenerator {

    public static void generateCsv(List<OriginDto> originsList, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=origins.csv");

        try (PrintWriter writer = response.getWriter();
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "Origin ID", "Origin Name", "Is Active"))) {

            for (OriginDto dto : originsList) {
                csvPrinter.printRecord(
                        dto.getOriginId(),
                        dto.getOriginName() != null ? dto.getOriginName() : "",
                        dto.getIsActive() != null && dto.getIsActive() ? "Active" : "Inactive"
                );
            }
            csvPrinter.flush();
        }
    }
}

