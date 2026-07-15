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

    /**
     * Проверяет, что после сортировки цена первого товара не больше цены второго.
     */
    @Test
    public void testSortByPriceAscending() {
        // 1. open the main page and search for the product
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();
        SearchResultPage results = mainPage.search(SEARCH_QUERY);

        // 2. sort the results by price, cheapest first
        results.sortByPriceAscending();

        // 3. read the prices of the loaded products
        List<Integer> prices = results.getProductPrices();

        assertThat(prices.size())
                .as("После сортировки на странице должно быть хотя бы два товара с ценой")
                .isGreaterThanOrEqualTo(2);

        System.out.println("Цена первого товара: " + prices.get(0));
        System.out.println("Цена второго товара: " + prices.get(1));

        // 4. the first price must not be greater than the second one
        assertThat(prices.get(0))
                .as("После сортировки цена первого товара должна быть не больше цены второго")
                .isLessThanOrEqualTo(prices.get(1));
    }
}
