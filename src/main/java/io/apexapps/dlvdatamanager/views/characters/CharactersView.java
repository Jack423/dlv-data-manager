package io.apexapps.dlvdatamanager.views.characters;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import io.apexapps.dlvdatamanager.data.entity.Character;
import io.apexapps.dlvdatamanager.data.service.CharacterService;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

@PageTitle("Characters")
@Route(value = "characters", layout = MainLayout.class)
@Slf4j
public class CharactersView extends Composite<VerticalLayout> {
    private final CharacterService characterService;
    private Button newCharacterButton = new Button();
    private Grid<Character> charactersList = new Grid<Character>(Character.class, false);
    private Character draggedItem;

    public CharactersView(CharacterService characterService) {
        this.characterService = characterService;
        getContent().setHeightFull();
        getContent().setWidthFull();
        newCharacterButton.setText("Create New Character");

        setupCharactersGrid();

        getContent().add(newCharacterButton);
    }

    private void setupCharactersGrid() {
        GridListDataView<Character> dataView = charactersList.setItems(
                characterService.list(Pageable.unpaged()).stream().toList());

        charactersList.addColumn(Character::getId).setFlexGrow(0).setHeader("ID");
        charactersList.addColumn(Character::getName).setHeader("Name");
        charactersList.addColumn(setupEditCharacterButton());
        charactersList.setItems(characterService.list(Pageable.unpaged()).stream().toList());
        charactersList.setDropMode(GridDropMode.BETWEEN);
        charactersList.setRowsDraggable(true);

        charactersList.addDragStartListener(e -> draggedItem = e.getDraggedItems().get(0));

        charactersList.addDropListener(e -> {
            Character targetCharacter = e.getDropTargetItem().orElse(null);
            GridDropLocation dropLocation = e.getDropLocation();

            boolean wasDraggedOnSelf = draggedItem.equals(targetCharacter);

            if (targetCharacter == null || wasDraggedOnSelf) {
                return;
            }

            dataView.removeItem(draggedItem);

            if (dropLocation == GridDropLocation.BELOW) {
                dataView.addItemAfter(draggedItem, targetCharacter);
            } else {
                dataView.addItemBefore(draggedItem, targetCharacter);
            }
        });

        charactersList.addDragEndListener(e -> draggedItem = null);

        getContent().add(charactersList);
    }

    private ComponentRenderer<Button, Character> setupEditCharacterButton() {
        return new ComponentRenderer<>((SerializableFunction<Character, Button>) character ->
                new Button("Edit", buttonClickEvent -> getUI().ifPresent(ui -> ui.navigate(
                    EditCharacterView.class,
                    new RouteParameters("characterId", character.getId().toString())
        ))));
    }
}
