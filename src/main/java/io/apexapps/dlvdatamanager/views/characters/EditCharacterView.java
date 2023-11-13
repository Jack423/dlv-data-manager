package io.apexapps.dlvdatamanager.views.characters;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.apexapps.dlvdatamanager.components.quest_required_item.QuestRequiredItems;
import io.apexapps.dlvdatamanager.data.entity.Character;
import io.apexapps.dlvdatamanager.data.service.CharacterService;
import io.apexapps.dlvdatamanager.utils.NotificationUtil;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@PageTitle("Characters")
@Route(value = "characters/:characterId?/edit", layout = MainLayout.class)
@Slf4j
public class EditCharacterView extends Composite<VerticalLayout> implements BeforeEnterObserver {
    private static final String CHARACTER_ICON_PATH_FORMAT = "/assets/images/characters/%s.png";

    private Character character;
    private final CharacterService characterService;
    private final VerticalLayout characterWrapper = new VerticalLayout();
    private final VerticalLayout editQuestWrapper = new VerticalLayout();
    private final FormLayout formLayout = new FormLayout();
    private final TextField name = new TextField("Character Name");
    private final TextField icon = new TextField("Path to Icon");
    private final TextField information = new TextField("Information");
    private Binder<Character.Quest> questBinder;
    private Binder<Character> characterBinder;
    private VerticalLayout questsViewLayout = new VerticalLayout();
    private TextField questName = new TextField("Quest Name");
    private TextField questDescription = new TextField("Quest Description");
    private ComboBox<Character.QuestCategory> questCategory = new ComboBox<>("Category");
    private Character.Quest selectedQuest = new Character.Quest();
    private final QuestRequiredItems questRequiredItems = new QuestRequiredItems();

    public EditCharacterView(CharacterService characterService) {
        this.characterService = characterService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Long characterId = Long.valueOf(event.getRouteParameters().get("characterId").get());
        character = characterService.get(characterId).orElse(null);

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setWidth("100%");
        splitLayout.setHeight("100%");

        characterWrapper.setHeight("100%");
        editQuestWrapper.setHeight("100%");

        questBinder = new Binder<>(Character.Quest.class);
        characterBinder = new Binder<>(Character.class);

        setupCharacterInfoLayout(splitLayout);
        setupEditorLayout(splitLayout);

        getContent().add(splitLayout);

        setupForm();
    }

    private void setupForm() {
        if (character != null) {
            try {
                characterBinder.writeBean(character);
            } catch (ValidationException e) {
                log.error("huh, why");
            }
            name.setValue(character.getName());
            icon.setValue(character.getIcon());
            icon.setReadOnly(true);
            information.setValue(character.getInformation());
        }

        formLayout.add(name, icon, information);
        formLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new FormLayout.ResponsiveStep("500px", 2));
        // Stretch the username field over 2 columns
        formLayout.setColspan(information, 2);

