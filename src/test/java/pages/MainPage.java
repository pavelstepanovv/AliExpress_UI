package pages;

import com.codeborne.selenide.ElementsCollection;
import elements.Button;
import elements.Input;

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

    private final Input searchInput = Input.byName("SearchText");
    private final Button searchButton = Button.byText("Найти");
    private final ElementsCollection searchSuggestions = $$x("//div[@ae_button_type='auto_suggestion' and @ae_object_type='keyword']");

    /**
     * Открывает главную страницу AliExpress.
     */
    public MainPage open() {
        open(URL);
        return this;
    }

    /**
     * Выполняет поиск товара по переданному запросу.
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
     * Возвращает текст первой поисковой подсказки.
     */
    public String getFirstSuggestionText() {
        if (searchSuggestions.isEmpty()) {
            return "Нет подсказок";
        }
        String suggestion = searchSuggestions.first().getAttribute("ae_object_value");
        return suggestion == null ? searchSuggestions.first().getText() : suggestion;
    }
}
