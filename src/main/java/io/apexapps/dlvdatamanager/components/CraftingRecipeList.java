package io.apexapps.dlvdatamanager.components;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import io.apexapps.dlvdatamanager.data.CraftingRecipe;

import java.util.List;

public class CraftingRecipeList extends Composite<VerticalLayout> {
    private List<CraftingRecipe> recipe;
    private final VerticalLayout recipeList = new VerticalLayout();

    public CraftingRecipeList() {
        buildHeader();
    }

    private void buildHeader() {
        HorizontalLayout listHeader = new HorizontalLayout();
        listHeader.setAlignItems(FlexComponent.Alignment.CENTER);
        listHeader.add(new Text("Crafting Recipe"));
        listHeader.add(new Button(new Icon(VaadinIcon.PLUS), event -> addItem()));
        getContent().add(listHeader);
    }

    public void setCraftingRecipe(List<CraftingRecipe> recipe) {
        this.recipe = recipe;
        buildList();
    }

    public List<CraftingRecipe> getRecipe() {
        return recipe;
    }

    public void buildList() {
        recipeList.setSpacing(false);
        recipeList.setPadding(false);
        getContent().add(recipeList);

        recipe.forEach(this::buildListItem);
    }

    private void buildListItem(CraftingRecipe item) {
        FormLayout formLayout = new FormLayout();
        TextField amount = new TextField("Amount");
        TextField itemName = new TextField("Item Name");
        Button delete = new Button(new Icon(VaadinIcon.TRASH), event -> removeItem(formLayout, item));

        if (item.getName() != null && item.getAmount() != null) {
            itemName.setValue(item.getName());
            amount.setValue(item.getAmount().toString());
        }

        amount.addValueChangeListener(event -> item.setAmount(Integer.valueOf(event.getValue())));
        itemName.addValueChangeListener(event -> item.setName(event.getValue()));

        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 6));
        formLayout.setColspan(amount, 1);
        formLayout.setColspan(itemName, 4);
        formLayout.setColspan(delete, 1);
        formLayout.add(amount, itemName, delete);
        recipeList.add(formLayout);
    }

    public void addItem() {
        var newRecipe = new CraftingRecipe();
        recipe.add(newRecipe);
        buildListItem(newRecipe);
    }

    private void removeItem(FormLayout item, CraftingRecipe craftingRecipe) {
        recipeList.remove(item);
        recipe.remove(craftingRecipe);
    }
}
