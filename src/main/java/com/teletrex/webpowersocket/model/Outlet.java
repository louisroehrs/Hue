package com.teletrex.webpowersocket.model;

import lombok.Data;

@Data
public class Outlet {
    Boolean critical;
    Boolean physical_state;
    Integer cycle_delay;
    Boolean state;
    Boolean locked;
    String name;
    Boolean transient_state;
}
