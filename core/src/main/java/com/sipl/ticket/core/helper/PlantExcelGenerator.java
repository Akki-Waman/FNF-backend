package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.PlantMasterResponseDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class PlantExcelGenerator {

    public static void generateCsv(List<PlantMasterResponseDTO> plants, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=plants.csv");

        try (CSVPrinter csvPrinter = new CSVPrinter(response.getWriter(),
                CSVFormat.DEFAULT.withHeader(
                        "Plant ID",
                        "Plant Code",
                        "Plant Description",
                        "Active"
                ))) {

            for (PlantMasterResponseDTO dto : plants) {
                csvPrinter.printRecord(
                        dto.getPlantId(),
                        dto.getPlantCode(),
                        dto.getPlantDescription(),
                        dto.getActive() != null && dto.getActive() ? "Yes" : "No"
                );
            }
            csvPrinter.flush();
        }
    }
}
