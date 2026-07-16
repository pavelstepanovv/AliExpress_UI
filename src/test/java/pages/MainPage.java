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

    private static final String URL = "https://aliexpress.ru";
    private static final String SEARCH_SUGGESTIONS_XPATH = "//div[@ae_button_type='auto_suggestion' and @ae_object_type='keyword']";

    private final Input searchInput = Input.byName("SearchText");
    private final Button searchButton = Button.byText("Найти");
    private final ElementsCollection searchSuggestions = $$x(SEARCH_SUGGESTIONS_XPATH);
    // та же кнопка поиска, но найденная по классу: её текст меняется после смены языка
    private final Button searchSubmitButton = Button.byClass("RedSearchBar_RedSearchBar__submit");
    private final Button clearSearchButton = Button.byClass("RedSearchBar_RedSearchBar__reset");
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

    /**
     * Открывает главную страницу AliExpress.
     */
    public MainPage open() {
        open(URL);
        dismissBlockingOverlays(Duration.ofSeconds(2));
        searchInput.waitUntilEditable(Duration.ofMinutes(3));
        dismissBlockingOverlays(Duration.ofSeconds(2));
        return this;
    }

    /**
     * Выполняет поиск товара по ключевому слову.
     */
    public SearchResultPage search(String query) {
        searchInput.fill(query);
        dismissBlockingOverlays(Duration.ofSeconds(4));
        searchButton.click();
        return new SearchResultPage();
    }

    /**
     * Очищает строку поиска.
     */
    public void clearSearch() {
        searchInput.clear();
    }

    /**
     * Заполняет строку поиска без отправки формы.
     */
    public void fillSearch(String query) {
        searchInput.fill(query);
        dismissBlockingOverlays(Duration.ofSeconds(2));
    }

    /**
     * Вводит часть запроса и динамически ждет появления поисковых подсказок.
     */
    public MainPage fillSearchAndWaitSuggestions(String query) {
        searchInput.fill(query);
        dismissBlockingOverlays(Duration.ofSeconds(2));
        searchSuggestions.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(10));
        return this;
    }

    /**
     * Возвращает количество найденных подсказок.
     */
    public int getSuggestionsCount() {
        return searchSuggestions.size();
    }

    /**
     * Возвращает тексты поисковых подсказок.
     */
    public List<String> getSuggestionValues() {
        List<String> suggestions = new ArrayList<>();
        for (int i = 0; i < searchSuggestions.size(); i++) {
            String suggestion = searchSuggestions.get(i).getAttribute("ae_object_value");
            if (suggestion == null) {
                suggestion = searchSuggestions.get(i).getText();
            }
            suggestions.add(suggestion);
        }
        return suggestions;
    }

    /**
     * Закрывает попап выбора региона, если он появился.
     * Этот попап иногда показывается при первом открытии сайта
     * и перекрывает другие элементы страницы.
     */
    public MainPage closeLocationPopupIfPresent() {
        dismissBlockingOverlays(Duration.ofSeconds(3));
        return this;
    }

    /**
     * Нажимает кнопку очистки поля поиска (крестик).
     */
    public void clickClearSearch() {
        dismissBlockingOverlays(Duration.ofSeconds(2));
        clearSearchButton.click();
    }

    /**
     * Возвращает текст, который сейчас введён в поле поиска.
     */
    public String getSearchFieldValue() {
        return searchInput.getValue();
    }

    /**
     * Переключает язык интерфейса на английский.
     * Открывает переключатель языка в шапке и выбирает "English".
     */
    public void switchLanguageToEnglish() {
        selectLanguageByFlag("US");
    }

    /**
     * Возвращает русский язык после проверки, не удаляя cookie текущей сессии.
     */
    public void switchLanguageToRussian() {
        SelenideElement languageSwitcher = getVisibleLanguageSwitcher();
        if ("RU".equalsIgnoreCase(languageSwitcher.getText().trim())) {
            return;
        }
        selectLanguageByFlag("RU");
    }

    private void openLanguagePanel() {
        dismissBlockingOverlays(Duration.ofSeconds(2));
        for (int attempt = 0; attempt < 6; attempt++) {
            SelenideElement languageSwitcher = getVisibleLanguageSwitcher();

            actions().moveToElement(languageSwitcher).click().perform();
            if (languagePanel.is(visible, Duration.ofSeconds(3))) {
                return;
            }
        }

        languagePanel.shouldBe(visible, Duration.ofSeconds(15));
    }

    private void selectLanguageByFlag(String flagCode) {
        openLanguagePanel();
        $x("//div[contains(@class,'ShipToHeaderItem_SnowTooltip__shown')]" +
                "//li[contains(@class,'ShipToHeaderItem_List__element')]" +
                "[.//img[@alt='" + flagCode + "']]")
                .shouldBe(visible, Duration.ofSeconds(15))
                .click();
        languagePanel.should(disappear, Duration.ofSeconds(15));
    }

    private SelenideElement getVisibleLanguageSwitcher() {
        return languageSwitchers.filterBy(visible).first()
                .shouldBe(visible, Duration.ofSeconds(15));
    }

    /**
     * Возвращает текст кнопки поиска.
     * После смены языка текст меняется с "Найти" на "Find".
     */
    public String getSearchButtonText() {
        return searchSubmitButton.getText();
    }

    /** Открывает выпадающее меню каталога. */
    public MainPage openCatalog() {
        dismissBlockingOverlays(Duration.ofSeconds(2));
        catalogButton.shouldBe(visible, Duration.ofSeconds(15)).click();
        catalogPopup.shouldBe(visible, Duration.ofSeconds(15));
        return this;
    }

    /** Выбирает раздел «Электроника» и ждёт появления его подкатегорий. */
    public MainPage selectElectronicsCategory() {
        electronicsCategory.shouldBe(visible, Duration.ofSeconds(15)).click();
        audioVideoSubcategory.shouldBe(visible, Duration.ofSeconds(15));
        return this;
    }

    /** Переходит в подкатегорию «Аудио- и видеотехника». */
    public CategoryPage openAudioVideoCategory() {
        audioVideoSubcategory.shouldBe(visible, Duration.ofSeconds(15)).click();
        return new CategoryPage().waitUntilOpened();
    }

    /** Открывает окно изменения адреса доставки через город в шапке. */
    public DeliveryAddressModal openDeliveryAddress() {
        dismissBlockingOverlays(Duration.ofSeconds(2));
        getVisibleDeliveryCityLabel().click();
        return new DeliveryAddressModal().waitUntilOpened();
    }

    /** Ждёт отображение выбранного города в шапке. */
    public MainPage waitUntilDeliveryCityDisplayed(String city) {
        getVisibleDeliveryCityLabel()
                .shouldHave(exactText(city), Duration.ofSeconds(30));
        return this;
    }

    /** Возвращает текущий город доставки из шапки. */
    public String getDeliveryCity() {
        return getVisibleDeliveryCityLabel().getText();
    }

    private SelenideElement getVisibleDeliveryCityLabel() {
        return deliveryCityLabels.filterBy(visible).first()
                .shouldBe(visible, Duration.ofSeconds(15));
    }
}
