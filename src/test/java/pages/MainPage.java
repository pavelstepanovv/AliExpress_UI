package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import elements.Button;
import elements.Input;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.actions;

/**
 * Page Object главной страницы AliExpress.
 * Автор: Шакуров 4382.
 */
public class MainPage extends BasePage {

    // СТАТИЧЕСКИЕ ПОЛЯ (константы)
    private static final int SHORT_TIMEOUT_SEC = 2;
    private static final int MEDIUM_TIMEOUT_SEC = 4;
    private static final int LONG_TIMEOUT_SEC = 10;
    private static final int EXTRA_LONG_TIMEOUT_SEC = 15;
    private static final int VERY_LONG_TIMEOUT_SEC = 30;
    private static final int WAIT_EDITABLE_TIMEOUT_MIN = 3;
    private static final int LANGUAGE_SWITCH_ATTEMPTS = 6;
    private static final int LANGUAGE_PANEL_CHECK_SEC = 3;

    private static final String URL = "https://aliexpress.ru";
    private static final String SEARCH_SUGGESTIONS_XPATH =
            "//div[@ae_button_type='auto_suggestion' and @ae_object_type='keyword']";
    private static final String LANGUAGE_FLAG_XPATH_TEMPLATE =
            "//div[contains(@class,'ShipToHeaderItem_SnowTooltip__shown')]" +
                    "//li[contains(@class,'ShipToHeaderItem_List__element')][.//img[@alt='%s']]";

    // ПОЛЯ ЭКЗЕМПЛЯРА
    private final Input searchInput = Input.byName("SearchText");
    private final Button searchButton = Button.byText("Найти");
    private final ElementsCollection searchSuggestions = $$x(SEARCH_SUGGESTIONS_XPATH);
    private final Button searchSubmitButton = Button.byClass(
            "RedSearchBar_RedSearchBar__submit");
    private final Button clearSearchButton = Button.byClass(
            "RedSearchBar_RedSearchBar__reset");
    private final SelenideElement catalogButton = $x(
            "//button[.//span[normalize-space()='Каталог']]");
    private final SelenideElement catalogPopup = $x(
            "//div[contains(@class,'RedHeaderCatalogPopup__container')]");
    private final SelenideElement electronicsCategory = $x(
            "//a[@data-category-id='15'][.//*[normalize-space()='Электроника']]");
    private final SelenideElement audioVideoSubcategory = $x(
            "//a[contains(@href,'/category/16046/audio-video') " +
                    "and normalize-space()='Аудио- и видеотехника']");
    private final ElementsCollection languageSwitchers = $$x(
            "//div[contains(@class,'ShipToHeaderItem_SnowTooltip__wrapper')]" +
                    "/div[contains(@class,'ShipToHeaderItem_ShipToHeaderItem__element') " +
                    "and not(contains(@class,'disabled'))]");
    private final SelenideElement languagePanel = $x(
            "//div[contains(@class,'ShipToHeaderItem_SnowTooltip__tooltipContent') " +
                    "and contains(@class,'ShipToHeaderItem_SnowTooltip__shown')]" +
                    "[.//li[contains(@class,'ShipToHeaderItem_List__element')]]");
    private final ElementsCollection deliveryCityLabels = $$x(
            "//span[contains(@class,'ShipToHeaderItem_GeoTooltip__text__')]");

    // КОНСТРУКТОРЫ
    public MainPage() {
        // пустой конструктор
    }

    // ПУБЛИЧНЫЕ МЕТОДЫ

    public MainPage open() {
        openPage();
        waitForPageReady();
        return this;
    }

    public SearchResultPage search(String query) {
        enterSearchQuery(query);
        submitSearch();
        return new SearchResultPage();
    }

    public void clearSearch() {
        searchInput.clear();
    }

    public void fillSearch(String query) {
        enterSearchQuery(query);
    }

    public MainPage fillSearchAndWaitSuggestions(String query) {
        enterSearchQuery(query);
        waitForSuggestions();
        return this;
    }

    public int getSuggestionsCount() {
        return searchSuggestions.size();
    }

    public List<String> getSuggestionValues() {
        List<String> suggestions = new ArrayList<>();
        for (int i = 0; i < searchSuggestions.size(); i++) {
            String suggestion = searchSuggestions.get(i)
                    .getAttribute("ae_object_value");
            if (suggestion == null) {
                suggestion = searchSuggestions.get(i).getText();
            }
            suggestions.add(suggestion);
        }
        return suggestions;
    }

    public MainPage closeLocationPopupIfPresent() {
        dismissBlockingOverlays(Duration.ofSeconds(SHORT_TIMEOUT_SEC));
        return this;
    }

    public void clickClearSearch() {
        dismissBlockingOverlays(Duration.ofSeconds(SHORT_TIMEOUT_SEC));
        clearSearchButton.click();
    }

    public String getSearchFieldValue() {
        return searchInput.getValue();
    }

    public void switchLanguageToEnglish() {
        selectLanguageByFlag("US");
    }

    public void switchLanguageToRussian() {
        SelenideElement languageSwitcher = getVisibleLanguageSwitcher();
        if ("RU".equalsIgnoreCase(languageSwitcher.getText().trim())) {
            return;
        }
        selectLanguageByFlag("RU");
    }

