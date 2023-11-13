package io.apexapps.dlvdatamanager.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.apexapps.dlvdatamanager.data.CraftingRecipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("refined_materials")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefinedMaterial {
    private long id;
    private Integer buyPrice;
    private List<CraftingRecipe> craftingRecipe;
    private String description;
    private String icon;
    private String name;
    private Integer sellPrice;
    private String soldAt;
}
