package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.DriversResponseDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DriverExcelGenerator {
    public static void generateCsv(List<DriversResponseDTO> driversList, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=drivers.csv");

        try (CSVPrinter csvPrinter = new CSVPrinter(response.getWriter(),
                CSVFormat.DEFAULT.withHeader(
                        "ID",
                        "Driver Code",
                        "Driver Name",
                        "DOB",
                        "Mobile No",
                        "License No",
                        "License Expiry",
                        "Aadhaar No",
                        "Plant Safety Induction",
                        "License Expiry Hazardous",
                        "Vehicle Type",
                        "Transporter"
                ))) {

            for (DriversResponseDTO dto : driversList) {
                csvPrinter.printRecord(
                        dto.getId(),
                        dto.getDriverCode(),
                        dto.getDriverName(),
                        dto.getDob() != null ? dto.getDob() : "",
                        dto.getDriverMobileNo(),
                        dto.getDriverLicenseNo(),
                        dto.getDriverLicenseExpiryDate() != null ? dto.getDriverLicenseExpiryDate() : "",
                        dto.getDriverAadhaarNo(),
                        dto.getPlantSafetyInduction() != null && dto.getPlantSafetyInduction() ? "Yes" : "No",
                        dto.getDriverLicenseExpiryDateHazardous() != null ? dto.getDriverLicenseExpiryDateHazardous() : "",
                        dto.getVehicleTypeName(),
                        dto.getCompanyName()
                );
            }
            csvPrinter.flush();
        }
    }

}
