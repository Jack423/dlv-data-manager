package io.apexapps.dlvdatamanager.views.fish;


import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.apexapps.dlvdatamanager.data.LocationEnum;
import io.apexapps.dlvdatamanager.data.entity.Fish;
import io.apexapps.dlvdatamanager.data.service.FishService;
import io.apexapps.dlvdatamanager.utils.NotificationUtil;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@PageTitle("Edit Fish")
@Route(value = "fish/:fishId?/edit", layout = MainLayout.class)
@Slf4j
public class EditFishView extends Composite<VerticalLayout> implements BeforeEnterObserver {
    private static final String FISH_ID = "fishId";
    private static final String FISH_ICON_FORMAT = "assets/images/fish/%s.png";
    private final FormLayout formLayout = new FormLayout();
    private final TextField name = new TextField("Name");
    private final TextField icon = new TextField("Icon");
    private final TextField description = new TextField("Description");
    private final TextField energy = new TextField("Energy");
    private final MultiSelectComboBox<LocationEnum> locations = new MultiSelectComboBox<>("Location");
    private final ComboBox<Fish.RippleColor> rippleColor = new ComboBox<>("Ripple Color");
    private final TextField sellPrice = new TextField("Sell Price");
    private final TextField weatherCondition = new TextField("Weather Condition");
    private final Binder<Fish> fishBinder = new Binder<>(Fish.class);
    private final FishService fishService;
    private Fish fish;

    public EditFishView(FishService fishService) {
        this.fishService = fishService;
        addClassNames("edit-fish-view");

        getContent().add(new Button("Back", new Icon(VaadinIcon.ARROW_LEFT), event -> UI
                .getCurrent().navigate(FishView.class)));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> fishId = event.getRouteParameters().get(FISH_ID).map(Long::parseLong);

        if (fishId.isPresent()) {
            Optional<Fish> fishFromBackend = fishService.get(fishId.get());
            if (fishFromBackend.isPresent()) {
                this.fish = fishFromBackend.get();
                fishBinder.setBean(fishFromBackend.get());
                buildEditFishView();
                buildSaveButtonView();
            } else {
                NotificationUtil.showError(String.format(
                        "The requested fish was not found, ID = %s",
                        fishId.get()
                ));
                event.forwardTo(FishView.class);
            }
        }
    }

    private void buildEditFishView() {
        icon.setReadOnly(true);

        locations.setItems(LocationEnum.values());
        locations.setItemLabelGenerator(location -> location.value);
        rippleColor.setItems(Fish.RippleColor.values());
        rippleColor.setItemLabelGenerator(rippleColor -> rippleColor.value);
        name.addValueChangeListener(event -> {
            icon.setValue(String.format(
                    FISH_ICON_FORMAT,
                    name.getValue().replaceAll(" ", "_")
            ));
        });
        formLayout.add(name, icon, description, energy, locations, rippleColor, sellPrice, weatherCondition);

        getContent().add(formLayout);

        fishBinder.bind(name, Fish::getName, Fish::setName);
        fishBinder.bind(icon, Fish::getIcon, Fish::setIcon);
        fishBinder.bind(description, Fish::getDescription, Fish::setDescription);
        fishBinder.forField(energy)
                .withConverter(new StringToIntegerConverter("Not a number"))
                .bind(Fish::getEnergy, Fish::setEnergy);
        fishBinder.bind(locations, Fish::getLocations, Fish::setLocations);
        fishBinder.bind(rippleColor, Fish::getRippleColor, Fish::setRippleColor);
        fishBinder.forField(sellPrice)
                .withConverter(new StringToIntegerConverter("Not a number"))
                .bind(Fish::getSellPrice, Fish::setSellPrice);
        fishBinder.bind(weatherCondition, Fish::getWeatherCondition, Fish::setWeatherCondition);
    }

    private void buildSaveButtonView() {
        HorizontalLayout buttonContainer = new HorizontalLayout();
        Button save = new Button("Save", event -> {
            try {
                if (fishBinder.isValid()) {
                    fishService.update(fish);
                    NotificationUtil.showSuccess("Fish updated successfully");
                }
            } catch (Exception e) {
                log.error("Unable to validate the entered information", e);
            }
        });

        buttonContainer.setWidthFull();
        buttonContainer.add(save);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(buttonContainer);
    }
}
