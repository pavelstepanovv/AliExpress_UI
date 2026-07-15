package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест проверяет появление поисковых подсказок на AliExpress.
 * Автор: Шакуров 4382.
 */
public class SearchSuggestionsTest extends BaseTest {

    /**
     * Проверяет, что при вводе части запроса появляются релевантные подсказки.
     */
    @Test
    public void testSearchSuggestions() {
        MainPage mainPage = new MainPage().open();

        mainPage.fillSearchAndWaitSuggestions(HEADPHONES_PARTIAL_QUERY);

        assertThat(mainPage.getSuggestionsCount())
                .as("Список поисковых подсказок не должен быть пустым")
                .isGreaterThan(0);

        assertThat(mainPage.getSuggestionValues())
                .as("Все подсказки должны содержать введенный текст 'науш'")
                .allMatch(suggestion -> suggestion.toLowerCase().contains(HEADPHONES_PARTIAL_QUERY.toLowerCase()));
    }
}
