package io.apexapps.dlvdatamanager.components.quest_required_item;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import io.apexapps.dlvdatamanager.data.entity.Character;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class QuestRequiredItems extends Composite<VerticalLayout> {
    private final List<Character.QuestItem> items;
    private final TextField name = new TextField("Name");
    private final TextField amount = new TextField("Amount");
    private final TextArea additionalInformation = new TextArea("Additional Information");
    private final Span detailsLabel = new Span();
    private final VerticalLayout questVerticalLayout = new VerticalLayout();
    private final Binder<Character.QuestItem> selectedItemBinder = new Binder<>(Character.QuestItem.class);
    private Character.QuestItem selectedItem = new Character.QuestItem();

    public QuestRequiredItems() {
        this.items = new ArrayList<>();

        name.setMinWidth("400px");
        amount.setMinWidth("400px");
        additionalInformation.setMinWidth("400px");

        getContent().setPadding(false);

        buildRequiredItemDetails();
    }

    public QuestRequiredItems(List<Character.QuestItem> items) {
        this.items = Objects.requireNonNullElseGet(items, ArrayList::new);

        name.setMinWidth("400px");
        amount.setMinWidth("400px");
        additionalInformation.setMinWidth("400px");

        getContent().setPadding(false);

        buildRequiredItemDetails();
    }

    @SneakyThrows
    public void buildRequiredItemDetails() {
        questVerticalLayout.setSpacing(false);

        Button addQuestItemButton = new Button(
                new Icon(VaadinIcon.PLUS),
                clickEvent -> {
                    Dialog dialog = new Dialog();
                    dialog.setHeaderTitle("Add Quest Item");
                    buildDialogContent(dialog, null);
                    dialog.open();
                }
        );

        HorizontalLayout summary = new HorizontalLayout(
                detailsLabel,
                addQuestItemButton
        );
        summary.setAlignItems(FlexComponent.Alignment.CENTER);
        Details details = new Details(summary, questVerticalLayout);
        details.addThemeVariants(DetailsVariant.FILLED);
        details.setWidth("100%");
        updateDetails();

        getContent().add(details);
    }

    private void buildDialogContent(Dialog dialog, Character.QuestItem questItem) {
        boolean batchModeEnabled = false;

        if (questItem != null) {
            name.setValue(questItem.getName());
            amount.setValue(questItem.getAmount().toString());
            additionalInformation.setValue(questItem.getAdditionalInformation());
        }

        selectedItemBinder
                .forField(name)
                .withValidator(name -> name != null && !name.isBlank(), "This field is required")
                .bind(Character.QuestItem::getName, Character.QuestItem::setName);
        selectedItemBinder
                .forField(amount)
                .withValidator(amount -> amount != null && !amount.isBlank(), "This field is required")
                .withConverter(new StringToIntegerConverter("Must be a number"))
                .bind(Character.QuestItem::getAmount, Character.QuestItem::setAmount);
        selectedItemBinder
                .forField(additionalInformation)
                .bind(Character.QuestItem::getAdditionalInformation, Character.QuestItem::setAdditionalInformation);

        VerticalLayout dialogLayout = new VerticalLayout(name, amount, additionalInformation);
        dialogLayout.setSpacing(false);
        dialogLayout.setPadding(false);

        Checkbox batchModeCheckbox = new Checkbox("Add another item", batchModeEnabled);
        Button save = new Button("Save", event -> {
            try {
                selectedItemBinder.writeBean(selectedItem);
                if (items.contains(questItem)) {
                    int oldItem = items.indexOf(questItem);
                    items.set(oldItem, selectedItem);
                } else {
                    items.add(selectedItem);
                }
                updateDetails();
                if (!batchModeCheckbox.getValue()) {
                    clearForm();
                    dialog.close();
                } else {
                    clearForm();
                    name.focus();
                }
            } catch (ValidationException e) {
                log.error(e.getMessage());
            }
        });
        Button cancel = new Button("Cancel", event -> dialog.close());

        dialog.add(dialogLayout);
        dialog.getFooter().add(batchModeCheckbox, save, cancel);
    }

    public void setItems(List<Character.QuestItem> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public void updateDetails() {
        questVerticalLayout.removeAll();
        items.forEach(requiredItem -> {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            Button questItemButton = new Button(
                    requiredItem.getName() + " (" + requiredItem.getAmount() + ")",
                    clickEvent -> {
                        Dialog dialog = new Dialog();
                        dialog.setHeaderTitle("Edit Quest");
                        buildDialogContent(dialog, requiredItem);
                        dialog.open();
                    }
            );
            Button deleteQuestItemButton = new Button(new Icon(VaadinIcon.TRASH), event -> {
                removeQuestItem(requiredItem);
            });

            horizontalLayout.setWidthFull();
            horizontalLayout.setPadding(false);
            horizontalLayout.setFlexGrow(1, questItemButton);
            deleteQuestItemButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON);
            questItemButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            horizontalLayout.add(questItemButton, deleteQuestItemButton);
            questVerticalLayout.add(horizontalLayout);
        });
        detailsLabel.setText("Required Items (" + items.size() + ")");
    }

    private void clearForm() {
        name.clear();
        amount.clear();
        additionalInformation.clear();
        this.selectedItem = new Character.QuestItem();
    }

    public void removeQuestItem(Character.QuestItem item) {
        items.remove(item);
        updateDetails();
    }

    public List<Character.QuestItem> getItems() {
        return items;
    }
}
