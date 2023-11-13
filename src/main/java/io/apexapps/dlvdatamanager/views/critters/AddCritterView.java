package io.apexapps.dlvdatamanager.views.critters;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.apexapps.dlvdatamanager.data.LocationEnum;
import io.apexapps.dlvdatamanager.data.entity.Critter;
import io.apexapps.dlvdatamanager.data.entity.Fish;
import io.apexapps.dlvdatamanager.data.entity.Foraging;
import io.apexapps.dlvdatamanager.data.entity.Ingredient;
import io.apexapps.dlvdatamanager.data.service.CritterService;
import io.apexapps.dlvdatamanager.data.service.FishService;
import io.apexapps.dlvdatamanager.data.service.ForagingService;
import io.apexapps.dlvdatamanager.data.service.IngredientService;
import io.apexapps.dlvdatamanager.utils.NotificationUtil;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@PageTitle("New Critter")
@Route(value = "critters/new", layout = MainLayout.class)
public class AddCritterView extends Composite<VerticalLayout> {
    private static final String CRITTER_ICON_FORMAT = "assets/images/critters/%s.jpg";
    private final Critter critter = new Critter();
    private final FormLayout formLayout = new FormLayout();
    private final TextField name = new TextField("Name");
    private final TextField icon = new TextField("Icon");
    private final TextArea howToFeed = new TextArea("How to Feed");
    private final ComboBox<Critter.CritterType> type = new ComboBox<>("Type");
    private final ComboBox<LocationEnum> location = new ComboBox<>("Location");
    private final MultiSelectComboBox<String> favoriteFood = new MultiSelectComboBox<>("Favorite Food");
    private final MultiSelectComboBox<String> likedFood = new MultiSelectComboBox<>("Liked Food");
    private final Binder<Critter> critterBinder = new Binder<>(Critter.class);
    VerticalLayout scheduleList = new VerticalLayout();
    private final CritterService critterService;
    private final IngredientService ingredientService;
    private final FishService fishService;
    private final ForagingService foragingService;

    public AddCritterView(
            CritterService critterService,
            IngredientService ingredientService,
            FishService fishService,
            ForagingService foragingService
    ) {
        this.critterService = critterService;
        this.ingredientService = ingredientService;
        this.fishService = fishService;
        this.foragingService = foragingService;

        critter.setSchedule(new ArrayList<>());

        scheduleList.setWidthFull();
        scheduleList.setPadding(false);
        scheduleList.setSpacing(false);

        createNewCritterView();
        createScheduleHeader();
        buildScheduleList(scheduleList);
        getContent().add(scheduleList);
        buildSaveButton();
    }

    private void createNewCritterView() {
        getContent().setHeightFull();
        icon.setReadOnly(true);

        Stream<String> allIngredients = ingredientService.list().stream()
                .filter(Objects::nonNull)
                .map(Ingredient::getName);
        Stream<String> allFish = fishService.list().stream().filter(Objects::nonNull).map(Fish::getName);
        Stream<String> allFlowers = foragingService.list().stream()
                .filter(Objects::nonNull)
                .filter(item -> item.getType().equals(Foraging.Type.FLOWERS))
                .map(Foraging::getName);
        Set<String> combinedItems = Stream.of(allIngredients, allFish, allFlowers)
                .flatMap(i -> i)
                .collect(Collectors.toSet());

        type.setItems(Critter.CritterType.values());
        type.setItemLabelGenerator(type -> type.value);
        likedFood.setItems(combinedItems);
        favoriteFood.setItems(combinedItems);
        location.setItems(LocationEnum.values());
        location.setItemLabelGenerator(location -> location.value);
        name.addValueChangeListener(event -> {
           icon.setValue(String.format(
                   CRITTER_ICON_FORMAT,
                   name.getValue().replaceAll(" ", "_")
           ));
        });

        formLayout.add(name, icon, howToFeed, type, location, likedFood, favoriteFood);

        getContent().add(formLayout);

        critterBinder.bind(name, Critter::getName, Critter::setName);
        critterBinder.bind(icon, Critter::getIcon, Critter::setIcon);
        critterBinder.bind(howToFeed, Critter::getHowToFeed, Critter::setHowToFeed);
        critterBinder.bind(type, Critter::getType, Critter::setType);
        critterBinder.bind(location, Critter::getLocation, Critter::setLocation);
        critterBinder.bind(likedFood, Critter::getLikedFood, Critter::setLikedFood);
        critterBinder.bind(favoriteFood, Critter::getFavoriteFood, Critter::setFavoriteFood);
        critterBinder.readBean(critter);
    }

