package com.util;

public class CarDetails {
    private String variantReg;
    private String year;

    public CarDetails(String variantReg, String year) {
        this.variantReg = variantReg;
        this.year = year;
    }

    public String getVariantReg() {
        return variantReg;
    }

    public String getYear() {
        return year;
    }
}
