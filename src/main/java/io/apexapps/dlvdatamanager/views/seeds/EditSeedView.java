package io.apexapps.dlvdatamanager.views.seeds;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.apexapps.dlvdatamanager.data.LocationEnum;
import io.apexapps.dlvdatamanager.data.entity.Ingredient;
import io.apexapps.dlvdatamanager.data.entity.Seed;
import io.apexapps.dlvdatamanager.data.service.SeedService;
import io.apexapps.dlvdatamanager.utils.NotificationUtil;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@PageTitle("Edit Seed")
@Route(value = "seeds/:seedId/edit", layout = MainLayout.class)
public class EditSeedView extends Composite<VerticalLayout> implements BeforeEnterObserver {
    private static final String SEED_ID = "seedId";
    private static final String ICON_FORMAT = "assets/images/seeds/%s.png";

    private final FormLayout form = new FormLayout();
    private final TextField name = new TextField("Name");
    private final TextField icon = new TextField("Icon");
    private final ComboBox<Ingredient.IngredientType> ingredientType = new ComboBox<>("Type");
    private final TextField growTime = new TextField("Grow Time");
    private final ComboBox<LocationEnum> nativeBiome = new ComboBox<>("Native Biome");
    private final TextField seedPrice = new TextField("Seed Price");
    private final TextField sellPrice = new TextField("Sell Price");
    private final TextField waterings = new TextField("Waterings");
    private final TextField yield = new TextField("Yield");
    private final Binder<Seed> seedBinder = new Binder<>(Seed.class);
    private final SeedService seedService;
    private Seed seed;

    public EditSeedView(SeedService seedService) {
        this.seedService = seedService;
        addClassName("edit-seed-view");

        getContent().add(new Button("Back", new Icon(VaadinIcon.ARROW_LEFT), event -> UI
                .getCurrent()
                .navigate(SeedsView.class)
        ));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> seedId = event.getRouteParameters().get(SEED_ID).map(Long::parseLong);

        if (seedId.isPresent()) {
            Optional<Seed> seedFromBackend = seedService.get(seedId.get());

            if (seedFromBackend.isPresent()) {
                populateForm(seedFromBackend.get());
                createEditSeedView();
                buildSaveButton();
            } else {
                NotificationUtil.showError(
                        String.format("The requested seed was not found, ID = %s", seedId.get())
                );
                event.forwardTo(SeedsView.class);
            }
        }
    }

    private void populateForm(Seed seed) {
        this.seed = seed;
        seedBinder.setBean(seed);
    }

    private void createEditSeedView() {
        getContent().setHeightFull();
        icon.setReadOnly(true);

        name.addValueChangeListener(this::updateIconPath);
        ingredientType.setItems(Ingredient.IngredientType.values());
        ingredientType.setItemLabelGenerator(item -> item.name);
        nativeBiome.setItems(LocationEnum.values());
        nativeBiome.setItemLabelGenerator(location -> location.value);

        seedBinder.bindInstanceFields(this);

        form.add(name, ingredientType, growTime, nativeBiome, seedPrice, sellPrice, waterings, yield);

        getContent().add(form);
    }

    private void updateIconPath(AbstractField.ComponentValueChangeEvent event) {
        final String value = String.format(
                ICON_FORMAT,
                name.getValue().replaceAll(" ", "_")
        );

        icon.setValue(value);
        seed.setIcon(value);
    }

    private void buildSaveButton() {
        Button saveButton = new Button("Save", event -> {
            try {
                if (seedBinder.isValid()) {
                    seedBinder.writeBean(seed);
                    seedService.update(seed);
                    NotificationUtil.showSuccess("Seed saved successfully");
                }
            } catch (Exception e) {
                log.error("Unable to save the seed", e);
                NotificationUtil.showError("Unable to save the seed");
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(saveButton);
    }
}