        characterWrapper.add(new Button("Back", new Icon(VaadinIcon.ARROW_LEFT), e -> getUI().ifPresent(ui ->
                ui.navigate("/characters"))));
        characterWrapper.add(formLayout);
        characterWrapper.add(questsViewLayout);
        buildQuestGrids();
        characterWrapper.add(new Button("Save", e -> {
            try {
                this.character.setName(name.getValue());
                this.character.setIcon(icon.getValue());
                this.character.setInformation(information.getValue());
                characterService.update(this.character);
                Notification success = Notification.show("Character updated successfully");
                success.setPosition(Notification.Position.BOTTOM_CENTER);
                success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (Exception error) {
                Notification success = Notification.show("Uh oh, there was a problem updating the character.");
                log.error("Unable to update the character.", error);
                success.setPosition(Notification.Position.BOTTOM_CENTER);
                success.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }));
    }

    private void buildQuestGrids() {
        questsViewLayout.removeAll();
        questsViewLayout.setPadding(false);

        List<Character.Quest> storyQuests = Optional.ofNullable(character.getQuests()).orElse(List.of()).stream()
                .filter(quest -> quest.getCategory().equals(Character.QuestCategory.STORY))
                .toList();
        List<Character.Quest> friendshipQuests = Optional.ofNullable(character.getQuests()).orElse(List.of()).stream()
                .filter(quest -> quest.getCategory().equals(Character.QuestCategory.FRIENDSHIP))
                .toList();
        List<Character.Quest> realmQuests = Optional.ofNullable(character.getQuests()).orElse(List.of()).stream()
                .filter(quest -> quest.getCategory().equals(Character.QuestCategory.REALM))
                .toList();

        questsViewLayout.add(
                buildQuestDetails(storyQuests, "Story"),
                buildQuestDetails(friendshipQuests, "Friendship"),
                buildQuestDetails(realmQuests, "Realm")
        );
    }

    Details buildQuestDetails(List<Character.Quest> quests, String type) {
        VerticalLayout questVerticalLayout = new VerticalLayout();
        questVerticalLayout.setSpacing(false);
        questVerticalLayout.setWidthFull();
        quests.forEach(quest -> {
            Button deleteQuestButton = new Button(new Icon(VaadinIcon.TRASH), clickEvent -> {
                deleteQuest(quest);
            });
            deleteQuestButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            Button questButton = new Button(quest.getName(), clickEvent -> {
                questBinder.readBean(quest);
                selectedQuest = quest;
                questRequiredItems.buildRequiredItemDetails();
            });
            questButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

            HorizontalLayout buttons = new HorizontalLayout(questButton, deleteQuestButton);
            buttons.setWidthFull();
            buttons.setPadding(false);
            buttons.setFlexGrow(1, questButton);

            questVerticalLayout.add(buttons);
        });
        HorizontalLayout summary  = new HorizontalLayout(
                new Span(type + " Quests (" + quests.size() + ")")
        );
        Details details = new Details(summary, questVerticalLayout);
        details.addThemeVariants(DetailsVariant.FILLED);
        details.setWidth("100%");

        return details;
    }

    private void deleteQuest(Character.Quest quest) {
        this.character.getQuests().remove(quest);

    }

    private void setupCharacterInfoLayout(SplitLayout splitLayout) {
        characterWrapper.setClassName("content-wrapper");
        splitLayout.addToPrimary(characterWrapper);
        characterWrapper.add(formLayout);
    }

    private void setupEditorLayout(SplitLayout splitLayout) {
        questCategory.setItems(
                Character.QuestCategory.STORY,
                Character.QuestCategory.FRIENDSHIP,
                Character.QuestCategory.REALM
        );
        questCategory.setItemLabelGenerator(questCategory -> {
            switch (questCategory) {
                case STORY -> {
                    return "Story";
                }
                case FRIENDSHIP -> {
                    return "Friendship";
                }
                case REALM -> {
                    return "Realm";
                }
                default -> { return ""; }
            }
        });

        // Setup binders
        questBinder.forField(questName)
                .withValidator(name -> name != null && !name.isBlank(), "This field is required")
                .bind(Character.Quest::getName, Character.Quest::setName);
        questBinder.forField(questDescription)
                .withValidator(description -> description != null && !description.isBlank(), "This field is required")
                .bind(Character.Quest::getDescription, Character.Quest::setDescription);
        questBinder.forField(questCategory)
                .withValidator(Objects::nonNull, "Select a quest category")
                .bind(Character.Quest::getCategory, Character.Quest::setCategory);

        questRequiredItems.setItems(selectedQuest.getRequiredItems());
        editQuestWrapper.add(
                new FormLayout(questName, questDescription, questCategory),
                new QuestRequiredItems(selectedQuest.getRequiredItems())
        );
        createEditorButtonLayout();

        splitLayout.addToSecondary(editQuestWrapper);
    }

    private void createEditorButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        Button cancel = new Button("Cancel", e -> {
            clearQuestForm();
            log.info("Quest form clear request.");
        });
        Button save = new Button("Save", e -> {
            try {
                questBinder.validate();
                if (questBinder.isValid()) {
                    saveQuest();
                    NotificationUtil.showSuccess("Updated quest successfully!");
                }
            } catch (Exception exception) {
                log.error("Unable to save the quest data", exception);
                NotificationUtil.showError("Unable to update or save the quest data.");
            }
        });
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editQuestWrapper.add(buttonLayout);
    }

    private void clearQuestForm() {
        selectedQuest = null;
        questBinder.getFields().forEach(HasValue::clear);
    }

    private void saveQuest() {
        // Update existing quest
        try {
            if (character.getQuests().contains(this.selectedQuest)) {
                int oldQuest = character.getQuests().indexOf(this.selectedQuest);
                questBinder.writeBean(this.selectedQuest);
                character.getQuests().set(oldQuest, this.selectedQuest);
            } else {
                // Create a new Quest.
                questBinder.writeBean(this.selectedQuest);
                character.getQuests().add(this.selectedQuest);
            }
        } catch (ValidationException e) {
            log.error("Unable to validate the quests.", e);
        }

        this.selectedQuest.setRequiredItems(questRequiredItems.getItems());
        buildQuestGrids();
    }
}
