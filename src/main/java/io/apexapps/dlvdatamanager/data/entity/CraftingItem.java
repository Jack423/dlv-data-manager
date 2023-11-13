package io.apexapps.dlvdatamanager.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.apexapps.dlvdatamanager.data.CraftingRecipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("crafting_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CraftingItem {
    @Id
    private long id;
    private String name;
    private String icon;
    private List<CraftingRecipe> craftingRecipe;
    private CraftingItemType type;

    public enum CraftingItemType{
        @JsonProperty("enchantment")
        ENCHANTMENT("Enchantment"),
        @JsonProperty("landscaping")
        LANDSCAPING("Landscaping"),
        @JsonProperty("functionalItem")
        FUNCTIONAL_ITEM("Functional Item"),
        @JsonProperty("furniture")
        FURNITURE("Furniture")
        ;

        public final String name;

        CraftingItemType(String name) {
            this.name = name;
        }
    }
}
