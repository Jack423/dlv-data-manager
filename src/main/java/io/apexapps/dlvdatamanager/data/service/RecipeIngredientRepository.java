package io.apexapps.dlvdatamanager.data.service;

import io.apexapps.dlvdatamanager.data.RecipeIngredient;

import java.util.List;

public interface RecipeIngredientRepository {
    List<RecipeIngredient> load();
    void updateCache();
}
