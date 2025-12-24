package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.CustomersResponseDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CustomerExcelGenerator {

    public static void generateCsv(List<CustomersResponseDTO> customers, HttpServletResponse response) throws Exception {
        response.setContentType("text/csv");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customers.csv";
        response.setHeader(headerKey, headerValue);

        PrintWriter writer = response.getWriter();

        // ✅ Updated header to include Customer Code and Registration Validity
        writer.println("Customer Code,Customer Name,Contact Person Name,PAN Number,GST Number,Contact Number,Registration Validity,Remarks,Status");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (CustomersResponseDTO c : customers) {
            String validityDate = "";
            if (c.getRegistrationValidityDate() != null) {
                validityDate = c.getRegistrationValidityDate().format(formatter);
            }

            writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                    escapeCsv(c.getCustomerCode()),
                    escapeCsv(c.getCustomerName()),
                    escapeCsv(c.getContactPersonName()),
                    escapeCsv(c.getPanNumber()),
                    escapeCsv(c.getGstRegNumber()),
                    c.getContactNumber() != null ? c.getContactNumber().toString() : "",
                    escapeCsv(validityDate),
                    escapeCsv(c.getRemarks()),
                    c.getStatus() != null && c.getStatus() ? "Active" : "Inactive"
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