package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.DepartmentResponseDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

public class DepartmentExcelGenerator {

    public static void generateCsv(List<DepartmentResponseDTO> departments,
                                   HttpServletResponse response) throws Exception {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=departments.csv");

        PrintWriter writer = response.getWriter();

        // Header
        writer.println("Department ID,Department Name,Active Status,Deleted Status");

        for (DepartmentResponseDTO d : departments) {
            writer.printf("%s,%s,%s,%s%n",
                    d.getDepartmentId(),
                    escapeCsv(d.getDepartmentName()),
                    d.getIsActive() != null && d.getIsActive() ? "Active" : "Inactive",
                    d.getIsDeleted() != null && d.getIsDeleted() ? "Deleted" : "Not Deleted"
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
