package io.apexapps.dlvdatamanager.views.refinedmaterials;

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
import io.apexapps.dlvdatamanager.data.entity.RefinedMaterial;
import io.apexapps.dlvdatamanager.data.service.RefinedMaterialService;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

@Slf4j
@PageTitle("Refined Materials")
@Route(value = "refined-materials", layout = MainLayout.class)
public class RefinedMaterialsView extends Composite<VerticalLayout> {
    private final String REFINED_MATERIAL_EDIT_ROUTE_TEMPLATE = "refined-materials/%s/edit";
    private final Grid<RefinedMaterial> grid = new Grid<>(RefinedMaterial.class, false);
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final Button createRefinedMaterialButton = new Button("Create a New Refined Material");
    private final RefinedMaterialService refinedMaterialService;

    public RefinedMaterialsView(RefinedMaterialService refinedMaterialService) {
        this.refinedMaterialService = refinedMaterialService;
        addClassNames("refined-materials-view");

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
        grid.addColumn("buyPrice").setAutoWidth(true);
        grid.addColumn("sellPrice").setAutoWidth(true);
        grid.addColumn("soldAt").setAutoWidth(true);
        grid.addComponentColumn((item) -> {
            Button delete = new Button(new Icon(VaadinIcon.TRASH));
            delete.addClickListener(event -> {
                refinedMaterialService.delete(item.getId());
                updateGridData();
            });
            return delete;
        });
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        updateGridData();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(REFINED_MATERIAL_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                UI.getCurrent().navigate(RefinedMaterialsView.class);
            }
        });
    }

    private void updateGridData() {
        grid.setItems(query -> refinedMaterialService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
    }

    private void setupFooter() {
        createRefinedMaterialButton.addClickListener(event -> {
            UI.getCurrent().navigate(NewRefinedMaterial.class);
        });

        buttonLayout.setWidthFull();
        buttonLayout.addClassNames(LumoUtility.Gap.MEDIUM);
        buttonLayout.add(createRefinedMaterialButton);

        getContent().add(buttonLayout);
    }
}
