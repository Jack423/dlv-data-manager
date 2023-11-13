package io.apexapps.dlvdatamanager.data.service;

import io.apexapps.dlvdatamanager.data.AllData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataImporterService {
    private final CharacterService characterService;
    private final CritterService critterService;
    private final FishService fishService;
    private final ForagingService foragingService;
    private final IngredientService ingredientService;
    private final MealService mealService;
    private final RefinedMaterialService refinedMaterialService;
    private final CraftingItemService craftingItemService;
    private final GemService gemService;
    private final LocationService locationService;
    private final SeedService seedService;

    public void importAll(AllData data) {
        data.getCharacters().forEach(characterService::update);
        data.getCritters().forEach(critterService::update);
        data.getFish().forEach(fishService::update);
        data.getForagingItems().forEach(foragingService::update);
        data.getIngredients().forEach(ingredientService::update);
        data.getRecipes().forEach(mealService::update);
        data.getRefinedMaterials().forEach(refinedMaterialService::update);
        data.getCraftingItems().forEach(craftingItemService::update);
        data.getGems().forEach(gemService::update);
        data.getLocations().forEach(locationService::update);
        data.getSeeds().forEach(seedService::update);
        log.info("Import was successful.");
    }
}
