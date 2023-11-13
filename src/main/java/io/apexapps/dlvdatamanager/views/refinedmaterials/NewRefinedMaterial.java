package io.apexapps.dlvdatamanager.views.refinedmaterials;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.apexapps.dlvdatamanager.components.CraftingRecipeList;
import io.apexapps.dlvdatamanager.data.entity.RefinedMaterial;
import io.apexapps.dlvdatamanager.data.service.RefinedMaterialService;
import io.apexapps.dlvdatamanager.utils.NotificationUtil;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
@PageTitle("New Refined Material")
@Route(value = "refined-materials/new", layout = MainLayout.class)
public class NewRefinedMaterial extends Composite<VerticalLayout> {
    private static final String REFINED_MATERIAL_ID = "refinedMaterialId";
    private static final String ICON_FORMAT = "assets/images/crafting/refinedMaterials/%s.png";
    private final FormLayout form = new FormLayout();
    private final TextField name = new TextField("Name");
    private final TextField icon = new TextField("Icon");
    private final TextArea description = new TextArea("Description");
    private final TextField buyPrice = new TextField("Buy Price");
    private final TextField sellPrice = new TextField("Sell Price");
    private final TextField soldAt = new TextField("Sold At");

    private final Binder<RefinedMaterial> refinedMaterialBinder = new Binder<>(RefinedMaterial.class);
    private final CraftingRecipeList recipeList = new CraftingRecipeList();
    private final RefinedMaterialService refinedMaterialService;
    private final RefinedMaterial refinedMaterial = new RefinedMaterial();

    public NewRefinedMaterial(RefinedMaterialService refinedMaterialService) {
        this.refinedMaterialService = refinedMaterialService;
        addClassName("new-refined-material-view");

        createNewRefinedMaterialView();
        buildSaveButton();
    }

    private void createNewRefinedMaterialView() {
        getContent().setHeightFull();
        icon.setReadOnly(true);

        name.addValueChangeListener(this::updateIconPath);

        refinedMaterialBinder.bindInstanceFields(this);

        form.add(name, icon, description, buyPrice, sellPrice, soldAt);

        refinedMaterial.setCraftingRecipe(new ArrayList<>());
        recipeList.setCraftingRecipe(refinedMaterial.getCraftingRecipe());

        getContent().add(form, recipeList);
    }

    private void updateIconPath(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        final String value = String.format(
                ICON_FORMAT,
                event.getValue().replaceAll(" ", "_")
        );

        icon.setValue(value);
        refinedMaterial.setIcon(value);
    }

    private void buildSaveButton() {
        Button saveButton = new Button("Save", event -> {
            try {
                if (refinedMaterialBinder.isValid()) {
                    refinedMaterial.setId(refinedMaterialService.count() + 1);
                    refinedMaterial.setCraftingRecipe(recipeList.getRecipe());
                    refinedMaterialBinder.writeBean(refinedMaterial);
                    refinedMaterialService.update(refinedMaterial);
                    NotificationUtil.showSuccess("Refined material saved successfully");
                }
            } catch (Exception e) {
                log.error("Unable to save the refined material", e);
                NotificationUtil.showError("Unable to save the refined material");
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(saveButton);
    }
}
