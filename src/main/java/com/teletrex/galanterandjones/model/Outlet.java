package com.teletrex.galanterandjones.model;

import lombok.Data;

@Data
public class Outlet {
    Integer key;
    Boolean critical;
    Boolean physical_state;
    Integer cycle_delay;
    Boolean state;
    Boolean locked;
    String name;
    Boolean transient_state;
}
