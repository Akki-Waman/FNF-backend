package com.sipl.ticket.core.helper;

import com.sipl.ticket.core.dto.response.TagResponseDto;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

public class TagExcelGenerator {

    public static void generateCsv(List<TagResponseDto> tags,
                                   HttpServletResponse response) throws Exception {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=tags.csv");

        PrintWriter writer = response.getWriter();

        // Header
        writer.println("Tag ID,Tag Name,Status");

        for (TagResponseDto t : tags) {
            writer.printf("%s,%s,%s%n",
                    t.getTagId(),
                    escapeCsv(t.getTagName()),
                    t.getIsActive() != null && t.getIsActive() ? "Active" : "Inactive"
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
