package io.apexapps.dlvdatamanager.views.ingredients;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.apexapps.dlvdatamanager.data.LocationEnum;
import io.apexapps.dlvdatamanager.data.entity.Ingredient;
import io.apexapps.dlvdatamanager.data.service.IngredientService;
import io.apexapps.dlvdatamanager.utils.NotificationUtil;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PageTitle("New Ingredient")
@Route(value = "ingredients/new", layout = MainLayout.class)
public class NewIngredientView extends Composite<VerticalLayout> {
    private final String INGREDIENT_ID = "ingredientId";
    private final String INGREDIENT_IMAGE_FORMAT = "assets/images/ingredients/%s.png";
    private final FormLayout formLayout = new FormLayout();
    private final TextField name = new TextField("Name");
    private final TextField icon = new TextField("Icon");
    private final TextArea description = new TextArea("Description");
    private final TextField buyPrice = new TextField("Buy Price");
    private final TextField energy = new TextField("Energy");
    private final TextField growTime = new TextField("Grow Time");
    private final Checkbox hidden = new Checkbox("Hidden");
    private final ComboBox<Ingredient.IngredientType> type = new ComboBox<>("Ingredient Type");
    private final TextField sellPrice = new TextField("Sell Price");
    private final TextField water = new TextField("Water");
    private final TextField yield = new TextField("Yield");
    private final MultiSelectComboBox<LocationEnum> location = new MultiSelectComboBox<>("Locations");
    private final Ingredient ingredient = new Ingredient();
    private final Binder<Ingredient> ingredientBinder = new Binder<>(Ingredient.class);
    private final IngredientService ingredientService;

    public NewIngredientView(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
        addClassName("new-ingredient-view");
        newIngredientView();
    }

    public void newIngredientView() {
        getContent().setHeightFull();
        icon.setReadOnly(true);

        name.addValueChangeListener(event -> {
            ingredient.setIcon(String.format(INGREDIENT_IMAGE_FORMAT, name.getValue()));
            icon.setValue(String.format(INGREDIENT_IMAGE_FORMAT, name.getValue()));
        });
        type.setItems(Ingredient.IngredientType.values());
        type.setItemLabelGenerator(type -> type.name);
        location.setItems(LocationEnum.values());
        location.setItemLabelGenerator(location -> location.value);
        growTime.setHelperText("Use the format H:M:S");

        formLayout.add(
                hidden,
                name,
                icon,
                description,
                buyPrice,
                energy,
                growTime,
                type,
                sellPrice,
                water,
                yield,
                location
        );

        getContent().add(formLayout);

        ingredientBinder.bindInstanceFields(this);

        ingredientBinder.bind(type, Ingredient::getIngredientType, Ingredient::setIngredientType);
        ingredientBinder.bind(location, Ingredient::getLocation, Ingredient::setLocation);
        ingredientBinder.forField(growTime)
                .withValidator(growTime ->
                                growTime.matches("(\\d\\d)+:+(\\d\\d)+:+(\\d\\d)"),
                        "The grow time must match the format H:M:S")
                .bind(Ingredient::getGrowTime, Ingredient::setGrowTime);
        ingredientBinder.readBean(ingredient);

        Button saveButton = new Button("Save", event -> {
            try {
                ingredientBinder.validate();
                if (ingredientBinder.isValid()) {
                    ingredient.setId(ingredientService.count() + 1);
                    ingredientBinder.writeBean(ingredient);
                    ingredientService.update(ingredient);
                    NotificationUtil.showSuccess("Ingredient saved successfully");
                }
            } catch (Exception e) {
                log.error("Unable to save the ingredient", e);
                NotificationUtil.showError("Unable to save the ingredient");
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(saveButton);
    }
}
