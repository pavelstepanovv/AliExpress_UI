package tests;

import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.MainPage;
import pages.ProductPage;
import pages.SearchResultPage;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест проверяет добавление товара в корзину и изменение его количества.
 * Автор: Шакуров 4382.
 */
public class AddProductToCartAndChangeQuantityTest extends BaseTest {

    /**
     * Проверяет, что товар добавляется в корзину, количество меняется до 3,
     * а итоговая стоимость пересчитывается с учетом нового количества.
     */
    @Test
    public void testAddProductToCartAndChangeQuantity() {
        MainPage mainPage = new MainPage().open();
        SearchResultPage results = mainPage.search(CART_PRODUCT_QUERY);
        ProductPage productPage = results.openFirstProduct();

        productPage.addToCart();
        CartPage cartPage = productPage.openCart();

        int unitPrice = cartPage.getProductUnitPrice();
        cartPage.increaseProductQuantityTo(CART_PRODUCT_QUANTITY)
                .waitTotalRecalculatedForQuantity(CART_PRODUCT_QUANTITY);

        assertThat(cartPage.getProductTitle())
                .as("В корзине должен отображаться добавленный товар")
                .containsIgnoringCase(CART_PRODUCT_EXPECTED_WORD);

        assertThat(cartPage.getProductQuantity())
                .as("Количество товара в корзине должно быть равно 3")
                .isEqualTo(CART_PRODUCT_QUANTITY);

        assertThat(cartPage.getTotalPrice())
                .as("Итоговая стоимость должна быть пересчитана с учетом количества")
                .isEqualTo(unitPrice * CART_PRODUCT_QUANTITY);
    }
}
