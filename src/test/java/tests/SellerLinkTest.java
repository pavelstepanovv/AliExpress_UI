package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.ProductPage;
import pages.SearchResultPage;
import pages.SellerPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест 14: Проверка ссылки на продавца.
 * Проверяет, что клик по названию магазина на странице товара
 * открывает страницу магазина продавца.
 * На десктопной версии сайта для этого нужен один клик,
 * отдельной иконки информации о продавце нет.
 */
public class SellerLinkTest extends BaseTest {

    private static final String SEARCH_QUERY = "наушники";

    /**
     * Проверяет, что страница магазина открывается и показывает его название.
     */
    @Test
    public void testSellerLinkOpensShopPage() {
        // 1. open the main page and search for the product
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();
        SearchResultPage results = mainPage.search(SEARCH_QUERY);

        // 2. open the first product card
        ProductPage productPage = results.openFirstProduct();

        // 3. click the shop name link on the product page
        SellerPage sellerPage = productPage.openSellerPage();

        String shopTitle = sellerPage.getShopTitle();

        // 4. the shop page must show the shop name
        assertThat(sellerPage.isShopTitleDisplayed())
                .as("Название магазина должно быть видно на странице продавца")
                .isTrue();

        assertThat(shopTitle)
                .as("Название магазина не должно быть пустым")
                .isNotBlank();

        System.out.println("Открыт магазин: " + shopTitle);
    }
}
