package com.teletrex.webpowersocket.model;

import lombok.Data;

@Data
public class Relay {
    private String name;
    private String online;
    private String model;
    private String version;
    private Outlet[] outlets;
    private Integer sequence_delay;
    private Integer min_sequence_delay;
    private Integer cycle_delay;
    private Integer relatch;
    private String recovery_mode;
    private Boolean keypad_enabled;
    private String beep_sequence;
    private String backlight_sequence;
    private Integer lcd_columns;
    private Integer lcd_rows;
    private String[] user_lines;
}

