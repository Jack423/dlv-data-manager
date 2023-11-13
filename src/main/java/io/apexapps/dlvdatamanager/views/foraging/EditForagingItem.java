package io.apexapps.dlvdatamanager.views.foraging;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.apexapps.dlvdatamanager.data.LocationEnum;
import io.apexapps.dlvdatamanager.data.entity.Foraging;
import io.apexapps.dlvdatamanager.data.service.ForagingService;
import io.apexapps.dlvdatamanager.utils.NotificationUtil;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@PageTitle("Edit Foraging Item")
@Route(value = "foraging-items/:foragingItemId/edit", layout = MainLayout.class)
public class EditForagingItem extends Composite<VerticalLayout> implements BeforeEnterObserver {
    private static final String FORAGING_ITEM_ID = "foragingItemId";
    private static final String FORAGING_ITEM_ICON_FORMAT = "assets/images/foraging/%s/%s.png";
    private Foraging foragingItem;
    private final FormLayout formLayout = new FormLayout();
    private final TextField name = new TextField("Name");
    private final TextField icon = new TextField("Icon");
    private final TextArea description = new TextArea("Description");
    private final ComboBox<Foraging.Type> type = new ComboBox<>("Type");
    private final TextField sellPrice = new TextField("Sell Price");
    private final MultiSelectComboBox<Foraging.AcquisitionMethods> acquisitionMethods = new MultiSelectComboBox<>("Acquisition Methods");
    private final MultiSelectComboBox<LocationEnum> locations = new MultiSelectComboBox<>("Locations");
    private final Binder<Foraging> foragingItemBinder = new Binder<>(Foraging.class);
    private final ForagingService foragingService;

    public EditForagingItem(ForagingService foragingService) {
        this.foragingService = foragingService;
        addClassName("edit-foraging-item-view");

        getContent().add(new Button("Back", new Icon(VaadinIcon.ARROW_LEFT), event -> UI
                .getCurrent()
                .navigate(ForagingView.class)
        ));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> foragingItemId = event.getRouteParameters().get(FORAGING_ITEM_ID).map(Long::parseLong);

        if (foragingItemId.isPresent()) {
            Optional<Foraging> foragingItemFromBackend = foragingService.get(foragingItemId.get());

            if (foragingItemFromBackend.isPresent()) {
                populateForm(foragingItemFromBackend.get());
                createEditForagingItemView();
            } else {
                NotificationUtil.showError(
                        String.format("The requested foraging item was not found, ID = %s", foragingItemId.get())
                );
                event.forwardTo(ForagingView.class);
            }
        }
    }

    private void populateForm(Foraging value) {
        this.foragingItem = value;
        foragingItemBinder.setBean(value);
    }

    private void createEditForagingItemView() {
        getContent().setHeightFull();
        icon.setReadOnly(true);

        acquisitionMethods.setItems(Foraging.AcquisitionMethods.values());
        acquisitionMethods.setItemLabelGenerator(method -> method.value);
        locations.setItems(LocationEnum.values());
        locations.setItemLabelGenerator(location -> location.value);
        type.setItems(Foraging.Type.values());
        type.setItemLabelGenerator(type -> type.value);
        type.addValueChangeListener(this::updateIconPath);
        name.addValueChangeListener(this::updateIconPath);

        formLayout.add(name, icon, description, acquisitionMethods, locations, sellPrice, type);

        getContent().add(formLayout);

        foragingItemBinder.bindInstanceFields(this);
        foragingItemBinder.readBean(foragingItem);

        Button saveButton = new Button("Save", event -> {
            try {
                if (foragingItemBinder.isValid()) {
                    foragingService.update(foragingItem);
                    NotificationUtil.showSuccess("Foraging item saved successfully");
                }
            } catch (Exception e) {
                log.error("Unable to save the foraging item", e);
                NotificationUtil.showError("Unable to save the foraging item");
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(saveButton);
    }

    private void updateIconPath(AbstractField.ComponentValueChangeEvent event) {
        final String value = String.format(
                FORAGING_ITEM_ICON_FORMAT,
                event.getValue().equals(Foraging.Type.CRAFTING_MATERIALS) ? "crafting_materials" : "flowers",
                name.getValue().replaceAll(" ", "_")
        );

        icon.setValue(value);
        foragingItem.setImage(value);
    }
}
