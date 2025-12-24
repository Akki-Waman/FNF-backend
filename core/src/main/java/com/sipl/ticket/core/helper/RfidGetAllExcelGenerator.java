package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.RfidsResponseDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RfidGetAllExcelGenerator {

    public static void generateCsv(List<RfidsResponseDTO> rfidList, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=rfids.csv");
        try (CSVPrinter csvPrinter = new CSVPrinter(response.getWriter(),
                CSVFormat.DEFAULT.withHeader(
                        "RFID ID",
                        "RFID Number",
                        "Is Active"
                ))) {
            for (RfidsResponseDTO dto : rfidList) {
                csvPrinter.printRecord(
                        dto.getId(),
                        dto.getRfidNumber(),
                        dto.getIsActive() != null && dto.getIsActive() ? "Yes" : "No"
                );
            }
            csvPrinter.flush();
        }
    }
}