    private void createScheduleHeader() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setPadding(false);
        horizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        Span scheduleSpan = new Span("Schedule");

        Button addScheduleItem = new Button("Add", new Icon(VaadinIcon.PLUS), clickEvent -> {
            critter.getSchedule().add(Critter.CritterSchedule.builder()
                    .day(Critter.DayOfTheWeek.MONDAY)
                    .start("01:00")
                    .end("12:00")
                    .build());
            buildScheduleList(scheduleList);
        });
        addScheduleItem.addThemeVariants(ButtonVariant.LUMO_SMALL);
        horizontalLayout.add(scheduleSpan, addScheduleItem);
        scheduleSpan.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.BOLD);
        getContent().add(horizontalLayout);
    }

    private void buildSaveButton() {
        Button saveButton = new Button("Save", event -> {
            try {
                if (critterBinder.isValid()) {
                    critter.setId(critterService.count() + 1);
                    critterBinder.writeBean(critter);
                    critterService.update(critter);
                    NotificationUtil.showSuccess("Critter saved successfully");
                }
            } catch (Exception e) {
                log.error("Unable to save the critter", e);
                NotificationUtil.showError("Unable to save the critter");
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(saveButton);
    }

    private void buildScheduleList(VerticalLayout scheduleList) {
        scheduleList.removeAll();

        critter.getSchedule().forEach(scheduleItem -> {
            HorizontalLayout schedule = new HorizontalLayout();
            schedule.setWidthFull();
            schedule.setPadding(false);

            ComboBox<Critter.DayOfTheWeek> dayOfTheWeek = new ComboBox<>("Day");
            dayOfTheWeek.setItems(Critter.DayOfTheWeek.values());
            dayOfTheWeek.setItemLabelGenerator(d -> d.value);
            dayOfTheWeek.setValue(scheduleItem.getDay());
            dayOfTheWeek.addValueChangeListener(event -> {
                critter.getSchedule().stream()
                        .filter(scheduleItem::equals)
                        .findFirst()
                        .get().setDay(event.getValue());
            });

            TimePicker start = new TimePicker("Start");
            start.addValueChangeListener(event -> critter.getSchedule().stream()
                    .filter(scheduleItem::equals)
                    .findFirst()
                    .get().setStart(event.getValue().toString()));

            TimePicker end = new TimePicker("End");
            end.addValueChangeListener(event -> critter.getSchedule().stream()
                    .filter(scheduleItem::equals)
                    .findFirst()
                    .get().setEnd(event.getValue().toString()));

            int startHour = Integer.parseInt(scheduleItem.getStart().split(":")[0]);
            int endHour = Integer.parseInt(scheduleItem.getEnd().split(":")[0]);

            start.setValue(LocalTime.of(startHour, 0));
            end.setValue(LocalTime.of(endHour, 0));

            Button delete = new Button(new Icon(VaadinIcon.TRASH), clickEvent -> {
                this.critter.getSchedule().remove(scheduleItem);
                critterBinder.writeBeanIfValid(critter);
                buildScheduleList(scheduleList);
            });
            delete.addThemeVariants(ButtonVariant.LUMO_SMALL);
            schedule.setAlignSelf(FlexComponent.Alignment.END, delete);

            schedule.add(dayOfTheWeek, start, end, delete);
            scheduleList.add(schedule);
        });
    }
}
