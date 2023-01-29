package com.chaosthedude.naturescompass.client;

public enum EnumOverlaySide {

    TOPLEFT,
    TOPRIGHT,
    BOTTOMLEFT,
    BOTTOMRIGHT;

    public static EnumOverlaySide fromString(String str) {
        switch (str) {
            case "TOPLEFT":
                return TOPLEFT;
            case "TOPRIGHT":
                return TOPRIGHT;
            case "BOTTOMLEFT":
                return BOTTOMLEFT;
            default:
                return BOTTOMRIGHT;
        }
    }
}
