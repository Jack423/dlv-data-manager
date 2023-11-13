package io.apexapps.dlvdatamanager.views.ingredients;

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
import io.apexapps.dlvdatamanager.data.entity.Ingredient;
import io.apexapps.dlvdatamanager.data.service.IngredientService;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

@Slf4j
@PageTitle("Ingredients")
@Route(value = "ingredients", layout = MainLayout.class)
public class IngredientsView extends Composite<VerticalLayout> {
    private final String INGREDIENT_EDIT_ROUTE_TEMPLATE = "ingredients/%s/edit";
    private final Grid<Ingredient> grid = new Grid<>(Ingredient.class, false);
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final Button createIngredientButton = new Button(
            "Create a New Ingredient",
            new Icon(VaadinIcon.PLUS)
    );
    private final IngredientService ingredientService;

    public IngredientsView(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
        addClassNames("ingredients-view");

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
        grid.addColumn("buyPrice").setAutoWidth(true);
        grid.addColumn("energy").setAutoWidth(true);
        grid.addColumn("growTime").setAutoWidth(true);
        grid.addColumn("ingredientType").setAutoWidth(true);
        grid.addColumn("sellPrice").setAutoWidth(true);
        grid.addColumn("water").setAutoWidth(true);
        grid.addColumn("yield").setAutoWidth(true);
        grid.addColumn("location").setAutoWidth(true);
        grid.addComponentColumn((ingredient -> {
            Button delete = new Button(new Icon(VaadinIcon.TRASH));
            delete.addClickListener(clickEvent -> {
               ingredientService.delete(ingredient.getId());
                updateGridData();
            });
            return delete;
        }));

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        updateGridData();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(INGREDIENT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                UI.getCurrent().navigate(IngredientsView.class);
            }
        });
    }

    private void updateGridData() {
        grid.setItems(query -> ingredientService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
    }

    private void setupFooter() {
        createIngredientButton.addClickListener(event -> {
            UI.getCurrent().navigate(NewIngredientView.class);
        });

        buttonLayout.setWidthFull();
        buttonLayout.addClassNames(LumoUtility.Gap.MEDIUM);
        buttonLayout.add(createIngredientButton);

        getContent().add(buttonLayout);
    }
}

