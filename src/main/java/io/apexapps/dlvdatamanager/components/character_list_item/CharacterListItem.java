package io.apexapps.dlvdatamanager.components.character_list_item;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.apexapps.dlvdatamanager.data.entity.Character;

public class CharacterListItem extends Composite<HorizontalLayout> {
    private Span name = new Span();

    public CharacterListItem(Character character) {
        getContent().setWidthFull();
        name.setText(character.getName());
    }
}
