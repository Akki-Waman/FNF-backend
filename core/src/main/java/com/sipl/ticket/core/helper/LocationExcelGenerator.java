package com.sipl.ticket.core.helper;


import com.sipl.ticket.core.dto.response.LocationResponseDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class LocationExcelGenerator {

    public static void generateCsv(List<LocationResponseDTO> locationList, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=locations.csv");

        try (PrintWriter writer = response.getWriter();
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "ID", "Location Name", "Location Type", "Location Capacity", "Is Active"))) {

            for (LocationResponseDTO dto : locationList) {
                csvPrinter.printRecord(
                        dto.getLocationId(),
                        dto.getLocationName(),
                        dto.getLocationType() != null ? dto.getLocationType() : "",
                        dto.getLocationCapacity() != null ? dto.getLocationCapacity() : "",
                        dto.getIsActive() != null && dto.getIsActive() ? "Active" : "Inactive"
                );
            }
            csvPrinter.flush();
        }
    }
}
