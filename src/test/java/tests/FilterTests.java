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
 * Группа тестов для проверки фильтрации.
 * Включает: фильтрацию товаров по цене.
 */
public class FilterTests extends BaseTest {
    // 1. СТАТИЧЕСКИЕ ПОЛЯ
    private static final String HEADPHONES_QUERY = "наушники";
    private static final int MAX_PRICE = 200;

    // 2. ТЕСТЫ

    /**
     * Тест №2: Фильтрация товаров по цене.
     * Автор: Шакуров Альберт, группа 4382
     *
     * Шаги:
     * 1. Открыть главную страницу
     * 2. Выполнить поиск "наушники"
     * 3. Применить фильтр "цена до 200"
     * 4. Дождаться применения фильтра
     * 5. Проверить, что товары остались
     * 6. Проверить, что у всех товаров цена ≤ 200
     */
    @Test
    public void testFilterProductsByMaxPrice() {
        // 1. Открыть главную страницу
        MainPage mainPage = new MainPage().open();

        // 2. Выполнить поиск
        SearchResultPage results = mainPage.search(HEADPHONES_QUERY);

        // 3. Применить фильтр по цене
        results.filterByMaxPrice(MAX_PRICE);

        // 4. Дождаться применения фильтра
        waitUntilPricesMatchFilter(results);

        // 5. Проверить, что товары остались
        assertThat(results.getProductsCount())
                .as("После фильтрации должны остаться товары")
                .isGreaterThan(0);

        // 6. Проверить, что у всех товаров цена ≤ 200
        assertThat(results.getProductPrices())
                .as("У всех загруженных товаров цена должна быть не больше %d рублей", MAX_PRICE)
                .isNotEmpty()
                .allMatch(price -> price <= MAX_PRICE);
    }

    /**
     * Динамически ждёт применения фильтра.
     * Проверяет, что цены загрузились и все ≤ MAX_PRICE.
     *
     * @param results страница результатов поиска
     */
    private void waitUntilPricesMatchFilter(SearchResultPage results) {
        new WebDriverWait(getWebDriver(), Duration.ofSeconds(20))
                .until(driver -> {
                    List<Integer> prices = results.getProductPrices();
                    return !prices.isEmpty() && prices.stream().allMatch(price -> price <= MAX_PRICE);
                });
    }
}
