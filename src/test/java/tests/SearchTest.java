package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.SearchResultPage;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchTest extends BaseTest {

    @Test
    public void testSearchProduct() {
        MainPage mainPage = new MainPage().open();
        SearchResultPage results = mainPage.search("наушники");

        assertThat(results.hasProducts())
                .as("Должны быть найдены товары по запросу 'наушники'")
                .isTrue();

        assertThat(results.hasProductContainingWord("наушники"))
                .as("Хотя бы один товар должен содержать слово 'наушники' в названии")
                .isTrue();

        System.out.println("Найдено товаров: " + results.getProductsCount());
        System.out.println("Первый товар: " + results.getFirstProductTitle() + " | Цена: " + results.getFirstProductPrice());
    }
}