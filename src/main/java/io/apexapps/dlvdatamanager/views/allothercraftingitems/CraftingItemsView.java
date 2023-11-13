package io.apexapps.dlvdatamanager.views.allothercraftingitems;

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
import io.apexapps.dlvdatamanager.data.entity.CraftingItem;
import io.apexapps.dlvdatamanager.data.service.CraftingItemService;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

@PageTitle("Crafting Items")
@Route(value = "crafting-items", layout = MainLayout.class)
@Slf4j
public class CraftingItemsView extends Composite<VerticalLayout> {
    private final String CRAFTING_ITEM_EDIT_ROUTE_TEMPLATE = "crafting-items/%s/edit";

    private final Grid<CraftingItem> grid = new Grid<>(CraftingItem.class, false);
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final Button createCraftingItemButton = new Button("Create a new Crafting Item");
    private final CraftingItemService craftingItemService;

    public CraftingItemsView(CraftingItemService craftingItemService) {
        this.craftingItemService = craftingItemService;
        addClassNames("all-other-crafting-items-view");

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
        grid.addComponentColumn((craftingItem) -> {
            Button delete = new Button(new Icon(VaadinIcon.TRASH));
            delete.addClickListener(event -> {
                craftingItemService.delete(craftingItem.getId());
                updateGridData();
            });
            return delete;
        });
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        updateGridData();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(CRAFTING_ITEM_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                UI.getCurrent().navigate(CraftingItemsView.class);
            }
        });
    }

    private void updateGridData() {
        grid.setItems(query -> craftingItemService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
    }

    private void setupFooter() {
        createCraftingItemButton.addClickListener(event -> {
            UI.getCurrent().navigate(NewCraftingItemView.class);
        });

        buttonLayout.setWidthFull();
        buttonLayout.addClassNames(LumoUtility.Gap.MEDIUM);
        buttonLayout.add(createCraftingItemButton);

        getContent().add(buttonLayout);
    }
}
