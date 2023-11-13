package io.apexapps.dlvdatamanager.views.locations;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import io.apexapps.dlvdatamanager.data.entity.Location;
import io.apexapps.dlvdatamanager.data.service.LocationService;
import io.apexapps.dlvdatamanager.utils.NotificationUtil;
import io.apexapps.dlvdatamanager.views.MainLayout;
import io.apexapps.dlvdatamanager.views.foraging.ForagingView;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@PageTitle("Edit Location")
@Route(value = "locations/:locationId/edit", layout = MainLayout.class)
public class EditLocationView extends Composite<VerticalLayout> implements BeforeEnterObserver {
    private static final String LOCATION_ID = "locationId";
    private static final String ICON_FORMAT = "assets/images/locations/%s.png";

    private final FormLayout form = new FormLayout();
    private final TextField name = new TextField("Name");
    private final TextField icon = new TextField("Icon");

    private final Binder<Location> locationBinder = new Binder<>(Location.class);
    private final LocationService locationService;
    private Location location;

    public EditLocationView(LocationService locationService) {
        this.locationService = locationService;
        addClassName("edit-location-view");

        getContent().add(new Button("Back", new Icon(VaadinIcon.ARROW_LEFT), event -> UI
                .getCurrent()
                .navigate(LocationsView.class)
        ));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> locationId = event.getRouteParameters().get(LOCATION_ID).map(Long::parseLong);

        if (locationId.isPresent()) {
            Optional<io.apexapps.dlvdatamanager.data.entity.Location> refinedMaterialFromBackend = locationService.get(locationId.get());

            if (refinedMaterialFromBackend.isPresent()) {
                populateForm(refinedMaterialFromBackend.get());
                createEditLocationView();
                buildSaveButton();
            } else {
                NotificationUtil.showError(
                        String.format("The requested location was not found, ID = %s", locationId.get())
                );
                event.forwardTo(ForagingView.class);
            }
        }
    }

    private void populateForm(Location location) {
        this.location = location;
        locationBinder.setBean(location);
    }

    private void createEditLocationView() {
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
                    locationService.update(location);
                    NotificationUtil.showSuccess("Location saved successfully");
                }
            } catch (Exception e) {
                log.error("Unable to save the location", e);
                NotificationUtil.showError("Unable to save the location");
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(saveButton);
    }
}