    public String getSearchButtonText() {
        return searchSubmitButton.getText();
    }

    public MainPage openCatalog() {
        clickCatalogButton();
        waitForCatalogPopup();
        return this;
    }

    public MainPage selectElectronicsCategory() {
        clickElectronicsCategory();
        waitForAudioVideoSubcategory();
        return this;
    }

    public CategoryPage openAudioVideoCategory() {
        clickAudioVideoSubcategory();
        return new CategoryPage().waitUntilOpened();
    }

    public DeliveryAddressModal openDeliveryAddress() {
        dismissBlockingOverlays(Duration.ofSeconds(SHORT_TIMEOUT_SEC));
        clickDeliveryCityLabel();
        return new DeliveryAddressModal().waitUntilOpened();
    }

    public MainPage waitUntilDeliveryCityDisplayed(String city) {
        getVisibleDeliveryCityLabel()
                .shouldHave(exactText(city), Duration.ofSeconds(VERY_LONG_TIMEOUT_SEC));
        return this;
    }

    public String getDeliveryCity() {
        return getVisibleDeliveryCityLabel().getText();
    }

    // ПРИВАТНЫЕ МЕТОДЫ

    private void openPage() {
        open(URL);
    }

    private void waitForPageReady() {
        dismissBlockingOverlays(Duration.ofSeconds(SHORT_TIMEOUT_SEC));
        searchInput.waitUntilEditable(Duration.ofMinutes(WAIT_EDITABLE_TIMEOUT_MIN));
        dismissBlockingOverlays(Duration.ofSeconds(SHORT_TIMEOUT_SEC));
    }

    private void enterSearchQuery(String query) {
        searchInput.fill(query);
        dismissBlockingOverlays(Duration.ofSeconds(SHORT_TIMEOUT_SEC));
    }

    private void submitSearch() {
        dismissBlockingOverlays(Duration.ofSeconds(MEDIUM_TIMEOUT_SEC));
        searchButton.click();
    }

    private void waitForSuggestions() {
        dismissBlockingOverlays(Duration.ofSeconds(SHORT_TIMEOUT_SEC));
        searchSuggestions.shouldHave(
                sizeGreaterThan(0),
                Duration.ofSeconds(LONG_TIMEOUT_SEC)
        );
    }

    private void openLanguagePanel() {
        dismissBlockingOverlays(Duration.ofSeconds(SHORT_TIMEOUT_SEC));
        for (int attempt = 0; attempt < LANGUAGE_SWITCH_ATTEMPTS; attempt++) {
            SelenideElement languageSwitcher = getVisibleLanguageSwitcher();
            actions().moveToElement(languageSwitcher).click().perform();
            if (languagePanel.is(visible, Duration.ofSeconds(LANGUAGE_PANEL_CHECK_SEC))) {
                return;
            }
        }
        languagePanel.shouldBe(visible, Duration.ofSeconds(EXTRA_LONG_TIMEOUT_SEC));
    }

    private void selectLanguageByFlag(String flagCode) {
        openLanguagePanel();
        $x(String.format(LANGUAGE_FLAG_XPATH_TEMPLATE, flagCode))
                .shouldBe(visible, Duration.ofSeconds(EXTRA_LONG_TIMEOUT_SEC))
                .click();
        languagePanel.should(disappear, Duration.ofSeconds(EXTRA_LONG_TIMEOUT_SEC));
    }

    private SelenideElement getVisibleLanguageSwitcher() {
        return languageSwitchers.filterBy(visible).first()
                .shouldBe(visible, Duration.ofSeconds(EXTRA_LONG_TIMEOUT_SEC));
    }

    private void clickCatalogButton() {
        dismissBlockingOverlays(Duration.ofSeconds(SHORT_TIMEOUT_SEC));
        catalogButton.shouldBe(visible, Duration.ofSeconds(EXTRA_LONG_TIMEOUT_SEC))
                .click();
    }

    private void waitForCatalogPopup() {
        catalogPopup.shouldBe(visible, Duration.ofSeconds(EXTRA_LONG_TIMEOUT_SEC));
    }

    private void clickElectronicsCategory() {
        electronicsCategory.shouldBe(visible, Duration.ofSeconds(EXTRA_LONG_TIMEOUT_SEC))
                .click();
    }

    private void waitForAudioVideoSubcategory() {
        audioVideoSubcategory.shouldBe(
                visible,
                Duration.ofSeconds(EXTRA_LONG_TIMEOUT_SEC)
        );
    }

    private void clickAudioVideoSubcategory() {
        audioVideoSubcategory.shouldBe(
                visible,
                Duration.ofSeconds(EXTRA_LONG_TIMEOUT_SEC)
        ).click();
    }

    private void clickDeliveryCityLabel() {
        getVisibleDeliveryCityLabel().click();
    }

    private SelenideElement getVisibleDeliveryCityLabel() {
        return deliveryCityLabels.filterBy(visible).first()
                .shouldBe(visible, Duration.ofSeconds(EXTRA_LONG_TIMEOUT_SEC));
    }
}
