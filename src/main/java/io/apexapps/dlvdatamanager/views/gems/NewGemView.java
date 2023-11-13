package io.apexapps.dlvdatamanager.views.gems;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.apexapps.dlvdatamanager.data.LocationEnum;
import io.apexapps.dlvdatamanager.data.entity.Gem;
import io.apexapps.dlvdatamanager.data.service.GemService;
import io.apexapps.dlvdatamanager.utils.NotificationUtil;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PageTitle("New Gem")
@Route(value = "gems/new", layout = MainLayout.class)
public class NewGemView extends Composite<VerticalLayout> {
    private static final String ICON_FORMAT = "assets/images/gems/%s.png";

    private final FormLayout form = new FormLayout();
    private final TextField name = new TextField("Name");
    private final TextField icon = new TextField("Icon");
    private final MultiSelectComboBox<LocationEnum> locations = new MultiSelectComboBox<>("Locations");
    private final TextField sellPrice = new TextField("Sell Price");
    private final Binder<Gem> gemBinder = new Binder<>(Gem.class);
    private final GemService gemService;
    private final Gem gem = new Gem();

    public NewGemView(GemService gemService) {
        this.gemService = gemService;
        addClassName("new-gem-view");

        createNewGemView();
        buildSaveButton();
    }

    private void createNewGemView() {
        getContent().setHeightFull();
        icon.setReadOnly(true);

        name.addValueChangeListener(this::updateIconPath);
        locations.setItems(LocationEnum.values());
        locations.setItemLabelGenerator(location -> location.value);

        gemBinder.bindInstanceFields(this);

        form.add(name, icon, locations, sellPrice);

        getContent().add(form);
    }

    private void updateIconPath(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        final String value = String.format(
                ICON_FORMAT,
                event.getValue().replaceAll(" ", "_")
        );

        icon.setValue(value);
        gem.setIcon(value);
    }

    private void buildSaveButton() {
        Button saveButton = new Button("Save", event -> {
            try {
                if (gemBinder.isValid()) {
                    gem.setId(gemService.count() + 1);
                    gemBinder.writeBean(gem);
                    gemService.update(gem);
                    NotificationUtil.showSuccess("New gem saved successfully");
                }
            } catch (Exception e) {
                log.error("Unable to save the gem", e);
                NotificationUtil.showError("Unable to save the gem");
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(saveButton);
    }
}
