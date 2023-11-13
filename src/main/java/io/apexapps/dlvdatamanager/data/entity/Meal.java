package io.apexapps.dlvdatamanager.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.apexapps.dlvdatamanager.data.RecipeIngredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("meals")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meal {
    @Id
    private long id;
    private String name;
    private String icon;
    private RecipeType recipeType;
    private Integer stars;
    private String description;
    private Integer energy;
    private List<RecipeIngredient> ingredients;
    private Integer sellPrice;

    public enum RecipeType {
        @JsonProperty("Appetizers")
        APPETIZERS("Appetizers"),
        @JsonProperty("Entrées")
        ENTREES("Entrées"),
        @JsonProperty("Desserts")
        DESSERTS("Desserts")
        ;

        public final String name;

        RecipeType(String name) {
            this.name = name;
        }
    }
}
