package io.apexapps.dlvdatamanager.views.allothercraftingitems;

import com.github.javaparser.utils.Utils;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.apexapps.dlvdatamanager.components.CraftingRecipeList;
import io.apexapps.dlvdatamanager.data.entity.CraftingItem;
import io.apexapps.dlvdatamanager.data.service.CraftingItemService;
import io.apexapps.dlvdatamanager.utils.NotificationUtil;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
@PageTitle("New Crafting Item")
@Route(value = "crafting-items/new", layout = MainLayout.class)
public class NewCraftingItemView extends Composite<VerticalLayout> {
    private static final String CRAFTING_ITEM_ICON_FORMAT = "assets/images/crafting/%s/%s.png";
    private final FormLayout form = new FormLayout();
    private final TextField name = new TextField("Name");
    private final TextField icon = new TextField("Icon");
    private final ComboBox<CraftingItem.CraftingItemType> type = new ComboBox<>("Type");
    private final Binder<CraftingItem> craftingItemBinder = new Binder<>(CraftingItem.class);
    private final CraftingRecipeList recipeList = new CraftingRecipeList();
    private final CraftingItemService craftingItemService;
    private final CraftingItem craftingItem = new CraftingItem();

    public NewCraftingItemView(CraftingItemService craftingItemService) {
        this.craftingItemService = craftingItemService;
        addClassNames("new-crafting-item-view");

        newCraftingItemView();
        buildSaveButton();
    }

    private void newCraftingItemView() {
        getContent().setHeightFull();
        icon.setReadOnly(true);

        name.addValueChangeListener(this::onNameChanged);
        type.setItems(CraftingItem.CraftingItemType.values());
        type.setItemLabelGenerator(craftingItem -> craftingItem.name);
        type.addValueChangeListener(this::onTypeChanged);

        form.add(name, icon, type);

        craftingItemBinder.bindInstanceFields(this);

        craftingItem.setCraftingRecipe(new ArrayList<>());
        recipeList.setCraftingRecipe(craftingItem.getCraftingRecipe());

        getContent().add(form, recipeList);
    }

    private void buildSaveButton() {
        Button saveButton = new Button("Save", event -> {
            try {
                if (craftingItemBinder.isValid()) {
                    craftingItem.setId(craftingItemService.count() + 1);
                    craftingItem.setCraftingRecipe(recipeList.getRecipe());
                    craftingItemBinder.writeBean(craftingItem);
                    craftingItemService.update(craftingItem);
                    NotificationUtil.showSuccess("Crafting item saved successfully");
                }
            } catch (Exception e) {
                log.error("Unable to save the crafting item", e);
                NotificationUtil.showError("Unable to save the crafting item");
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(saveButton);
    }

    private void onNameChanged(AbstractField.ComponentValueChangeEvent event) {
        final var value = String.format(
                CRAFTING_ITEM_ICON_FORMAT,
                type.getValue() == null ? "" : Utils.screamingToCamelCase(event.getValue().toString()),
                event.getValue()
        );

        icon.setValue(value);
        craftingItem.setIcon(value);
    }

    private void onTypeChanged(AbstractField.ComponentValueChangeEvent event) {
        final var value = String.format(
                CRAFTING_ITEM_ICON_FORMAT,
                Utils.screamingToCamelCase(event.getValue().toString()),
                name.getValue() == null ? "" : name.getValue()
        );

        icon.setValue(value);
        craftingItem.setIcon(value);
    }
}