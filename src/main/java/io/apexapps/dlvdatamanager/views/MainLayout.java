package io.apexapps.dlvdatamanager.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.apexapps.dlvdatamanager.data.service.*;
import io.apexapps.dlvdatamanager.views.allothercraftingitems.CraftingItemsView;
import io.apexapps.dlvdatamanager.views.characters.CharactersView;
import io.apexapps.dlvdatamanager.views.critters.CrittersView;
import io.apexapps.dlvdatamanager.views.fish.FishView;
import io.apexapps.dlvdatamanager.views.foraging.ForagingView;
import io.apexapps.dlvdatamanager.views.gems.GemsView;
import io.apexapps.dlvdatamanager.views.home.HomeView;
import io.apexapps.dlvdatamanager.views.ingredients.IngredientsView;
import io.apexapps.dlvdatamanager.views.locations.LocationsView;
import io.apexapps.dlvdatamanager.views.meals.MealsView;
import io.apexapps.dlvdatamanager.views.refinedmaterials.RefinedMaterialsView;
import io.apexapps.dlvdatamanager.views.seeds.SeedsView;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private final CharacterService characterService;
    private final CritterService critterService;
    private final FishService fishService;
    private final ForagingService foragingService;
    private final IngredientService ingredientService;
    private final MealService mealService;
    private final RefinedMaterialService refinedMaterialService;
    private final CraftingItemService craftingItemService;
    private final GemService gemService;
    private final LocationService locationService;
    private final SeedService seedService;

    private H2 viewTitle;

    public MainLayout(
            CharacterService characterService,
            CritterService critterService,
            FishService fishService,
            ForagingService foragingService,
            IngredientService ingredientService,
            MealService mealService,
            RefinedMaterialService refinedMaterialService,
            CraftingItemService craftingItemService,
            GemService gemService,
            LocationService locationService,
            SeedService seedService
    ) {
        this.characterService = characterService;
        this.critterService = critterService;
        this.fishService = fishService;
        this.foragingService = foragingService;
        this.ingredientService = ingredientService;
        this.mealService = mealService;
        this.refinedMaterialService = refinedMaterialService;
        this.craftingItemService = craftingItemService;
        this.gemService = gemService;
        this.locationService = locationService;
        this.seedService = seedService;
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("DLV Data Manager");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Home", HomeView.class, LineAwesomeIcon.HOME_SOLID.create()));

        var charactersSideItem = new SideNavItem("Characters", CharactersView.class, LineAwesomeIcon.MALE_SOLID.create());
        charactersSideItem.setSuffixComponent(buildCountSuffixItem(characterService.count()));
        nav.addItem(charactersSideItem);

        var crittersSideItem = new SideNavItem("Critters", CrittersView.class, LineAwesomeIcon.CAT_SOLID.create());
        crittersSideItem.setSuffixComponent(buildCountSuffixItem(critterService.count()));
        nav.addItem(crittersSideItem);

        var fishSideItem = new SideNavItem("Fish", FishView.class, LineAwesomeIcon.FISH_SOLID.create());
        fishSideItem.setSuffixComponent(buildCountSuffixItem(fishService.count()));
        nav.addItem(fishSideItem);

        var foragingSideItem = new SideNavItem("Foraging", ForagingView.class, LineAwesomeIcon.SHOPPING_BASKET_SOLID.create());
        foragingSideItem.setSuffixComponent(buildCountSuffixItem(foragingService.count()));
        nav.addItem(foragingSideItem);

        var ingredientsSideItem = new SideNavItem("Ingredients", IngredientsView.class, LineAwesomeIcon.APPLE_ALT_SOLID.create());
        ingredientsSideItem.setSuffixComponent(buildCountSuffixItem(ingredientService.count()));
        nav.addItem(ingredientsSideItem);

        var mealsSideItem = new SideNavItem("Meals", MealsView.class, LineAwesomeIcon.BREAD_SLICE_SOLID.create());
        mealsSideItem.setSuffixComponent(buildCountSuffixItem(mealService.count()));
        nav.addItem(mealsSideItem);

        var refinedMaterialsSideItem = new SideNavItem("Refined Materials", RefinedMaterialsView.class,
                LineAwesomeIcon.HAMMER_SOLID.create());
        refinedMaterialsSideItem.setSuffixComponent(buildCountSuffixItem(refinedMaterialService.count()));
        nav.addItem(refinedMaterialsSideItem);

        var craftingItems = new SideNavItem("All Other Crafting Items", CraftingItemsView.class,
                LineAwesomeIcon.COLUMNS_SOLID.create());
        craftingItems.setSuffixComponent(buildCountSuffixItem(craftingItemService.count()));
        nav.addItem(craftingItems);

        var gemsSideItem = new SideNavItem("Gems", GemsView.class, LineAwesomeIcon.GEM_SOLID.create());
        gemsSideItem.setSuffixComponent(buildCountSuffixItem(gemService.count()));
        nav.addItem(gemsSideItem);

        var locationsSideItem = new SideNavItem("Locations", LocationsView.class,
                LineAwesomeIcon.MAP_MARKER_SOLID.create());
        locationsSideItem.setSuffixComponent(buildCountSuffixItem(locationService.count()));
        nav.addItem(locationsSideItem);

        var seedsSideItem = new SideNavItem("Seeds", SeedsView.class, LineAwesomeIcon.SEEDLING_SOLID.create());
        seedsSideItem.setSuffixComponent(buildCountSuffixItem(seedService.count()));
        nav.addItem(seedsSideItem);

        return nav;
    }

    private Span buildCountSuffixItem(Integer count) {
        Span serviceItemCounter = new Span(count.toString());
        serviceItemCounter.getElement().getThemeList().add("badge contrast pill");
        serviceItemCounter.getElement().setAttribute("aria-label", "12 unread messages");
        return serviceItemCounter;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
