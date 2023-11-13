package io.apexapps.dlvdatamanager.views.gems;

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
import io.apexapps.dlvdatamanager.data.entity.Gem;
import io.apexapps.dlvdatamanager.data.service.GemService;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

@Slf4j
@PageTitle("Gems")
@Route(value = "gems", layout = MainLayout.class)
public class GemsView extends Composite<VerticalLayout> {

    private final String GEM_ID = "gemId";
    private final String GEM_EDIT_ROUTE_TEMPLATE = "gems/%s/edit";
    private final Grid<Gem> grid = new Grid<>(Gem.class, false);
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final Button createGemButton = new Button("Create a New Gem");
    private final GemService gemService;

    public GemsView(GemService gemService) {
        this.gemService = gemService;
        addClassNames("gems-view");

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
        grid.addColumn("locations").setAutoWidth(true);
        grid.addColumn("sellPrice").setAutoWidth(true);
        grid.addComponentColumn((gem) -> {
            Button delete = new Button(new Icon(VaadinIcon.TRASH));
            delete.addClickListener(event -> {
                gemService.delete(gem.getId());
                updateGridData();
            });
            return delete;
        });
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        updateGridData();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(GEM_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                UI.getCurrent().navigate(GemsView.class);
            }
        });
    }

    private void updateGridData() {
        grid.setItems(query -> gemService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
    }

    private void setupFooter() {
        createGemButton.addClickListener(event -> {
            UI.getCurrent().navigate(NewGemView.class);
        });

        buttonLayout.setWidthFull();
        buttonLayout.addClassNames(LumoUtility.Gap.MEDIUM);
        buttonLayout.add(createGemButton);

        getContent().add(buttonLayout);
    }
}
