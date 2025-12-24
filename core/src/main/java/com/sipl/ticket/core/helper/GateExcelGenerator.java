package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.GatesResponseDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GateExcelGenerator {


    public static void generateCsv(List<GatesResponseDTO> gatesList, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=gates.csv");

        try (PrintWriter writer = response.getWriter();
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "ID", "Gate Number", "Description", "Is External", "Is Active"))) {

            for (GatesResponseDTO dto : gatesList) {
                csvPrinter.printRecord(
                        dto.getId(),
                        dto.getGateNumber(),
                        dto.getGateDescription() != null ? dto.getGateDescription() : "",
                        dto.getIsExternal() != null && dto.getIsExternal() ? "Yes" : "No",
                        dto.getIsActive() != null && dto.getIsActive() ? "Active" : "Inactive"
                );
            }
            csvPrinter.flush();
        }
    }
}
