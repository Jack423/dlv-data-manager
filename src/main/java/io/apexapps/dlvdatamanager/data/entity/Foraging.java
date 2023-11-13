package io.apexapps.dlvdatamanager.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.apexapps.dlvdatamanager.data.LocationEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document("foraging_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Foraging {
    @Id
    private long id;
    private String name;
    private String image;
    private String description;
    private Set<AcquisitionMethods> acquisitionMethods;
    private Set<LocationEnum> locations;
    private Integer sellPrice;
    private Type type;

    public enum Type {
        @JsonProperty("craftingMaterials")
        CRAFTING_MATERIALS("Crafting Materials"),
        @JsonProperty("flowers")
        FLOWERS("Flowers");

        public final String value;

        Type(String value) {
            this.value = value;
        }
    }

    public enum AcquisitionMethods {
        @JsonProperty("Digging the ground")
        DIGGING_THE_GROUND("Digging the ground"),
        @JsonProperty("Mining rock spots")
        MINING_ROCK_SPOTS("Mining rock spots"),
        @JsonProperty("Removing Night Thorns")
        REMOVING_NIGHT_THORNS("Removing night thorns"),
        @JsonProperty("Kristoff's Stall")
        KRISTOFFS_STALL("Kristoff's Stall"),
        @JsonProperty("On the ground")
        ON_THE_GROUND("On the ground"),
        @JsonProperty("Digging sparkling ground spots")
        DIGGING_SPARKLING_GROUND_SPOTS("Digging sparkling ground spots"),
        @JsonProperty("Harvesting crops")
        HARVESTING_CROPS("Harvesting crops"),
        @JsonProperty("Sea debris removal")
        SEA_DEBRIS_REMOVAL("Sea debris removal"),
        @JsonProperty("Destroying ice blocks")
        DESTROYING_ICE_BLOCKS("Destroying ice blocks"),
        @JsonProperty("Removing stone debris")
        REMOVING_STONE_DEBRIS("Removing stone debris")
        ;

        public final String value;

        AcquisitionMethods(String value) {
            this.value = value;
        }
    }
}
