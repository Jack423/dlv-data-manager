package io.apexapps.dlvdatamanager.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum LocationEnum {
    @JsonProperty("Plaza")
    PLAZA("Plaza"),
    @JsonProperty("Peaceful Meadow")
    PEACEFUL_MEADOW("Peaceful Meadow"),
    @JsonProperty("Sunlit Plateau")
    SUNLIT_PLATEAU("Sunlit Plateau"),
    @JsonProperty("Dazzle Beach")
    DAZZLE_BEACH("Dazzle Beach"),
    @JsonProperty("Forest of Valor")
    FOREST_OF_VALOR("Forest of Valor"),
    @JsonProperty("Glade of Trust")
    GLADE_OF_TRUST("Glade of Trust"),
    @JsonProperty("Frosted Heights")
    FROSTED_HEIGHTS("Frosted Heights"),
    @JsonProperty("Forgotten Lands")
    FORGOTTEN_LANDS("Forgotten Lands"),
    @JsonProperty("Vitalys Mine")
    VITALYS_MINE("Vitalys Mine"),
    @JsonProperty("Everywhere")
    EVERYWHERE("Everywhere")
    ;

    public final String value;

    LocationEnum(String name) {
        this.value = name;
    }
}
