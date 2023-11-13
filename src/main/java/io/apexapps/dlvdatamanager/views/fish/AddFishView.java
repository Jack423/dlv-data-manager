package io.apexapps.dlvdatamanager.views.fish;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.apexapps.dlvdatamanager.data.LocationEnum;
import io.apexapps.dlvdatamanager.data.entity.Fish;
import io.apexapps.dlvdatamanager.data.service.FishService;
import io.apexapps.dlvdatamanager.utils.NotificationUtil;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;

@PageTitle("Create a New Fish")
@Route(value = "fish/new", layout = MainLayout.class)
@Slf4j
public class AddFishView extends Composite<VerticalLayout> {
    private static final String FISH_ICON_FORMAT = "assets/images/fish/%s.png";
    private final FormLayout formLayout = new FormLayout();
    private final TextField name = new TextField("Name");
    private final TextField icon = new TextField("Icon");
    private final TextField description = new TextField("Description");
    private final TextField energy = new TextField("Energy");
    private final MultiSelectComboBox<LocationEnum> location = new MultiSelectComboBox<>("Location");
    private final ComboBox<Fish.RippleColor> rippleColor = new ComboBox<>("Ripple Color");
    private final TextField sellPrice = new TextField("Sell Price");
    private final TextField weatherCondition = new TextField("Weather Condition");
    private final Binder<Fish> fishBinder = new Binder<>(Fish.class);
    private final FishService fishService;
    private final Fish fish = new Fish();

    public AddFishView(FishService fishService) {
        this.fishService = fishService;

        createNewFishView();
        buildSaveButton();
    }

    private void createNewFishView() {
        getContent().setHeightFull();
        icon.setReadOnly(true);

        rippleColor.setItems(Fish.RippleColor.values());
        rippleColor.setItemLabelGenerator(rippleColor -> rippleColor.value);
        location.setItems(LocationEnum.values());
        location.setItemLabelGenerator(location -> location.value);
        name.addValueChangeListener(event -> {
            icon.setValue(String.format(
                    FISH_ICON_FORMAT,
                    name.getValue().replaceAll(" ", "_")
            ));
        });

        fishBinder.bind(location, Fish::getLocations, Fish::setLocations);

        formLayout.add(name, icon, description, energy, location, sellPrice, weatherCondition);

        getContent().add(formLayout);

        fishBinder.bindInstanceFields(this);
    }

    private void buildSaveButton() {
        Button saveButton = new Button("Save", event -> {
            try {
                if (fishBinder.isValid()) {
                    fish.setId(fishService.count() + 1);
                    fishBinder.writeBean(fish);
                    fishService.update(fish);
                    NotificationUtil.showSuccess("Fish saved successfully");
                }
            } catch (Exception e) {
                log.error("Unable to save the fish", e);
                NotificationUtil.showError("Unable to save the fish");
            }
        });

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(saveButton);
    }
}
