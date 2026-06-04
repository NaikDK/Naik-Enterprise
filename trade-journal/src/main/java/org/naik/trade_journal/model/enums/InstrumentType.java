package org.naik.trade_journal.model.enums;

public enum InstrumentType {
    OPTION("Option", 100),
    STOCK("Stock", 1),
    FUTURES("Futures", 1);

    private final String displayName;
    private final int multiplier;

        InstrumentType(String displayName, int multiplier) {
        this.displayName = displayName;
        this.multiplier = multiplier;
    }

    public String getDisplayName() { return displayName; }
    public int getMultiplier() { return multiplier; }

}
