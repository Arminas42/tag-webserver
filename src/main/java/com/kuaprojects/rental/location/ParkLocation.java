package com.kuaprojects.rental.location;

import lombok.Getter;

@Getter
public enum ParkLocation {
    PANEVEZYS("Panevėžys"),
    KEDAINIAI("Kėdainiai"),
    SIAULIAI("Šiauliai"),
    UKMERGE("Ukmergė");

    private final String name;

    ParkLocation(String name){
        this.name = name;
    }
}
