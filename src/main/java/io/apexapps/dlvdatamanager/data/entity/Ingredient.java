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

@Document("ingredients")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingredient {
    @Id
    private long id;
    private String name;
    private String icon;
    private String description;
    private Integer buyPrice;
    private Integer energy;
    private String growTime;
    private boolean hidden;
    private IngredientType ingredientType;
    private Integer sellPrice;
    private Integer water;
    private Integer yield;
    private Set<LocationEnum> location;

    public enum IngredientType {
        @JsonProperty("Vegetables")
        VEGETABLES("Vegetables"),
        @JsonProperty("Fruit")
        FRUIT("Fruit"),
        @JsonProperty("Grains")
        GRAINS("Grains"),
        @JsonProperty("Dairy and Oil")
        DAIRY_AND_OIL("Dairy & Oil"),
        @JsonProperty("Spices & Herbs")
        SPICES_AND_HERBS("Spices & Herbs"),
        @JsonProperty("Sweets")
        SWEETS("Sweets"),
        @JsonProperty("Ice")
        ICE("Ice"),
        @JsonProperty("Seafood")
        SEAFOOD("Seafood"),
        @JsonProperty("Fish")
        FISH("Fish"),
        @JsonProperty("Material")
        MATERIAL("Material")
        ;

        public final String name;

        IngredientType(String name) {
            this.name = name;
        }
    }
}
