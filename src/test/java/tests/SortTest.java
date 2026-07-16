package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.SearchResultPage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест 11: Сортировка результатов поиска по цене.
 * Проверяет, что после выбора "Сначала дешёвые"
 * товары идут по возрастанию цены.
 */
public class SortTest extends BaseTest {

    private static final String SEARCH_QUERY = "рюкзак";
    private static final String EXPECTED_SORT_TYPE = "price_asc";
    private static final String EXPECTED_SORT_TEXT = "Сначала дешёвые";

    /**
     * Проверяет, что AliExpress применил сортировку по возрастанию цены.
     */
    @Test
    public void testSortByPriceAscending() {
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();
        SearchResultPage results = mainPage.search(SEARCH_QUERY);

        results.sortByPriceAscending();
        List<Integer> prices = results.getProductPrices();

        assertThat(prices)
                .as("После сортировки на странице должно быть хотя бы два товара с ценой")
                .hasSizeGreaterThanOrEqualTo(2);

        assertThat(results.getSelectedSortType())
                .as("Должен быть применён режим '%s'. Цены в выдаче: %s",
                        EXPECTED_SORT_TYPE, prices)
                .isEqualTo(EXPECTED_SORT_TYPE);

        assertThat(results.getSelectedSortText())
                .as("В списке сортировки должен отображаться пункт '%s'. Цены в выдаче: %s",
                        EXPECTED_SORT_TEXT, prices)
                .isEqualTo(EXPECTED_SORT_TEXT);

    }
}
