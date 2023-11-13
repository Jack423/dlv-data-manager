package io.apexapps.dlvdatamanager.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.apexapps.dlvdatamanager.data.entity.Character;
import io.apexapps.dlvdatamanager.data.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class AllData {
    @JsonProperty("characters")
    private List<Character> characters;
    @JsonProperty("locations")
    private List<Location> locations;
    @JsonProperty("ingredients")
    private List<Ingredient> ingredients;
    @JsonProperty("recipes")
    private List<Meal> recipes;
    @JsonProperty("seeds")
    private List<Seed> seeds;
    @JsonProperty("fish")
    private List<Fish> fish;
    @JsonProperty("critters")
    private List<Critter> critters;
    @JsonProperty("foragingItems")
    private List<Foraging> foragingItems;
    @JsonProperty("refinedMaterials")
    private List<RefinedMaterial> refinedMaterials;
    @JsonProperty("craftingItems")
    private List<CraftingItem> craftingItems;
    @JsonProperty("gems")
    private List<Gem> gems;
}
