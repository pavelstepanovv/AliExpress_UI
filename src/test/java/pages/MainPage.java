package pages;

import com.codeborne.selenide.ElementsCollection;
import elements.Button;
import elements.Input;
import elements.Item;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$$x;

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
    // в шапке несколько элементов с этим классом (валюта, язык, город), поэтому ищем ещё и по тексту
    private final Item languageSwitcher = Item.byClassAndText("ShipToHeaderItem_ShipToHeaderItem__element", "RU");
    private final Item englishOption = Item.byClassAndText("ShipToHeaderItem_List__element", "English");
    private final Item locationPopupBackdrop = Item.byClass("ShipToHeaderItem_Backdrop__background");

    /**
     * Открывает главную страницу AliExpress.
     */
    public MainPage open() {
        open(URL);
        return this;
    }

    /**
     * Выполняет поиск товара по ключевому слову.
     */
    public SearchResultPage search(String query) {
        searchInput.fill(query);
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
    }

    /**
     * Вводит часть запроса и динамически ждет появления поисковых подсказок.
     */
    public MainPage fillSearchAndWaitSuggestions(String query) {
        searchInput.fill(query);
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
        // динамическое ожидание: проверяем, появится ли попап в течение 3 секунд
        if (locationPopupBackdrop.isDisplayed(Duration.ofSeconds(3))) {
            locationPopupBackdrop.click();
            locationPopupBackdrop.waitUntilHidden();
        }
        return this;
    }

    /**
     * Нажимает кнопку очистки поля поиска (крестик).
     */
    public void clickClearSearch() {
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
        languageSwitcher.click();
        englishOption.click();
        // динамическое ожидание: список языков должен закрыться после выбора
        englishOption.waitUntilHidden();
    }

    /**
     * Возвращает текст кнопки поиска.
     * После смены языка текст меняется с "Найти" на "Find".
     */
    public String getSearchButtonText() {
        return searchSubmitButton.getText();
    }
}
