package io.apexapps.dlvdatamanager.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.apexapps.dlvdatamanager.data.entity.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeIngredient {
    private String icon;
    private String name;
    private Ingredient.IngredientType type;
}
