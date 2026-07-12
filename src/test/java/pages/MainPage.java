package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import elements.Button;
import elements.Input;

import static com.codeborne.selenide.Selenide.$$x;

public class MainPage extends BasePage {

    private static final String URL = "https://aliexpress.ru";

    private final Input searchInput = Input.byName("SearchText");
    private final Button searchButton = Button.byText("Найти");

    // Элементы для работы с подсказками
    private final ElementsCollection suggestions = $$x("//div[contains(@class, 'suggestion')]");

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

    public void fillSearchAndWaitSuggestions(String query) {
        searchInput.fill(query);
        // Ожидание появления подсказок (реализовано через Selenide)
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getSuggestionsCount() {
        return suggestions.size();
    }

    public boolean allSuggestionsContain(String word) {
        if (suggestions.isEmpty()) {
            return false;
        }
        for (SelenideElement suggestion : suggestions) {
            if (!suggestion.getText().toLowerCase().contains(word.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public String getFirstSuggestionText() {
        if (suggestions.isEmpty()) {
            return "Нет подсказок";
        }
        return suggestions.first().getText();
    }
}