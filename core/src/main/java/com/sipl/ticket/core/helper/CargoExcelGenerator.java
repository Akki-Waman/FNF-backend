package com.sipl.ticket.core.helper;
import com.sipl.ticket.core.dto.response.CargosResponseDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CargoExcelGenerator {

    public static void generateCsv(List<CargosResponseDTO> cargoList, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=cargos.csv");

        try (PrintWriter writer = response.getWriter();
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "ID",
                     "Material Code",
                     "Material Description",
                     "Material Short Description",
                     "Material Type",
                     "Division Code",
                     "HSN Code",
                     "HSN Description",
                     "Unit Name",
                     "Batch Flag",
                     "Int Flag",
                     "Cargo Type",
                     "Status"
             ))) {

            for (CargosResponseDTO dto : cargoList) {
                csvPrinter.printRecord(
                        dto.getId(),
                        dto.getMaterialCode(),
                        dto.getMaterialDesc() != null ? dto.getMaterialDesc() : "",
                        dto.getMaterialShortDesc() != null ? dto.getMaterialShortDesc() : "",
                        dto.getMatType() != null ? dto.getMatType() : "",
                        dto.getDivisionCode() != null ? dto.getDivisionCode() : "",
                        dto.getHsnCode() != null ? dto.getHsnCode() : "",
                        dto.getHsnDescription() != null ? dto.getHsnDescription() : "",
                        dto.getUnitName() != null ? dto.getUnitName() : "",
                        dto.getBatchFlag() != null ? dto.getBatchFlag() : "",
                        dto.getIntFlag() != null ? dto.getIntFlag() : "",
                        dto.getCargoTypeName() != null ? dto.getCargoTypeName() : "",
                        dto.getIsActive() != null && dto.getIsActive() ? "Active" : "Inactive"
                );
            }
            csvPrinter.flush();
        }
    }
}

