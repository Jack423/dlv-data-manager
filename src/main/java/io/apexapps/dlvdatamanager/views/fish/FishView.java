package io.apexapps.dlvdatamanager.views.fish;

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
import io.apexapps.dlvdatamanager.data.entity.Fish;
import io.apexapps.dlvdatamanager.data.service.FishService;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

@PageTitle("Fish")
@Route(value = "fish", layout = MainLayout.class)
@Slf4j
public class FishView extends Composite<VerticalLayout> {
    private final String FISH_EDIT_ROUTE_TEMPLATE = "fish/%s/edit";
    private final HorizontalLayout buttonLayout = new HorizontalLayout();

    private final Grid<Fish> grid = new Grid<>(Fish.class, false);
    private final Button createFishButton = new Button("Create a New Fish");
    private final FishService fishService;

    public FishView(FishService fishService) {
        this.fishService = fishService;
        addClassNames("fish-view");

        getContent().setHeightFull();
        getContent().setWidthFull();

        VerticalLayout mainLayout = new VerticalLayout();
        getContent().setFlexGrow(1.0, mainLayout);

        mainLayout.setWidthFull();
        mainLayout.setHeightFull();

        setupGrid();
        setupFooter();
    }

    private void setupGrid() {
        getContent().add(grid);

        // Configure Grid
        grid.addColumn("id").setWidth("40px");
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("energy").setAutoWidth(true);
        grid.addColumn("locations").setAutoWidth(true)
                .addClassNames(GridVariant.LUMO_WRAP_CELL_CONTENT.toString());
        grid.addColumn("rippleColor").setAutoWidth(true);
        grid.addColumn("sellPrice").setAutoWidth(true);
        grid.addColumn("weatherCondition").setAutoWidth(true);
        grid.addComponentColumn(fish -> {
            Button delete = new Button(new Icon(VaadinIcon.TRASH));
            delete.addClickListener(event -> {
                fishService.delete(fish.getId());
                updateGridData();
            });
            return delete;
        });
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        updateGridData();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(FISH_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                UI.getCurrent().navigate(FishView.class);
            }
        });
    }

    private void updateGridData() {
        grid.setItems(query -> fishService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
    }

    private void setupFooter() {
        createFishButton.addClickListener(event -> {
            UI.getCurrent().navigate(AddFishView.class);
        });

        buttonLayout.setWidthFull();
        buttonLayout.addClassNames(LumoUtility.Gap.MEDIUM);
        buttonLayout.add(createFishButton);

        getContent().add(buttonLayout);
    }
}
