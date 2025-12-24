package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.VehiclesResponseDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class VehicleExcelGenerator {

    public static void generateCsv(List<VehiclesResponseDTO> vehiclesList, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=vehicles.csv");

        try (CSVPrinter csvPrinter = new CSVPrinter(response.getWriter(),
                CSVFormat.DEFAULT.withHeader(
                        "ID",
                        "Vehicle Registration Number",
                        "Owner Name",
                        "Registration Date",
                        "RTO Fitness Expiry",
                        "RTO Vehicle Permit Expiry",
                        "Insurance Expiry",
                        "PUC Validity",
                        "Gross Weight",
                        "Tare Weight",
                        "Carrying Capacity",
                        "Vehicle Age",
                        "Transporter",
                        "Is Blocked",
                        "Is Active",
                        "IMEI Number",
                        "Vehicle Type",
                        "GPS ID",
                        "GPS Company",
                        "Material",
                        "Last Trip LEP Status"
                ))) {

            for (VehiclesResponseDTO dto : vehiclesList) {
                csvPrinter.printRecord(
                        dto.getId(),
                        dto.getVehicleRegistrationNumber(),
                        dto.getOwnerName(),
                        dto.getRegistrationDate() != null ? dto.getRegistrationDate() : "",
                        dto.getRtoFitnessExpiryDate() != null ? dto.getRtoFitnessExpiryDate() : "",
                        dto.getRtoVehiclePermitExpiry() != null ? dto.getRtoVehiclePermitExpiry() : "",
                        dto.getInsuranceExpiryDate() != null ? dto.getInsuranceExpiryDate() : "",
                        dto.getPucValidity() != null ? dto.getPucValidity() : "",
                        dto.getRegGrossWeight(),
                        dto.getRegTareWeight(),
                        dto.getRegCarryingCapacity(),
                        dto.getVehicleAge(),
                        dto.getCompanyName(),
                        dto.getIsBlock() != null && dto.getIsBlock() ? "Yes" : "No",
                        dto.getIsActive() != null && dto.getIsActive() ? "Yes" : "No",
                        dto.getImeiNumber(),
                        dto.getVehicleTypeName(),
                        dto.getGpsId(),
                        dto.getGpsCompany(),
                        dto.getMaterial(),
                        dto.getLastTripLepStatus()
                );
            }

            csvPrinter.flush();
        }
    }
}
