package io.apexapps.dlvdatamanager.views.locations;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.apexapps.dlvdatamanager.data.entity.Location;
import io.apexapps.dlvdatamanager.data.service.LocationService;
import io.apexapps.dlvdatamanager.utils.NotificationUtil;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PageTitle("New Location")
@Route(value = "locations/new", layout = MainLayout.class)
public class NewLocationView extends Composite<VerticalLayout> {
    private static final String ICON_FORMAT = "assets/images/locations/%s.png";

    private final FormLayout form = new FormLayout();
    private final TextField name = new TextField("Name");
    private final TextField icon = new TextField("Icon");

    private final Binder<Location> locationBinder = new Binder<>(Location.class);
    private final LocationService locationService;
    private final Location location = new Location();

    public NewLocationView(LocationService locationService) {
        this.locationService = locationService;
        addClassName("new-location-view");

        createNewLocationView();
        buildSaveButton();
    }

    private void createNewLocationView() {
        getContent().setHeightFull();
        icon.setReadOnly(true);

        name.addValueChangeListener(this::updateIconPath);

        locationBinder.bindInstanceFields(this);

        form.add(name, icon);

        getContent().add(form);
    }

    private void updateIconPath(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        final String value = String.format(
                ICON_FORMAT,
                event.getValue().replaceAll(" ", "_")
        );

        icon.setValue(value);
        location.setIcon(value);
    }

    private void buildSaveButton() {
        Button saveButton = new Button("Save", event -> {
            try {
                if (locationBinder.isValid()) {
                    location.setId(locationService.count() + 1);
                    locationBinder.writeBean(location);
                    locationService.update(location);
                    NotificationUtil.showSuccess("New location saved successfully");
                }
            } catch (Exception e) {
                log.error("Unable to save the location", e);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(saveButton);
    }
}
