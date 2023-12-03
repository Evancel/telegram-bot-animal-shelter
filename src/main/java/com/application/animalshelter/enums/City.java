package com.application.animalshelter.enums;

public enum City {
    ALMATY("Almaty"),
    ARKALYK("Arkalyk"),
    ASTANA("Astana");

    private final String name;
    City(String name){
        this.name=name;
    }

    public String getName(){
        return this.name;
    }
}
