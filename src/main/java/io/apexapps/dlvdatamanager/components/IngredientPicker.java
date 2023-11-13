package io.apexapps.dlvdatamanager.components;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.apexapps.dlvdatamanager.data.RecipeIngredient;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class IngredientPicker extends Composite<VerticalLayout> {
    private final List<RecipeIngredient> allIngredients;
    private RecipeIngredient[] ingredients = new RecipeIngredient[5];
    private final FormLayout formLayout = new FormLayout();
    private final ComboBox<RecipeIngredient> ingredient1 = new ComboBox<>();
    private final ComboBox<RecipeIngredient> ingredient2 = new ComboBox<>();
    private final ComboBox<RecipeIngredient> ingredient3 = new ComboBox<>();
    private final ComboBox<RecipeIngredient> ingredient4 = new ComboBox<>();
    private final ComboBox<RecipeIngredient> ingredient5 = new ComboBox<>();

    public IngredientPicker(List<RecipeIngredient> allIngredients) {
        this.allIngredients = allIngredients;
        this.ingredients = allIngredients.toArray(ingredients);
        buildView();
    }

    private void buildView() {
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 5)
        );

        ingredient1.setItems(allIngredients);
        ingredient2.setItems(allIngredients);
        ingredient3.setItems(allIngredients);
        ingredient4.setItems(allIngredients);
        ingredient5.setItems(allIngredients);

        ingredient1.setItemLabelGenerator(this::generateLabel);
        ingredient2.setItemLabelGenerator(this::generateLabel);
        ingredient3.setItemLabelGenerator(this::generateLabel);
        ingredient4.setItemLabelGenerator(this::generateLabel);
        ingredient5.setItemLabelGenerator(this::generateLabel);

        ingredient1.addValueChangeListener(event -> {
            ingredients[0] = event.getValue();
        });
        ingredient2.addValueChangeListener(event -> {
            ingredients[1] = event.getValue();
        });
        ingredient3.addValueChangeListener(event -> {
            ingredients[2] = event.getValue();
        });
        ingredient4.addValueChangeListener(event -> {
            ingredients[3] = event.getValue();
        });
        ingredient5.addValueChangeListener(event -> {
            ingredients[4] = event.getValue();
        });

        formLayout.add(ingredient1, ingredient2, ingredient3, ingredient4, ingredient5);

        getContent().add(formLayout);
    }

    private void setIngredients() {
        if (ingredients[0] != null) {
            ingredient1.setValue(ingredients[0]);
        }
        if (ingredients[1] != null) {
            ingredient2.setValue(ingredients[1]);
        }
        if (ingredients[2] != null) {
            ingredient3.setValue(ingredients[2]);
        }
        if (ingredients[3] != null) {
            ingredient4.setValue(ingredients[3]);
        }
        if (ingredients[4] != null) {
            ingredient5.setValue(ingredients[4]);
        }
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        if (ingredients == null) {
            this.ingredients = new RecipeIngredient[5];
        } else {
            this.ingredients = ingredients.toArray(new RecipeIngredient[5]);
        }

        setIngredients();
    }

    public List<RecipeIngredient> getIngredients() {
        return Stream.of(ingredients).filter(Objects::nonNull).toList();
    }

    private String generateLabel(RecipeIngredient ingredient) {
        return ingredient.getName();
    }
}
