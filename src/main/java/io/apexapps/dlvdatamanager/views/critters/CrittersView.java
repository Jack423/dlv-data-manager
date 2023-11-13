package io.apexapps.dlvdatamanager.views.critters;

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
import io.apexapps.dlvdatamanager.data.entity.Critter;
import io.apexapps.dlvdatamanager.data.service.CritterService;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

@PageTitle("Critters")
@Route(value = "critters", layout = MainLayout.class)
@Slf4j
public class CrittersView extends Composite<VerticalLayout> {
    private final String CRITTER_EDIT_ROUTE_TEMPLATE = "critters/%s/edit";
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final Grid<Critter> grid = new Grid<>(Critter.class, false);
    private final Button createCritterButton = new Button("Create a New Critter", new Icon(VaadinIcon.PLUS));
    private final CritterService critterService;

    public CrittersView(CritterService critterService) {
        this.critterService = critterService;
        addClassNames("critters-view");

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

        // Configure Grid
        grid.addColumn("id").setWidth("40px");
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("type").setAutoWidth(true);
        grid.addColumn("likedFood").setAutoWidth(true);
        grid.addColumn("favoriteFood").setAutoWidth(true);
        grid.addColumn("location").setAutoWidth(true);
        grid.addComponentColumn((critter) -> {
            Button delete = new Button(new Icon(VaadinIcon.TRASH));
            delete.addClickListener(event -> {
                critterService.delete(critter.getId());
                updateGridData();
            });
            return delete;
        });
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        updateGridData();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(CRITTER_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                UI.getCurrent().navigate(CrittersView.class);
            }
        });
    }

    private void updateGridData() {
        grid.setItems(query -> critterService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
    }

    private void setupFooter() {
        createCritterButton.addClickListener(event -> {
            UI.getCurrent().navigate(AddCritterView.class);
        });

        buttonLayout.setWidthFull();
        buttonLayout.addClassNames(LumoUtility.Gap.MEDIUM);
        buttonLayout.add(createCritterButton);

        getContent().add(buttonLayout);
    }
}
