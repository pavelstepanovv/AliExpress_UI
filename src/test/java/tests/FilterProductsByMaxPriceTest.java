package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.SearchResultPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест проверяет фильтрацию товаров AliExpress по максимальной цене.
 * Автор: Шакуров 4382.
 */
public class FilterProductsByMaxPriceTest extends BaseTest {

    /**
     * Проверяет, что после применения фильтра "до 200" в выдаче остаются товары с подходящей ценой.
     */
    @Test
    public void testFilterProductsByMaxPrice() {
        MainPage mainPage = new MainPage().open();
        SearchResultPage results = mainPage.search(HEADPHONES_QUERY);

        results.filterByMaxPrice(MAX_PRICE);

        assertThat(results.getProductsCount())
                .as("После фильтрации должны остаться товары")
                .isGreaterThan(0);

        assertThat(results.getProductPrices())
                .as("У всех загруженных товаров цена должна быть не больше 200 рублей")
                .isNotEmpty()
                .allMatch(price -> price <= MAX_PRICE);
    }
}
