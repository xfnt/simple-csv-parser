package io.github.xfnt.constant;

public final class Constants {
    public static final String CREATE_TABLE_EXCEPTION_MESSAGE_TEMPLATE = "Error creating table %s";
    public static final String INSERT_VALUES_EXCEPTION_MESSAGE = "Error inserting values";
    public static final String NO_DATA_EXCEPTION_MESSAGE = "No Data";

    private Constants() {
        throw new RuntimeException("Utils class");
    }
}
