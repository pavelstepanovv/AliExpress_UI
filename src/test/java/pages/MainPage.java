package pages;

import com.codeborne.selenide.ElementsCollection;
import elements.Button;
import elements.Input;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$$x;

public class MainPage extends BasePage {

    private static final String URL = "https://aliexpress.ru";

    private final Input searchInput = Input.byName("SearchText");
    private final Button searchButton = Button.byText("Найти");
    private final ElementsCollection searchSuggestions = $$x("//div[@ae_button_type='auto_suggestion' and @ae_object_type='keyword']");

    public MainPage open() {
        open(URL);
        return this;
    }

    public SearchResultPage search(String query) {
        searchInput.fill(query);
        searchButton.click();
        return new SearchResultPage();
    }

    public void clearSearch() {
        searchInput.clear();
    }

    public void fillSearch(String query) {
        searchInput.fill(query);
    }

    // Блок написан Шакуров 4382: вводим неполный запрос и ждем появления поисковых подсказок.
    public MainPage fillSearchAndWaitSuggestions(String query) {
        searchInput.fill(query);
        searchSuggestions.shouldHave(sizeGreaterThan(0), Duration.ofSeconds(10));
        return this;
    }

    // Блок написан Шакуров 4382: проверяем, что каждая подсказка содержит введенный пользователем текст.
    public boolean allSuggestionsContain(String text) {
        if (searchSuggestions.isEmpty()) {
            return false;
        }

        for (int i = 0; i < searchSuggestions.size(); i++) {
            String suggestion = searchSuggestions.get(i).getAttribute("ae_object_value");
            if (suggestion == null || !suggestion.toLowerCase().contains(text.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public int getSuggestionsCount() {
        return searchSuggestions.size();
    }
}
