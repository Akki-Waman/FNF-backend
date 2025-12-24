package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.UsersResponseDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UserExcelGenerator {

    public static void generateCsv(List<UsersResponseDTO> usersList, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=users.csv");

        try (PrintWriter writer = response.getWriter();
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "ID", "First Name", "Middle Name", "Last Name", "Username", "Phone Number", "Email", "DOB", "Is Active"))) {

            for (UsersResponseDTO dto : usersList) {
                csvPrinter.printRecord(
                        dto.getId(),
                        dto.getFirstName(),
                        dto.getMiddleName() != null ? dto.getMiddleName() : "",
                        dto.getLastName(),
                        dto.getUserName(),
                        dto.getPhoneNumber() != null ? dto.getPhoneNumber() : "",
                        dto.getEmailId() != null ? dto.getEmailId() : "",
                        dto.getDob() != null ? dto.getDob().toString() : "",
                        dto.getIsActive() != null && dto.getIsActive() ? "Active" : "Inactive"
                );
            }
            csvPrinter.flush();
        }
    }
}
