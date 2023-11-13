package io.apexapps.dlvdatamanager.views.seeds;

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
import io.apexapps.dlvdatamanager.data.entity.Seed;
import io.apexapps.dlvdatamanager.data.service.SeedService;
import io.apexapps.dlvdatamanager.views.MainLayout;
import org.springframework.data.domain.PageRequest;

@PageTitle("Seeds")
@Route(value = "seeds", layout = MainLayout.class)
public class SeedsView extends Composite<VerticalLayout> {
    private final String SEED_EDIT_ROUTE_TEMPLATE = "seeds/%s/edit";

    private final Grid<Seed> grid = new Grid<>(Seed.class, false);
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final Button createSeedButton = new Button("Create a new Seed");
    private final SeedService seedService;

    public SeedsView(SeedService seedService) {
        this.seedService = seedService;
        addClassNames("seeds-view");

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
        grid.addColumn("ingredientType").setAutoWidth(true);
        grid.addColumn("growTime").setAutoWidth(true);
        grid.addColumn("nativeBiome").setAutoWidth(true);
        grid.addColumn("seedPrice").setAutoWidth(true);
        grid.addColumn("sellPrice").setAutoWidth(true);
        grid.addColumn("waterings").setAutoWidth(true);
        grid.addColumn("yield").setAutoWidth(true);
        grid.addComponentColumn((foragingItem) -> {
            Button delete = new Button(new Icon(VaadinIcon.TRASH));
            delete.addClickListener(event -> {
                seedService.delete(foragingItem.getId());
                updateGridData();
            });
            return delete;
        });
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        updateGridData();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SEED_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                UI.getCurrent().navigate(SeedsView.class);
            }
        });
    }

    private void updateGridData() {
        grid.setItems(query -> seedService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
    }

    private void setupFooter() {
        createSeedButton.addClickListener(event -> {
            UI.getCurrent().navigate(NewSeedView.class);
        });

        buttonLayout.setWidthFull();
        buttonLayout.addClassNames(LumoUtility.Gap.MEDIUM);
        buttonLayout.add(createSeedButton);

        getContent().add(buttonLayout);
    }
}
