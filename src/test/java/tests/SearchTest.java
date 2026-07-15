package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.SearchResultPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест проверяет базовый поиск товара на AliExpress.
 * Автор: Шакуров 4382.
 */
public class SearchTest extends BaseTest {

    /**
     * Проверяет, что по запросу находятся товары и хотя бы один результат содержит искомое слово.
     */
    @Test
    public void testSearchProduct() {
        MainPage mainPage = new MainPage().open();
        SearchResultPage results = mainPage.search(HEADPHONES_QUERY);

        assertThat(results.getProductsCount())
                .as("Должны быть найдены товары по запросу 'наушники'")
                .isGreaterThan(0);

        assertThat(results.getProductTitles())
                .as("Хотя бы один товар должен содержать слово 'наушники' в названии")
                .anyMatch(title -> title.toLowerCase().contains(HEADPHONES_QUERY.toLowerCase()));

        System.out.println("Найдено товаров: " + results.getProductsCount());
        System.out.println("Первый товар: " + results.getFirstProductTitle() + " | Цена: " + results.getFirstProductPrice());
    }
}
