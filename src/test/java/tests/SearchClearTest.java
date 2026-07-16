package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест 12: Очистка строки поиска.
 * Проверяет, что кнопка очистки удаляет старый запрос
 * и новый запрос вводится в уже пустое поле.
 */
public class SearchClearTest extends BaseTest {

    private static final String FIRST_QUERY = "наушники";
    private static final String SECOND_QUERY = "смартфон";

    /**
     * Проверяет, что после очистки поля в нём остаётся только новый запрос.
     */
    @Test
    public void testClearSearchField() {
        // 1. open the main page (and close the region popup if it appears)
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();

        // 2. type the first query into the search field
        mainPage.fillSearch(FIRST_QUERY);

        // 3. clear the field with the "X" button
        mainPage.clickClearSearch();

        // 4. type the second query
        mainPage.fillSearch(SECOND_QUERY);

        String fieldValue = mainPage.getSearchFieldValue();

        // 5. the field must contain only the new query
        assertThat(fieldValue)
                .as("В поле поиска должно быть '%s'", SECOND_QUERY)
                .isEqualTo(SECOND_QUERY);

        assertThat(fieldValue)
                .as("Старого запроса '%s' в поле быть не должно", FIRST_QUERY)
                .doesNotContain(FIRST_QUERY);

    }
}
