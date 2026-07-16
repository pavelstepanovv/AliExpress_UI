package tests;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.ProductPage;
import pages.SearchResultPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест 15: Проверка выбора количества товара в карточке.
 * Проверяет, что количество товара можно увеличить до 2.
 * Отдельного поля количества на странице нет:
 * счётчик появляется после нажатия кнопки "В корзину".
 */
public class QuantitySelectorTest extends BaseTest {

    private static final String SEARCH_QUERY = "наушники";

    /**
     * Проверяет, что счётчик показывает 1 после добавления
     * в корзину и 2 после нажатия "+".
     */
    @Test
    public void testIncreaseQuantityToTwo() {
        // 1. open the main page and search for the product
        MainPage mainPage = new MainPage().open().closeLocationPopupIfPresent();
        SearchResultPage results = mainPage.search(SEARCH_QUERY);

        // 2. open the first product card
        ProductPage productPage = results.openFirstProduct();

        // 3. add the product to the cart - the button turns into a counter
        productPage.addToCart();

        assertThat(productPage.getCurrentQuantity())
                .as("После добавления в корзину счётчик должен показывать '1'")
                .isEqualTo("1");

        // 4. click "+" to increase the quantity
        productPage.incrementQuantity();

        // 5. the counter must show the new quantity
        assertThat(productPage.getCurrentQuantity())
                .as("После нажатия '+' счётчик должен показывать '2'")
                .isEqualTo("2");
    }

    /**
     * Удаляет cookie после теста, чтобы очистить корзину:
     * браузер не закрывается между тестами, и добавленный товар
     * мог бы помешать другим тестам.
     */
    @AfterEach
    public void resetCart() {
        Selenide.clearBrowserCookies();
    }
}
