package io.apexapps.dlvdatamanager.data.service;

import io.apexapps.dlvdatamanager.data.RecipeIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeIngredientService implements RecipeIngredientRepository {
    private List<RecipeIngredient> ingredientsCache = new ArrayList<>();
    private final IngredientService ingredientService;

    @Override
    public List<RecipeIngredient> load() {
        if (!ingredientsCache.isEmpty()) {
            return ingredientsCache;
        }

        updateCache();

        return ingredientsCache;
    }

    @Override
    public void updateCache() {
        ingredientsCache = ingredientService.list().stream()
                .map(i -> RecipeIngredient.builder()
                        .name(i.getName())
                        .icon(i.getIcon())
                        .type(i.getIngredientType())
                        .build())
                .toList();
    }
}
