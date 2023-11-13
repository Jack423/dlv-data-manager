package io.apexapps.dlvdatamanager.views.locations;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.apexapps.dlvdatamanager.data.entity.Location;
import io.apexapps.dlvdatamanager.data.service.LocationService;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

@Slf4j
@PageTitle("Locations")
@Route(value = "locations", layout = MainLayout.class)
public class LocationsView extends Composite<VerticalLayout> {
    private final String LOCATION_EDIT_ROUTE_TEMPLATE = "locations/%s/edit";

    private final Grid<Location> grid = new Grid<>(Location.class, false);
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final Button createLocationButton = new Button("Create a New Location");
    private final LocationService locationService;

    public LocationsView(LocationService locationService) {
        this.locationService = locationService;
        addClassName("foraging-items-view");

        getContent().setHeightFull();
        getContent().setWidthFull();

        VerticalLayout mainLayout = new VerticalLayout();
        getContent().setFlexGrow(1.0, mainLayout);

        mainLayout.setWidthFull();
        mainLayout.setPadding(false);

        setupGrid();
        setupFooter();
    }

    private void setupGrid() {
        getContent().add(grid);

        grid.addColumn("id").setWidth("40px");
        grid.addColumn("name");
        grid.addComponentColumn((location) -> {
            Button delete = new Button(new Icon(VaadinIcon.TRASH));
            delete.addClickListener(event -> {
                locationService.delete(location.getId());
                updateGridData();
            });
            return delete;
        });
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        updateGridData();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(LOCATION_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                UI.getCurrent().navigate(LocationsView.class);
            }
        });
    }

    private void updateGridData() {
        grid.setItems(query -> locationService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
    }

    private void setupFooter() {
        createLocationButton.addClickListener(event -> {
            UI.getCurrent().navigate(NewLocationView.class);
        });

        buttonLayout.setWidthFull();
        buttonLayout.addClassNames(LumoUtility.Gap.MEDIUM);
        buttonLayout.add(createLocationButton);

        getContent().add(buttonLayout);
    }
}
