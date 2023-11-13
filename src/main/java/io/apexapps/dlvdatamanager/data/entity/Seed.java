package io.apexapps.dlvdatamanager.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.apexapps.dlvdatamanager.data.LocationEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("seeds")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Seed {
    @Id
    private long id;
    private String growTime;
    private String icon;
    private Ingredient.IngredientType ingredientType;
    private String name;
    private LocationEnum nativeBiome;
    private Integer seedPrice;
    private Integer sellPrice;
    private Integer waterings;
    private Integer yield;
}
