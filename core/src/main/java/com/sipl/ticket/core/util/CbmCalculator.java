package com.sipl.ticket.core.util;

import org.springframework.stereotype.Component;

@Component
public class CbmCalculator {
    public Double calculateCbm(Double length, Double width, Double height) {
        if (length == null || width == null || height == null) {
            return null;
        }
        return length * width * height;
    }
}