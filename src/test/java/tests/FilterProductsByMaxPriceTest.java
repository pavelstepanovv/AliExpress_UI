package tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.MainPage;
import pages.SearchResultPage;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
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
        waitUntilPricesMatchFilter(results);

        assertThat(results.getProductsCount())
                .as("После фильтрации должны остаться товары")
                .isGreaterThan(0);

        assertThat(results.getProductPrices())
                .as("У всех загруженных товаров цена должна быть не больше 200 рублей")
                .isNotEmpty()
                .allMatch(price -> price <= MAX_PRICE);
    }

    /**
     * Динамически ждет применения фильтра; само сравнение остается в тестовом слое.
     */
    private void waitUntilPricesMatchFilter(SearchResultPage results) {
        new WebDriverWait(getWebDriver(), Duration.ofSeconds(20))
                .until(driver -> {
                    List<Integer> prices = results.getProductPrices();
                    return !prices.isEmpty() && prices.stream().allMatch(price -> price <= MAX_PRICE);
                });
    }
}
