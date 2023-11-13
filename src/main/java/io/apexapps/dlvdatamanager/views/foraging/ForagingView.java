package io.apexapps.dlvdatamanager.views.foraging;

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
import io.apexapps.dlvdatamanager.data.entity.Foraging;
import io.apexapps.dlvdatamanager.data.service.ForagingService;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

@PageTitle("Foraging")
@Route(value = "foraging-items", layout = MainLayout.class)
@Slf4j
public class ForagingView extends Composite<VerticalLayout> {
    private final String FORAGING_EDIT_ROUTE_TEMPLATE = "foraging-items/%s/edit";
    private final Grid<Foraging> grid = new Grid<>(Foraging.class, false);
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final Button createForagingItemButton = new Button("Create a New Foraging Item");
    private final ForagingService foragingService;

    public ForagingView(ForagingService foragingService) {
        this.foragingService = foragingService;
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
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("type").setAutoWidth(true);
        grid.addColumn("acquisitionMethods").setAutoWidth(true);
        grid.addColumn("locations").setAutoWidth(true);
        grid.addColumn("sellPrice").setAutoWidth(true);
        grid.addComponentColumn((foragingItem) -> {
            Button delete = new Button(new Icon(VaadinIcon.TRASH));
            delete.addClickListener(event -> {
                foragingService.delete(foragingItem.getId());
                updateGridData();
            });
            return delete;
        });
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        updateGridData();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(FORAGING_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                UI.getCurrent().navigate(ForagingView.class);
            }
        });
    }

    private void updateGridData() {
        grid.setItems(query -> foragingService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
    }

    private void setupFooter() {
        createForagingItemButton.addClickListener(event -> {
            UI.getCurrent().navigate(NewForagingItem.class);
        });

        buttonLayout.setWidthFull();
        buttonLayout.addClassNames(LumoUtility.Gap.MEDIUM);
        buttonLayout.add(createForagingItemButton);

        getContent().add(buttonLayout);
    }
}
