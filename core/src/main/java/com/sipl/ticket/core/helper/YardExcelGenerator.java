package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.YardResponseDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class YardExcelGenerator {

    public static void generateCsv(List<YardResponseDTO> yardList, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=yards.csv");

        try (PrintWriter writer = response.getWriter();
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "ID", "Yard Name", "Description", "Is Active"))) {

            for (YardResponseDTO dto : yardList) {
                csvPrinter.printRecord(
                        dto.getYardId(),
                        dto.getYardName(),
                        dto.getYardDescription() != null ? dto.getYardDescription() : "",
                        dto.getIsActive() != null && dto.getIsActive() ? "Active" : "Inactive"
                );
            }
            csvPrinter.flush();
        }
    }
}