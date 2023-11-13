package io.apexapps.dlvdatamanager.views.ingredients;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.apexapps.dlvdatamanager.data.LocationEnum;
import io.apexapps.dlvdatamanager.data.entity.Ingredient;
import io.apexapps.dlvdatamanager.data.service.IngredientService;
import io.apexapps.dlvdatamanager.utils.NotificationUtil;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@PageTitle("Edit Ingredient")
@Route(value = "ingredients/:ingredientId?/edit", layout = MainLayout.class)
public class EditIngredientView extends Composite<VerticalLayout> implements BeforeEnterObserver {
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

    private Ingredient ingredient;
    private final Binder<Ingredient> ingredientBinder = new Binder<>(Ingredient.class);
    private final IngredientService ingredientService;

    public EditIngredientView(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
        addClassNames("edit-ingredient-view");

        getContent().add(new Button("Back", new Icon(VaadinIcon.ARROW_LEFT), event -> UI
                .getCurrent().navigate(IngredientsView.class)));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> ingredientId = event.getRouteParameters().get(INGREDIENT_ID).map(Long::parseLong);

        if (ingredientId.isPresent()) {
            Optional<Ingredient> ingredientFromBackend = ingredientService.get(ingredientId.get());
            if (ingredientFromBackend.isPresent()) {
                createEditIngredientView();
                populateForm(ingredientFromBackend.get());
            } else {
                NotificationUtil.showError("The requested ingredient was not found, ID = " + ingredientId);
                event.forwardTo(IngredientsView.class);
            }
        }
    }

    private void createEditIngredientView() {
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
        ingredientBinder.forField(growTime)
                .withValidator(growTime ->
                                growTime.matches("(\\d\\d)+:+(\\d\\d)+:+(\\d\\d)"),
                        "The grow time must match the format H:M:S")
                .bind(Ingredient::getGrowTime, Ingredient::setGrowTime);

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

        ingredientBinder.bind(type, Ingredient::getIngredientType, Ingredient::setIngredientType);
        ingredientBinder.bind(location, Ingredient::getLocation, Ingredient::setLocation);
        ingredientBinder.bindInstanceFields(this);
        ingredientBinder.readBean(ingredient);

        Button saveButton = new Button("Save", event -> {
            try {
                if (ingredientBinder.isValid()) {
                    ingredientService.update(ingredient);
                    NotificationUtil.showSuccess("Ingredient item saved successfully");
                }
            } catch (Exception e) {
                log.error("Unable to save the ingredient", e);
                NotificationUtil.showError("Unable to save the ingredient");
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(saveButton);
    }

    private void populateForm(Ingredient ingredient) {
        this.ingredient = ingredient;
        ingredientBinder.setBean(ingredient);
    }
}
