package io.apexapps.dlvdatamanager.views.gems;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.apexapps.dlvdatamanager.data.LocationEnum;
import io.apexapps.dlvdatamanager.data.entity.Gem;
import io.apexapps.dlvdatamanager.data.service.GemService;
import io.apexapps.dlvdatamanager.utils.NotificationUtil;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@PageTitle("Edit Gem")
@Route(value = "gems/:gemId/edit", layout = MainLayout.class)
public class EditGemView extends Composite<VerticalLayout> implements BeforeEnterObserver {
    private static final String GEM_ID = "gemId";
    private static final String ICON_FORMAT = "assets/images/gems/%s.png";

    private final FormLayout form = new FormLayout();
    private final TextField name = new TextField("Name");
    private final TextField icon = new TextField("Icon");
    private final MultiSelectComboBox<LocationEnum> locations = new MultiSelectComboBox<>("Locations");
    private final TextField sellPrice = new TextField("Sell Price");
    private final Binder<Gem> gemBinder = new Binder<>(Gem.class);
    private final GemService gemService;
    private Gem gem;

    public EditGemView(GemService gemService) {
        this.gemService = gemService;
        addClassName("edit-gem-view");

        getContent().add(new Button("Back", new Icon(VaadinIcon.ARROW_LEFT), event -> UI
                .getCurrent()
                .navigate(GemsView.class)
        ));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> gemId = event.getRouteParameters().get(GEM_ID).map(Long::parseLong);

        if (gemId.isPresent()) {
            Optional<Gem> refinedMaterialFromBackend = gemService.get(gemId.get());

            if (refinedMaterialFromBackend.isPresent()) {
                populateForm(refinedMaterialFromBackend.get());
                createEditGemView();
                buildSaveButton();
            } else {
                NotificationUtil.showError(
                        String.format("The requested gem was not found, ID = %s", gemId.get())
                );
                event.forwardTo(GemsView.class);
            }
        }
    }

    private void populateForm(Gem gem) {
        this.gem = gem;
        gemBinder.setBean(gem);
    }

    private void createEditGemView() {
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
                    gemBinder.writeBean(gem);
                    gemService.update(gem);
                    NotificationUtil.showSuccess("Gem saved successfully");
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
