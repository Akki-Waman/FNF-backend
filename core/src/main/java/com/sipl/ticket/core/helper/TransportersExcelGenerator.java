package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.TransportersResponseDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TransportersExcelGenerator {

    public static void generateCsv(List<TransportersResponseDTO> transportersList,
                                   HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=transporters.csv");

        try (PrintWriter writer = response.getWriter();
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "ID", "Company Name", "GST Number", "Address",
                     "Email ID", "Contact Number", "Transporter Type ID",
                     "Transporter Type Code", "Is Active"))) {

            for (TransportersResponseDTO dto : transportersList) {
                csvPrinter.printRecord(
                        dto.getId(),
                        dto.getCompanyName(),
                        dto.getGstNumber() != null ? dto.getGstNumber() : "",
                        dto.getAddress() != null ? dto.getAddress() : "",
                        dto.getEmailId() != null ? dto.getEmailId() : "",
                        dto.getContactNumber() != null ? dto.getContactNumber() : "",
                        dto.getTransporterTypeId() != null ? dto.getTransporterTypeId() : "",
                        dto.getTransporterTypeCode() != null ? dto.getTransporterTypeCode() : "",
                        dto.getIsActive() != null && dto.getIsActive() ? "Active" : "Inactive"
                );
            }
            csvPrinter.flush();
        }
    }
}
