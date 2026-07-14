package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.SearchResultPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест №1: Поиск товара по ключевому слову.
 * Проверяет, что поиск работает и выдаёт товары с нужным словом в названии.
 */
public class SearchTest extends BaseTest {
    // СТАТИЧЕСКИЕ ПОЛЯ
    private static final String SEARCH_QUERY = "наушники";

    @Test
    public void testSearchProduct() {
        // 1. Открыть главную страницу
        MainPage mainPage = new MainPage().open();

        // 2. Выполнить поиск
        SearchResultPage results = mainPage.search(SEARCH_QUERY);

        // 3. Проверить, что товары найдены
        assertThat(results.hasProducts())
                .as("Должны быть найдены товары по запросу '%s'", SEARCH_QUERY)
                .isTrue();

        // 4. Проверить, что есть товар со словом "наушники" в названии
        assertThat(results.hasProductContainingWord(SEARCH_QUERY))
                .as("Хотя бы один товар должен содержать слово '%s' в названии", SEARCH_QUERY)
                .isTrue();

        // 5. Вывод информации для отладки
        System.out.println("Найдено товаров: " + results.getProductsCount());
        System.out.println("Первый товар: " + results.getFirstProductTitle() + " | Цена: " + results.getFirstProductPrice());
    }
}
