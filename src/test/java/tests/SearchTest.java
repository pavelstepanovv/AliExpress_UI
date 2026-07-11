package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.SearchResultPage;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchTest extends BaseTest {

    // Блок написан Шакуров 4382: общие тестовые данные для сценариев поиска.
    private static final String HEADPHONES_QUERY = "наушники";
    private static final String HEADPHONES_PARTIAL_QUERY = "науш";
    private static final int MAX_PRICE = 200;

    @Test
    public void testSearchProduct() {
        MainPage mainPage = new MainPage().open();
        SearchResultPage results = mainPage.search(HEADPHONES_QUERY);

        assertThat(results.hasProducts())
                .as("Должны быть найдены товары по запросу 'наушники'")
                .isTrue();

        assertThat(results.hasProductContainingWord(HEADPHONES_QUERY))
                .as("Хотя бы один товар должен содержать слово 'наушники' в названии")
                .isTrue();

        System.out.println("Найдено товаров: " + results.getProductsCount());
        System.out.println("Первый товар: " + results.getFirstProductTitle() + " | Цена: " + results.getFirstProductPrice());
    }

    @Test
    public void testFilterProductsByMaxPrice() {
        MainPage mainPage = new MainPage().open();
        SearchResultPage results = mainPage.search(HEADPHONES_QUERY);

        // Блок написан Шакуров 4382.
        // Проверяем сценарий из чек-листа: поиск товара и ограничение цены значением "до 200".
        results.filterByMaxPrice(MAX_PRICE);

        assertThat(results.hasProducts())
                .as("После фильтрации должны остаться товары")
                .isTrue();

        assertThat(results.allProductsHavePriceLessThan(MAX_PRICE))
                .as("У всех загруженных товаров цена должна быть не больше 200 рублей")
                .isTrue();
    }

    @Test
    public void testSearchSuggestions() {
        MainPage mainPage = new MainPage().open();

        // Блок написан Шакуров 4382.
        // Проверяем сценарий из чек-листа: вводим часть запроса и анализируем выпадающие подсказки.
        mainPage.fillSearchAndWaitSuggestions(HEADPHONES_PARTIAL_QUERY);

        assertThat(mainPage.getSuggestionsCount())
                .as("Список поисковых подсказок не должен быть пустым")
                .isGreaterThan(0);

        assertThat(mainPage.allSuggestionsContain(HEADPHONES_PARTIAL_QUERY))
                .as("Все подсказки должны содержать введенный текст 'науш'")
                .isTrue();
    }
}
